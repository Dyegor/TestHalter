package com.halter.herd;

import static java.util.UUID.fromString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.halter.herd.dto.CollarResponseDto;
import com.halter.herd.dto.CowRequestDto;
import com.halter.herd.dto.CowResponseDto;
import com.halter.herd.exception.DatabaseConnectionException;
import com.halter.herd.exception.ExternalRequestException;
import com.halter.herd.exception.InvalidRequestException;
import com.halter.herd.external.CollarClient;
import com.halter.herd.persistence.dao.CowRepository;
import com.halter.herd.persistence.model.Cow;
import com.halter.herd.service.HerdService;
import com.halter.herd.service.impl.HerdServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

public class HerdServiceImplTest {

  private HerdService herdService;
  private CowRepository cowRepository;
  private CollarClient collarClient;

  private final String UUID_STRING = "14d4acea-ec2d-400d-9d2f-57f7e2d01f66";
  private final String STATUS_HEALTHY = "Healthy";

  @BeforeEach
  void init() {
    cowRepository = mock(CowRepository.class);
    collarClient = mock(CollarClient.class);

    herdService = new HerdServiceImpl(cowRepository, collarClient);
  }

  private Cow createNewCow() {
    Cow cow = new Cow();

    cow.setId(fromString(UUID_STRING));
    cow.setCowNumber(222222L);
    cow.setCollarId(333333L);

    return cow;
  }

  private List<Cow> createNewCowsList() {
    List<Cow> cows = new ArrayList<>();

    cows.add(createNewCow());

    return cows;
  }

  private CollarResponseDto createNewCollarResponseDto() {
    CollarResponseDto collarResponseDto = new CollarResponseDto();

    collarResponseDto.setHealthy(true);
    collarResponseDto.setLat("44444.4");
    collarResponseDto.setLng("55555.5");

    return collarResponseDto;
  }

  private CowRequestDto createCowRequestDto() {
    CowRequestDto cowRequestDto = new CowRequestDto();

    cowRequestDto.setCowNumber(123456L);
    cowRequestDto.setCollarId(789789L);

    return cowRequestDto;
  }

  @Test
  @DisplayName("Test retrieve all cows")
  void testGetCows() {
    when(cowRepository.findAll()).thenReturn(createNewCowsList());
    when(collarClient.getCollarById(any())).thenReturn(createNewCollarResponseDto());

    List<CowResponseDto> cows = herdService.getAllCows();

    verify(cowRepository, times(1)).findAll();
    verify(collarClient, times(1)).getCollarById(any());

    CowResponseDto cowResponseDto = cows.get(0);

    assertEquals(1, cows.size());
    assertEquals(222222L, cowResponseDto.getCowNumber());
    assertEquals(333333L, cowResponseDto.getCollarId());
    assertEquals(STATUS_HEALTHY, cowResponseDto.getCollarStatus());
    assertEquals("44444.4", cowResponseDto.getLastLocation().getLat());
    assertEquals("55555.5", cowResponseDto.getLastLocation().getLng());
  }

  @Test
  @DisplayName("Test new cow creation")
  void testCreateNewCow() {
    when(cowRepository.save(any())).thenReturn(createNewCow());
    when(collarClient.getCollarById(any())).thenReturn(createNewCollarResponseDto());

    CowResponseDto cowResponseDto = herdService.createCow(createCowRequestDto());

    verify(cowRepository, times(1)).save(any());
    verify(collarClient, times(1)).getCollarById(any());

    assertEquals(fromString(UUID_STRING), cowResponseDto.getId());
    assertEquals(222222L, cowResponseDto.getCowNumber());
    assertEquals(333333L, cowResponseDto.getCollarId());
    assertEquals(STATUS_HEALTHY, cowResponseDto.getCollarStatus());
    assertEquals("44444.4", cowResponseDto.getLastLocation().getLat());
    assertEquals("55555.5", cowResponseDto.getLastLocation().getLng());
  }

  @Test
  @DisplayName("Test update existing cow")
  void testUpdateCow() {
    when(cowRepository.findOneById(any())).thenReturn(createNewCow());
    when(collarClient.getCollarById(any())).thenReturn(createNewCollarResponseDto());

    CowResponseDto cowResponseDto = herdService.updateCow(UUID_STRING,
        createCowRequestDto());

    verify(cowRepository, times(1)).findOneById(any());
    verify(collarClient, times(1)).getCollarById(any());

    assertEquals(fromString(UUID_STRING), cowResponseDto.getId());
    assertEquals(123456L, cowResponseDto.getCowNumber());
    assertEquals(789789L, cowResponseDto.getCollarId());
    assertEquals(STATUS_HEALTHY, cowResponseDto.getCollarStatus());
    assertEquals("44444.4", cowResponseDto.getLastLocation().getLat());
    assertEquals("55555.5", cowResponseDto.getLastLocation().getLng());
  }

  @Test
  @DisplayName("Test error when retrieving all cows")
  void testGetCowsException() {
    when(cowRepository.findAll()).thenThrow(new HibernateException("Database Exception"));

    Exception thrown = assertThrows(DatabaseConnectionException.class,
        () -> herdService.getAllCows());
    assertTrue(thrown.getMessage().contains("Error retrieving cows data"));
  }

  @Test
  @DisplayName("Test error when creating new cow, duplicate id")
  void testCreateCowException() {
    when(cowRepository.save(any())).thenThrow(new DataIntegrityViolationException("Id exists"));

    Exception thrown = assertThrows(InvalidRequestException.class,
        () -> herdService.createCow(createCowRequestDto()));
    assertTrue(
        thrown.getMessage().contains("Cow with collar id or cow number provided already exists"));
  }

  @Test
  @DisplayName("Test error when updating existing cow, invalid id")
  void testUpdateCowException() {
    when(cowRepository.save(any())).thenThrow(new IllegalArgumentException("Id exists"));

    Exception thrown = assertThrows(InvalidRequestException.class,
        () -> herdService.updateCow("123456", createCowRequestDto()));
    assertTrue(thrown.getMessage().contains("Can not find cow with id"));
  }

  @Test
  @DisplayName("Test error when retrieving collar data by collar id")
  void testGetCollarByIdException() {
    when(collarClient.getCollarById(any())).thenThrow(
        new ExternalRequestException("Error retrieving collar data for collar id"));

    Exception thrown = assertThrows(
        ExternalRequestException.class, () -> collarClient.getCollarById(any()));
    assertTrue(thrown.getMessage().contains("Error retrieving collar data for collar id"));
  }
}
