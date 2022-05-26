package com.halter.herd.external;

import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;

import com.halter.herd.dto.CollarResponseDto;
import com.halter.herd.exception.ExternalRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CollarClient {

  private final String baseUrl;
  private final RestTemplate restTemplate;

  public CollarClient(@Value("${collarClient.getCollarByIdUrl}") String baseUrl,
      RestTemplateBuilder builder) {
    this.baseUrl = baseUrl;
    this.restTemplate = builder.build();
  }

  /**
   * @param collarId
   * @return CowResponse
   */
  @Cacheable(value = "collarCache")
  public CollarResponseDto getCollarById(Long collarId) {
    try {
      ResponseEntity<CollarResponseDto[]> response = restTemplate.getForEntity(baseUrl,
          CollarResponseDto[].class, collarId);

      return stream(requireNonNull(response.getBody()))
          .max(comparing(CollarResponseDto::getTimestamp))
          .orElseThrow(() -> new ExternalRequestException(String.format(
              "Error retrieving collar data for collar id: %s", collarId)));
    } catch (Exception e) {

      throw new ExternalRequestException(String.format(
          "Error retrieving collar data for collar id: %s", collarId));
    }
  }
}
