package com.codeup.novabook.service;

import com.codeup.novabook.domain.Loan;
import com.codeup.novabook.exception.DatabaseException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ILoanService {
    Loan create(Loan loan) throws DatabaseException;
    Loan update(Loan loan) throws DatabaseException;
    boolean delete(Integer id) throws DatabaseException;

    Optional<Loan> findById(Integer id) throws DatabaseException;
    List<Loan> findAll() throws DatabaseException;
    List<Loan> findByMemberId(Integer memberId) throws DatabaseException;
    List<Loan> findByBookId(Integer bookId) throws DatabaseException;
    List<Loan> findActiveLoansByMemberId(Integer memberId) throws DatabaseException;
    List<Loan> findActiveLoansByBookId(Integer bookId) throws DatabaseException;
    List<Loan> findOverdueLoans() throws DatabaseException;

    boolean markAsReturned(Integer loanId) throws DatabaseException;
    Integer countActiveLoansByMemberId(Integer memberId) throws DatabaseException;
    boolean hasActiveLoan(Integer memberId, Integer bookId) throws DatabaseException;

    // Business operations
    Loan borrowBook(Integer memberId, Integer bookId, LocalDate dueDate) throws DatabaseException;
    boolean returnBook(Integer loanId) throws DatabaseException;
}
