package com.dogosclinicandshelter.backendapp.dog.service;

import com.dogosclinicandshelter.backendapp.dataRequest.dogHealthRequest.DogHealthRequest;
import com.dogosclinicandshelter.backendapp.dog.model.dto.DogHealthDto;
import java.util.List;

public interface DogHealthService {

  List<DogHealthDto> getAllDogsHealth();

  DogHealthDto getDogHealth(Long l);

  boolean addDogHealth(DogHealthRequest dogHealthRequest);

  boolean deleteDogHealthById(Long dogHealthId);

  boolean updateDogHealth(Long dogHealthId, DogHealthRequest updateReq);
}
