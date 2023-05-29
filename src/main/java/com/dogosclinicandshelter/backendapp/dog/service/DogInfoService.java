package com.dogosclinicandshelter.backendapp.dog.service;

import com.dogosclinicandshelter.backendapp.dataRequest.dogInfoRequest.DogInfoRequest;
import com.dogosclinicandshelter.backendapp.dog.model.dto.DogInfoDto;
import java.util.List;

public interface DogInfoService {

  List<DogInfoDto> getAllDogsInfo();

  DogInfoDto getDogInfo(Long l);

  boolean addDogInfo(DogInfoRequest dogInfoRequest);

  boolean deleteDogInfoById(Long dogInfoId);

  boolean updateDogInfo(Long dogInfoId, DogInfoRequest updateReq);
}
