package com.dogosclinicandshelter.backendapp.adoptivePerson.service.impl;

import com.dogosclinicandshelter.backendapp.adoptivePerson.mapper.AdoptivePersonMapper;
import com.dogosclinicandshelter.backendapp.adoptivePerson.model.dto.AdoptivePersonDto;
import com.dogosclinicandshelter.backendapp.adoptivePerson.model.persistance.AdoptivePerson;
import com.dogosclinicandshelter.backendapp.adoptivePerson.repository.AdoptivePersonRepository;
import com.dogosclinicandshelter.backendapp.adoptivePerson.service.AdoptivePersonService;
import com.dogosclinicandshelter.backendapp.exception.DuplicateResourceException;
import com.dogosclinicandshelter.backendapp.exception.RequestValidationException;
import com.dogosclinicandshelter.backendapp.exception.ResourceNotFoundException;
import com.dogosclinicandshelter.backendapp.personDataRequest.request.PersonDataRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdoptivePersonServiceImpl implements AdoptivePersonService {

  private final AdoptivePersonRepository adoptivePersonRepository;
  private final AdoptivePersonMapper adoptivePersonMapper;

  @Override
  public List<AdoptivePersonDto> getAllAdoptivePersons() {
    return adoptivePersonRepository.findAll().stream()
        .map(adoptivePersonMapper::mapToDto)
        .collect(Collectors.toList());
  }

  @Override
  public AdoptivePersonDto getAdoptivePerson(long adoptivePersonId) {
    return adoptivePersonRepository.findById(adoptivePersonId).map(adoptivePersonMapper::mapToDto)
        .orElseThrow(() -> new ResourceNotFoundException(
            String.format("adoptive person with id %s not found", adoptivePersonId)));
  }

  @Override
  public boolean deleteAdoptivePersonById(Long adoptivePersonId) {
    if (!adoptivePersonRepository.existsAdoptivePersonById(adoptivePersonId)) {
      throw new ResourceNotFoundException(
          String.format("adoptive person with id %s not found", adoptivePersonId));
    }
    adoptivePersonRepository.deleteById(adoptivePersonId);
    return true;
  }

  @Override
  public boolean addAdoptivePerson(PersonDataRequest request) {
    if (adoptivePersonRepository.existsAdoptivePersonByEmail(request.getEmail())) {
      throw new DuplicateResourceException("email already taken");
    }

    AdoptivePerson adoptivePerson = new AdoptivePerson(request.getFirstName(),
        request.getLastName(),
        request.getEmail(), request.getCity(), request.getAddress(), request.getPhoneNumber());
    adoptivePersonRepository.save(adoptivePerson);
    return true;
  }

  @Override
  public boolean updateAdoptivePerson(Long adoptivePersonId, PersonDataRequest updateReq) {
    AdoptivePerson adoptivePerson = adoptivePersonRepository.findById(adoptivePersonId)
        .orElseThrow(() -> new ResourceNotFoundException(
            String.format("adoptive person with id %s not found", adoptivePersonId)));

    boolean changes = false;

    changes = checkFieldsForUpdate(updateReq, adoptivePerson, changes);

    adoptivePersonRepository.save(adoptivePerson);

    return changes;
  }

  private boolean checkFieldsForUpdate(PersonDataRequest updateRequest, AdoptivePerson adoptivePerson,
      boolean changes) {
    if (updateRequest.getFirstName() != null && !updateRequest
        .getFirstName().equals(adoptivePerson.getFirstName())) {
      adoptivePerson.setFirstName(updateRequest.getFirstName());
      changes = true;
    }

    if (updateRequest.getLastName() != null && !updateRequest
        .getLastName().equals(adoptivePerson.getLastName())) {
      adoptivePerson.setLastName(updateRequest.getLastName());
      changes = true;
    }
    if (updateRequest.getEmail() != null && !updateRequest.getEmail()
        .equals(adoptivePerson.getEmail())) {
      if (adoptivePersonRepository.existsAdoptivePersonByEmail(updateRequest.getEmail())) {
        throw new DuplicateResourceException("email already taken");
      }
      adoptivePerson.setEmail(updateRequest.getEmail());
      changes = true;
    }

    if (updateRequest.getCity() != null && !updateRequest
        .getCity().equals(adoptivePerson.getCity())) {
      adoptivePerson.setCity(updateRequest.getCity());
      changes = true;
    }
    if (updateRequest.getAddress() != null && !updateRequest
        .getAddress().equals(adoptivePerson.getAddress())) {
      adoptivePerson.setAddress(updateRequest.getAddress());
      changes = true;
    }
    if (updateRequest.getPhoneNumber() != null && !updateRequest
        .getPhoneNumber().equals(adoptivePerson.getPhoneNumber())) {
      adoptivePerson.setPhoneNumber(updateRequest.getPhoneNumber());
      changes = true;
    }

    if (!changes) {
      throw new RequestValidationException("no data changes found");
    }

    return changes;
  }
}
