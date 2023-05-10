package com.dogosclinicandshelter.backendapp.dog.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dogosclinicandshelter.backendapp.dog.enumClasses.AdoptionStatus;
import com.dogosclinicandshelter.backendapp.dog.enumClasses.DogSize;
import com.dogosclinicandshelter.backendapp.dog.model.persistance.Dog;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DogRepositoryTest {

  @Autowired
  private DogRepository dogRepository;

  private Dog dog;

  @Test
  void findAllDogsTest() {
    List<Dog> dogList = this.dogRepository.findAll();
    assertEquals(1, dogList.size());
  }

  @Test
  void findDogByIdTest() {
    Optional<Dog> dog = this.dogRepository.findById(1L);
    assertTrue(dog.isPresent());
  }

  @Test
  void findDogByNameTest() {
    Optional<Dog> dog = this.dogRepository.findByDogName("rexy");
    assertEquals("rexy", dog.get().getDogName());
  }

  @Test
  void addDogTest() {
    Dog savedDog = this.dogRepository.save(createDog());
    assertNotNull(savedDog);
    assertEquals("Boby", savedDog.getDogName());
  }

  @Test
  void updateDogTest() {
    Optional<Dog> dog = this.dogRepository.findById(1L);
    dog.get().setDogName("Rex");
    Dog updatedDog = dogRepository.save(dog.get());
    assertEquals("Rex", updatedDog.getDogName());

  }

  @Test
  void deleteDogTest(){
    this.dogRepository.deleteById(1L);
    Optional<Dog> dog = this.dogRepository.findById(1L);
    assertFalse(dog.isPresent());
  }

  private Dog createDog() {
    dog = new Dog();
    dog.setDogName("Boby");
    dog.setDogSize(DogSize.M);
    dog.setDogSex('F');
    dog.setDogAge(2);
    dog.setDogBreed("Beagle");
    dog.setDogChipped(false);
    dog.setDogSpayed(true);
    dog.setDogVaccinated(false);
    dog.setDogDewormed(true);
    dog.setAdoptionStatus(AdoptionStatus.NOT_READY_FOR_ADOPTION);
    dog.setEntryDateTime(LocalDateTime.now());
    return dog;
  }

}
