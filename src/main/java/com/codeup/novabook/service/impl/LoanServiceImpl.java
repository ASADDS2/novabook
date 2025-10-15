package com.codeup.novabook.service.impl;

import com.codeup.novabook.domain.Book;
import com.codeup.novabook.domain.Loan;
import com.codeup.novabook.domain.Member;
import com.codeup.novabook.exception.DatabaseException;
import com.codeup.novabook.jdbc.JdbcTemplateLight;
import com.codeup.novabook.repository.IBookRepository;
import com.codeup.novabook.repository.ILoanRepository;
import com.codeup.novabook.repository.IMemberRepository;
import com.codeup.novabook.service.ILoanService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class LoanServiceImpl implements ILoanService {

    private final ILoanRepository loanRepo;
    private final IBookRepository bookRepo;
    private final IMemberRepository memberRepo;
    private final JdbcTemplateLight jdbc;
    private final com.codeup.novabook.service.FineCalculator fineCalculator;

    public LoanServiceImpl(ILoanRepository loanRepo, IBookRepository bookRepo, IMemberRepository memberRepo, JdbcTemplateLight jdbc, com.codeup.novabook.service.FineCalculator fineCalculator) {
        this.loanRepo = loanRepo;
        this.bookRepo = bookRepo;
        this.memberRepo = memberRepo;
        this.jdbc = jdbc;
        this.fineCalculator = fineCalculator;
    }

    @Override
    public Loan create(Loan loan) throws DatabaseException { return loanRepo.save(loan); }

    @Override
    public Loan update(Loan loan) throws DatabaseException { return loanRepo.update(loan); }

    @Override
    public boolean delete(Integer id) throws DatabaseException { return loanRepo.delete(id); }

    @Override
    public Optional<Loan> findById(Integer id) throws DatabaseException { return loanRepo.findById(id); }

    @Override
    public List<Loan> findAll() throws DatabaseException { return loanRepo.findAll(); }

    @Override
    public List<Loan> findByMemberId(Integer memberId) throws DatabaseException { return loanRepo.findByMemberId(memberId); }

    @Override
    public List<Loan> findByBookId(Integer bookId) throws DatabaseException { return loanRepo.findByBookId(bookId); }

    @Override
    public List<Loan> findActiveLoansByMemberId(Integer memberId) throws DatabaseException { return loanRepo.findActiveLoansByMemberId(memberId); }

    @Override
    public List<Loan> findActiveLoansByBookId(Integer bookId) throws DatabaseException { return loanRepo.findActiveLoansByBookId(bookId); }

    @Override
    public List<Loan> findOverdueLoans() throws DatabaseException { return loanRepo.findOverdueLoans(); }

    @Override
    public boolean markAsReturned(Integer loanId) throws DatabaseException { return loanRepo.markAsReturned(loanId); }

    @Override
    public Integer countActiveLoansByMemberId(Integer memberId) throws DatabaseException { return loanRepo.countActiveLoansByMemberId(memberId); }

    @Override
    public boolean hasActiveLoan(Integer memberId, Integer bookId) throws DatabaseException { return loanRepo.hasActiveLoan(memberId, bookId); }

    @Override
    public Loan borrowBook(Integer memberId, Integer bookId, LocalDate dueDate) throws DatabaseException {
        return jdbc.txExecute(conn -> {
            // validations
            Member member = memberRepo.findById(memberId).orElseThrow(() -> new DatabaseException("Member not found: " + memberId));
            if (!member.isActiveAndNotDeleted()) throw new DatabaseException("Member is not active");

            Book book = bookRepo.findById(bookId).orElseThrow(() -> new DatabaseException("Book not found: " + bookId));
            if (!book.isAvailable()) throw new DatabaseException("Book not available in stock");

            if (loanRepo.hasActiveLoan(memberId, bookId)) throw new DatabaseException("Member already has an active loan for this book");

            // update stock and create loan
            book.decreaseStock();
            if (!bookRepo.updateStock(bookId, book.getStock())) {
                throw new DatabaseException("Failed to update book stock");
            }

            Loan loan = new Loan(memberId, bookId, LocalDate.now(), dueDate);
            return loanRepo.save(loan);
        });
    }

    @Override
    public boolean returnBook(Integer loanId) throws DatabaseException {
        return jdbc.txExecute(conn -> {
            Loan loan = loanRepo.findById(loanId).orElseThrow(() -> new DatabaseException("Loan not found: " + loanId));
            if (Boolean.TRUE.equals(loan.getReturned())) return true; // idempotent

            // mark as returned
            if (!loanRepo.markAsReturned(loanId)) {
                throw new DatabaseException("Failed to mark loan as returned");
            }

            // calculate fine and log
            long fine = fineCalculator.calculateFine(loan.getDateDue(), java.time.LocalDate.now());
            com.codeup.novabook.infra.HttpLogger.log("PATCH /loans/" + loanId + " return -> fine=" + fine);

            // increase stock
            Book book = bookRepo.findById(loan.getBookId()).orElseThrow(() -> new DatabaseException("Book not found: " + loan.getBookId()));
            book.increaseStock();
            return bookRepo.updateStock(book.getId(), book.getStock());
        });
    }
}
