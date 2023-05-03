package com.dogosclinicandshelter.backendapp.adoptivePerson.model.persistance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "adoptive_person")
public class AdoptivePerson {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, name = "first_name")
  private String firstName;

  @Column(nullable = false, name = "last_name")
  private String lastName;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String city;

  @Column(nullable = false)
  private String address;

  @Column(nullable = false, name = "phone_number")
  private String phoneNumber;

  public AdoptivePerson(String firstName, String lastName, String email, String city,
      String address, String phoneNumber) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.city = city;
    this.address = address;
    this.phoneNumber = phoneNumber;
  }
}
