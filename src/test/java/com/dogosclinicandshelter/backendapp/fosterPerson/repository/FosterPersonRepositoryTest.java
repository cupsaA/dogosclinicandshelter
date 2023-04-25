package com.dogosclinicandshelter.backendapp.fosterPerson.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.dogosclinicandshelter.backendapp.fosterPerson.model.persistance.FosterPerson;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class FosterPersonRepositoryTest {

  @Autowired
  private FosterPersonRepository fosterPersonRepository;

  @Test
  void findAllFosterPersonsTest() {
    List<FosterPerson> fosterPeople = this.fosterPersonRepository.findAll();
    assertEquals(1, fosterPeople.size());
  }

  @Test
  void findFosterPersonById() {
    Optional<FosterPerson> fosterPerson = this.fosterPersonRepository.findById(1L);
    assertTrue(fosterPerson.isPresent());
  }

  @Test
  void existsFosterPersonByEmailTest() {
    boolean personByEmail = this.fosterPersonRepository
        .existsFosterPersonByEmail("janedoe@email.com");
    assertTrue(personByEmail);
  }

  @Test
  void existsFosterPersonByIdTest() {
    boolean personById = this.fosterPersonRepository.existsFosterPersonById(1L);
    assertTrue(personById);
  }

  @Test
  void addFosterPersonTest() {
    FosterPerson fosterPerson = new FosterPerson("mike", "test", "miketest@gmail.com", "Brasov",
        "Street y Nr 2", "0744999888");

    FosterPerson savedFosterPerson = this.fosterPersonRepository.save(fosterPerson);
    assertNotNull(savedFosterPerson);
    assertEquals(2L, fosterPerson.getId());
  }

  @Test
  void updateFosterPersonTest(){
    FosterPerson updatedFosterPerson = new FosterPerson("janet", "doe", "janetdoe@gmail.com", "Brasov",
        "Street ABC Nr 22", "0744123123");
    FosterPerson update = this.fosterPersonRepository.save(updatedFosterPerson);
    assertNotNull(update);
    assertEquals("janet",update.getFirstName());
    assertEquals("janetdoe@gmail.com",update.getEmail());
    assertEquals("Brasov",update.getCity());
    assertEquals("Street ABC Nr 22",update.getAddress());
    assertEquals("0744123123",update.getPhoneNumber());
  }

  @Test
  void deleteFosterPerson(){
    this.fosterPersonRepository.deleteById(1L);
    Optional<FosterPerson> personById = this.fosterPersonRepository.findById(1L);
    assertFalse(personById.isPresent());
  }
}