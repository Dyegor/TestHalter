package com.halter.herd.enums;

public enum Statuses {
  HEALTHY("Healthy"),
  BROKEN("Broken");

  private String code;

  Statuses(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
