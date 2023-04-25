package com.dogosclinicandshelter.backendapp.dog.enumClasses;

public enum DogSize {
  XS("XTRASMALL"),
  S("SMALL"),
  M("MEDIUM"),
  L("LARGE"),
  XL("XTRALARGE");

  public final String size;

  DogSize(String size) {
    this.size = size;
  }
}
