package com.dogosclinicandshelter.backendapp.dog.controller;

import com.dogosclinicandshelter.backendapp.dataRequest.dogInfoRequest.DogInfoRequest;
import com.dogosclinicandshelter.backendapp.dog.model.dto.DogInfoDto;
import com.dogosclinicandshelter.backendapp.dog.service.DogInfoService;
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
@RequestMapping("api/v1/dog/info")
public class DogInfoController {

  private final DogInfoService dogInfoService;

  public DogInfoController(
      DogInfoService dogInfoService) {
    this.dogInfoService = dogInfoService;
  }

  @GetMapping
  public ResponseEntity<List<DogInfoDto>> getDogsInfo() {
    return new ResponseEntity<>(dogInfoService.getAllDogsInfo(), HttpStatus.OK);
  }

  @GetMapping("{dogInfoId}")
  public ResponseEntity<DogInfoDto> getDogInfo(
      @PathVariable("dogInfoId") Long dogInfoId) {
    try {
      return new ResponseEntity<>(dogInfoService.getDogInfo(dogInfoId),
          HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping
  public ResponseEntity<String> registerDogInfo(
      @RequestBody DogInfoRequest request) {
    if (dogInfoService.addDogInfo(request)) {
      return new ResponseEntity<>(MessageUtils.DOG_INFO_SAVE_DONE.toString(),
          HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(MessageUtils.DOG_INFO_SAVE_FAILED.toString(), HttpStatus.OK);
    }
  }


  @PutMapping("{dogInfoId}")
  public ResponseEntity<String> updateDogInfo(
      @PathVariable Long dogInfoId,
      @RequestBody DogInfoRequest updateDogInfoRequest) {
    if (dogInfoService.updateDogInfo(dogInfoId, updateDogInfoRequest)) {
      return new ResponseEntity<>(MessageUtils.DOG_INFO_UPDATE_DONE.toString(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(MessageUtils.DOG_INFO_UPDATE_FAILED.toString(),
          HttpStatus.OK);
    }
  }

  @DeleteMapping("{dogInfoId}")
  public ResponseEntity<String> deleteDogInfo(
      @PathVariable("dogInfoId") Long dogInfoId) {
    if (dogInfoService.deleteDogInfoById(dogInfoId)) {
      return new ResponseEntity<>(MessageUtils.DOG_INFO_DELETE_DONE.toString(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(MessageUtils.DOG_INFO_DELETE_FAILED.toString(),
          HttpStatus.OK);
    }
  }
}
