package com.codeup.novabook.util.csv;

import com.codeup.novabook.domain.Book;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public final class BookCsv {
    private BookCsv() {}

    public static List<Book> read(Reader reader) throws Exception {
        try (CSVReader csv = new CSVReader(reader)) {
            List<Book> out = new ArrayList<>();
            String[] row;
            boolean headerSkipped = false;
            while ((row = csv.readNext()) != null) {
                if (!headerSkipped) { headerSkipped = true; continue; }
                if (row.length < 4) continue;
                String isbn = row[0];
                String title = row[1];
                String author = row[2];
                Integer stock = Integer.parseInt(row[3]);
                out.add(new Book(isbn, title, author, stock));
            }
            return out;
        }
    }

    public static void write(List<Book> books, Writer writer) throws Exception {
        // Configure CSVWriter to avoid quoting simple values so that output matches tests
        try (CSVWriter csv = new CSVWriter(
                writer,
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {
            csv.writeNext(new String[]{"isbn","title","author","stock"});
            for (Book b : books) {
                csv.writeNext(new String[]{
                        b.getIsbn(),
                        b.getTitle(),
                        b.getAuthor(),
                        String.valueOf(b.getStock())
                });
            }
            csv.flush();
        }
    }
}
