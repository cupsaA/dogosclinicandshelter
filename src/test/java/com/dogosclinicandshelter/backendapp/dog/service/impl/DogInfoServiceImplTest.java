package com.dogosclinicandshelter.backendapp.dog.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dogosclinicandshelter.backendapp.dataRequest.dogInfoRequest.DogInfoRequest;
import com.dogosclinicandshelter.backendapp.dog.enumClasses.DogSize;
import com.dogosclinicandshelter.backendapp.dog.mapper.DogInfoMapper;
import com.dogosclinicandshelter.backendapp.dog.model.dto.DogInfoDto;
import com.dogosclinicandshelter.backendapp.dog.model.persistance.DogInfo;
import com.dogosclinicandshelter.backendapp.dog.repository.DogInfoRepository;
import com.dogosclinicandshelter.backendapp.dog.service.DogInfoService;
import com.dogosclinicandshelter.backendapp.exception.RequestValidationException;
import com.dogosclinicandshelter.backendapp.exception.ResourceNotFoundException;
import com.dogosclinicandshelter.backendapp.message.ExceptionUtils;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DogInfoServiceImplTest {

  private DogInfoService underTest;
  private DogInfo dogInfo;


  @Mock
  private DogInfoRepository dogInfoRepository;

  @Mock
  private DogInfoMapper dogInfoMapper;


  @BeforeEach
  void setup() {
    underTest = new DogInfoServiceImpl(dogInfoRepository, dogInfoMapper);
    dogInfo = this.buildDogInfo();
  }

  @Test
  void getAllDogInfosTest() {
    when(dogInfoRepository.findAll()).thenReturn(List.of(dogInfo));
    List<DogInfoDto> dogInfoDtos = underTest.getAllDogsInfo();

    assertEquals(1, dogInfoDtos.size());
    verify(dogInfoRepository).findAll();
  }

  @Test
  void getDogInfoTest() {
    when(dogInfoRepository.findById(1L)).thenReturn(Optional.of(dogInfo));
    when(dogInfoMapper.mapToDto(dogInfo))
        .thenReturn(
            new DogInfoDto(dogInfo.getDogInfoId(), dogInfo.getDogName(), dogInfo.getDogSize(),
                dogInfo.getDogSex(), dogInfo.getDogAge(), dogInfo.getDogBreed(),
                dogInfo.isDogChipped()));

    DogInfoDto dogInfoDto = underTest.getDogInfo(1L);

    assertEquals("max", dogInfoDto.getDogName());
    assertEquals(DogSize.S, dogInfoDto.getDogSize());
    assertEquals('M', dogInfoDto.getDogSex());
    assertEquals(8, dogInfoDto.getDogAge());
    assertEquals("shih-tzu", dogInfoDto.getDogBreed());
    assertFalse(dogInfoDto.isDogChipped());
  }

  @Test
  void getDogInfoThrowResourceNotFoundExceptionTest() {
    when(dogInfoRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> underTest.getDogInfo(dogInfo.getDogInfoId()))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(
            String.format("dog information for id %s was not found", dogInfo.getDogInfoId()));
  }

  @Test
  void addDogInfoTest() {
    DogInfoRequest dogInfoRequest = requestDogInfo();

    underTest.addDogInfo(dogInfoRequest);
    ArgumentCaptor<DogInfo> infoArgumentCaptor = ArgumentCaptor
        .forClass(DogInfo.class);
    verify(dogInfoRepository).save(infoArgumentCaptor.capture());
    DogInfo capturedDogInfo = infoArgumentCaptor.getValue();

    assertNull(capturedDogInfo.getDogInfoId());
    assertEquals("rocky", capturedDogInfo.getDogName());
    assertEquals(DogSize.S, capturedDogInfo.getDogSize());
    assertEquals('M', capturedDogInfo.getDogSex());
    assertEquals(8, capturedDogInfo.getDogAge());
    assertEquals("bichon", capturedDogInfo.getDogBreed());
    assertTrue(capturedDogInfo.isDogChipped());
  }

  @Test
  void updateDogInfoThrowResourceNotFoundTest() {
    DogInfoRequest updateReq = new DogInfoRequest("rocky", DogSize.S, 'M', 8, "bichon", true);

    when(dogInfoRepository.findById(dogInfo.getDogInfoId())).thenReturn(Optional.empty());
    assertThatThrownBy(() -> underTest.updateDogInfo(dogInfo.getDogInfoId(), updateReq))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(String.format("dog info with id %s not found", dogInfo.getDogInfoId()));
  }

  @Test
  void updateAllDogInfoFieldsTest() {
    when(dogInfoRepository.findById(dogInfo.getDogInfoId())).thenReturn(Optional.of(dogInfo));

    DogInfoRequest updateReq = new DogInfoRequest("bobby", DogSize.M, 'M', 9, "beagle", false);
    underTest.updateDogInfo(dogInfo.getDogInfoId(), updateReq);

    ArgumentCaptor<DogInfo> dogInfoArgumentCaptor = ArgumentCaptor
        .forClass(DogInfo.class);
    verify(dogInfoRepository).save(dogInfoArgumentCaptor.capture());
    DogInfo capturedDogInfo = dogInfoArgumentCaptor.getValue();

    assertEquals("bobby", capturedDogInfo.getDogName());
    assertEquals(DogSize.M, capturedDogInfo.getDogSize());
    assertEquals('M', capturedDogInfo.getDogSex());
    assertEquals("beagle", capturedDogInfo.getDogBreed());
    assertEquals(9, capturedDogInfo.getDogAge());
    assertFalse(capturedDogInfo.isDogChipped());
  }

  @Test
  void updateDogInfoNameFieldTest() {
    when(dogInfoRepository.findById(dogInfo.getDogInfoId())).thenReturn(Optional.of(dogInfo));

    DogInfoRequest updateReq = new DogInfoRequest("bobby", DogSize.S, 'M', 9, "bichon", true);
    underTest.updateDogInfo(dogInfo.getDogInfoId(), updateReq);

    ArgumentCaptor<DogInfo> dogInfoArgumentCaptor = ArgumentCaptor
        .forClass(DogInfo.class);
    verify(dogInfoRepository).save(dogInfoArgumentCaptor.capture());
    DogInfo capturedDogInfo = dogInfoArgumentCaptor.getValue();

    assertEquals("bobby", capturedDogInfo.getDogName());
    assertEquals(DogSize.S, capturedDogInfo.getDogSize());
    assertEquals('M', capturedDogInfo.getDogSex());
    assertEquals("bichon", capturedDogInfo.getDogBreed());
    assertEquals(9, capturedDogInfo.getDogAge());
    assertTrue(capturedDogInfo.isDogChipped());
  }

  @Test
  void updateDogInfoSizeFieldTest() {
    when(dogInfoRepository.findById(dogInfo.getDogInfoId())).thenReturn(Optional.of(dogInfo));

    DogInfoRequest updateReq = new DogInfoRequest("rocky", DogSize.M, 'M', 9, "bichon", true);
    underTest.updateDogInfo(dogInfo.getDogInfoId(), updateReq);

    ArgumentCaptor<DogInfo> dogInfoArgumentCaptor = ArgumentCaptor
        .forClass(DogInfo.class);
    verify(dogInfoRepository).save(dogInfoArgumentCaptor.capture());
    DogInfo capturedDogInfo = dogInfoArgumentCaptor.getValue();

    assertEquals("rocky", capturedDogInfo.getDogName());
    assertEquals(DogSize.M, capturedDogInfo.getDogSize());
    assertEquals('M', capturedDogInfo.getDogSex());
    assertEquals("bichon", capturedDogInfo.getDogBreed());
    assertEquals(9, capturedDogInfo.getDogAge());
    assertTrue(capturedDogInfo.isDogChipped());
  }

  @Test
  void updateDogInfoSexFieldTest() {
    when(dogInfoRepository.findById(dogInfo.getDogInfoId())).thenReturn(Optional.of(dogInfo));

    DogInfoRequest updateReq = new DogInfoRequest("rocky", DogSize.S, 'F', 9, "bichon", true);
    underTest.updateDogInfo(dogInfo.getDogInfoId(), updateReq);

    ArgumentCaptor<DogInfo> dogInfoArgumentCaptor = ArgumentCaptor
        .forClass(DogInfo.class);
    verify(dogInfoRepository).save(dogInfoArgumentCaptor.capture());
    DogInfo capturedDogInfo = dogInfoArgumentCaptor.getValue();

    assertEquals("rocky", capturedDogInfo.getDogName());
    assertEquals(DogSize.S, capturedDogInfo.getDogSize());
    assertEquals('F', capturedDogInfo.getDogSex());
    assertEquals("bichon", capturedDogInfo.getDogBreed());
    assertEquals(9, capturedDogInfo.getDogAge());
    assertTrue(capturedDogInfo.isDogChipped());
  }

  @Test
  void updateDogInfoAgeFieldTest() {
    when(dogInfoRepository.findById(dogInfo.getDogInfoId())).thenReturn(Optional.of(dogInfo));

    DogInfoRequest updateReq = new DogInfoRequest("rocky", DogSize.S, 'M', 7, "bichon", true);
    underTest.updateDogInfo(dogInfo.getDogInfoId(), updateReq);

    ArgumentCaptor<DogInfo> dogInfoArgumentCaptor = ArgumentCaptor
        .forClass(DogInfo.class);
    verify(dogInfoRepository).save(dogInfoArgumentCaptor.capture());
    DogInfo capturedDogInfo = dogInfoArgumentCaptor.getValue();

    assertEquals("rocky", capturedDogInfo.getDogName());
    assertEquals(DogSize.S, capturedDogInfo.getDogSize());
    assertEquals('M', capturedDogInfo.getDogSex());
    assertEquals("bichon", capturedDogInfo.getDogBreed());
    assertEquals(7, capturedDogInfo.getDogAge());
    assertTrue(capturedDogInfo.isDogChipped());
  }

  @Test
  void updateDogInfoDogBreedFieldTest() {
    when(dogInfoRepository.findById(dogInfo.getDogInfoId())).thenReturn(Optional.of(dogInfo));

    DogInfoRequest updateReq = new DogInfoRequest("rocky", DogSize.S, 'M', 9, "beagle", true);
    underTest.updateDogInfo(dogInfo.getDogInfoId(), updateReq);

    ArgumentCaptor<DogInfo> dogInfoArgumentCaptor = ArgumentCaptor
        .forClass(DogInfo.class);
    verify(dogInfoRepository).save(dogInfoArgumentCaptor.capture());
    DogInfo capturedDogInfo = dogInfoArgumentCaptor.getValue();

    assertEquals("rocky", capturedDogInfo.getDogName());
    assertEquals(DogSize.S, capturedDogInfo.getDogSize());
    assertEquals('M', capturedDogInfo.getDogSex());
    assertEquals("beagle", capturedDogInfo.getDogBreed());
    assertEquals(9, capturedDogInfo.getDogAge());
    assertTrue(capturedDogInfo.isDogChipped());
  }

  @Test
  void updateDogInfoDogIsChippedFieldTest() {
    when(dogInfoRepository.findById(dogInfo.getDogInfoId())).thenReturn(Optional.of(dogInfo));

    DogInfoRequest updateReq = new DogInfoRequest("rocky", DogSize.S, 'M', 9, "bichon", false);
    underTest.updateDogInfo(dogInfo.getDogInfoId(), updateReq);

    ArgumentCaptor<DogInfo> dogInfoArgumentCaptor = ArgumentCaptor
        .forClass(DogInfo.class);
    verify(dogInfoRepository).save(dogInfoArgumentCaptor.capture());
    DogInfo capturedDogInfo = dogInfoArgumentCaptor.getValue();

    assertEquals("rocky", capturedDogInfo.getDogName());
    assertEquals(DogSize.S, capturedDogInfo.getDogSize());
    assertEquals('M', capturedDogInfo.getDogSex());
    assertEquals("bichon", capturedDogInfo.getDogBreed());
    assertEquals(9, capturedDogInfo.getDogAge());
    assertFalse(capturedDogInfo.isDogChipped());
  }

  @Test
  void updateWillThrowNoDataChangesDogInfoTest() {
    when(dogInfoRepository.findById(dogInfo.getDogInfoId())).thenReturn(
        Optional.of(dogInfo));

    DogInfoRequest updateReq = new DogInfoRequest(dogInfo.getDogName(),
        dogInfo.getDogSize(), dogInfo.getDogSex(),
        dogInfo.getDogAge(), dogInfo.getDogBreed(), dogInfo.isDogChipped());

    assertThatThrownBy(() -> underTest.updateDogInfo(dogInfo.getDogInfoId(), updateReq))
        .isInstanceOf(RequestValidationException.class).hasMessage(ExceptionUtils.NO_DATA_CHANGES_FOUND.toString());
  }


  @Test
  void deleteDogInfoByIdTest() {
    when(dogInfoRepository.existsDogInfoByDogInfoId(dogInfo.getDogInfoId())).thenReturn(true);

    underTest.deleteDogInfoById(dogInfo.getDogInfoId());
    verify(dogInfoRepository).deleteById(dogInfo.getDogInfoId());
  }

  @Test
  void deleteNonExistentDogInfoByIdTest() {

    when(dogInfoRepository.existsDogInfoByDogInfoId(dogInfo.getDogInfoId()))
        .thenReturn(false);

    assertThatThrownBy(() -> underTest.deleteDogInfoById(dogInfo.getDogInfoId()))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(
            String.format("dog information for id %s was not found", dogInfo.getDogInfoId()));
  }

  private DogInfoRequest requestDogInfo() {
    return new DogInfoRequest("rocky", DogSize.S, 'M', 8, "bichon", true);
  }

  private DogInfo buildDogInfo() {
    DogInfo buildDogInfo = new DogInfo();
    buildDogInfo.setDogInfoId(1L);
    buildDogInfo.setDogName("max");
    buildDogInfo.setDogSize(DogSize.S);
    buildDogInfo.setDogSex('M');
    buildDogInfo.setDogAge(8);
    buildDogInfo.setDogBreed("shih-tzu");
    buildDogInfo.setDogChipped(false);
    return buildDogInfo;

  }

}
