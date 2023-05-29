package com.dogosclinicandshelter.backendapp.adoptivePerson.service;

import com.dogosclinicandshelter.backendapp.adoptivePerson.model.dto.AdoptivePersonDto;
import com.dogosclinicandshelter.backendapp.dataRequest.personRequest.PersonDataRequest;
import java.util.List;

public interface AdoptivePersonService {

  List<AdoptivePersonDto> getAllAdoptivePersons();

  AdoptivePersonDto getAdoptivePerson(long l);

  boolean deleteAdoptivePersonById(Long id);

  boolean addAdoptivePerson(PersonDataRequest request);

  boolean updateAdoptivePerson(Long adoptivePersonId, PersonDataRequest updateReq);
}
