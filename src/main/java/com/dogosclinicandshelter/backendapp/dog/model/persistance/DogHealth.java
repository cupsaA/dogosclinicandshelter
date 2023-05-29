package com.dogosclinicandshelter.backendapp.dog.model.persistance;

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

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "dog_health")
public class DogHealth {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long dogHealthId;

  @Column(nullable = false, name = "spayed")
  private boolean dogSpayed;

  @Column(nullable = false, name = "vaccinated")
  private boolean dogVaccinated;

  @Column(nullable = false, name = "dewormed")
  private boolean dogDewormed;

  public DogHealth(boolean dogSpayed, boolean dogVaccinated, boolean dogDewormed) {
    this.dogSpayed = dogSpayed;
    this.dogVaccinated = dogVaccinated;
    this.dogDewormed = dogDewormed;
  }
}
