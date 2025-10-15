package com.codeup.novabook.tests;

import com.codeup.novabook.domain.Book;
import com.codeup.novabook.repository.IBookRepository;
import com.codeup.novabook.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceImplTest {

    @Test
    void importAndExportCsv() throws Exception {
        IBookRepository repo = mock(IBookRepository.class);
        when(repo.findByIsbn(anyString())).thenReturn(Optional.empty());
        when(repo.findAll()).thenReturn(new ArrayList<>());
        when(repo.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

        BookServiceImpl svc = new BookServiceImpl(repo);

        String csv = "isbn,title,author,stock\n" +
                "111,A Book,Someone,5\n" +
                "222,B Book,Other,3\n";
        int imported = svc.importFromCsv(new StringReader(csv));
        assertEquals(2, imported);

        // capture saves
        ArgumentCaptor<Book> saved = ArgumentCaptor.forClass(Book.class);
        verify(repo, times(2)).save(saved.capture());
        List<Book> list = saved.getAllValues();
        assertEquals("111", list.get(0).getIsbn());
        assertEquals("222", list.get(1).getIsbn());

        // export
        when(repo.findAll()).thenReturn(list);
        StringWriter out = new StringWriter();
        svc.exportToCsv(out);
        String result = out.toString();
        assertTrue(result.contains("isbn,title,author,stock"));
        assertTrue(result.contains("111"));
        assertTrue(result.contains("222"));
    }
}
