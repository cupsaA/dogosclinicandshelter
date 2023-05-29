package com.dogosclinicandshelter.backendapp.adoptivePerson.controller;

import com.dogosclinicandshelter.backendapp.adoptivePerson.model.dto.AdoptivePersonDto;
import com.dogosclinicandshelter.backendapp.adoptivePerson.service.AdoptivePersonService;
import com.dogosclinicandshelter.backendapp.message.MessageUtils;
import com.dogosclinicandshelter.backendapp.dataRequest.personRequest.PersonDataRequest;
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
@RequestMapping("api/v1/adoptive")
public class AdoptivePersonController {

  private final AdoptivePersonService adoptivePersonService;

  public AdoptivePersonController(
      AdoptivePersonService adoptivePersonService) {
    this.adoptivePersonService = adoptivePersonService;
  }

  @GetMapping
  public ResponseEntity<List<AdoptivePersonDto>> getAdoptivePersons() {
    return new ResponseEntity<>(adoptivePersonService.getAllAdoptivePersons(), HttpStatus.OK);
  }

  @GetMapping("{adoptivePersonId}")
  public ResponseEntity<AdoptivePersonDto> getAdoptivePerson(
      @PathVariable("adoptivePersonId") Long adoptivePersonId) {
    try {
      return new ResponseEntity<>(adoptivePersonService.getAdoptivePerson(adoptivePersonId),
          HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping
  public ResponseEntity<String> registerAdoptivePerson(
      @RequestBody PersonDataRequest request) {
    if (adoptivePersonService.addAdoptivePerson(request)) {
      return new ResponseEntity<>(MessageUtils.ADOPTIVE_PERSON_SAVE_DONE.toString(),
          HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(MessageUtils.ADOPTIVE_PERSON_SAVE_FAILED.toString(),
          HttpStatus.OK);
    }
  }

  @PutMapping("{adoptivePersonId}")
  public ResponseEntity<String> updateAdoptivePerson(
      @PathVariable Long adoptivePersonId,
      @RequestBody PersonDataRequest updatePersonDataRequest) {
    if (adoptivePersonService.updateAdoptivePerson(adoptivePersonId, updatePersonDataRequest)) {
      return new ResponseEntity<>(MessageUtils.ADOPTIVE_PERSON_UPDATE_DONE.toString(),
          HttpStatus.OK);
    } else {
      return new ResponseEntity<>(MessageUtils.ADOPTIVE_PERSON_UPDATE_FAILED.toString(),
          HttpStatus.OK);
    }
  }

  @DeleteMapping("{adoptivePersonId}")
  public ResponseEntity<String> deleteAdoptivePerson(
      @PathVariable("adoptivePersonId") Long adoptivePersonId) {
    if (adoptivePersonService.deleteAdoptivePersonById(adoptivePersonId)) {
      return new ResponseEntity<>(MessageUtils.ADOPTIVE_PERSON_DELETE_DONE.toString(),
          HttpStatus.OK);
    } else {
      return new ResponseEntity<>(MessageUtils.ADOPTIVE_PERSON_DELETE_FAILED.toString(),
          HttpStatus.OK);
    }
  }
}
