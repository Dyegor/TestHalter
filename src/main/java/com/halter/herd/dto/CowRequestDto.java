package com.halter.herd.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonDeserialize
public class CowRequestDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long collarId;

  private Long cowNumber;
}
