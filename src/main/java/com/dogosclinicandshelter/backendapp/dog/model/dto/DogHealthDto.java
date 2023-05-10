package com.dogosclinicandshelter.backendapp.dog.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DogHealthDto {

  private boolean dogSpayed;

  private boolean dogVaccinated;

  private boolean dogDewormed;

  public DogHealthDto(boolean dogSpayed, boolean dogVaccinated, boolean dogDewormed) {
    this.dogSpayed = dogSpayed;
    this.dogVaccinated = dogVaccinated;
    this.dogDewormed = dogDewormed;
  }
}
