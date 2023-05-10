package com.dogosclinicandshelter.backendapp.dog.service.impl;

import com.dogosclinicandshelter.backendapp.dog.mapper.DogMapper;
import com.dogosclinicandshelter.backendapp.dog.model.persistance.Dog;
import com.dogosclinicandshelter.backendapp.dog.repository.DogRepository;
import com.dogosclinicandshelter.backendapp.dog.service.DogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DogServiceImplTest {

  private DogService underTest;
  private Dog dog;

  @Mock
  private DogRepository dogRepository;

  @Mock
  private DogMapper dogMapper;


  @BeforeEach
  void setUp() {
    underTest = new DogServiceImpl(dogRepository, dogMapper);
  }


}
