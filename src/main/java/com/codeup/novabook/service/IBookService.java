package com.codeup.novabook.service;

import com.codeup.novabook.domain.Book;
import com.codeup.novabook.exception.DatabaseException;

import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Optional;

public interface IBookService {
    Book create(Book book) throws DatabaseException;
    Book update(Book book) throws DatabaseException;
    boolean delete(Integer id) throws DatabaseException;

    Optional<Book> findById(Integer id) throws DatabaseException;
    Optional<Book> findByIsbn(String isbn) throws DatabaseException;
    List<Book> findAll() throws DatabaseException;
    List<Book> findByTitle(String title) throws DatabaseException;
    List<Book> findByAuthor(String author) throws DatabaseException;

    boolean updateStock(Integer bookId, Integer newStock) throws DatabaseException;

    int importFromCsv(Reader reader) throws Exception;
    void exportToCsv(Writer writer) throws Exception;
}
