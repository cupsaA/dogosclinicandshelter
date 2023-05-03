package com.dogosclinicandshelter.backendapp.fosterPerson.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.dogosclinicandshelter.backendapp.exception.DuplicateResourceException;
import com.dogosclinicandshelter.backendapp.exception.RequestValidationException;
import com.dogosclinicandshelter.backendapp.exception.ResourceNotFoundException;
import com.dogosclinicandshelter.backendapp.fosterPerson.mapper.FosterPersonMapper;
import com.dogosclinicandshelter.backendapp.fosterPerson.model.dto.FosterPersonDto;
import com.dogosclinicandshelter.backendapp.fosterPerson.model.persistance.FosterPerson;
import com.dogosclinicandshelter.backendapp.fosterPerson.repository.FosterPersonRepository;
import com.dogosclinicandshelter.backendapp.personDataRequest.request.PersonDataRequest;
import com.dogosclinicandshelter.backendapp.fosterPerson.service.FosterPersonService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FosterPersonServiceImplTest {


  private FosterPersonService underTest;
  private FosterPerson fosterPerson;

  @Mock
  private FosterPersonRepository fosterPersonRepository;

  @Mock
  private FosterPersonMapper fosterPersonMapper;


  @BeforeEach
  void setup() {
    underTest = new FosterPersonServiceImpl(fosterPersonRepository, fosterPersonMapper);
    fosterPerson = this.buildFosterPerson();
  }

  @Test
  void getAllFosterPersonsTest() {
    when(fosterPersonRepository.findAll()).thenReturn(List.of(fosterPerson));
    List<FosterPersonDto> allFosterPersons = underTest.getAllFosterPersons();

    assertEquals(1, allFosterPersons.size());
    verify(fosterPersonRepository).findAll();
  }

  @Test
  void getFosterPersonThrowResourceNotFoundExceptionTest() {

    when(fosterPersonRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> underTest.getFosterPerson(fosterPerson.getId()))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(String.format("foster person with id %s not found", fosterPerson.getId()));
  }

  @Test
  void getFosterPersonTest() {

    when(fosterPersonRepository.findById(1L)).thenReturn(Optional.of(fosterPerson));

    when(fosterPersonMapper
        .mapToDto(fosterPerson))
        .thenReturn(new FosterPersonDto(fosterPerson.getId(), fosterPerson.getFirstName(),
            fosterPerson.getLastName(), fosterPerson.getEmail(), fosterPerson.getCity(),
            fosterPerson.getAddress(), fosterPerson.getPhoneNumber()));

    FosterPersonDto fosterPersonDto = underTest.getFosterPerson(1L);

    checkFosterPersonDtoFields(fosterPersonDto);
  }

  @Test
  void addFosterPersonWithExistingEmailTest() {
    PersonDataRequest request = requestFosterPerson();

    when(fosterPersonRepository.existsFosterPersonByEmail(request.getEmail())).thenReturn(true);

    assertThatThrownBy(() -> underTest.addFosterPerson(request))
        .isInstanceOf(DuplicateResourceException.class).hasMessage("email already taken");
  }

  @Test
  void addFosterPersonTest() {
    PersonDataRequest request = requestFosterPerson();

    when(fosterPersonRepository.existsFosterPersonByEmail(request.getEmail())).thenReturn(false);

    underTest.addFosterPerson(request);
    ArgumentCaptor<FosterPerson> fosterPersonArgumentCaptor = ArgumentCaptor
        .forClass(FosterPerson.class);
    verify(fosterPersonRepository).save(fosterPersonArgumentCaptor.capture());
    FosterPerson capturedFosterPerson = fosterPersonArgumentCaptor.getValue();

    assertEquals(null, capturedFosterPerson.getId());
    assertEquals("john", capturedFosterPerson.getFirstName());
    assertEquals("doe", capturedFosterPerson.getLastName());
    assertEquals("jon@email.com", capturedFosterPerson.getEmail());
    assertEquals("Random Street", capturedFosterPerson.getAddress());
    assertEquals("Bucuresti", capturedFosterPerson.getCity());
    assertEquals("0749334455", capturedFosterPerson.getPhoneNumber());
  }

  @Test
  void updateFosterPersonThrowResourceNotFoundTest() {
    PersonDataRequest updateReq = new PersonDataRequest("jonny", "donny", "newEmail",
        "Brasov", "Street NewStreet 1", "0755223311");

    when(fosterPersonRepository.findById(fosterPerson.getId())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> underTest.updateFosterPerson(fosterPerson.getId(), updateReq))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(String.format("foster person with id %s not found", fosterPerson.getId()));
  }

  @Test
  void updateAllFosterPersonPropertiesTest() {
    when(fosterPersonRepository.findById(fosterPerson.getId())).thenReturn(
        Optional.of(fosterPerson));

    String newEmail = "jonny@yahoo.com";

    PersonDataRequest updateReq = new PersonDataRequest("jonny", "donny", newEmail,
        "Brasov", "Street NewStreet 1", "0755223311");

    when(fosterPersonRepository.existsFosterPersonByEmail(newEmail)).thenReturn(false);

    underTest.updateFosterPerson(fosterPerson.getId(), updateReq);

    ArgumentCaptor<FosterPerson> fosterPersonArgumentCaptor = ArgumentCaptor
        .forClass(FosterPerson.class);
    verify(fosterPersonRepository).save(fosterPersonArgumentCaptor.capture());
    FosterPerson capturedFosterPerson = fosterPersonArgumentCaptor.getValue();

    assertEquals("jonny", capturedFosterPerson.getFirstName());
    assertEquals("donny", capturedFosterPerson.getLastName());
    assertEquals(newEmail, capturedFosterPerson.getEmail());
    assertEquals("Brasov", capturedFosterPerson.getCity());
    assertEquals("Street NewStreet 1", capturedFosterPerson.getAddress());
    assertEquals("0755223311", capturedFosterPerson.getPhoneNumber());
  }

  @Test
  void updateFirstNameFosterPersonTest() {
    when(fosterPersonRepository.findById(fosterPerson.getId())).thenReturn(
        Optional.of(fosterPerson));

    PersonDataRequest updateReq = new PersonDataRequest("jonny", null, null,
        null, null, null);

    underTest.updateFosterPerson(fosterPerson.getId(), updateReq);

    ArgumentCaptor<FosterPerson> fosterPersonArgumentCaptor = ArgumentCaptor
        .forClass(FosterPerson.class);
    verify(fosterPersonRepository).save(fosterPersonArgumentCaptor.capture());
    FosterPerson capturedFosterPerson = fosterPersonArgumentCaptor.getValue();

    assertEquals("jonny", capturedFosterPerson.getFirstName());
    assertEquals(fosterPerson.getLastName(), capturedFosterPerson.getLastName());
    assertEquals(fosterPerson.getEmail(), capturedFosterPerson.getEmail());
    assertEquals(fosterPerson.getCity(), capturedFosterPerson.getCity());
    assertEquals(fosterPerson.getAddress(), capturedFosterPerson.getAddress());
    assertEquals(fosterPerson.getPhoneNumber(), capturedFosterPerson.getPhoneNumber());
  }


  @Test
  void updateLastNameFosterPersonTest() {
    when(fosterPersonRepository.findById(fosterPerson.getId())).thenReturn(
        Optional.of(fosterPerson));

    PersonDataRequest updateReq = new PersonDataRequest(null, "donny", null,
        null, null, null);

    underTest.updateFosterPerson(fosterPerson.getId(), updateReq);

    ArgumentCaptor<FosterPerson> fosterPersonArgumentCaptor = ArgumentCaptor
        .forClass(FosterPerson.class);
    verify(fosterPersonRepository).save(fosterPersonArgumentCaptor.capture());
    FosterPerson capturedFosterPerson = fosterPersonArgumentCaptor.getValue();

    assertEquals(fosterPerson.getFirstName(), capturedFosterPerson.getFirstName());
    assertEquals("donny", capturedFosterPerson.getLastName());
    assertEquals(fosterPerson.getEmail(), capturedFosterPerson.getEmail());
    assertEquals(fosterPerson.getCity(), capturedFosterPerson.getCity());
    assertEquals(fosterPerson.getAddress(), capturedFosterPerson.getAddress());
    assertEquals(fosterPerson.getPhoneNumber(), capturedFosterPerson.getPhoneNumber());
  }


  @Test
  void updateEmailThrowDuplicateResourceNameFosterPersonTest() {
    when(fosterPersonRepository.findById(fosterPerson.getId())).thenReturn(
        Optional.of(fosterPerson));

    PersonDataRequest updateReq = new PersonDataRequest(null, null, "newEmail",
        null, null, null);
    when(fosterPersonRepository.existsFosterPersonByEmail("newEmail"))
        .thenReturn(true);

    assertThatThrownBy(() -> underTest.updateFosterPerson(fosterPerson.getId(), updateReq))
        .isInstanceOf(DuplicateResourceException.class)
        .hasMessage("email already taken");
  }

  @Test
  void updateEmailNameFosterPersonTest() {
    when(fosterPersonRepository.findById(fosterPerson.getId())).thenReturn(
        Optional.of(fosterPerson));

    String newEmail = "jonny@yahoo.com";

    PersonDataRequest updateReq = new PersonDataRequest(null, null, newEmail,
        null, null, null);
    when(fosterPersonRepository.existsFosterPersonByEmail(newEmail)).thenReturn(false);

    underTest.updateFosterPerson(fosterPerson.getId(), updateReq);

    ArgumentCaptor<FosterPerson> fosterPersonArgumentCaptor = ArgumentCaptor
        .forClass(FosterPerson.class);
    verify(fosterPersonRepository).save(fosterPersonArgumentCaptor.capture());
    FosterPerson capturedFosterPerson = fosterPersonArgumentCaptor.getValue();

    assertEquals(fosterPerson.getFirstName(), capturedFosterPerson.getFirstName());
    assertEquals(fosterPerson.getLastName(), capturedFosterPerson.getLastName());
    assertEquals(newEmail, capturedFosterPerson.getEmail());
    assertEquals(fosterPerson.getCity(), capturedFosterPerson.getCity());
    assertEquals(fosterPerson.getAddress(), capturedFosterPerson.getAddress());
    assertEquals(fosterPerson.getPhoneNumber(), capturedFosterPerson.getPhoneNumber());
  }

  @Test
  void updateCityFosterPersonTest() {
    when(fosterPersonRepository.findById(fosterPerson.getId())).thenReturn(
        Optional.of(fosterPerson));

    PersonDataRequest updateReq = new PersonDataRequest(null, null, null,
        "Floresti", null, null);

    underTest.updateFosterPerson(fosterPerson.getId(), updateReq);

    ArgumentCaptor<FosterPerson> fosterPersonArgumentCaptor = ArgumentCaptor
        .forClass(FosterPerson.class);
    verify(fosterPersonRepository).save(fosterPersonArgumentCaptor.capture());
    FosterPerson capturedFosterPerson = fosterPersonArgumentCaptor.getValue();

    assertEquals(fosterPerson.getFirstName(), capturedFosterPerson.getFirstName());
    assertEquals(fosterPerson.getLastName(), capturedFosterPerson.getLastName());
    assertEquals(fosterPerson.getEmail(), capturedFosterPerson.getEmail());
    assertEquals("Floresti", capturedFosterPerson.getCity());
    assertEquals(fosterPerson.getAddress(), capturedFosterPerson.getAddress());
    assertEquals(fosterPerson.getPhoneNumber(), capturedFosterPerson.getPhoneNumber());
  }

  @Test
  void updateStreetFosterPersonTest() {
    when(fosterPersonRepository.findById(fosterPerson.getId())).thenReturn(
        Optional.of(fosterPerson));

    PersonDataRequest updateReq = new PersonDataRequest(null, null, null,
        null, "Street Some Street 9", null);

    underTest.updateFosterPerson(fosterPerson.getId(), updateReq);

    ArgumentCaptor<FosterPerson> fosterPersonArgumentCaptor = ArgumentCaptor
        .forClass(FosterPerson.class);
    verify(fosterPersonRepository).save(fosterPersonArgumentCaptor.capture());
    FosterPerson capturedFosterPerson = fosterPersonArgumentCaptor.getValue();

    assertEquals(fosterPerson.getFirstName(), capturedFosterPerson.getFirstName());
    assertEquals(fosterPerson.getLastName(), capturedFosterPerson.getLastName());
    assertEquals(fosterPerson.getEmail(), capturedFosterPerson.getEmail());
    assertEquals(fosterPerson.getCity(), capturedFosterPerson.getCity());
    assertEquals("Street Some Street 9", capturedFosterPerson.getAddress());
    assertEquals(fosterPerson.getPhoneNumber(), capturedFosterPerson.getPhoneNumber());
  }

  @Test
  void updatePhoneNumberFosterPersonTest() {
    when(fosterPersonRepository.findById(fosterPerson.getId())).thenReturn(
        Optional.of(fosterPerson));

    PersonDataRequest updateReq = new PersonDataRequest(null, null, null,
        null, null, "0934223344");

    underTest.updateFosterPerson(fosterPerson.getId(), updateReq);

    ArgumentCaptor<FosterPerson> fosterPersonArgumentCaptor = ArgumentCaptor
        .forClass(FosterPerson.class);
    verify(fosterPersonRepository).save(fosterPersonArgumentCaptor.capture());
    FosterPerson capturedFosterPerson = fosterPersonArgumentCaptor.getValue();

    assertEquals(fosterPerson.getFirstName(), capturedFosterPerson.getFirstName());
    assertEquals(fosterPerson.getLastName(), capturedFosterPerson.getLastName());
    assertEquals(fosterPerson.getEmail(), capturedFosterPerson.getEmail());
    assertEquals(fosterPerson.getCity(), capturedFosterPerson.getCity());
    assertEquals(fosterPerson.getAddress(), capturedFosterPerson.getAddress());
    assertEquals("0934223344", capturedFosterPerson.getPhoneNumber());
  }

  @Test
  void updateWillThrowNoDataChangesFosterPersonTest() {
    when(fosterPersonRepository.findById(fosterPerson.getId())).thenReturn(
        Optional.of(fosterPerson));

    PersonDataRequest updateReq = new PersonDataRequest(fosterPerson.getFirstName(),
        fosterPerson.getLastName(), fosterPerson.getEmail(),
        fosterPerson.getCity(), fosterPerson.getAddress(), fosterPerson.getPhoneNumber());

    assertThatThrownBy(() -> underTest.updateFosterPerson(fosterPerson.getId(), updateReq))
        .isInstanceOf(RequestValidationException.class).hasMessage("no data changes found");
  }

  @Test
  void deleteNonExistentFosterPersonByIdTest() {

    when(fosterPersonRepository.existsFosterPersonById(fosterPerson.getId())).thenReturn(false);

    assertThatThrownBy(() -> underTest.deleteFosterPersonById(fosterPerson.getId()))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(String.format("foster person with id %s not found", fosterPerson.getId()));
  }

  @Test
  void deleteFosterPersonByIdTest() {
    when(fosterPersonRepository.existsFosterPersonById(fosterPerson.getId())).thenReturn(true);

    underTest.deleteFosterPersonById(fosterPerson.getId());

    verify(fosterPersonRepository).deleteById(fosterPerson.getId());
  }


  private FosterPerson buildFosterPerson() {
    FosterPerson fosterPerson = new FosterPerson();
    fosterPerson.setId(1L);
    fosterPerson.setFirstName("john");
    fosterPerson.setLastName("doe");
    fosterPerson.setEmail("jonnyd@email.com");
    fosterPerson.setCity("Bucuresti");
    fosterPerson.setAddress("Strada AAA nr 44");
    fosterPerson.setPhoneNumber("0789231221");
    return fosterPerson;
  }

  private PersonDataRequest requestFosterPerson() {
    PersonDataRequest personDataRequest = new PersonDataRequest("john", "doe",
        "jon@email.com", "Bucuresti", "Random Street", "0749334455");
    return personDataRequest;
  }

  private void checkFosterPersonDtoFields(FosterPersonDto fosterPersonDto) {
    assertEquals("john", fosterPersonDto.getFirstName());
    assertEquals("doe", fosterPersonDto.getLastName());
    assertEquals("jonnyd@email.com", fosterPersonDto.getEmail());
    assertEquals("Bucuresti", fosterPersonDto.getCity());
    assertEquals("Strada AAA nr 44", fosterPersonDto.getAddress());
    assertEquals("0789231221", fosterPersonDto.getPhoneNumber());
  }


}