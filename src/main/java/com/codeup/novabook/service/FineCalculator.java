package com.codeup.novabook.service;

import java.time.LocalDate;

/**
 * Calculates fines based on due date and a per-day rate.
 * If returned on or before due date, fine is 0.
 */
public class FineCalculator {
    private final int loanDays;
    private final long finePerDay;

    public FineCalculator(int loanDays, long finePerDay) {
        this.loanDays = loanDays;
        this.finePerDay = finePerDay;
    }

    /**
     * Calculates fine using the due date and the actual return date.
     */
    public long calculateFine(LocalDate dueDate, LocalDate returnDate) {
        if (dueDate == null || returnDate == null) return 0L;
        if (!returnDate.isAfter(dueDate)) return 0L;
        long days = java.time.temporal.ChronoUnit.DAYS.between(dueDate, returnDate);
        return days * finePerDay;
    }
}
