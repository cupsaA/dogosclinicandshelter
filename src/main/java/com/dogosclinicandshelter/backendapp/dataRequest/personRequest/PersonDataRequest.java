package com.dogosclinicandshelter.backendapp.dataRequest.personRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PersonDataRequest {
  private String firstName;

  private String lastName;

  private String email;

  private String city;

  private String address;

  private String phoneNumber;
}
