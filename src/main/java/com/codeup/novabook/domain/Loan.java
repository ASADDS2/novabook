/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Domain entity representing a book loan transaction.
 * 
 * <p>A loan tracks when a member borrows a book, the due date,
 * and whether the book has been returned.</p>
 * 
 * @author Coder
 * @version 1.0
 */
public class Loan {
    
    private Integer id;
    private Integer memberId;
    private Integer bookId;
    private LocalDate dateLoaned;
    private LocalDate dateDue;
    private Boolean returned;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Default constructor.
     */
    public Loan() {
        this.returned = false;
    }

    /**
     * Constructor with required fields (without due date).
     * 
     * @param memberId Member ID who is borrowing
     * @param bookId Book ID being borrowed
     * @param dateLoaned Date when the book was loaned
     */
    public Loan(Integer memberId, Integer bookId, LocalDate dateLoaned) {
        this.memberId = memberId;
        this.bookId = bookId;
        this.dateLoaned = dateLoaned;
        this.returned = false;
    }

    /**
     * Constructor with all required fields including due date.
     * 
     * @param memberId Member ID who is borrowing
     * @param bookId Book ID being borrowed
     * @param dateLoaned Date when the book was loaned
     * @param dateDue Date when the book is due for return
     */
    public Loan(Integer memberId, Integer bookId, LocalDate dateLoaned, LocalDate dateDue) {
        this.memberId = memberId;
        this.bookId = bookId;
        this.dateLoaned = dateLoaned;
        this.dateDue = dateDue;
        this.returned = false;
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public LocalDate getDateLoaned() {
        return dateLoaned;
    }

    public void setDateLoaned(LocalDate dateLoaned) {
        this.dateLoaned = dateLoaned;
    }

    public LocalDate getDateDue() {
        return dateDue;
    }

    public void setDateDue(LocalDate dateDue) {
        this.dateDue = dateDue;
    }

    public Boolean getReturned() {
        return returned;
    }

    public void setReturned(Boolean returned) {
        this.returned = returned;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Checks if the loan is currently overdue.
     * 
     * @return true if past due date and not returned
     */
    public boolean isOverdue() {
        return !returned && dateDue != null && LocalDate.now().isAfter(dateDue);
    }

    /**
     * Checks if the loan is still active (not returned).
     * 
     * @return true if not returned
     */
    public boolean isActive() {
        return !returned;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", bookId=" + bookId +
                ", dateLoaned=" + dateLoaned +
                ", dateDue=" + dateDue +
                ", returned=" + returned +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}