package com.dogosclinicandshelter.backendapp.dog.mapper;

import com.dogosclinicandshelter.backendapp.dog.model.dto.DogInfoDto;
import com.dogosclinicandshelter.backendapp.dog.model.persistance.DogInfo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface DogInfoMapper {

  DogInfoDto mapToDto(DogInfo dogInfo);

  DogInfo mapToObject(DogInfoDto dogInfoDto);
}
