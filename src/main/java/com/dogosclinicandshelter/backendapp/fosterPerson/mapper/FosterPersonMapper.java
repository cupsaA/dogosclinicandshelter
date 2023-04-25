package com.dogosclinicandshelter.backendapp.fosterPerson.mapper;

import com.dogosclinicandshelter.backendapp.fosterPerson.model.dto.FosterPersonDto;
import com.dogosclinicandshelter.backendapp.fosterPerson.model.persistance.FosterPerson;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface FosterPersonMapper {

  FosterPersonDto mapToDto(FosterPerson fosterPerson);

  FosterPerson mapToObject(FosterPersonDto fosterPersonDto);
}
