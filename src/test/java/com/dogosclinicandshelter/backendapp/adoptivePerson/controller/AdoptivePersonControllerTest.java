package com.dogosclinicandshelter.backendapp.adoptivePerson.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dogosclinicandshelter.backendapp.adoptivePerson.model.dto.AdoptivePersonDto;
import com.dogosclinicandshelter.backendapp.adoptivePerson.service.AdoptivePersonService;
import com.dogosclinicandshelter.backendapp.exception.ResourceNotFoundException;
import com.dogosclinicandshelter.backendapp.message.MessageUtils;
import com.dogosclinicandshelter.backendapp.dataRequest.personRequest.PersonDataRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(AdoptivePersonController.class)
class AdoptivePersonControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AdoptivePersonService adoptivePersonService;

  private AdoptivePersonDto adoptivePersonDto;

  private PersonDataRequest personDataRequest;

  @BeforeEach
  void setUp() {
    adoptivePersonDto = buildAdoptivePersonDto();
    personDataRequest = adoptivePersonRequest();
  }


  @Test
  void getAdoptivePersonsTest() throws Exception {
    when(adoptivePersonService.getAllAdoptivePersons()).thenReturn(List.of(adoptivePersonDto));
    MvcResult mvcResult = mockMvc.perform(get("/api/v1/adoptive/"))
        .andExpect(status().isOk()).andReturn();
    assertEquals(List.of(convertToJson(adoptivePersonDto)).toString(),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void getAdoptivePersonTest() throws Exception {
    when(adoptivePersonService.getAdoptivePerson(1l)).thenReturn(adoptivePersonDto);
    MvcResult mvcResult = mockMvc.perform(get("/api/v1/adoptive/" + "1"))
        .andExpect(status().isOk()).andReturn();
    assertEquals(convertToJson(adoptivePersonDto), mvcResult.getResponse().getContentAsString());
  }

  @Test
  void getAdoptivePersonNotFoundTest() throws Exception {
    when(adoptivePersonService.getAdoptivePerson(1l)).thenThrow(
        new ResourceNotFoundException(String.format("adoptive person with id %s not found", 1)));
    MvcResult mvcResult = mockMvc.perform(get("/api/v1/adoptive/" + "1"))
        .andExpect(status().isNotFound()).andReturn();
    assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
  }


  @Test
  void registerAdoptivePersonTest() throws Exception {
    when(adoptivePersonService.addAdoptivePerson(any())).thenReturn(true);
    MvcResult mvcResult = mockMvc.perform(post("/api/v1/adoptive/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(personDataRequest))
        .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isCreated()).andReturn();

    assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.ADOPTIVE_PERSON_SAVE_DONE.toString(),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void registerAdoptivePersonFailedTest() throws Exception {
    when(adoptivePersonService.addAdoptivePerson(any())).thenReturn(false);
    MvcResult mvcResult = mockMvc.perform(post("/api/v1/adoptive/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(personDataRequest))
        .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isOk()).andReturn();

    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.ADOPTIVE_PERSON_SAVE_FAILED.toString(),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void updateAdoptivePersonTest() throws Exception {
    when(adoptivePersonService.updateAdoptivePerson(any(), any())).thenReturn(true);
    MvcResult mvcResult = mockMvc.perform(put("/api/v1/adoptive/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(personDataRequest))
        .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isOk()).andReturn();

    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.ADOPTIVE_PERSON_UPDATE_DONE.toString(),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void updateAdoptivePersonFailedTest() throws Exception {
    when(adoptivePersonService.updateAdoptivePerson(any(), any())).thenReturn(false);
    MvcResult mvcResult = mockMvc.perform(put("/api/v1/adoptive/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(personDataRequest))
        .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isOk()).andReturn();

    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.ADOPTIVE_PERSON_UPDATE_FAILED.toString(),
        mvcResult.getResponse().getContentAsString());
  }



  @Test
  void deleteAdoptivePersonTest() throws Exception {
    when(adoptivePersonService.deleteAdoptivePersonById(1L)).thenReturn(true);

    MvcResult mvcResult = mockMvc.perform(delete("/api/v1/adoptive/" + "1"))
        .andExpect(status().isOk()).andReturn();

    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.ADOPTIVE_PERSON_DELETE_DONE.toString(),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void deleteAdoptivePersonFailedTest() throws Exception {
    when(adoptivePersonService.deleteAdoptivePersonById(1L)).thenReturn(false);

    MvcResult mvcResult = mockMvc.perform(delete("/api/v1/adoptive/" + "1"))
        .andExpect(status().isOk()).andReturn();

    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.ADOPTIVE_PERSON_DELETE_FAILED.toString(),
        mvcResult.getResponse().getContentAsString());
  }


  private String convertToJson(AdoptivePersonDto adoptivePersonDto) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(adoptivePersonDto);
  }

  private String convertToJson(PersonDataRequest personDataRequest)
      throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(personDataRequest);
  }

  private AdoptivePersonDto buildAdoptivePersonDto() {
    AdoptivePersonDto adoptivePersonDto = new AdoptivePersonDto();
    adoptivePersonDto.setId(1L);
    adoptivePersonDto.setFirstName("jon");
    adoptivePersonDto.setLastName("doe");
    adoptivePersonDto.setEmail("jon.doe@email.com");
    adoptivePersonDto.setCity("Cluj-Napoca");
    adoptivePersonDto.setAddress("Street Random Name 1");
    adoptivePersonDto.setPhoneNumber("0744556688");
    return adoptivePersonDto;
  }

  private PersonDataRequest adoptivePersonRequest() {
    PersonDataRequest personDataRequest = new PersonDataRequest("jonny", "dorian",
        "jonny@email.com", "Cluj-Napoca", "Street New 1", "0749223311");
    return personDataRequest;
  }


}
