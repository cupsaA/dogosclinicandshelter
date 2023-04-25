package com.dogosclinicandshelter.backendapp.fosterPerson.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FosterPersonRequest {
  private String firstName;

  private String lastName;

  private String email;

  private String city;

  private String address;

  private String phoneNumber;
}
