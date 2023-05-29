//package com.dogosclinicandshelter.backendapp.dog.service.impl;
//
//import com.dogosclinicandshelter.backendapp.dog.mapper.DogMapper;
//import com.dogosclinicandshelter.backendapp.dog.model.dto.DogDto;
//import com.dogosclinicandshelter.backendapp.dog.repository.DogRepository;
//import com.dogosclinicandshelter.backendapp.dog.service.DogService;
//import java.util.List;
//import java.util.stream.Collectors;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class DogServiceImpl implements DogService {
//
//  private final DogRepository dogRepository;
//
//  private final DogMapper dogMapper;
//
//
//  @Override
//  public List<DogDto> getAllDogs() {
//    return dogRepository.findAll().stream().map(dogMapper::mapToDto).collect(Collectors.toList());
//  }
//}
