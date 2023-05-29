package com.dogosclinicandshelter.backendapp.dog.service.impl;

import com.dogosclinicandshelter.backendapp.dataRequest.dogInfoRequest.DogInfoRequest;
import com.dogosclinicandshelter.backendapp.dog.mapper.DogInfoMapper;
import com.dogosclinicandshelter.backendapp.dog.model.dto.DogInfoDto;
import com.dogosclinicandshelter.backendapp.dog.model.persistance.DogInfo;
import com.dogosclinicandshelter.backendapp.dog.repository.DogInfoRepository;
import com.dogosclinicandshelter.backendapp.dog.service.DogInfoService;
import com.dogosclinicandshelter.backendapp.exception.RequestValidationException;
import com.dogosclinicandshelter.backendapp.exception.ResourceNotFoundException;
import com.dogosclinicandshelter.backendapp.message.ExceptionUtils;
import com.dogosclinicandshelter.backendapp.message.MessageUtils;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DogInfoServiceImpl implements DogInfoService {

  private final DogInfoRepository dogInfoRepository;
  private final DogInfoMapper dogInfoMapper;


  @Override
  public List<DogInfoDto> getAllDogsInfo() {
    return dogInfoRepository.findAll().stream()
        .map(dogInfoMapper::mapToDto)
        .collect(Collectors.toList());
  }

  @Override
  public DogInfoDto getDogInfo(Long dogInfoId) {
    return dogInfoRepository.findById(dogInfoId).map(dogInfoMapper::mapToDto)
        .orElseThrow(() -> new ResourceNotFoundException(
            String.format("dog information for id %s was not found", dogInfoId)
        ));
  }

  @Override
  public boolean addDogInfo(DogInfoRequest dogInfoRequest) {
    DogInfo dogInfo = new DogInfo(dogInfoRequest.getDogName(), dogInfoRequest.getDogSize(),
        dogInfoRequest.getDogSex(), dogInfoRequest.getDogAge(), dogInfoRequest.getDogBreed(),
        dogInfoRequest.isDogChipped());

    dogInfoRepository.save(dogInfo);
    return true;
  }

  @Override
  public boolean updateDogInfo(Long dogInfoId, DogInfoRequest updateReq) {
    DogInfo dogInfo = dogInfoRepository.findById(dogInfoId)
        .orElseThrow(() -> new ResourceNotFoundException(
            String.format(MessageUtils.DOG_INFO_WITH_ID_S_NOT_FOUND.toString(), dogInfoId)));

    boolean changes = false;

    changes = checkFieldsForUpdate(updateReq, dogInfo, changes);

    dogInfoRepository.save(dogInfo);

    return changes;
  }


  @Override
  public boolean deleteDogInfoById(Long dogInfoId) {
    if (!dogInfoRepository.existsDogInfoByDogInfoId(dogInfoId)) {
      throw new ResourceNotFoundException(
          String.format("dog information for id %s was not found", dogInfoId));
    }
    dogInfoRepository.deleteById(dogInfoId);
    return true;
  }

  private boolean checkFieldsForUpdate(DogInfoRequest updateReq, DogInfo dogInfo, boolean changes) {
    if (updateReq.getDogName() != null && !updateReq
        .getDogName().equals(dogInfo.getDogName())) {
      dogInfo.setDogName(updateReq.getDogName());
      changes = true;
    }
    if (updateReq.getDogSize() != null && !updateReq
        .getDogSize().equals(dogInfo.getDogSize())) {
      dogInfo.setDogSize(updateReq.getDogSize());
      changes = true;
    }
    if (updateReq.getDogSex() != '\0' && updateReq
        .getDogSex() != dogInfo.getDogSex()) {
      dogInfo.setDogSex(updateReq.getDogSex());
      changes = true;
    }
    if (updateReq.getDogAge() >= 0 && updateReq
        .getDogAge() != dogInfo.getDogAge()) {
      dogInfo.setDogAge(updateReq.getDogAge());
      changes = true;
    }
    if (updateReq.getDogBreed() != null && !updateReq
        .getDogBreed().equals(dogInfo.getDogBreed())) {
      dogInfo.setDogBreed(updateReq.getDogBreed());
      changes = true;
    }
    if (updateReq.isDogChipped() != dogInfo.isDogChipped()) {
      dogInfo.setDogChipped(updateReq.isDogChipped());
      changes = true;
    }

    if (!changes) {
      throw new RequestValidationException(ExceptionUtils.NO_DATA_CHANGES_FOUND.toString());
    }

    return changes;
  }


}
