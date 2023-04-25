package com.dogosclinicandshelter.backendapp.fosterPerson.service;

import com.dogosclinicandshelter.backendapp.fosterPerson.model.dto.FosterPersonDto;
import com.dogosclinicandshelter.backendapp.fosterPerson.request.FosterPersonRequest;
import java.util.List;

public interface FosterPersonService {

  List<FosterPersonDto> getAllFosterPersons();

  FosterPersonDto getFosterPerson(Long fosterPersonId);

  boolean addFosterPerson(FosterPersonRequest request);

  boolean deleteFosterPersonById(Long fosterPersonId);

  boolean updateFosterPerson(Long fosterPersonId, FosterPersonRequest updateFosterPersonRequest);
}
