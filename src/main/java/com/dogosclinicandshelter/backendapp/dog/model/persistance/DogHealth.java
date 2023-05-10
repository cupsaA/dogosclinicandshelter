package com.dogosclinicandshelter.backendapp.dog.model.persistance;

import javax.persistence.Column;
import javax.persistence.Entity;
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
