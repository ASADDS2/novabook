/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.repository.impl;

import com.codeup.novabook.domain.Book;
import com.codeup.novabook.exception.DatabaseException;
import com.codeup.novabook.jdbc.JdbcTemplateLight;
import com.codeup.novabook.jdbc.RowMapper;
import com.codeup.novabook.repository.IBookRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JDBC implementation of the Book repository interface.
 * 
 * <p>This repository provides data access operations for {@link Book} entities using
 * JDBC through the {@link JdbcTemplateLight} abstraction. It handles book catalog
 * management including stock tracking and search capabilities.</p>
 * 
 * <p><strong>Key Features:</strong></p>
 * <ul>
 *   <li><strong>ISBN Uniqueness</strong> - Ensures no duplicate ISBNs in the system</li>
 *   <li><strong>Stock Management</strong> - Tracks book availability for lending</li>
 *   <li><strong>Search Capabilities</strong> - Supports search by title, author, and ISBN</li>
 * </ul>
 * 
 * @author Coder
 * @version 1.0
 * @since 1.0
 */
public class BookjdbcRepository implements IBookRepository {
    
    private final JdbcTemplateLight jdbc;
    private static final Logger logger = Logger.getLogger(BookjdbcRepository.class.getName());

    /**
     * Constructs a new BookjdbcRepository with the specified JDBC template.
     * 
     * @param jdbc the JDBC template for database operations
     * @throws NullPointerException if jdbc is null
     */
    public BookjdbcRepository(JdbcTemplateLight jdbc) { 
        this.jdbc = jdbc; 
    }

    // Row MAPPER
    private static final RowMapper<Book> BOOK_MAPPER = rs -> {
        Book book = new Book(
            rs.getString("isbn"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getInt("stock")
        );
        book.setId(rs.getInt("id"));
        book.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        book.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return book;
    };

    @Override
    public Book save(Book book) throws DatabaseException {
        if (existsByIsbn(book.getIsbn())) {
            throw new DatabaseException("ISBN already exists: " + book.getIsbn());
        }

        String sql = "INSERT INTO book (isbn, title, author, stock) VALUES (?, ?, ?, ?)";
        try {
            int rows = jdbc.update(sql, ps -> {
                try {
                    ps.setString(1, book.getIsbn());
                    ps.setString(2, book.getTitle());
                    ps.setString(3, book.getAuthor());
                    ps.setInt(4, book.getStock());
                } catch (SQLException e) {
                    throw new RuntimeException("Error creating book", e);
                }
            });
            if (rows != 1) {
                throw new DatabaseException("Failed to create book");
            }

            return findByIsbn(book.getIsbn()).orElseThrow(() -> 
                new DatabaseException("Failed to retrieve created book"));
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error creating book", e);
            throw e;
        }
    }

    @Override
    public Book update(Book book) throws DatabaseException {
        String sql = "UPDATE book SET isbn=?, title=?, author=?, stock=? WHERE id=?";
        try {
            int rows = jdbc.update(sql, ps -> {
                try {
                    ps.setString(1, book.getIsbn());
                    ps.setString(2, book.getTitle());
                    ps.setString(3, book.getAuthor());
                    ps.setInt(4, book.getStock());
                    ps.setInt(5, book.getId());
                } catch (SQLException e) {
                    throw new RuntimeException("Error updating book", e);
                }
            });
            if (rows != 1) {
                throw new DatabaseException("Failed to update book with id: " + book.getId());
            }
            return book;
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error updating book", e);
            throw e;
        }
    }

    @Override
    public boolean delete(Integer id) throws DatabaseException {
        String sql = "DELETE FROM book WHERE id = ?";
        try {
            int rows = jdbc.update(sql, ps -> {
                try {
                    ps.setInt(1, id);
                } catch (SQLException e) {
                    throw new RuntimeException("Error deleting book", e);
                }
            });
            return rows > 0;
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error deleting book", e);
            throw e;
        }
    }

    @Override
    public Optional<Book> findById(Integer id) throws DatabaseException {
        String sql = "SELECT * FROM book WHERE id = ?";
        try {
            List<Book> books = jdbc.query(sql, ps -> {
                try {
                    ps.setInt(1, id);
                } catch (SQLException e) {
                    throw new RuntimeException("Error setting parameters", e);
                }
            }, BOOK_MAPPER);
            return books.isEmpty() ? Optional.empty() : Optional.of(books.get(0));
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error finding book by id", e);
            throw e;
        }
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) throws DatabaseException {
        String sql = "SELECT * FROM book WHERE isbn=?";
        try {
            List<Book> list = jdbc.query(sql, ps -> {
                try { 
                    ps.setString(1, isbn); 
                } catch (SQLException e) { 
                    throw new RuntimeException("Error searching book by ISBN", e); 
                }
            }, BOOK_MAPPER);
            logger.log(Level.INFO, "Book search by ISBN executed: {0}", isbn);
            return list.stream().findFirst();
        } catch (DatabaseException e) { 
            logger.log(Level.SEVERE, "Error executing search by ISBN: {0}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Book> findAll() throws DatabaseException {
        String sql = "SELECT * FROM book ORDER BY title";
        try {
            logger.log(Level.INFO, "Book list executed");
            return jdbc.query(sql, null, BOOK_MAPPER);
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error executing list books: {0}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Book> findByTitle(String title) throws DatabaseException {
        String sql = "SELECT * FROM book WHERE LOWER(title) LIKE LOWER(?) ORDER BY title";
        try {
            List<Book> list = jdbc.query(sql, ps -> {
                try { 
                    ps.setString(1, "%" + title + "%"); 
                } catch (SQLException e) { 
                    throw new RuntimeException("Error searching books by title", e); 
                }
            }, BOOK_MAPPER);
            logger.log(Level.INFO, "Book search by title executed: {0}", title);
            return list;
        } catch (DatabaseException e) { 
            logger.log(Level.SEVERE, "Error executing search by title: {0}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Book> findByAuthor(String author) throws DatabaseException {
        String sql = "SELECT * FROM book WHERE LOWER(author) LIKE LOWER(?) ORDER BY author, title";
        try {
            List<Book> list = jdbc.query(sql, ps -> {
                try { 
                    ps.setString(1, "%" + author + "%"); 
                } catch (SQLException e) { 
                    throw new RuntimeException("Error searching books by author", e); 
                }
            }, BOOK_MAPPER);
            logger.log(Level.INFO, "Book search by author executed: {0}", author);
            return list;
        } catch (DatabaseException e) { 
            logger.log(Level.SEVERE, "Error executing search by author: {0}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Book> findByStockGreaterThan(Integer minStock) throws DatabaseException {
        String sql = "SELECT * FROM book WHERE stock >= ? ORDER BY title";
        try {
            List<Book> list = jdbc.query(sql, ps -> {
                try { 
                    ps.setInt(1, minStock); 
                } catch (SQLException e) { 
                    throw new RuntimeException("Error searching books by stock", e); 
                }
            }, BOOK_MAPPER);
            logger.log(Level.INFO, "Book search by stock executed: {0}", minStock);
            return list;
        } catch (DatabaseException e) { 
            logger.log(Level.SEVERE, "Error executing search by stock: {0}", e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean updateStock(Integer bookId, Integer newStock) throws DatabaseException {
        String sql = "UPDATE book SET stock=? WHERE id=?";
        try {
            int rows = jdbc.update(sql, ps -> {
                try {
                    ps.setInt(1, newStock);
                    ps.setInt(2, bookId);
                } catch (SQLException e) {
                    throw new RuntimeException("Error updating book stock", e);
                }
            });
            return rows > 0;
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error updating book stock", e);
            throw e;
        }
    }

    @Override
    public boolean existsByIsbn(String isbn) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM book WHERE isbn=?";
        try {
            List<Integer> count = jdbc.query(sql, ps -> {
                try { 
                    ps.setString(1, isbn); 
                } catch (SQLException e) { 
                    throw new RuntimeException("Error checking book existence", e); 
                }
            }, rs -> rs.getInt(1));
            logger.log(Level.INFO, "Book exists check executed: {0}", isbn);
            return count.stream().findFirst().orElse(0) > 0;
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error executing book exists check: {0}", e.getMessage());
            throw e;
        }
    }
}