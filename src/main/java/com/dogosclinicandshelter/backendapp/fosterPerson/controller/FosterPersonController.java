package com.dogosclinicandshelter.backendapp.fosterPerson.controller;

import com.dogosclinicandshelter.backendapp.fosterPerson.model.dto.FosterPersonDto;
import com.dogosclinicandshelter.backendapp.dataRequest.personRequest.PersonDataRequest;
import com.dogosclinicandshelter.backendapp.fosterPerson.service.FosterPersonService;
import com.dogosclinicandshelter.backendapp.message.MessageUtils;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/foster")
public class FosterPersonController {

  private final FosterPersonService fosterPersonService;

  public FosterPersonController(
      FosterPersonService fosterPersonService) {
    this.fosterPersonService = fosterPersonService;
  }

  @GetMapping
  public ResponseEntity<List<FosterPersonDto>> getFosterPersons() {
    return new ResponseEntity<>(fosterPersonService.getAllFosterPersons(), HttpStatus.OK);
  }

  @GetMapping("{fosterPersonId}")
  public ResponseEntity<FosterPersonDto> getFosterPerson(
      @PathVariable("fosterPersonId") Long fosterPersonId) {
    try {
      return new ResponseEntity<>(fosterPersonService.getFosterPerson(fosterPersonId),
          HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping
  public ResponseEntity<String> registerFosterPerson(
      @RequestBody PersonDataRequest request) {
    if (fosterPersonService.addFosterPerson(request)) {
      return new ResponseEntity<>(MessageUtils.FOSTER_PERSON_SAVE_DONE.toString(),
          HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(MessageUtils.FOSTER_PERSON_SAVE_FAILED.toString(), HttpStatus.OK);
    }
  }


  @PutMapping("{fosterPersonId}")
  public ResponseEntity<String> updateFosterPerson(
      @PathVariable Long fosterPersonId,
      @RequestBody PersonDataRequest updatePersonDataRequest) {
    if (fosterPersonService.updateFosterPerson(fosterPersonId, updatePersonDataRequest)) {
      return new ResponseEntity<>(MessageUtils.FOSTER_PERSON_UPDATE_DONE.toString(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(MessageUtils.FOSTER_PERSON_UPDATE_FAILED.toString(),
          HttpStatus.OK);
    }
  }

  @DeleteMapping("{fosterPersonId}")
  public ResponseEntity<String> deleteFosterPerson(
      @PathVariable("fosterPersonId") Long fosterPersonId) {
    if (fosterPersonService.deleteFosterPersonById(fosterPersonId)) {
      return new ResponseEntity<>(MessageUtils.FOSTER_PERSON_DELETE_DONE.toString(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(MessageUtils.FOSTER_PERSON_DELETE_FAILED.toString(),
          HttpStatus.OK);
    }
  }
}
