package com.dogosclinicandshelter.backendapp.dog.repository;

import com.dogosclinicandshelter.backendapp.dog.model.persistance.DogInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogInfoRepository extends JpaRepository<DogInfo, Long> {

}
