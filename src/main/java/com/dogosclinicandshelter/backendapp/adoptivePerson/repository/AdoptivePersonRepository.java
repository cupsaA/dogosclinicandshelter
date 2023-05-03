package com.dogosclinicandshelter.backendapp.adoptivePerson.repository;

import com.dogosclinicandshelter.backendapp.adoptivePerson.model.persistance.AdoptivePerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdoptivePersonRepository extends JpaRepository<AdoptivePerson, Long> {

  boolean existsAdoptivePersonByEmail(String email);

  boolean existsAdoptivePersonById(Long id);
}
