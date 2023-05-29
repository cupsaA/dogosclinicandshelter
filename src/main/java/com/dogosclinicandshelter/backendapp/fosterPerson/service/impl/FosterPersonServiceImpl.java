package com.dogosclinicandshelter.backendapp.fosterPerson.service.impl;

import com.dogosclinicandshelter.backendapp.exception.DuplicateResourceException;
import com.dogosclinicandshelter.backendapp.exception.RequestValidationException;
import com.dogosclinicandshelter.backendapp.exception.ResourceNotFoundException;
import com.dogosclinicandshelter.backendapp.fosterPerson.mapper.FosterPersonMapper;
import com.dogosclinicandshelter.backendapp.fosterPerson.model.dto.FosterPersonDto;
import com.dogosclinicandshelter.backendapp.fosterPerson.model.persistance.FosterPerson;
import com.dogosclinicandshelter.backendapp.fosterPerson.repository.FosterPersonRepository;
import com.dogosclinicandshelter.backendapp.dataRequest.personRequest.PersonDataRequest;
import com.dogosclinicandshelter.backendapp.fosterPerson.service.FosterPersonService;
import com.dogosclinicandshelter.backendapp.message.ExceptionUtils;
import com.dogosclinicandshelter.backendapp.message.MessageUtils;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FosterPersonServiceImpl implements FosterPersonService {

  private final FosterPersonRepository fosterPersonRepository;
  private final FosterPersonMapper fosterPersonMapper;

  @Override
  public List<FosterPersonDto> getAllFosterPersons() {
    return fosterPersonRepository.findAll().stream()
        .map(fosterPersonMapper::mapToDto)
        .collect(
            Collectors.toList());
  }

  @Override
  public FosterPersonDto getFosterPerson(Long fosterPersonId) {
    return fosterPersonRepository.findById(fosterPersonId)
        .map(fosterPersonMapper::mapToDto)
        .orElseThrow(() -> new ResourceNotFoundException(
            String.format(MessageUtils.FOSTER_PERSON_WITH_ID_S_NOT_FOUND.toString(), fosterPersonId)));
  }

  @Override
  public boolean addFosterPerson(PersonDataRequest request) {
    if (fosterPersonRepository.existsFosterPersonByEmail(request.getEmail())) {
      throw new DuplicateResourceException(ExceptionUtils.EMAIL_ALREADY_TAKEN.toString());
    }
    FosterPerson fosterPerson = new FosterPerson(request.getFirstName(), request.getLastName(),
        request.getEmail(), request.getCity(), request.getAddress(), request.getPhoneNumber());
    fosterPersonRepository.save(fosterPerson);
    return true;
  }

  @Override
  public boolean deleteFosterPersonById(Long fosterPersonId) {
    if (!fosterPersonRepository.existsFosterPersonById(fosterPersonId)) {
      throw new ResourceNotFoundException(
          String.format(MessageUtils.FOSTER_PERSON_WITH_ID_S_NOT_FOUND.toString(), fosterPersonId));
    }
    fosterPersonRepository.deleteById(fosterPersonId);
    return true;
  }

  @Override
  public boolean updateFosterPerson(Long fosterPersonId,
      PersonDataRequest updateRequest) {
    FosterPerson fosterPerson = fosterPersonRepository.findById(fosterPersonId)
        .orElseThrow(() -> new ResourceNotFoundException(
            String.format(MessageUtils.FOSTER_PERSON_WITH_ID_S_NOT_FOUND.toString(), fosterPersonId)));

    boolean changes = false;

    changes = checkFieldsForUpdate(updateRequest, fosterPerson, changes);

    fosterPersonRepository.save(fosterPerson);
    return changes;

  }

  private boolean checkFieldsForUpdate(PersonDataRequest updateRequest, FosterPerson fosterPerson,
      boolean changes) {
    if (updateRequest.getFirstName() != null && !updateRequest
        .getFirstName().equals(fosterPerson.getFirstName())) {
      fosterPerson.setFirstName(updateRequest.getFirstName());
      changes = true;
    }

    if (updateRequest.getLastName() != null && !updateRequest
        .getLastName().equals(fosterPerson.getLastName())) {
      fosterPerson.setLastName(updateRequest.getLastName());
      changes = true;
    }
    if (updateRequest.getEmail() != null && !updateRequest.getEmail()
        .equals(fosterPerson.getEmail())) {
      if (fosterPersonRepository.existsFosterPersonByEmail(updateRequest.getEmail())) {
          throw new DuplicateResourceException(ExceptionUtils.EMAIL_ALREADY_TAKEN.toString());
      }
      fosterPerson.setEmail(updateRequest.getEmail());
      changes = true;
    }

    if (updateRequest.getCity() != null && !updateRequest
        .getCity().equals(fosterPerson.getCity())) {
      fosterPerson.setCity(updateRequest.getCity());
      changes = true;
    }
    if (updateRequest.getAddress() != null && !updateRequest
        .getAddress().equals(fosterPerson.getAddress())) {
      fosterPerson.setAddress(updateRequest.getAddress());
      changes = true;
    }
    if (updateRequest.getPhoneNumber() != null && !updateRequest
        .getPhoneNumber().equals(fosterPerson.getPhoneNumber())) {
      fosterPerson.setPhoneNumber(updateRequest.getPhoneNumber());
      changes = true;
    }

    if (!changes) {
      throw new RequestValidationException(ExceptionUtils.NO_DATA_CHANGES_FOUND.toString());
    }

    return changes;
  }
}
