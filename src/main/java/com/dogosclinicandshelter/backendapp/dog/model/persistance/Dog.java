package com.dogosclinicandshelter.backendapp.dog.model.persistance;

import com.dogosclinicandshelter.backendapp.dog.enumClasses.AdoptionStatus;
import com.dogosclinicandshelter.backendapp.dog.enumClasses.DogSize;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "dog")
public class Dog {
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long dogId;

  @Column(nullable = false, name = "name")
  private String dogName;

  @Column(nullable = false, name = "size")
  @Enumerated(EnumType.STRING)
  private DogSize dogSize;

  @Column(nullable = false, name = "sex")
  private char dogSex;

  @Column(nullable = false, name = "age")
  private int dogAge;

  @Column(nullable = false, name = "breed")
  private String dogBreed;

  @Column(nullable = false, name = "chipped")
  private boolean dogChipped;

  @Column(nullable = false, name = "spayed")
  private boolean dogSpayed;

  @Column(nullable = false, name = "vaccinated")
  private boolean dogVaccinated;

  @Column(nullable = false, name = "dewormed")
  private boolean dogDewormed;

  private AdoptionStatus adoptionStatus;

  private LocalDateTime entryDateTime;

  public Dog(String dogName, DogSize dogSize, char dogSex, int dogAge, String dogBreed,
      boolean dogChipped, boolean dogSpayed, boolean dogVaccinated, boolean dogDewormed,
      AdoptionStatus adoptionStatus, LocalDateTime entryDateTime) {
    this.dogName = dogName;
    this.dogSize = dogSize;
    this.dogSex = dogSex;
    this.dogAge = dogAge;
    this.dogBreed = dogBreed;
    this.dogChipped = dogChipped;
    this.dogSpayed = dogSpayed;
    this.dogVaccinated = dogVaccinated;
    this.dogDewormed = dogDewormed;
    this.adoptionStatus = adoptionStatus;
    this.entryDateTime = entryDateTime;
  }
}
