package com.dogosclinicandshelter.backendapp.dog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dogosclinicandshelter.backendapp.dataRequest.dogInfoRequest.DogInfoRequest;
import com.dogosclinicandshelter.backendapp.dog.enumClasses.DogSize;
import com.dogosclinicandshelter.backendapp.dog.model.dto.DogInfoDto;
import com.dogosclinicandshelter.backendapp.dog.service.DogInfoService;
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

@WebMvcTest(DogInfoController.class)
class DogInfoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private DogInfoService dogInfoService;

  private DogInfoDto dogInfoDto;

  private DogInfoRequest dogInfoRequest;

  @BeforeEach
  void setUp() {
    dogInfoDto = buildDogInfoDto();
    dogInfoRequest = dogInfoDataRequest();
  }

  @Test
  void getDogsInfoTest() throws Exception {
    when(dogInfoService.getAllDogsInfo()).thenReturn(List.of(dogInfoDto));
    MvcResult mvcResult = mockMvc.perform(get("/api/v1/dog/info/"))
        .andExpect(status().isOk()).andReturn();
    assertEquals(List.of(convertToJson(dogInfoDto)).toString(),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void getDogInfoTest() throws Exception {
    when(dogInfoService.getDogInfo(1L)).thenReturn(dogInfoDto);
    MvcResult mvcResult = mockMvc.perform(get("/api/v1/dog/info/" + "1"))
        .andExpect(status().isOk()).andReturn();
    assertEquals(convertToJson(dogInfoDto), mvcResult.getResponse().getContentAsString());
  }

  @Test
  void getDogInfoNotFoundTest() throws Exception {
    when(dogInfoService.getDogInfo(1L)).thenThrow(
        new ResourceNotFoundException(String.format("dog info with id %s not found", 1)));
    MvcResult mvcResult = mockMvc.perform(get("/api/v1/dog/info/" + "1"))
        .andExpect(status().isNotFound()).andReturn();
    assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
  }

  @Test
  void registerFosterPersonTest() throws Exception {
    when(dogInfoService.addDogInfo(any())).thenReturn(true);
    MvcResult mvcResult = mockMvc.perform(post("/api/v1/dog/info/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(dogInfoRequest))
        .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isCreated()).andReturn();

    assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.DOG_INFO_SAVE_DONE.toString(),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void registerFosterPersonFailedTest() throws Exception {
    when(dogInfoService.addDogInfo(any())).thenReturn(false);
    MvcResult mvcResult = mockMvc.perform(post("/api/v1/dog/info/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(dogInfoRequest))
        .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isOk()).andReturn();

    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.DOG_INFO_SAVE_FAILED.toString(),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void deleteFosterPersonTest() throws Exception {
    when(dogInfoService.deleteDogInfoById(1L)).thenReturn(true);

    MvcResult mvcResult = mockMvc.perform(delete("/api/v1/dog/info/" + "1"))
        .andExpect(status().isOk()).andReturn();

    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.DOG_INFO_DELETE_DONE.toString(),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void deleteFosterPersonFailedTest() throws Exception {
    when(dogInfoService.deleteDogInfoById(1L)).thenReturn(false);

    MvcResult mvcResult = mockMvc.perform(delete("/api/v1/dog/info/" + "1"))
        .andExpect(status().isOk()).andReturn();

    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.DOG_INFO_DELETE_FAILED.toString(),
        mvcResult.getResponse().getContentAsString());

  }

  @Test
  void updateFosterPersonTest() throws Exception {
    when(dogInfoService.updateDogInfo(any(), any())).thenReturn(true);
    MvcResult mvcResult = mockMvc.perform(put("/api/v1/dog/info/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(dogInfoRequest))
        .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isOk()).andReturn();

    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.DOG_INFO_UPDATE_DONE.toString(),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  void updateFosterPersonFailedTest() throws Exception {
    when(dogInfoService.updateDogInfo(any(), any())).thenReturn(false);
    MvcResult mvcResult = mockMvc.perform(put("/api/v1/dog/info/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(convertToJson(dogInfoRequest))
        .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isOk()).andReturn();

    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    assertEquals(MessageUtils.DOG_INFO_UPDATE_FAILED.toString(),
        mvcResult.getResponse().getContentAsString());
  }

  private DogInfoRequest dogInfoDataRequest() {
    return new DogInfoRequest("max", DogSize.M, 'F', 3, "bulldog", true);
  }

  private DogInfoDto buildDogInfoDto() {
    DogInfoDto dogInfoDto = new DogInfoDto();
    dogInfoDto.setId(1L);
    dogInfoDto.setDogName("rex");
    dogInfoDto.setDogSize(DogSize.M);
    dogInfoDto.setDogSex('M');
    dogInfoDto.setDogAge(2);
    dogInfoDto.setDogBreed("corgi");
    dogInfoDto.setDogChipped(true);
    return dogInfoDto;
  }

  private String convertToJson(DogInfoDto dogInfoDto) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(dogInfoDto);
  }

  private String convertToJson(DogInfoRequest dogInfoRequest)
      throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(dogInfoRequest);
  }

}
