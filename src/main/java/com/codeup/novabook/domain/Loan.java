/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a book loan transaction.
 * <p>
 * Tracks the lending of books to members.
 * Corresponds to the 'loan' table in the database.
 * </p>
 * 
 * @author Coder
 * @version 1.0
 * @since 1.0
 * @see Book
 * @see Member
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
    
    // Related entities (optional, for display purposes)
    private Book book;
    private Member member;
    
    // Constructors
    
    /**
     * Default constructor.
     */
    public Loan() {
        this.dateLoaned = LocalDate.now();
        this.returned = false;
    }
    
    /**
     * Creates a new loan.
     * 
     * @param memberId ID of the member borrowing the book
     * @param bookId ID of the book being loaned
     * @param dateDue date when the book should be returned
     */
    public Loan(Integer memberId, Integer bookId, LocalDate dateDue) {
        this();
        this.memberId = memberId;
        this.bookId = bookId;
        this.dateDue = dateDue;
    }
    
    // Business logic methods
    
    /**
     * Checks if the loan is overdue.
     * 
     * @return true if current date is past due date and not returned
     */
    public boolean isOverdue() {
        return !returned && 
               dateDue != null && 
               LocalDate.now().isAfter(dateDue);
    }
    
    /**
     * Marks the loan as returned.
     */
    public void markAsReturned() {
        this.returned = true;
        markAsUpdated();
    }
    
    /**
     * Calculates days overdue.
     * 
     * @return number of days overdue, or 0 if not overdue
     */
    public long getDaysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(dateDue, LocalDate.now());
    }
    
    /**
     * Calculates days until due date.
     * 
     * @return days remaining until due, negative if overdue
     */
    public long getDaysUntilDue() {
        if (returned || dateDue == null) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), dateDue);
    }
    
    /**
     * Checks if the loan is active (not returned).
     * 
     * @return true if loan is still active
     */
    public boolean isActive() {
        return !returned;
    }
    
    /**
     * Updates the updatedAt timestamp to current time.
     */
    public void markAsUpdated() {
        this.updatedAt = LocalDateTime.now();
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
    
    public Book getBook() {
        return book;
    }
    
    public void setBook(Book book) {
        this.book = book;
    }
    
    public Member getMember() {
        return member;
    }
    
    public void setMember(Member member) {
        this.member = member;
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
                ", isOverdue=" + isOverdue() +
                '}';
    }
}