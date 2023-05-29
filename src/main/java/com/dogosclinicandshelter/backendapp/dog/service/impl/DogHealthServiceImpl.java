package com.dogosclinicandshelter.backendapp.dog.service.impl;

import com.dogosclinicandshelter.backendapp.dataRequest.dogHealthRequest.DogHealthRequest;
import com.dogosclinicandshelter.backendapp.dog.mapper.DogHealthMapper;
import com.dogosclinicandshelter.backendapp.dog.model.dto.DogHealthDto;
import com.dogosclinicandshelter.backendapp.dog.model.persistance.DogHealth;
import com.dogosclinicandshelter.backendapp.dog.repository.DogHealthRepository;
import com.dogosclinicandshelter.backendapp.dog.service.DogHealthService;
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
public class DogHealthServiceImpl implements DogHealthService {

  private final DogHealthRepository dogHealthRepository;
  private final DogHealthMapper dogHealthMapper;

  @Override
  public List<DogHealthDto> getAllDogsHealth() {
    return dogHealthRepository.findAll().stream().map(dogHealthMapper::mapToDto)
        .collect(Collectors.toList());
  }

  @Override
  public DogHealthDto getDogHealth(Long dogHealthId) {
    return dogHealthRepository.findById(dogHealthId).map(dogHealthMapper::mapToDto)
        .orElseThrow(() -> new ResourceNotFoundException(
            String.format("dog health for id %s was not found", dogHealthId)
        ));
  }

  @Override
  public boolean addDogHealth(DogHealthRequest dogHealthRequest) {
    DogHealth dogHealth = new DogHealth(dogHealthRequest.isDogSpayed(),
        dogHealthRequest.isDogVaccinated(), dogHealthRequest.isDogDewormed());
    dogHealthRepository.save(dogHealth);

    return true;
  }

  @Override
  public boolean deleteDogHealthById(Long dogHealthId) {
    if (!dogHealthRepository.existsDogHealthByDogHealthId(dogHealthId)) {
      throw new ResourceNotFoundException(
          String.format("dog health for id %s was not found", dogHealthId));
    }
    dogHealthRepository.deleteById(dogHealthId);
    return true;
  }

  @Override
  public boolean updateDogHealth(Long dogHealthId, DogHealthRequest updateReq) {
    DogHealth dogHealth = dogHealthRepository.findById(dogHealthId)
        .orElseThrow(() -> new ResourceNotFoundException(
            String.format(MessageUtils.DOG_HEALTH_WITH_ID_S_NOT_FOUND.toString(), dogHealthId)));

    boolean changes = false;

    changes = checkFieldsForUpdate(updateReq, dogHealth, changes);

    dogHealthRepository.save(dogHealth);

    return changes;
  }

  private boolean checkFieldsForUpdate(DogHealthRequest updateReq, DogHealth dogHealth,
      boolean changes) {
    if (updateReq.isDogSpayed() != dogHealth.isDogSpayed()) {
      dogHealth.setDogSpayed(updateReq.isDogSpayed());
      changes = true;
    }

    if (updateReq.isDogVaccinated() != dogHealth.isDogVaccinated()) {
      dogHealth.setDogSpayed(updateReq.isDogVaccinated());
      changes = true;
    }


    if (updateReq.isDogDewormed() != dogHealth.isDogDewormed()) {
      dogHealth.setDogDewormed(updateReq.isDogDewormed());
      changes = true;
    }

    if (!changes) {
      throw new RequestValidationException(ExceptionUtils.NO_DATA_CHANGES_FOUND.toString());
    }

    return changes;
  }
}
