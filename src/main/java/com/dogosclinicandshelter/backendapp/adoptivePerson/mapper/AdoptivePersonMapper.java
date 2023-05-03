package com.dogosclinicandshelter.backendapp.adoptivePerson.mapper;

import com.dogosclinicandshelter.backendapp.adoptivePerson.model.dto.AdoptivePersonDto;
import com.dogosclinicandshelter.backendapp.adoptivePerson.model.persistance.AdoptivePerson;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AdoptivePersonMapper {

  AdoptivePersonDto mapToDto(AdoptivePerson adoptivePerson);

  AdoptivePerson mapToObject(AdoptivePersonDto adoptivePersonDto);

}
