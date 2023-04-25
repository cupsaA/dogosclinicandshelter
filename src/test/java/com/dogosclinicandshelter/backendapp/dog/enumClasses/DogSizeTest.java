package com.dogosclinicandshelter.backendapp.dog.enumClasses;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DogSizeTest {
  @Test
  void testDogSizeEnum() {
    assertEquals("XTRASMALL", DogSize.XS.size);
    assertEquals("SMALL", DogSize.S.size);
    assertEquals("MEDIUM", DogSize.M.size);
    assertEquals("LARGE", DogSize.L.size);
    assertEquals("XTRALARGE", DogSize.XL.size);
  }
}