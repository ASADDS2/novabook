/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.repository.impl;

import com.codeup.novabook.domain.Loan;
import com.codeup.novabook.exception.DatabaseException;
import com.codeup.novabook.jdbc.JdbcTemplateLight;
import com.codeup.novabook.jdbc.RowMapper;
import com.codeup.novabook.repository.ILoanRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JDBC implementation of the Loan repository interface.
 * 
 * <p>This repository provides data access operations for {@link Loan} entities using
 * JDBC through the {@link JdbcTemplateLight} abstraction. It handles loan transactions
 * including creation, updates, and tracking of borrowed books.</p>
 * 
 * <p><strong>Key Features:</strong></p>
 * <ul>
 *   <li><strong>Loan Tracking</strong> - Manages book lending transactions</li>
 *   <li><strong>Overdue Detection</strong> - Identifies loans past their due date</li>
 *   <li><strong>Return Management</strong> - Tracks returned and active loans</li>
 * </ul>
 * 
 * @author NovaBook Team
 * @version 1.0
 * @since 1.0
 */
public class LoanjdbcRepository implements ILoanRepository {
    
    private final JdbcTemplateLight jdbc;
    private static final Logger logger = Logger.getLogger(LoanjdbcRepository.class.getName());

    /**
     * Constructs a new LoanjdbcRepository with the specified JDBC template.
     * 
     * @param jdbc the JDBC template for database operations
     * @throws NullPointerException if jdbc is null
     */
    public LoanjdbcRepository(JdbcTemplateLight jdbc) { 
        this.jdbc = jdbc; 
    }

    // Row MAPPER
    private static final RowMapper<Loan> LOAN_MAPPER = rs -> {
        Loan loan = new Loan(
            rs.getInt("member_id"),
            rs.getInt("book_id"),
            rs.getDate("date_loaned").toLocalDate(),
            rs.getDate("date_due").toLocalDate()
        );
        loan.setId(rs.getInt("id"));
        loan.setReturned(rs.getBoolean("returned"));
        loan.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        loan.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return loan;
    };

    @Override
    public Loan save(Loan loan) throws DatabaseException {
        String sql = "INSERT INTO loan (member_id, book_id, date_loaned, date_due, returned) VALUES (?, ?, ?, ?, ?)";
        try {
            int rows = jdbc.update(sql, ps -> {
                try {
                    ps.setInt(1, loan.getMemberId());
                    ps.setInt(2, loan.getBookId());
                    ps.setDate(3, java.sql.Date.valueOf(loan.getDateLoaned()));
                    ps.setDate(4, java.sql.Date.valueOf(loan.getDateDue()));
                    ps.setBoolean(5, loan.getReturned());
                } catch (SQLException e) {
                    throw new RuntimeException("Error creating loan", e);
                }
            });
            if (rows != 1) {
                throw new DatabaseException("Failed to create loan");
            }

            return findByMemberId(loan.getMemberId()).stream()
                    .findFirst()
                    .orElseThrow(() -> new DatabaseException("Failed to retrieve created loan"));
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error creating loan", e);
            throw e;
        }
    }

    @Override
    public Loan update(Loan loan) throws DatabaseException {
        String sql = "UPDATE loan SET member_id=?, book_id=?, date_loaned=?, date_due=?, returned=? WHERE id=?";
        try {
            int rows = jdbc.update(sql, ps -> {
                try {
                    ps.setInt(1, loan.getMemberId());
                    ps.setInt(2, loan.getBookId());
                    ps.setDate(3, java.sql.Date.valueOf(loan.getDateLoaned()));
                    ps.setDate(4, java.sql.Date.valueOf(loan.getDateDue()));
                    ps.setBoolean(5, loan.getReturned());
                    ps.setInt(6, loan.getId());
                } catch (SQLException e) {
                    throw new RuntimeException("Error updating loan", e);
                }
            });
            if (rows != 1) {
                throw new DatabaseException("Failed to update loan with id: " + loan.getId());
            }
            return loan;
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error updating loan", e);
            throw e;
        }
    }

    @Override
    public boolean delete(Integer id) throws DatabaseException {
        String sql = "DELETE FROM loan WHERE id = ?";
        try {
            int rows = jdbc.update(sql, ps -> {
                try {
                    ps.setInt(1, id);
                } catch (SQLException e) {
                    throw new RuntimeException("Error deleting loan", e);
                }
            });
            return rows > 0;
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error deleting loan", e);
            throw e;
        }
    }

    @Override
    public Optional<Loan> findById(Integer id) throws DatabaseException {
        String sql = "SELECT * FROM loan WHERE id = ?";
        try {
            List<Loan> loans = jdbc.query(sql, ps -> {
                try {
                    ps.setInt(1, id);
                } catch (SQLException e) {
                    throw new RuntimeException("Error setting parameters", e);
                }
            }, LOAN_MAPPER);
            return loans.isEmpty() ? Optional.empty() : Optional.of(loans.get(0));
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error finding loan by id", e);
            throw e;
        }
    }

    @Override
    public List<Loan> findAll() throws DatabaseException {
        String sql = "SELECT * FROM loan ORDER BY date_loaned DESC";
        try {
            logger.log(Level.INFO, "Loan list executed");
            return jdbc.query(sql, null, LOAN_MAPPER);
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error executing list loans: {0}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Loan> findByMemberId(Integer memberId) throws DatabaseException {
        String sql = "SELECT * FROM loan WHERE member_id = ? ORDER BY date_loaned DESC";
        try {
            List<Loan> list = jdbc.query(sql, ps -> {
                try { 
                    ps.setInt(1, memberId); 
                } catch (SQLException e) { 
                    throw new RuntimeException("Error searching loans by member", e); 
                }
            }, LOAN_MAPPER);
            logger.log(Level.INFO, "Loan search by member executed: {0}", memberId);
            return list;
        } catch (DatabaseException e) { 
            logger.log(Level.SEVERE, "Error executing search by member: {0}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Loan> findByBookId(Integer bookId) throws DatabaseException {
        String sql = "SELECT * FROM loan WHERE book_id = ? ORDER BY date_loaned DESC";
        try {
            List<Loan> list = jdbc.query(sql, ps -> {
                try { 
                    ps.setInt(1, bookId); 
                } catch (SQLException e) { 
                    throw new RuntimeException("Error searching loans by book", e); 
                }
            }, LOAN_MAPPER);
            logger.log(Level.INFO, "Loan search by book executed: {0}", bookId);
            return list;
        } catch (DatabaseException e) { 
            logger.log(Level.SEVERE, "Error executing search by book: {0}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Loan> findActiveLoansByMemberId(Integer memberId) throws DatabaseException {
        String sql = "SELECT * FROM loan WHERE member_id = ? AND returned = FALSE ORDER BY date_loaned DESC";
        try {
            List<Loan> list = jdbc.query(sql, ps -> {
                try { 
                    ps.setInt(1, memberId); 
                } catch (SQLException e) { 
                    throw new RuntimeException("Error searching active loans by member", e); 
                }
            }, LOAN_MAPPER);
            logger.log(Level.INFO, "Active loan search by member executed: {0}", memberId);
            return list;
        } catch (DatabaseException e) { 
            logger.log(Level.SEVERE, "Error executing search active loans by member: {0}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Loan> findActiveLoansByBookId(Integer bookId) throws DatabaseException {
        String sql = "SELECT * FROM loan WHERE book_id = ? AND returned = FALSE ORDER BY date_loaned DESC";
        try {
            List<Loan> list = jdbc.query(sql, ps -> {
                try { 
                    ps.setInt(1, bookId); 
                } catch (SQLException e) { 
                    throw new RuntimeException("Error searching active loans by book", e); 
                }
            }, LOAN_MAPPER);
            logger.log(Level.INFO, "Active loan search by book executed: {0}", bookId);
            return list;
        } catch (DatabaseException e) { 
            logger.log(Level.SEVERE, "Error executing search active loans by book: {0}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Loan> findOverdueLoans() throws DatabaseException {
        String sql = "SELECT * FROM loan WHERE date_due < CURDATE() AND returned = FALSE ORDER BY date_due";
        try {
            logger.log(Level.INFO, "Overdue loans list executed");
            return jdbc.query(sql, null, LOAN_MAPPER);
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error executing overdue loans list: {0}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Loan> findByDateRange(LocalDate startDate, LocalDate endDate) throws DatabaseException {
        String sql = "SELECT * FROM loan WHERE date_loaned BETWEEN ? AND ? ORDER BY date_loaned DESC";
        try {
            List<Loan> list = jdbc.query(sql, ps -> {
                try { 
                    ps.setDate(1, java.sql.Date.valueOf(startDate));
                    ps.setDate(2, java.sql.Date.valueOf(endDate));
                } catch (SQLException e) { 
                    throw new RuntimeException("Error searching loans by date range", e); 
                }
            }, LOAN_MAPPER);
            logger.log(Level.INFO, "Loan search by date range executed: {0} to {1}", new Object[]{startDate, endDate});
            return list;
        } catch (DatabaseException e) { 
            logger.log(Level.SEVERE, "Error executing search by date range: {0}", e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean markAsReturned(Integer loanId) throws DatabaseException {
        String sql = "UPDATE loan SET returned = TRUE WHERE id = ?";
        try {
            int rows = jdbc.update(sql, ps -> {
                try {
                    ps.setInt(1, loanId);
                } catch (SQLException e) {
                    throw new RuntimeException("Error marking loan as returned", e);
                }
            });
            return rows > 0;
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error marking loan as returned", e);
            throw e;
        }
    }

    @Override
    public Integer countActiveLoansByMemberId(Integer memberId) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM loan WHERE member_id = ? AND returned = FALSE";
        try {
            List<Integer> count = jdbc.query(sql, ps -> {
                try { 
                    ps.setInt(1, memberId); 
                } catch (SQLException e) { 
                    throw new RuntimeException("Error counting active loans", e); 
                }
            }, rs -> rs.getInt(1));
            logger.log(Level.INFO, "Active loans count executed for member: {0}", memberId);
            return count.stream().findFirst().orElse(0);
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error executing active loans count: {0}", e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean hasActiveLoan(Integer memberId, Integer bookId) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM loan WHERE member_id = ? AND book_id = ? AND returned = FALSE";
        try {
            List<Integer> count = jdbc.query(sql, ps -> {
                try { 
                    ps.setInt(1, memberId);
                    ps.setInt(2, bookId);
                } catch (SQLException e) { 
                    throw new RuntimeException("Error checking active loan", e); 
                }
            }, rs -> rs.getInt(1));
            logger.log(Level.INFO, "Active loan check executed for member: {0} and book: {1}", new Object[]{memberId, bookId});
            return count.stream().findFirst().orElse(0) > 0;
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error executing active loan check: {0}", e.getMessage());
            throw e;
        }
    }
}