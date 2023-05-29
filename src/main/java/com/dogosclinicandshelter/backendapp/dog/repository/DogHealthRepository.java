package com.dogosclinicandshelter.backendapp.dog.repository;

import com.dogosclinicandshelter.backendapp.dog.model.persistance.DogHealth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogHealthRepository extends JpaRepository<DogHealth, Long> {

  boolean existsDogHealthByDogHealthId(Long dogHealthId);
}
