package com.dogosclinicandshelter.backendapp.fosterPerson.repository;

import com.dogosclinicandshelter.backendapp.fosterPerson.model.persistance.FosterPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FosterPersonRepository extends JpaRepository<FosterPerson, Long> {

  boolean existsFosterPersonByEmail(String email);

  boolean existsFosterPersonById(Long id);
}
