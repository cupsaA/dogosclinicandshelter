package com.dogosclinicandshelter.backendapp.dog.model.persistance;

import com.dogosclinicandshelter.backendapp.dog.enumClasses.DogSize;
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
@Table(name = "dog_info")
public class DogInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long dogInfoId;

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


  public DogInfo(String dogName, DogSize dogSize, char dogSex, int dogAge, String dogBreed,
      boolean dogChipped) {
    this.dogName = dogName;
    this.dogSize = dogSize;
    this.dogSex = dogSex;
    this.dogAge = dogAge;
    this.dogBreed = dogBreed;
    this.dogChipped = dogChipped;
  }
}
