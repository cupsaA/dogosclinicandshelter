package com.dogosclinicandshelter.backendapp.adoptivePerson.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdoptivePersonDto {
  private Long id;

  private String firstName;

  private String lastName;

  private String email;

  private String city;

  private String address;

  private String phoneNumber;

  public AdoptivePersonDto(Long id, String firstName, String lastName, String email,
      String city, String address, String phoneNumber) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.city = city;
    this.address = address;
    this.phoneNumber = phoneNumber;
  }
}
