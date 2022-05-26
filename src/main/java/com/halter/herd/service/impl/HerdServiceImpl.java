package com.halter.herd.service.impl;

import static com.halter.herd.enums.Statuses.BROKEN;
import static com.halter.herd.enums.Statuses.HEALTHY;
import static java.util.UUID.fromString;
import static java.util.stream.Collectors.toList;

import com.halter.herd.dto.CollarResponseDto;
import com.halter.herd.dto.CowRequestDto;
import com.halter.herd.dto.CowResponseDto;
import com.halter.herd.dto.CowResponseDto.LastLocationDto;
import com.halter.herd.exception.DatabaseConnectionException;
import com.halter.herd.exception.InvalidRequestException;
import com.halter.herd.external.CollarClient;
import com.halter.herd.persistence.dao.CowRepository;
import com.halter.herd.persistence.model.Cow;
import com.halter.herd.service.HerdService;
import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class HerdServiceImpl implements HerdService {

  private static final Logger LOGGER = LoggerFactory.getLogger(HerdServiceImpl.class);

  private final CowRepository cowRepository;
  private final CollarClient collarClient;

  public HerdServiceImpl(CowRepository cowRepository, CollarClient collarClient) {
    this.cowRepository = cowRepository;
    this.collarClient = collarClient;
  }

  /**
   * Gets List of all cows from db
   *
   * @return list of cows
   */
  @Override
  public List<CowResponseDto> getAllCows() {
    List<Cow> cows;

    try {
      cows = cowRepository.findAll();
    } catch (Exception e) {
      LOGGER.error("Error retrieving cows data, error message: {}", e.getMessage());

      throw new DatabaseConnectionException(
          "Error retrieving cows data, pls refer to the logs for more information");
    }

    return cows
        .stream()
        .map(this::toCowResponseDTO)
        .collect(toList());
  }

  /**
   * @param cowRequest contains collarId and cowNumber
   * @return created Cow
   */
  @Override
  public CowResponseDto createCow(CowRequestDto cowRequest) {
    Cow createdCow;

    try {
      Cow cow = new Cow();

      cow.setCowNumber(cowRequest.getCowNumber());
      cow.setCollarId(cowRequest.getCollarId());

      createdCow = cowRepository.save(cow);
    } catch (Exception e) {
      LOGGER.error("Error creating new cow, error message: {}", e.getMessage());

      throw new InvalidRequestException(String.format(
          "Cow with collar id or cow number provided already exists, collarId: %s, cowNumber: %s",
          cowRequest.getCollarId(), cowRequest.getCowNumber()));
    }

    return toCowResponseDTO(createdCow);
  }

  /**
   * @param cowId      id of existing Cow in db
   * @param cowRequest contains collarId and cowNumber
   * @return updated Cow
   */
  @Transactional
  @Override
  public CowResponseDto updateCow(String cowId, CowRequestDto cowRequest) {
    Cow cow;

    try {
      cow = cowRepository.findOneById(fromString(cowId));

      cow.setCowNumber(cowRequest.getCowNumber());
      cow.setCollarId(cowRequest.getCollarId());
    } catch (Exception e) {
      LOGGER.error("Error updating cow data, cowId: {}, error message: {}", cowId, e.getMessage());

      throw new InvalidRequestException(String.format("Can not find cow with id: %s", cowId));
    }

    return toCowResponseDTO(cow);
  }

  /**
   * @return CowResponse mapped from Cow entity and external api call
   */
  private CowResponseDto toCowResponseDTO(Cow cow) {
    CollarResponseDto collarResponseDto = collarClient.getCollarById(cow.getCollarId());

    return CowResponseDto.builder()
        .id(cow.getId())
        .collarId(cow.getCollarId())
        .cowNumber(cow.getCowNumber())
        .collarStatus(resolveStatus(collarResponseDto.getHealthy()))
        .lastLocation(LastLocationDto.builder()
            .lat(collarResponseDto.getLat())
            .lng(collarResponseDto.getLng())
            .build())
        .build();
  }

  /**
   * @param collarStatus
   * @return Status of the collar
   */
  private String resolveStatus(Boolean collarStatus) {

    return collarStatus ? HEALTHY.getCode() : BROKEN.getCode();
  }
}
