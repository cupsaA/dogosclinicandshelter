package com.dogosclinicandshelter.backendapp.dataRequest.dogInfoRequest;

import com.dogosclinicandshelter.backendapp.dog.enumClasses.DogSize;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DogInfoRequest {

  private String dogName;

  private DogSize dogSize;

  private char dogSex;

  private int dogAge;

  private String dogBreed;

  private boolean dogChipped;
}
