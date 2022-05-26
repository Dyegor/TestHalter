package com.halter.herd.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonSerialize
public class CowResponseDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private UUID id;

  private Long collarId;

  private Long cowNumber;

  private String collarStatus;

  LastLocationDto lastLocation;

  @Getter
  @Setter
  @Builder
  public static class LastLocationDto {
    private String lat;
    private String lng;
  }
}
