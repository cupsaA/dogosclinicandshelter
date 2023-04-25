package com.dogosclinicandshelter.backendapp.dog.repository;

import com.dogosclinicandshelter.backendapp.dog.model.persistance.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogRepository extends JpaRepository<Dog, Long> {

}
