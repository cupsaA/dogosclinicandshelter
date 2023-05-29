//package com.dogosclinicandshelter.backendapp.dog.model.persistance;
//
//import com.dogosclinicandshelter.backendapp.dog.enumClasses.AdoptionStatus;
//import java.time.LocalDateTime;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import lombok.ToString;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@ToString
//@EqualsAndHashCode
//@Entity
//@Table(name = "dog")
//public class Dog {
//
//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  private Long dogId;
//
//  @Column(nullable = false, name = "info")
//  private DogInfo dogInfo;
//
//  @Column(nullable = false, name = "health")
//  private DogHealth dogHealth;
//
//  @Enumerated(EnumType.STRING)
//  private AdoptionStatus adoptionStatus;
//
//  private LocalDateTime entryDateTime;
//
//  public Dog(DogInfo dogInfo,
//      DogHealth dogHealth,
//      AdoptionStatus adoptionStatus, LocalDateTime entryDateTime) {
//    this.dogInfo = dogInfo;
//    this.dogHealth = dogHealth;
//    this.adoptionStatus = adoptionStatus;
//    this.entryDateTime = entryDateTime;
//  }
//}
