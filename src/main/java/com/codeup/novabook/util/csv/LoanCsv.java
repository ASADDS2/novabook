package com.codeup.novabook.util.csv;

import com.codeup.novabook.domain.Loan;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.Reader;
import java.io.Writer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class LoanCsv {
    private LoanCsv() {}

    public static List<Loan> read(Reader reader) throws Exception {
        try (CSVReader csv = new CSVReader(reader)) {
            List<Loan> out = new ArrayList<>();
            String[] row;
            boolean headerSkipped = false;
            while ((row = csv.readNext()) != null) {
                if (!headerSkipped) { headerSkipped = true; continue; }
                if (row.length < 4) continue;
                Integer memberId = Integer.parseInt(row[0]);
                Integer bookId = Integer.parseInt(row[1]);
                LocalDate dateLoaned = LocalDate.parse(row[2]);
                LocalDate dateDue = LocalDate.parse(row[3]);
                Loan loan = new Loan(memberId, bookId, dateLoaned, dateDue);
                out.add(loan);
            }
            return out;
        }
    }

    public static void write(List<Loan> loans, Writer writer) throws Exception {
        try (CSVWriter csv = new CSVWriter(writer)) {
            csv.writeNext(new String[]{"memberId","bookId","dateLoaned","dateDue","returned"});
            for (Loan l : loans) {
                csv.writeNext(new String[]{
                        String.valueOf(l.getMemberId()),
                        String.valueOf(l.getBookId()),
                        l.getDateLoaned() == null ? "" : l.getDateLoaned().toString(),
                        l.getDateDue() == null ? "" : l.getDateDue().toString(),
                        String.valueOf(l.getReturned())
                });
            }
            csv.flush();
        }
    }
}
