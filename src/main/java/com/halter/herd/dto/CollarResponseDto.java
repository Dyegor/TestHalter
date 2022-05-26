package com.halter.herd.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonDeserialize
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CollarResponseDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private Long serialNumber;

  private String lat;

  private String lng;

  private Boolean healthy;

  private Instant timestamp;
}
