package com.dogosclinicandshelter.backendapp.dataRequest.dogHealthRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DogHealthRequest {

  private boolean dogSpayed;

  private boolean dogVaccinated;

  private boolean dogDewormed;
}
