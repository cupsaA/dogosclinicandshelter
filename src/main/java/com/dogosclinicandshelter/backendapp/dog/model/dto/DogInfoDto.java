package com.dogosclinicandshelter.backendapp.dog.model.dto;

import com.dogosclinicandshelter.backendapp.dog.enumClasses.DogSize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DogInfoDto {

  private Long id;

  private String dogName;

  private DogSize dogSize;

  private char dogSex;

  private int dogAge;

  private String dogBreed;

  private boolean dogChipped;

  public DogInfoDto(Long id, String dogName,
      DogSize dogSize, char dogSex, int dogAge, String dogBreed, boolean dogChipped) {
    this.id = id;
    this.dogName = dogName;
    this.dogSize = dogSize;
    this.dogSex = dogSex;
    this.dogAge = dogAge;
    this.dogBreed = dogBreed;
    this.dogChipped = dogChipped;
  }
}
