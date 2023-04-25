package com.dogosclinicandshelter.backendapp.dog.enumClasses;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AdoptionStatusTest {
  @Test
  void testAdoptionStatusEnum() {
    assertEquals("ADOPTED", AdoptionStatus.ADOPTED.status);
    assertEquals("IN PROGRESS", AdoptionStatus.IN_PROGRESS.status);
    assertEquals("NOT READY FOR ADOPTION", AdoptionStatus.NOT_READY_FOR_ADOPTION.status);
    assertEquals("READY FOR ADOPTION", AdoptionStatus.READY_FOR_ADOPTION.status);
    assertEquals("UNADOPTABLE", AdoptionStatus.UNADOPTABLE.status);

  }
}