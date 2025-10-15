package com.codeup.novabook.tests;

import com.codeup.novabook.service.FineCalculator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FineCalculatorTest {

    @Test
    void zeroFineWhenReturnedOnOrBeforeDue() {
        FineCalculator calc = new FineCalculator(7, 1500);
        assertEquals(0, calc.calculateFine(LocalDate.of(2025,1,10), LocalDate.of(2025,1,10)));
        assertEquals(0, calc.calculateFine(LocalDate.of(2025,1,10), LocalDate.of(2025,1,9)));
    }

    @Test
    void fineAccumulatesPerDayLate() {
        FineCalculator calc = new FineCalculator(7, 1500);
        // 3 days late
        assertEquals(4500, calc.calculateFine(LocalDate.of(2025,1,10), LocalDate.of(2025,1,13)));
    }
}
