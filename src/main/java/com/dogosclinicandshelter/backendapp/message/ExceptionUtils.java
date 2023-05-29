package com.dogosclinicandshelter.backendapp.message;

public enum ExceptionUtils {

  EMAIL_ALREADY_TAKEN("email already taken"),
  NO_DATA_CHANGES_FOUND("no data changes found");

  private final String message;

  ExceptionUtils(String s) {
    message = s;
  }

  @Override
  public String toString() {
    return message;
  }
}
