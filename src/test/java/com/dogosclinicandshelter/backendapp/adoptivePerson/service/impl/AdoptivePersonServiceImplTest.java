package com.dogosclinicandshelter.backendapp.adoptivePerson.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dogosclinicandshelter.backendapp.adoptivePerson.mapper.AdoptivePersonMapper;
import com.dogosclinicandshelter.backendapp.adoptivePerson.model.dto.AdoptivePersonDto;
import com.dogosclinicandshelter.backendapp.adoptivePerson.model.persistance.AdoptivePerson;
import com.dogosclinicandshelter.backendapp.adoptivePerson.repository.AdoptivePersonRepository;
import com.dogosclinicandshelter.backendapp.adoptivePerson.service.AdoptivePersonService;
import com.dogosclinicandshelter.backendapp.exception.DuplicateResourceException;
import com.dogosclinicandshelter.backendapp.exception.RequestValidationException;
import com.dogosclinicandshelter.backendapp.exception.ResourceNotFoundException;
import com.dogosclinicandshelter.backendapp.dataRequest.personRequest.PersonDataRequest;
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
class AdoptivePersonServiceImplTest {

  private AdoptivePersonService underTest;
  private AdoptivePerson adoptivePerson;

  @Mock
  private AdoptivePersonRepository adoptivePersonRepository;

  @Mock
  private AdoptivePersonMapper adoptivePersonMapper;

  Long adoptivePersonId;

  @BeforeEach
  void setup() {
    underTest = new AdoptivePersonServiceImpl(adoptivePersonRepository, adoptivePersonMapper);
    adoptivePerson = this.buildAdoptivePerson();
    adoptivePersonId = adoptivePerson.getId();
  }


  @Test
  void getAllAdoptivePersonsTest() {
    when(adoptivePersonRepository.findAll()).thenReturn(List.of(adoptivePerson));
    List<AdoptivePersonDto> allAdoptivePersons = underTest.getAllAdoptivePersons();

    assertEquals(1, allAdoptivePersons.size());
    verify(adoptivePersonRepository).findAll();
  }

  @Test
  void getAdoptivePersonTest() {
    when(adoptivePersonRepository.findById(1L)).thenReturn(Optional.of(adoptivePerson));

    when(adoptivePersonMapper.mapToDto(adoptivePerson)).thenReturn(
        new AdoptivePersonDto(adoptivePerson.getId(), adoptivePerson.getFirstName(),
            adoptivePerson.getLastName(), adoptivePerson.getEmail(), adoptivePerson.getCity(),
            adoptivePerson.getAddress(), adoptivePerson.getPhoneNumber()));

    AdoptivePersonDto adoptivePersonDto = underTest.getAdoptivePerson(1L);

    checkAdoptivePersonDtoFields(adoptivePersonDto);
  }

  @Test
  void getAdoptivePersonThrowResourceNotFoundExceptionTest() {
    when(adoptivePersonRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> underTest.getAdoptivePerson(adoptivePersonId))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(String.format("adoptive person with id %s not found", adoptivePerson.getId()));
  }

  @Test
  void addAdoptivePersonTest() {
    PersonDataRequest request = requestAdoptivePerson();

    when(adoptivePersonRepository.existsAdoptivePersonByEmail(request.getEmail()))
        .thenReturn(false);
    underTest.addAdoptivePerson(request);
    ArgumentCaptor<AdoptivePerson> adoptivePersonArgumentCaptor = ArgumentCaptor
        .forClass(AdoptivePerson.class);
    verify(adoptivePersonRepository).save(adoptivePersonArgumentCaptor.capture());
    AdoptivePerson capturedAdoptivePerson = adoptivePersonArgumentCaptor.getValue();

    assertNull(capturedAdoptivePerson.getId());
    assertEquals("john", capturedAdoptivePerson.getFirstName());
    assertEquals("doe", capturedAdoptivePerson.getLastName());
    assertEquals("jon@email.com", capturedAdoptivePerson.getEmail());
    assertEquals("Random Street", capturedAdoptivePerson.getAddress());
    assertEquals("Bucuresti", capturedAdoptivePerson.getCity());
    assertEquals("0749334455", capturedAdoptivePerson.getPhoneNumber());
  }

  @Test
  void addAdoptivePersonWithExistingEmailTest() {
    PersonDataRequest request = requestAdoptivePerson();

    when(adoptivePersonRepository.existsAdoptivePersonByEmail(request.getEmail())).thenReturn(true);

    assertThatThrownBy(() -> underTest.addAdoptivePerson(request))
        .isInstanceOf(DuplicateResourceException.class).hasMessage(ExceptionUtils.EMAIL_ALREADY_TAKEN.toString());
  }

  @Test
  void updateAdoptivePersonThrowResourceNotFoundTest() {
    PersonDataRequest updateReq = new PersonDataRequest("jonny", "donny", "newEmail",
        "Brasov", "Street NewStreet 1", "0755223311");

    when(adoptivePersonRepository.findById(adoptivePerson.getId())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> underTest.updateAdoptivePerson(adoptivePersonId, updateReq))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(String.format("adoptive person with id %s not found", adoptivePerson.getId()));
  }

  @Test
  void updateAllAdoptivePersonPropertiesTest() {
    when(adoptivePersonRepository.findById(adoptivePerson.getId())).thenReturn(
        Optional.of(adoptivePerson));

    String newEmail = "jonny@yahoo.com";
    PersonDataRequest updateReq = new PersonDataRequest("jonny", "donny", newEmail,
        "Brasov", "Street NewStreet 1", "0755223311");

    when(adoptivePersonRepository.existsAdoptivePersonByEmail(newEmail)).thenReturn(false);

    underTest.updateAdoptivePerson(adoptivePerson.getId(), updateReq);

    ArgumentCaptor<AdoptivePerson> adoptivePersonArgumentCaptor = ArgumentCaptor
        .forClass(AdoptivePerson.class);
    verify(adoptivePersonRepository).save(adoptivePersonArgumentCaptor.capture());
    AdoptivePerson capturedAdoptivePerson = adoptivePersonArgumentCaptor.getValue();

    assertEquals("jonny", capturedAdoptivePerson.getFirstName());
    assertEquals("donny", capturedAdoptivePerson.getLastName());
    assertEquals(newEmail, capturedAdoptivePerson.getEmail());
    assertEquals("Brasov", capturedAdoptivePerson.getCity());
    assertEquals("Street NewStreet 1", capturedAdoptivePerson.getAddress());
    assertEquals("0755223311", capturedAdoptivePerson.getPhoneNumber());

  }

  @Test
  void updateFirstNameAdoptivePersonTest() {
    when(adoptivePersonRepository.findById(adoptivePerson.getId())).thenReturn(
        Optional.of(adoptivePerson));

    PersonDataRequest updateReq = new PersonDataRequest("jonny", null, null,
        null, null, null);

    underTest.updateAdoptivePerson(adoptivePerson.getId(), updateReq);

    ArgumentCaptor<AdoptivePerson> adoptivePersonArgumentCaptor = ArgumentCaptor
        .forClass(AdoptivePerson.class);
    verify(adoptivePersonRepository).save(adoptivePersonArgumentCaptor.capture());
    AdoptivePerson capturedAdoptivePerson = adoptivePersonArgumentCaptor.getValue();

    assertEquals("jonny", capturedAdoptivePerson.getFirstName());
    assertEquals(adoptivePerson.getLastName(), capturedAdoptivePerson.getLastName());
    assertEquals(adoptivePerson.getEmail(), capturedAdoptivePerson.getEmail());
    assertEquals(adoptivePerson.getCity(), capturedAdoptivePerson.getCity());
    assertEquals(adoptivePerson.getAddress(), capturedAdoptivePerson.getAddress());
    assertEquals(adoptivePerson.getPhoneNumber(), capturedAdoptivePerson.getPhoneNumber());
  }


  @Test
  void updateLastNameAdoptivePersonTest() {
    when(adoptivePersonRepository.findById(adoptivePerson.getId())).thenReturn(
        Optional.of(adoptivePerson));

    PersonDataRequest updateReq = new PersonDataRequest(null, "donny", null,
        null, null, null);

    underTest.updateAdoptivePerson(adoptivePerson.getId(), updateReq);

    ArgumentCaptor<AdoptivePerson> adoptivePersonArgumentCaptor = ArgumentCaptor
        .forClass(AdoptivePerson.class);
    verify(adoptivePersonRepository).save(adoptivePersonArgumentCaptor.capture());
    AdoptivePerson capturedAdoptivePerson = adoptivePersonArgumentCaptor.getValue();

    assertEquals(adoptivePerson.getFirstName(), capturedAdoptivePerson.getFirstName());
    assertEquals("donny", capturedAdoptivePerson.getLastName());
    assertEquals(adoptivePerson.getEmail(), capturedAdoptivePerson.getEmail());
    assertEquals(adoptivePerson.getCity(), capturedAdoptivePerson.getCity());
    assertEquals(adoptivePerson.getAddress(), capturedAdoptivePerson.getAddress());
    assertEquals(adoptivePerson.getPhoneNumber(), capturedAdoptivePerson.getPhoneNumber());
  }


  @Test
  void updateEmailThrowDuplicateResourceNameAdoptivePersonTest() {
    when(adoptivePersonRepository.findById(adoptivePerson.getId())).thenReturn(
        Optional.of(adoptivePerson));

    PersonDataRequest updateReq = new PersonDataRequest(null, null, "newEmail",
        null, null, null);
    when(adoptivePersonRepository.existsAdoptivePersonByEmail("newEmail"))
        .thenReturn(true);

    assertThatThrownBy(() -> underTest.updateAdoptivePerson(adoptivePersonId, updateReq))
        .isInstanceOf(DuplicateResourceException.class)
        .hasMessage(ExceptionUtils.EMAIL_ALREADY_TAKEN.toString());
  }

  @Test
  void updateEmailNameAdoptivePersonTest() {
    when(adoptivePersonRepository.findById(adoptivePerson.getId())).thenReturn(
        Optional.of(adoptivePerson));

    String newEmail = "jonny@yahoo.com";

    PersonDataRequest updateReq = new PersonDataRequest(null, null, newEmail,
        null, null, null);
    when(adoptivePersonRepository.existsAdoptivePersonByEmail(newEmail)).thenReturn(false);

    underTest.updateAdoptivePerson(adoptivePerson.getId(), updateReq);

    ArgumentCaptor<AdoptivePerson> adoptivePersonArgumentCaptor = ArgumentCaptor
        .forClass(AdoptivePerson.class);
    verify(adoptivePersonRepository).save(adoptivePersonArgumentCaptor.capture());
    AdoptivePerson capturedAdoptivePerson = adoptivePersonArgumentCaptor.getValue();

    assertEquals(adoptivePerson.getFirstName(), capturedAdoptivePerson.getFirstName());
    assertEquals(adoptivePerson.getLastName(), capturedAdoptivePerson.getLastName());
    assertEquals(newEmail, capturedAdoptivePerson.getEmail());
    assertEquals(adoptivePerson.getCity(), capturedAdoptivePerson.getCity());
    assertEquals(adoptivePerson.getAddress(), capturedAdoptivePerson.getAddress());
    assertEquals(adoptivePerson.getPhoneNumber(), capturedAdoptivePerson.getPhoneNumber());
  }

  @Test
  void updateCityAdoptivePersonTest() {
    when(adoptivePersonRepository.findById(adoptivePerson.getId())).thenReturn(
        Optional.of(adoptivePerson));

    PersonDataRequest updateReq = new PersonDataRequest(null, null, null,
        "Floresti", null, null);

    underTest.updateAdoptivePerson(adoptivePerson.getId(), updateReq);

    ArgumentCaptor<AdoptivePerson> adoptivePersonArgumentCaptor = ArgumentCaptor
        .forClass(AdoptivePerson.class);
    verify(adoptivePersonRepository).save(adoptivePersonArgumentCaptor.capture());
    AdoptivePerson capturedAdoptivePerson = adoptivePersonArgumentCaptor.getValue();

    assertEquals(adoptivePerson.getFirstName(), capturedAdoptivePerson.getFirstName());
    assertEquals(adoptivePerson.getLastName(), capturedAdoptivePerson.getLastName());
    assertEquals(adoptivePerson.getEmail(), capturedAdoptivePerson.getEmail());
    assertEquals("Floresti", capturedAdoptivePerson.getCity());
    assertEquals(adoptivePerson.getAddress(), capturedAdoptivePerson.getAddress());
    assertEquals(adoptivePerson.getPhoneNumber(), capturedAdoptivePerson.getPhoneNumber());
  }

  @Test
  void updateStreetAdoptivePersonTest() {
    when(adoptivePersonRepository.findById(adoptivePerson.getId())).thenReturn(
        Optional.of(adoptivePerson));

    PersonDataRequest updateReq = new PersonDataRequest(null, null, null,
        null, "Street Some Street 9", null);

    underTest.updateAdoptivePerson(adoptivePerson.getId(), updateReq);

    ArgumentCaptor<AdoptivePerson> adoptivePersonArgumentCaptor = ArgumentCaptor
        .forClass(AdoptivePerson.class);
    verify(adoptivePersonRepository).save(adoptivePersonArgumentCaptor.capture());
    AdoptivePerson capturedAdoptivePerson = adoptivePersonArgumentCaptor.getValue();

    assertEquals(adoptivePerson.getFirstName(), capturedAdoptivePerson.getFirstName());
    assertEquals(adoptivePerson.getLastName(), capturedAdoptivePerson.getLastName());
    assertEquals(adoptivePerson.getEmail(), capturedAdoptivePerson.getEmail());
    assertEquals(adoptivePerson.getCity(), capturedAdoptivePerson.getCity());
    assertEquals("Street Some Street 9", capturedAdoptivePerson.getAddress());
    assertEquals(adoptivePerson.getPhoneNumber(), capturedAdoptivePerson.getPhoneNumber());
  }

  @Test
  void updatePhoneNumberAdoptivePersonTest() {
    when(adoptivePersonRepository.findById(adoptivePerson.getId())).thenReturn(
        Optional.of(adoptivePerson));

    PersonDataRequest updateReq = new PersonDataRequest(null, null, null,
        null, null, "0934223344");

    underTest.updateAdoptivePerson(adoptivePerson.getId(), updateReq);

    ArgumentCaptor<AdoptivePerson> adoptivePersonArgumentCaptor = ArgumentCaptor
        .forClass(AdoptivePerson.class);
    verify(adoptivePersonRepository).save(adoptivePersonArgumentCaptor.capture());
    AdoptivePerson capturedAdoptivePerson = adoptivePersonArgumentCaptor.getValue();

    assertEquals(adoptivePerson.getFirstName(), capturedAdoptivePerson.getFirstName());
    assertEquals(adoptivePerson.getLastName(), capturedAdoptivePerson.getLastName());
    assertEquals(adoptivePerson.getEmail(), capturedAdoptivePerson.getEmail());
    assertEquals(adoptivePerson.getCity(), capturedAdoptivePerson.getCity());
    assertEquals(adoptivePerson.getAddress(), capturedAdoptivePerson.getAddress());
    assertEquals("0934223344", capturedAdoptivePerson.getPhoneNumber());
  }

  @Test
  void updateWillThrowNoDataChangesAdoptivePersonTest() {
    when(adoptivePersonRepository.findById(adoptivePerson.getId())).thenReturn(
        Optional.of(adoptivePerson));

    PersonDataRequest updateReq = new PersonDataRequest(adoptivePerson.getFirstName(),
        adoptivePerson.getLastName(), adoptivePerson.getEmail(),
        adoptivePerson.getCity(), adoptivePerson.getAddress(), adoptivePerson.getPhoneNumber());

    assertThatThrownBy(() -> underTest.updateAdoptivePerson(adoptivePersonId, updateReq))
        .isInstanceOf(RequestValidationException.class).hasMessage(ExceptionUtils.NO_DATA_CHANGES_FOUND.toString());
  }


  @Test
  void deleteAdoptivePersonByIdTest() {
    when(adoptivePersonRepository.existsAdoptivePersonById(adoptivePerson.getId()))
        .thenReturn(true);

    underTest.deleteAdoptivePersonById(adoptivePerson.getId());
    verify(adoptivePersonRepository).deleteById(adoptivePerson.getId());

  }

  @Test
  void deleteNonExistentAdoptivePersonByIdTest() {

    when(adoptivePersonRepository.existsAdoptivePersonById(adoptivePerson.getId()))
        .thenReturn(false);

    assertThatThrownBy(() -> underTest.deleteAdoptivePersonById(adoptivePersonId))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(String.format("adoptive person with id %s not found", adoptivePerson.getId()));
  }

  private AdoptivePerson buildAdoptivePerson() {
    AdoptivePerson adoptivePerson = new AdoptivePerson();
    adoptivePerson.setId(1L);
    adoptivePerson.setFirstName("john");
    adoptivePerson.setLastName("doe");
    adoptivePerson.setEmail("jonnyd@email.com");
    adoptivePerson.setCity("Bucuresti");
    adoptivePerson.setAddress("Strada AAA nr 44");
    adoptivePerson.setPhoneNumber("0789231221");
    return adoptivePerson;
  }

  private void checkAdoptivePersonDtoFields(AdoptivePersonDto adoptivePersonDto) {
    assertEquals("john", adoptivePersonDto.getFirstName());
    assertEquals("doe", adoptivePersonDto.getLastName());
    assertEquals("jonnyd@email.com", adoptivePersonDto.getEmail());
    assertEquals("Bucuresti", adoptivePersonDto.getCity());
    assertEquals("Strada AAA nr 44", adoptivePersonDto.getAddress());
    assertEquals("0789231221", adoptivePersonDto.getPhoneNumber());
  }

  private PersonDataRequest requestAdoptivePerson() {
    PersonDataRequest personDataRequest = new PersonDataRequest("john", "doe",
        "jon@email.com", "Bucuresti", "Random Street", "0749334455");
    return personDataRequest;
  }


}
