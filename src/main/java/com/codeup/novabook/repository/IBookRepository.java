package com.codeup.novabook.repository;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
import com.codeup.novabook.domain.Book;
import com.codeup.novabook.exception.DatabaseException;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Book entity operations.
 * Defines CRUD operations and custom queries for Book management.
 * 
 * @author Coder
 * @version 1.0
 */
public interface IBookRepository {
    
    /**
     * Saves a new book to the database.
     * 
     * @param book Book entity to save
     * @return The saved book with generated ID
     * @throws DatabaseException if a database error occurs
     */
    Book save(Book book) throws DatabaseException;
    
    /**
     * Updates an existing book in the database.
     * 
     * @param book Book entity with updated data
     * @return The updated book
     * @throws DatabaseException if a database error occurs
     */
    Book update(Book book) throws DatabaseException;
    
    /**
     * Deletes a book by its ID.
     * 
     * @param id Book ID to delete
     * @return true if deletion was successful, false otherwise
     * @throws DatabaseException if a database error occurs
     */
    boolean delete(Integer id) throws DatabaseException;
    
    /**
     * Finds a book by its ID.
     * 
     * @param id Book ID to search
     * @return Optional containing the book if found, empty otherwise
     * @throws DatabaseException if a database error occurs
     */
    Optional<Book> findById(Integer id) throws DatabaseException;
    
    /**
     * Finds a book by its ISBN.
     * 
     * @param isbn ISBN to search
     * @return Optional containing the book if found, empty otherwise
     * @throws DatabaseException if a database error occurs
     */
    Optional<Book> findByIsbn(String isbn) throws DatabaseException;
    
    /**
     * Retrieves all books from the database.
     * 
     * @return List of all books
     * @throws DatabaseException if a database error occurs
     */
    List<Book> findAll() throws DatabaseException;
    
    /**
     * Searches books by title (partial match, case-insensitive).
     * 
     * @param title Title or part of title to search
     * @return List of books matching the criteria
     * @throws DatabaseException if a database error occurs
     */
    List<Book> findByTitle(String title) throws DatabaseException;
    
    /**
     * Searches books by author (partial match, case-insensitive).
     * 
     * @param author Author name or part of it to search
     * @return List of books matching the criteria
     * @throws DatabaseException if a database error occurs
     */
    List<Book> findByAuthor(String author) throws DatabaseException;
    
    /**
     * Finds books with stock greater than or equal to specified quantity.
     * 
     * @param minStock Minimum stock quantity
     * @return List of books with sufficient stock
     * @throws DatabaseException if a database error occurs
     */
    List<Book> findByStockGreaterThan(Integer minStock) throws DatabaseException;
    
    /**
     * Updates the stock of a book.
     * 
     * @param bookId Book ID to update
     * @param newStock New stock quantity
     * @return true if update was successful, false otherwise
     * @throws DatabaseException if a database error occurs
     */
    boolean updateStock(Integer bookId, Integer newStock) throws DatabaseException;
    
    /**
     * Checks if a book with the given ISBN exists.
     * 
     * @param isbn ISBN to check
     * @return true if exists, false otherwise
     * @throws DatabaseException if a database error occurs
     */
    boolean existsByIsbn(String isbn) throws DatabaseException;
}
