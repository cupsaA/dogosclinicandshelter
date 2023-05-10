package com.dogosclinicandshelter.backendapp.dog.mapper;

import com.dogosclinicandshelter.backendapp.dog.model.dto.DogHealthDto;
import com.dogosclinicandshelter.backendapp.dog.model.persistance.DogHealth;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface DogHealthMapper {

  DogHealthDto mapToDto(DogHealth dogHealth);

  DogHealth mapToObject(DogHealthDto dogHealthDto);
}
