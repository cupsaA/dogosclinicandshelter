package com.dogosclinicandshelter.backendapp.fosterPerson.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dogosclinicandshelter.backendapp.exception.ResourceNotFoundException;
import com.dogosclinicandshelter.backendapp.fosterPerson.model.dto.FosterPersonDto;
import com.dogosclinicandshelter.backendapp.personDataRequest.request.PersonDataRequest;
import com.dogosclinicandshelter.backendapp.fosterPerson.service.FosterPersonService;
import com.dogosclinicandshelter.backendapp.message.MessageUtils;
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

@WebMvcTest(FosterPersonController.class)
class FosterPersonControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FosterPersonService fosterPersonService;

  private FosterPersonDto fosterPersonDto;

  private PersonDataRequest personDataRequest;

  @BeforeEach
  void setUp() {
    fosterPersonDto = buildFosterPersonDto();
    personDataRequest = fosterPersonRequest();
  }


  @Test
  void getFosterPersons() throws Exception {
    when(fosterPersonService.getAllFosterPersons()).thenReturn(List.of(fosterPersonDto));
    MvcResult mvcResult = mockMvc.perform(get("/api/v1/foster/"))
        .andExpect(status().isOk()).andReturn();
    assertEquals(List.of(convertToJson(fosterPersonDto)).toString(),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void getFosterPerson() throws Exception {
    when(fosterPersonService.getFosterPerson(1l)).thenReturn(fosterPersonDto);
    MvcResult mvcResult = mockMvc.perform(get("/api/v1/foster/" + "1"))
        .andExpect(status().isOk()).andReturn();
    assertEquals(convertToJson(fosterPersonDto), mvcResult.getResponse().getContentAsString());
  }

  @Test
  void getFosterPersonNotFound() throws Exception {
    when(fosterPersonService.getFosterPerson(1l)).thenThrow(
        new ResourceNotFoundException(String.format("foster person with id %s not found", 1)));
    MvcResult mvcResult = mockMvc.perform(get("/api/v1/foster/" + "1"))
        .andExpect(status().isNotFound()).andReturn();
    assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
  }

  @Test
  void registerFosterPerson() throws Exception {
    when(fosterPersonService.addFosterPerson(any())).thenReturn(true);
    MvcResult mvcResult = mockMvc.perform(post("/api/v1/foster/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(personDataRequest))
        .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isCreated()).andReturn();

    assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.FOSTER_PERSON_SAVE_DONE.toString(),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void registerFosterPersonFailed() throws Exception {
    when(fosterPersonService.addFosterPerson(any())).thenReturn(false);
    MvcResult mvcResult = mockMvc.perform(post("/api/v1/foster/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(personDataRequest))
        .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isOk()).andReturn();

    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.FOSTER_PERSON_SAVE_FAILED.toString(),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void deleteFosterPerson() throws Exception {
    when(fosterPersonService.deleteFosterPersonById(1L)).thenReturn(true);

    MvcResult mvcResult = mockMvc.perform(delete("/api/v1/foster/" + "1"))
        .andExpect(status().isOk()).andReturn();

    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.FOSTER_PERSON_DELETE_DONE.toString(),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void deleteFosterPersonFailed() throws Exception {
    when(fosterPersonService.deleteFosterPersonById(1L)).thenReturn(false);

    MvcResult mvcResult = mockMvc.perform(delete("/api/v1/foster/" + "1"))
        .andExpect(status().isOk()).andReturn();

    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.FOSTER_PERSON_DELETE_FAILED.toString(),
        mvcResult.getResponse().getContentAsString());

  }

  @Test
  void updateFosterPerson() throws Exception {
    when(fosterPersonService.updateFosterPerson(any(), any())).thenReturn(true);
    MvcResult mvcResult = mockMvc.perform(put("/api/v1/foster/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(personDataRequest))
        .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isOk()).andReturn();

    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.FOSTER_PERSON_UPDATE_DONE.toString(),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void updateFosterPersonFailed() throws Exception {
    when(fosterPersonService.updateFosterPerson(any(), any())).thenReturn(false);
    MvcResult mvcResult = mockMvc.perform(put("/api/v1/foster/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(personDataRequest))
        .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isOk()).andReturn();

    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.FOSTER_PERSON_UPDATE_FAILED.toString(),
        mvcResult.getResponse().getContentAsString());
  }

  private FosterPersonDto buildFosterPersonDto() {
    FosterPersonDto fosterPersonDto = new FosterPersonDto();
    fosterPersonDto.setId(1L);
    fosterPersonDto.setFirstName("jon");
    fosterPersonDto.setLastName("doe");
    fosterPersonDto.setEmail("jon.doe@email.com");
    fosterPersonDto.setCity("Cluj-Napoca");
    fosterPersonDto.setAddress("Street Random Name 1");
    fosterPersonDto.setPhoneNumber("0744556688");
    return fosterPersonDto;
  }

  private PersonDataRequest fosterPersonRequest() {
    PersonDataRequest personDataRequest = new PersonDataRequest("jonny", "dorian",
        "jonny@email.com", "Cluj-Napoca", "Street New 1", "0749223311");
    return personDataRequest;

  }

  private String convertToJson(FosterPersonDto fosterPersonDto) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(fosterPersonDto);
  }

  private String convertToJson(PersonDataRequest personDataRequest)
      throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(personDataRequest);
  }
}