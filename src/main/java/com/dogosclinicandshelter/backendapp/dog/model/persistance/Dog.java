package com.dogosclinicandshelter.backendapp.dog.model.persistance;

import com.dogosclinicandshelter.backendapp.dog.enumClasses.AdoptionStatus;
import com.dogosclinicandshelter.backendapp.dog.enumClasses.DogSize;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Dog {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long dogId;
  private String dogName;
  private DogSize dogSize;
  private char dogSex;
  private int dogAge;
  private String dogBreed;
  private boolean dogChipped;
  private boolean dogSpayed;
  private boolean dogVaccinated;
  private boolean dogDewormed;
  private AdoptionStatus adoptionStatus;
  private LocalDateTime entryDateTime;
}
