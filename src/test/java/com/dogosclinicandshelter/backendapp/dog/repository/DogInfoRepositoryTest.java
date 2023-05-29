package com.dogosclinicandshelter.backendapp.dog.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dogosclinicandshelter.backendapp.dog.enumClasses.DogSize;
import com.dogosclinicandshelter.backendapp.dog.model.persistance.DogInfo;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DogInfoRepositoryTest {

  @Autowired
  private DogInfoRepository dogInfoRepository;

  private DogInfo dogInfo;

  @Test
  void retrieveAllDogsInfoTest() {
    List<DogInfo> dogInfos = this.dogInfoRepository.findAll();
    assertEquals(1, dogInfos.size());
  }

  @Test
  void findDogInfoByIdTest() {
    Optional<DogInfo> dogInfo = this.dogInfoRepository.findById(1L);
    assertTrue(dogInfo.isPresent());
  }

  @Test
  void addDogInfoTest() {
    dogInfo = createDogInfo();
    DogInfo dogInfoSaved = this.dogInfoRepository.save(this.dogInfo);
    assertNotNull(dogInfoSaved);
    assertEquals("rexy", dogInfoSaved.getDogName());
  }

  @Test
  void updateDogInfoTest() {
    Optional<DogInfo> dogInfo = this.dogInfoRepository.findById(1L);
    dogInfo.get().setDogName("ace");
    DogInfo dogInfoSaved = this.dogInfoRepository.save(dogInfo.get());
    assertEquals("ace", dogInfoSaved.getDogName());
  }

  @Test
  void deleteDogInfoTest() {
    this.dogInfoRepository.deleteById(1L);
    Optional<DogInfo> dogInfo = this.dogInfoRepository.findById(1L);
    assertFalse(dogInfo.isPresent());
  }

  @Test
  void existsDogInfoByIdTest() {
    boolean dogInfoById = this.dogInfoRepository.existsDogInfoByDogInfoId(1L);
    assertTrue(dogInfoById);
  }

  private DogInfo createDogInfo() {
    return new DogInfo("rexy", DogSize.M, 'M', 2, "Beagle", true);
  }
}
