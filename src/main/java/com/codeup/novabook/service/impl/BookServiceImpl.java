package com.codeup.novabook.service.impl;

import com.codeup.novabook.domain.Book;
import com.codeup.novabook.exception.DatabaseException;
import com.codeup.novabook.repository.IBookRepository;
import com.codeup.novabook.service.IBookService;
import com.codeup.novabook.util.csv.BookCsv;

import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements IBookService {

    private final IBookRepository repo;

    public BookServiceImpl(IBookRepository repo) {
        this.repo = repo;
    }

    @Override
    public Book create(Book book) throws DatabaseException {
        validateBook(book);
        return repo.save(book);
    }

    @Override
    public Book update(Book book) throws DatabaseException {
        validateBook(book);
        return repo.update(book);
    }

    @Override
    public boolean delete(Integer id) throws DatabaseException {
        return repo.delete(id);
    }

    @Override
    public Optional<Book> findById(Integer id) throws DatabaseException {
        return repo.findById(id);
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) throws DatabaseException {
        return repo.findByIsbn(isbn);
    }

    @Override
    public List<Book> findAll() throws DatabaseException {
        return repo.findAll();
    }

    @Override
    public List<Book> findByTitle(String title) throws DatabaseException {
        return repo.findByTitle(title);
    }

    @Override
    public List<Book> findByAuthor(String author) throws DatabaseException {
        return repo.findByAuthor(author);
    }

    @Override
    public boolean updateStock(Integer bookId, Integer newStock) throws DatabaseException {
        return repo.updateStock(bookId, newStock);
    }

    @Override
    public int importFromCsv(Reader reader) throws Exception {
        List<Book> books = BookCsv.read(reader);
        int count = 0;
        for (Book b : books) {
            validateBook(b);
            if (repo.findByIsbn(b.getIsbn()).isPresent()) {
                // update existing
                Book existing = repo.findByIsbn(b.getIsbn()).get();
                existing.setTitle(b.getTitle());
                existing.setAuthor(b.getAuthor());
                existing.setStock(b.getStock());
                repo.update(existing);
            } else {
                repo.save(b);
            }
            count++;
        }
        return count;
    }

    @Override
    public void exportToCsv(Writer writer) throws Exception {
        List<Book> all = repo.findAll();
        BookCsv.write(all, writer);
    }
    private void validateBook(Book b) {
        if (b.getStock() == null || b.getStock() < 0) {
            throw new com.codeup.novabook.exception.BusinessException("Stock must be >= 0");
        }
        if (b.getIsbn() == null || b.getIsbn().isBlank()) {
            throw new com.codeup.novabook.exception.BusinessException("ISBN is required");
        }
        if (b.getTitle() == null || b.getTitle().isBlank()) {
            throw new com.codeup.novabook.exception.BusinessException("Title is required");
        }
    }
}
