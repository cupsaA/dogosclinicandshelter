package com.dogosclinicandshelter.backendapp.dog.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dogosclinicandshelter.backendapp.dataRequest.dogHealthRequest.DogHealthRequest;
import com.dogosclinicandshelter.backendapp.dog.mapper.DogHealthMapper;
import com.dogosclinicandshelter.backendapp.dog.model.dto.DogHealthDto;
import com.dogosclinicandshelter.backendapp.dog.model.persistance.DogHealth;
import com.dogosclinicandshelter.backendapp.dog.repository.DogHealthRepository;
import com.dogosclinicandshelter.backendapp.dog.service.DogHealthService;
import com.dogosclinicandshelter.backendapp.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DogHealthServiceImplTest {


  private DogHealthService underTest;
  private DogHealth dogHealth;
  private Long id;


  @Mock
  private DogHealthRepository dogHealthRepository;

  @Mock
  private DogHealthMapper dogHealthMapper;

  @BeforeEach
  void setup() {
    underTest = new DogHealthServiceImpl(dogHealthRepository, dogHealthMapper);
    dogHealth = this.buildDogHealth();
    id = dogHealth.getDogHealthId();
  }

  @Test
  void getAllDogInfosTest() {
    when(dogHealthRepository.findAll()).thenReturn(List.of(dogHealth));
    List<DogHealthDto> dogHealthDtos = underTest.getAllDogsHealth();

    assertEquals(1, dogHealthDtos.size());
    verify(dogHealthRepository).findAll();
  }

  @Test
  void getDogHealthTest() {
    when(dogHealthRepository.findById(1L)).thenReturn(Optional.of(dogHealth));
    when(dogHealthMapper.mapToDto(dogHealth))
        .thenReturn(
            new DogHealthDto(dogHealth.getDogHealthId(), dogHealth.isDogSpayed(),
                dogHealth.isDogVaccinated(),
                dogHealth.isDogDewormed()));

    DogHealthDto dogHealthDto = underTest.getDogHealth(1L);

    assertTrue(dogHealthDto.isDogSpayed());
    assertTrue(dogHealthDto.isDogVaccinated());
    assertTrue(dogHealthDto.isDogDewormed());
  }

  @Test
  void getDogHealthThrowResourceNotFoundExceptionTest() {
    when(dogHealthRepository.findById(1L)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> underTest.getDogHealth(id))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(
            String.format("dog health for id %s was not found", dogHealth.getDogHealthId()));
  }

  @Test
  void addDogInfoTest() {
    DogHealthRequest dogHealthRequest = requestDogHealth();

    underTest.addDogHealth(dogHealthRequest);
    ArgumentCaptor<DogHealth> healthArgumentCaptor = ArgumentCaptor
        .forClass(DogHealth.class);
    verify(dogHealthRepository).save(healthArgumentCaptor.capture());
    DogHealth capturedDogHealth = healthArgumentCaptor.getValue();

    assertNull(capturedDogHealth.getDogHealthId());
    assertTrue(capturedDogHealth.isDogDewormed());
    assertTrue(capturedDogHealth.isDogVaccinated());
    assertTrue(capturedDogHealth.isDogSpayed());
  }


  @Test
  void updateDogHealthThrowResourceNotFoundTest() {
    DogHealthRequest updateReq = new DogHealthRequest(false, false, false);

    when(dogHealthRepository.findById(dogHealth.getDogHealthId())).thenReturn(Optional.empty());
    assertThatThrownBy(() -> underTest.updateDogHealth(id, updateReq))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(String.format("dog health with id %s not found", dogHealth.getDogHealthId()));
  }

  @Test
  void updateAllDogHealthFieldsTest() {
    when(dogHealthRepository.findById(dogHealth.getDogHealthId()))
        .thenReturn(Optional.of(dogHealth));

    DogHealthRequest updateReq = new DogHealthRequest(false, false, false);
    underTest.updateDogHealth(dogHealth.getDogHealthId(), updateReq);

    ArgumentCaptor<DogHealth> dogHealthArgumentCaptor = ArgumentCaptor
        .forClass(DogHealth.class);
    verify(dogHealthRepository).save(dogHealthArgumentCaptor.capture());
    DogHealth capturedDogHealth = dogHealthArgumentCaptor.getValue();

  }

  @Test
  void deleteDogHealthByIdTest() {
    when(dogHealthRepository.existsDogHealthByDogHealthId(dogHealth.getDogHealthId()))
        .thenReturn(true);

    underTest.deleteDogHealthById(dogHealth.getDogHealthId());
    verify(dogHealthRepository).deleteById(dogHealth.getDogHealthId());
  }

  @Test
  void deleteNonExistentDogHealthByIdTest() {
    when(dogHealthRepository.existsDogHealthByDogHealthId(dogHealth.getDogHealthId()))
        .thenReturn(false);

    assertThatThrownBy(() -> underTest.deleteDogHealthById(id))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(
            String.format("dog health for id %s was not found", dogHealth.getDogHealthId()));
  }

  private DogHealthRequest requestDogHealth() {
    return new DogHealthRequest(true, true, true);
  }

  private DogHealth buildDogHealth() {
    DogHealth dogHealth = new DogHealth();
    dogHealth.setDogHealthId(1L);
    dogHealth.setDogSpayed(true);
    dogHealth.setDogVaccinated(true);
    dogHealth.setDogDewormed(true);
    return dogHealth;
  }
}
