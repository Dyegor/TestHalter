package com.halter.herd.exception;

public class DatabaseConnectionException extends RuntimeException {

  public DatabaseConnectionException(String message) {
    super(message);
  }
}
