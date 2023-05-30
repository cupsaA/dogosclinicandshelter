package com.dogosclinicandshelter.backendapp.dog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dogosclinicandshelter.backendapp.dataRequest.dogHealthRequest.DogHealthRequest;
import com.dogosclinicandshelter.backendapp.dog.model.dto.DogHealthDto;
import com.dogosclinicandshelter.backendapp.dog.service.DogHealthService;
import com.dogosclinicandshelter.backendapp.exception.ResourceNotFoundException;
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

@WebMvcTest(DogHealthController.class)
class DogHealthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private DogHealthService dogHealthService;

  private DogHealthDto dogHealthDto;

  private DogHealthRequest dogHealthRequest;

  @BeforeEach
  void setUp() {
    dogHealthDto = buildDogHealthDto();
    dogHealthRequest = dogHealthDataRequest();
  }


  @Test
  void getDogsHealthTest() throws Exception {
    when(dogHealthService.getAllDogsHealth()).thenReturn(List.of(dogHealthDto));
    MvcResult mvcResult = mockMvc.perform(get("/api/v1/dog/health/"))
        .andExpect(status().isOk()).andReturn();
    assertEquals(List.of(convertToJson(dogHealthDto)).toString(),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void getDogHealthTest() throws Exception {
    when(dogHealthService.getDogHealth(1L)).thenReturn(dogHealthDto);
    MvcResult mvcResult = mockMvc.perform(get("/api/v1/dog/health/" + "1"))
        .andExpect(status().isOk()).andReturn();
    assertEquals(convertToJson(dogHealthDto), mvcResult.getResponse().getContentAsString());
  }

  @Test
  void getDogHealthNotFoundTest() throws Exception {
    when(dogHealthService.getDogHealth(1L)).thenThrow(
        new ResourceNotFoundException(String.format("dog health with id %s not found", 1)));
    MvcResult mvcResult = mockMvc.perform(get("/api/v1/dog/health/" + "1"))
        .andExpect(status().isNotFound()).andReturn();
    assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
  }

  @Test
  void registerFosterPersonTest() throws Exception {
    when(dogHealthService.addDogHealth(any())).thenReturn(true);
    MvcResult mvcResult = mockMvc.perform(post("/api/v1/dog/health/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(dogHealthRequest))
        .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isCreated()).andReturn();

    assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.DOG_HEALTH_SAVE_DONE.toString(),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void registerFosterPersonFailedTest() throws Exception {
    when(dogHealthService.addDogHealth(any())).thenReturn(false);
    MvcResult mvcResult = mockMvc.perform(post("/api/v1/dog/health/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(dogHealthRequest))
        .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isOk()).andReturn();

    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.DOG_HEALTH_SAVE_FAILED.toString(),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void deleteFosterPersonTest() throws Exception {
    when(dogHealthService.deleteDogHealthById(1L)).thenReturn(true);

    MvcResult mvcResult = mockMvc.perform(delete("/api/v1/dog/health/" + "1"))
        .andExpect(status().isOk()).andReturn();

    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.DOG_HEALTH_DELETE_DONE.toString(),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void deleteFosterPersonFailedTest() throws Exception {
    when(dogHealthService.deleteDogHealthById(1L)).thenReturn(false);

    MvcResult mvcResult = mockMvc.perform(delete("/api/v1/dog/health/" + "1"))
        .andExpect(status().isOk()).andReturn();

    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.DOG_HEALTH_DELETE_FAILED.toString(),
        mvcResult.getResponse().getContentAsString());

  }

  @Test
  void updateFosterPersonTest() throws Exception {
    when(dogHealthService.updateDogHealth(any(), any())).thenReturn(true);
    MvcResult mvcResult = mockMvc.perform(put("/api/v1/dog/health/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(dogHealthRequest))
        .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isOk()).andReturn();

    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.DOG_HEALTH_UPDATE_DONE.toString(),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void updateFosterPersonFailedTest() throws Exception {
    when(dogHealthService.updateDogHealth(any(), any())).thenReturn(false);
    MvcResult mvcResult = mockMvc.perform(put("/api/v1/dog/health/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(dogHealthRequest))
        .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isOk()).andReturn();

    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.DOG_HEALTH_UPDATE_FAILED.toString(),
        mvcResult.getResponse().getContentAsString());
  }

  private DogHealthRequest dogHealthDataRequest() {
    return new DogHealthRequest(true, true, true);
  }

  private DogHealthDto buildDogHealthDto() {
    DogHealthDto dogHealthDto = new DogHealthDto();
    dogHealthDto.setId(1L);
    dogHealthDto.setDogSpayed(true);
    dogHealthDto.setDogVaccinated(true);
    dogHealthDto.setDogDewormed(true);
    return dogHealthDto;
  }


  private String convertToJson(DogHealthDto dogHealthDto) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(dogHealthDto);
  }

  private String convertToJson(DogHealthRequest dogHealthRequest)
      throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(dogHealthRequest);
  }

}
