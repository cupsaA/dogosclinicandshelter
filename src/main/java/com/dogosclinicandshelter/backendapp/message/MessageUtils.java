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
  ADOPTIVE_PERSON_SAVE_FAILED("Adoptive Person save failed"),
  ADOPTIVE_PERSON_WITH_ID_S_NOT_FOUND("adoptive person with id %s not found"),
  DOG_INFO_WITH_ID_S_NOT_FOUND("dog info with id %s not found"),
  DOG_HEALTH_WITH_ID_S_NOT_FOUND("dog health with id %s not found"),
  FOSTER_PERSON_WITH_ID_S_NOT_FOUND ("foster person with id %s not found"),
  DOG_INFO_SAVE_DONE("Dog Info save done"),
  DOG_INFO_SAVE_FAILED("Dog Info save failed"),
  DOG_INFO_DELETE_DONE("Dog Info delete done"),
  DOG_INFO_DELETE_FAILED("Dog Info delete failed"),
  DOG_INFO_UPDATE_DONE("Dog Info update done"),
  DOG_INFO_UPDATE_FAILED ("Dog Info update failed"),
  DOG_HEALTH_SAVE_DONE("Dog Health save done"),
  DOG_HEALTH_SAVE_FAILED("Dog health save failed"),
  DOG_HEALTH_DELETE_DONE("Dog Health delete done"),
  DOG_HEALTH_DELETE_FAILED("Dog health delete failed"),
  DOG_HEALTH_UPDATE_DONE("Dog Health update done"),
  DOG_HEALTH_UPDATE_FAILED("Dog Health update failed");
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
