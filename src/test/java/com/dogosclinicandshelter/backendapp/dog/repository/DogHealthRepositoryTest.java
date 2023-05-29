package com.dogosclinicandshelter.backendapp.dog.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dogosclinicandshelter.backendapp.dog.model.persistance.DogHealth;
import com.dogosclinicandshelter.backendapp.dog.model.persistance.DogInfo;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DogHealthRepositoryTest {

  @Autowired
  private DogHealthRepository dogHealthRepository;

  private DogHealth dogHealth;

  @Test
  void retrieveAllDogsHealthTest() {
    List<DogHealth> dogHealths = this.dogHealthRepository.findAll();
    assertEquals(1, dogHealths.size());
  }

  @Test
  void findDogHealthByIdTest() {
    Optional<DogHealth> dogHealth = this.dogHealthRepository.findById(1L);
    assertTrue(dogHealth.isPresent());
  }


  @Test
  void addDogHealthTest() {
    dogHealth = createDogHealth();
    DogHealth dogHealth = this.dogHealthRepository.save(this.dogHealth);
    assertNotNull(dogHealth);
    assertFalse(dogHealth.isDogVaccinated());
  }


  @Test
  void updateDogHealthTest() {
    Optional<DogHealth> dogHealth = this.dogHealthRepository.findById(1L);
    dogHealth.get().setDogSpayed(true);
    DogHealth dogHealthUpdated = this.dogHealthRepository.save(dogHealth.get());
    assertTrue(dogHealthUpdated.isDogSpayed());
  }

  @Test
  void deleteDogHealthTest() {
    this.dogHealthRepository.deleteById(1L);
    Optional<DogHealth> dogHealth = this.dogHealthRepository.findById(1L);
    assertFalse(dogHealth.isPresent());
  }

  private DogHealth createDogHealth() {
    return new DogHealth(false, false, false);
  }

}
