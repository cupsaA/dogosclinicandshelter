package com.dogosclinicandshelter.backendapp.message;

public enum MessageUtils {
  FOSTER_PERSON_UPDATE_DONE("Foster Person update done"),
  FOSTER_PERSON_UPDATE_FAILED("Foster Person update failed"),
  FOSTER_PERSON_DELETE_DONE("Foster Person delete done"),
  FOSTER_PERSON_DELETE_FAILED("Foster Person delete failed"),
  FOSTER_PERSON_SAVE_DONE("Foster Person save done"),
  FOSTER_PERSON_SAVE_FAILED("Foster Person save failed"),
  FOSTER_PERSON_NOT_FOUND("Foster Person not found"),
  ADOPTIVE_PERSON_UPDATE_DONE("Adoptive Person update done"),
  ADOPTIVE_PERSON_UPDATE_FAILED("Adoptive Person update failed"),
  ADOPTIVE_PERSON_DELETE_DONE("Adoptive Person delete done"),
  ADOPTIVE_PERSON_DELETE_FAILED("Adoptive Person delete failed"),
  ADOPTIVE_PERSON_SAVE_DONE("Adoptive Person save done"),
  ADOPTIVE_PERSON_SAVE_FAILED("Adoptive Person save failed");
  private final String message;


  MessageUtils(String s) {
    message = s;
  }

  @Override
  public String toString()
  {
    return message;
  }
}
