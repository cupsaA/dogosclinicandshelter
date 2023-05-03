package com.dogosclinicandshelter.backendapp.fosterPerson.service;

import com.dogosclinicandshelter.backendapp.fosterPerson.model.dto.FosterPersonDto;
import com.dogosclinicandshelter.backendapp.personDataRequest.request.PersonDataRequest;
import java.util.List;

public interface FosterPersonService {

  List<FosterPersonDto> getAllFosterPersons();

  FosterPersonDto getFosterPerson(Long fosterPersonId);

  boolean addFosterPerson(PersonDataRequest request);

  boolean deleteFosterPersonById(Long fosterPersonId);

  boolean updateFosterPerson(Long fosterPersonId, PersonDataRequest updateReq);
}
