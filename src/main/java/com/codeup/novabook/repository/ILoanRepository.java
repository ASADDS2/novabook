/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.codeup.novabook.repository;

import com.codeup.novabook.domain.Loan;
import com.codeup.novabook.exception.DatabaseException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Loan entity operations.
 * Defines CRUD operations and custom queries for Loan management.
 * 
 * @author Coder
 * @version 1.0
 */
public interface ILoanRepository {
    
    /**
     * Saves a new loan to the database.
     * 
     * @param loan Loan entity to save
     * @return The saved loan with generated ID
     * @throws DatabaseException if a database error occurs
     */
    Loan save(Loan loan) throws DatabaseException;
    
    /**
     * Updates an existing loan in the database.
     * 
     * @param loan Loan entity with updated data
     * @return The updated loan
     * @throws DatabaseException if a database error occurs
     */
    Loan update(Loan loan) throws DatabaseException;
    
    /**
     * Deletes a loan by its ID.
     * 
     * @param id Loan ID to delete
     * @return true if deletion was successful, false otherwise
     * @throws DatabaseException if a database error occurs
     */
    boolean delete(Integer id) throws DatabaseException;
    
    /**
     * Finds a loan by its ID.
     * 
     * @param id Loan ID to search
     * @return Optional containing the loan if found, empty otherwise
     * @throws DatabaseException if a database error occurs
     */
    Optional<Loan> findById(Integer id) throws DatabaseException;
    
    /**
     * Retrieves all loans from the database.
     * 
     * @return List of all loans
     * @throws DatabaseException if a database error occurs
     */
    List<Loan> findAll() throws DatabaseException;
    
    /**
     * Finds all loans for a specific member.
     * 
     * @param memberId Member ID to search loans
     * @return List of loans for the member
     * @throws DatabaseException if a database error occurs
     */
    List<Loan> findByMemberId(Integer memberId) throws DatabaseException;
    
    /**
     * Finds all loans for a specific book.
     * 
     * @param bookId Book ID to search loans
     * @return List of loans for the book
     * @throws DatabaseException if a database error occurs
     */
    List<Loan> findByBookId(Integer bookId) throws DatabaseException;
    
    /**
     * Finds all active loans (not returned) for a specific member.
     * 
     * @param memberId Member ID to search active loans
     * @return List of active loans for the member
     * @throws DatabaseException if a database error occurs
     */
    List<Loan> findActiveLoansByMemberId(Integer memberId) throws DatabaseException;
    
    /**
     * Finds all active loans (not returned) for a specific book.
     * 
     * @param bookId Book ID to search active loans
     * @return List of active loans for the book
     * @throws DatabaseException if a database error occurs
     */
    List<Loan> findActiveLoansByBookId(Integer bookId) throws DatabaseException;
    
    /**
     * Finds all overdue loans (date_due passed and not returned).
     * 
     * @return List of overdue loans
     * @throws DatabaseException if a database error occurs
     */
    List<Loan> findOverdueLoans() throws DatabaseException;
    
    /**
     * Finds loans by date range.
     * 
     * @param startDate Start date of range
     * @param endDate End date of range
     * @return List of loans within the date range
     * @throws DatabaseException if a database error occurs
     */
    List<Loan> findByDateRange(LocalDate startDate, LocalDate endDate) throws DatabaseException;
    
    /**
     * Marks a loan as returned.
     * 
     * @param loanId Loan ID to mark as returned
     * @return true if update was successful, false otherwise
     * @throws DatabaseException if a database error occurs
     */
    boolean markAsReturned(Integer loanId) throws DatabaseException;
    
    /**
     * Counts active loans for a specific member.
     * 
     * @param memberId Member ID to count loans
     * @return Number of active loans
     * @throws DatabaseException if a database error occurs
     */
    Integer countActiveLoansByMemberId(Integer memberId) throws DatabaseException;
    
    /**
     * Checks if a member has an active loan for a specific book.
     * 
     * @param memberId Member ID to check
     * @param bookId Book ID to check
     * @return true if member has active loan for the book, false otherwise
     * @throws DatabaseException if a database error occurs
     */
    boolean hasActiveLoan(Integer memberId, Integer bookId) throws DatabaseException;
}
