package com.codeup.novabook.service;

import com.codeup.novabook.domain.Loan;
import com.codeup.novabook.repository.ILoanRepository;
import com.codeup.novabook.util.csv.LoanCsv;

import java.io.FileWriter;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Export-related operations (CSV files) without changing project structure.
 */
public class ExportService {

    private final ILoanRepository loanRepository;

    public ExportService(ILoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public Path exportOverdueLoansCsv(Path path) throws Exception {
        List<Loan> all = loanRepository.findAll();
        List<Loan> overdue = all.stream()
                .filter(l -> l.getDateDue() != null && !Boolean.TRUE.equals(l.getReturned()) && LocalDate.now().isAfter(l.getDateDue()))
                .collect(Collectors.toList());
        try (FileWriter w = new FileWriter(path.toFile())) {
            LoanCsv.write(overdue, w);
        }
        return path;
    }
}
