package com.dogosclinicandshelter.backendapp.dog.enumClasses;

public enum AdoptionStatus {
  ADOPTED("ADOPTED"),
  IN_PROGRESS("IN PROGRESS"),
  READY_FOR_ADOPTION("READY FOR ADOPTION"),
  NOT_READY_FOR_ADOPTION("NOT READY FOR ADOPTION"),
  UNADOPTABLE("UNADOPTABLE");

  public final String status;

  AdoptionStatus(String status) {
    this.status = status;
  }
}
