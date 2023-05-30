package com.dogosclinicandshelter.backendapp.dog.controller;

import com.dogosclinicandshelter.backendapp.dataRequest.dogHealthRequest.DogHealthRequest;
import com.dogosclinicandshelter.backendapp.dog.model.dto.DogHealthDto;
import com.dogosclinicandshelter.backendapp.dog.service.DogHealthService;
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
@RequestMapping("api/v1/dog/health")
public class DogHealthController {
  private final DogHealthService dogHealthService;

  public DogHealthController(
      DogHealthService dogHealthService) {
    this.dogHealthService = dogHealthService;
  }

  @GetMapping
  public ResponseEntity<List<DogHealthDto>> getDogsHealth() {
    return new ResponseEntity<>(dogHealthService.getAllDogsHealth(), HttpStatus.OK);
  }

  @GetMapping("{dogHealthId}")
  public ResponseEntity<DogHealthDto> getDogHealth(
      @PathVariable("dogHealthId") Long dogHealthId) {
    try {
      return new ResponseEntity<>(dogHealthService.getDogHealth(dogHealthId),
          HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping
  public ResponseEntity<String> registerDogHealth(
      @RequestBody DogHealthRequest request) {
    if (dogHealthService.addDogHealth(request)) {
      return new ResponseEntity<>(MessageUtils.DOG_HEALTH_SAVE_DONE.toString(),
          HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(MessageUtils.DOG_HEALTH_SAVE_FAILED.toString(), HttpStatus.OK);
    }
  }


  @PutMapping("{dogHealthId}")
  public ResponseEntity<String> updateDogHealth(
      @PathVariable Long dogHealthId,
      @RequestBody DogHealthRequest updateDogHealthRequest) {
    if (dogHealthService.updateDogHealth(dogHealthId, updateDogHealthRequest)) {
      return new ResponseEntity<>(MessageUtils.DOG_HEALTH_UPDATE_DONE.toString(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(MessageUtils.DOG_HEALTH_UPDATE_FAILED.toString(),
          HttpStatus.OK);
    }
  }

  @DeleteMapping("{dogHealthId}")
  public ResponseEntity<String> deleteDogHealth(
      @PathVariable("dogHealthId") Long dogHealthId) {
    if (dogHealthService.deleteDogHealthById(dogHealthId)) {
      return new ResponseEntity<>(MessageUtils.DOG_HEALTH_DELETE_DONE.toString(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(MessageUtils.DOG_HEALTH_DELETE_FAILED.toString(),
          HttpStatus.OK);
    }
  }
}
