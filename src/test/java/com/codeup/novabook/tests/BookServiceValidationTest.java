package com.codeup.novabook.tests;

import com.codeup.novabook.domain.Book;
import com.codeup.novabook.exception.BusinessException;
import com.codeup.novabook.repository.IBookRepository;
import com.codeup.novabook.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class BookServiceValidationTest {

    @Test
    void rejectNegativeStockOnCreate() {
        IBookRepository repo = mock(IBookRepository.class);
        BookServiceImpl svc = new BookServiceImpl(repo);
        Book b = new Book("999","T","A", -1);
        assertThrows(BusinessException.class, () -> svc.create(b));
    }
}
