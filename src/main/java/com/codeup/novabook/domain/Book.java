/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.domain;

import java.time.LocalDateTime;

/**
 * Represents a book in the library system.
 * <p>
 * Contains bibliographic information and tracks stock availability.
 * Corresponds to the 'book' table in the database.
 * </p>
 * 
 * @author Coder
 * @version 1.0
 * @since 1.0
 * @see Loan
 */
public class Book {
    
    private Integer id;
    private String isbn;
    private String title;
    private String author;
    private Integer stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    
    /**
     * Default constructor.
     */
    public Book() {}
    
    /**
     * Creates a book with basic information.
     * 
     * @param isbn International Standard Book Number
     * @param title book title
     * @param author book author
     * @param stock number of copies in stock
     */
    public Book(String isbn, String title, String author, Integer stock) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.stock = stock;
    }
    
    // Business logic methods
    
    /**
     * Checks if the book is available for loan.
     * 
     * @return true if stock is greater than 0
     */
    public boolean isAvailable() {
        return stock != null && stock > 0;
    }
    
    /**
     * Decreases stock when a book is loaned.
     * 
     * @throws IllegalStateException if no stock is available
     */
    public void decreaseStock() {
        if (!isAvailable()) {
            throw new IllegalStateException("No stock available for book: " + title);
        }
        this.stock--;
        markAsUpdated();
    }
    
    /**
     * Increases stock when a book is returned.
     */
    public void increaseStock() {
        this.stock++;
        markAsUpdated();
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
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public Integer getStock() {
        return stock;
    }
    
    public void setStock(Integer stock) {
        this.stock = stock;
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
    
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", stock=" + stock +
                '}';
    }
}