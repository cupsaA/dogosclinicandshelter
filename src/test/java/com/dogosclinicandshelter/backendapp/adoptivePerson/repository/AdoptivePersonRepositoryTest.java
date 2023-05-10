package com.dogosclinicandshelter.backendapp.adoptivePerson.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.dogosclinicandshelter.backendapp.adoptivePerson.model.persistance.AdoptivePerson;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AdoptivePersonRepositoryTest {

  @Autowired
  private AdoptivePersonRepository adoptivePersonRepository;

  private AdoptivePerson adoptivePerson;

  @Test
  void findAllAdoptivePersonsTest() {
    List<AdoptivePerson> allAdoptivePersons = this.adoptivePersonRepository.findAll();
    assertEquals(1, allAdoptivePersons.size());
  }

  @Test
  void findAdoptivePersonByIdTest() {
    Optional<AdoptivePerson> adoptivePerson = this.adoptivePersonRepository.findById(1L);
    assertTrue(adoptivePerson.isPresent());
  }

  @Test
  void existsAdoptivePersonByEmailTest() {
    boolean adoptivePersonByEmail = this.adoptivePersonRepository
        .existsAdoptivePersonByEmail("andrew@email.com");
    assertTrue(adoptivePersonByEmail);
  }

  @Test
  void existsAdoptivePersonByIdTest() {
    boolean adoptivePersonById = this.adoptivePersonRepository.existsAdoptivePersonById(1L);
    assertTrue(adoptivePersonById);
  }

  @Test
  void addAdoptivePersonTest() {
    adoptivePerson = createAdoptivePerson();
    AdoptivePerson saveAdoptivePerson = this.adoptivePersonRepository.save(adoptivePerson);
    assertNotNull(saveAdoptivePerson);
    assertEquals("janet", adoptivePerson.getFirstName());
  }

  @Test
  void updateAdoptivePersonTest() {
    adoptivePerson = createAdoptivePerson();
    AdoptivePerson updateAdoptivePerson = this.adoptivePersonRepository.save(adoptivePerson);
    assertNotNull(updateAdoptivePerson);
    assertEquals("janet", updateAdoptivePerson.getFirstName());
    assertEquals("janetdoe@gmail.com", updateAdoptivePerson.getEmail());
    assertEquals("Brasov", updateAdoptivePerson.getCity());
    assertEquals("Street ABC Nr 22", updateAdoptivePerson.getAddress());
    assertEquals("0744123123", updateAdoptivePerson.getPhoneNumber());
  }


  @Test
  void deleteAdoptivePerson() {
    this.adoptivePersonRepository.deleteById(1L);
    Optional<AdoptivePerson> personById = this.adoptivePersonRepository.findById(1L);
    assertFalse(personById.isPresent());
  }

  private AdoptivePerson createAdoptivePerson() {
    return adoptivePerson = new AdoptivePerson("janet", "doe", "janetdoe@gmail.com", "Brasov",
        "Street ABC Nr 22", "0744123123");
  }

}
