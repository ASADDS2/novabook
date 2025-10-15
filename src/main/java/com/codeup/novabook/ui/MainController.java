package com.codeup.novabook.ui;

import com.codeup.novabook.domain.Book;
import com.codeup.novabook.domain.Loan;
import com.codeup.novabook.domain.Member;
import com.codeup.novabook.domain.MemberRole;
import com.codeup.novabook.domain.AccessLevel;
import com.codeup.novabook.infra.ServiceRegistry;
import com.codeup.novabook.service.IBookService;
import com.codeup.novabook.service.ILoanService;
import com.codeup.novabook.service.IMemberService;
import com.codeup.novabook.util.csv.LoanCsv;
import com.codeup.novabook.util.csv.MemberCsv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.nio.file.Path;

public class MainController {

    private final ServiceRegistry registry = ServiceRegistry.getInstance();
    private final IBookService bookService = registry.bookService();
    private final IMemberService memberService = registry.memberService();
    private final ILoanService loanService = registry.loanService();
    private final com.codeup.novabook.service.ExportService exportService = registry.exportService();

    // Books UI
    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, String> colIsbn;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, Number> colStock;
    @FXML private TextField bookSearchField;
    @FXML private TextField isbnField;
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField stockField;

    // Members UI
    @FXML private TableView<Member> membersTable;
    @FXML private TableColumn<Member, Number> colMemberId;
    @FXML private TableColumn<Member, String> colMemberName;
    @FXML private TableColumn<Member, String> colMemberRole;
    @FXML private TableColumn<Member, String> colMemberAccess;
    @FXML private TableColumn<Member, Boolean> colMemberActive;
    @FXML private TextField memberSearchField;
    @FXML private TextField memberNameField;
    @FXML private ChoiceBox<MemberRole> memberRoleChoice;
    @FXML private ChoiceBox<AccessLevel> memberAccessChoice;
    @FXML private CheckBox memberActiveCheck;

    // Loans UI
    @FXML private TableView<Loan> loansTable;
    @FXML private TableColumn<Loan, Number> colLoanId;
    @FXML private TableColumn<Loan, Number> colLoanMember;
    @FXML private TableColumn<Loan, Number> colLoanBook;
    @FXML private TableColumn<Loan, String> colLoanLoaned;
    @FXML private TableColumn<Loan, String> colLoanDue;
    @FXML private TableColumn<Loan, Boolean> colLoanReturned;
    @FXML private TextField memberIdField;
    @FXML private TextField bookIdField;
    @FXML private TextField dueDateField;

    @FXML
    public void initialize() {
        // Books table bindings
        colIsbn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getIsbn()));
        colTitle.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTitle()));
        colAuthor.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAuthor()));
        colStock.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getStock()));

        // Members table bindings
        if (colMemberId != null) {
            colMemberId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()));
            colMemberName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));
            colMemberRole.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getRole() == null ? "" : c.getValue().getRole().name()));
            colMemberAccess.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAccessLevel() == null ? "" : c.getValue().getAccessLevel().name()));
            colMemberActive.setCellValueFactory(c -> new javafx.beans.property.SimpleBooleanProperty(Boolean.TRUE.equals(c.getValue().getActive())));
            if (memberRoleChoice != null) memberRoleChoice.getItems().addAll(MemberRole.values());
            if (memberAccessChoice != null) memberAccessChoice.getItems().addAll(AccessLevel.values());
        }

        // Loans table bindings
        if (colLoanId != null) {
            colLoanId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()));
            colLoanMember.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getMemberId()));
            colLoanBook.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getBookId()));
            colLoanLoaned.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDateLoaned() == null ? "" : c.getValue().getDateLoaned().toString()));
            colLoanDue.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDateDue() == null ? "" : c.getValue().getDateDue().toString()));
            colLoanReturned.setCellValueFactory(c -> new javafx.beans.property.SimpleBooleanProperty(Boolean.TRUE.equals(c.getValue().getReturned())));
        }

        refreshBooks();
        refreshMembers();
        refreshLoans();
    }

    private void refreshBooks() {
        try {
            ObservableList<Book> data = FXCollections.observableArrayList(bookService.findAll());
            booksTable.setItems(data);
        } catch (Exception e) {
            showError(e);
        }
    }

    private void refreshLoans() {
        try {
            ObservableList<Loan> data = FXCollections.observableArrayList(loanService.findAll());
            loansTable.setItems(data);
        } catch (Exception e) {
            showError(e);
        }
    }

    private void refreshMembers() {
        try {
            ObservableList<Member> data = FXCollections.observableArrayList(memberService.findAll());
            membersTable.setItems(data);
        } catch (Exception e) { showError(e); }
    }

    @FXML
    public void onSearchBooks(ActionEvent e) {
        com.codeup.novabook.infra.HttpLogger.log("GET /books?query=" + bookSearchField.getText());
        String q = bookSearchField.getText();
        try {
            if (q == null || q.isBlank()) {
                refreshBooks();
            } else {
                // search by title first, could combine
                booksTable.setItems(FXCollections.observableArrayList(bookService.findByTitle(q)));
            }
        } catch (Exception ex) { showError(ex); }
    }

    @FXML
    public void onAddBook(ActionEvent e) {
        try {
            Book b = new Book(isbnField.getText(), titleField.getText(), authorField.getText(), Integer.parseInt(stockField.getText()));
            com.codeup.novabook.infra.HttpLogger.log("POST /books");
            bookService.create(b);
            refreshBooks();
        } catch (Exception ex) { showError(ex); }
    }

    @FXML
    public void onUpdateBook(ActionEvent e) {
        Book selected = booksTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        try {
            selected.setIsbn(isbnField.getText());
            selected.setTitle(titleField.getText());
            selected.setAuthor(authorField.getText());
            selected.setStock(Integer.parseInt(stockField.getText()));
            com.codeup.novabook.infra.HttpLogger.log("PATCH /books/" + selected.getId());
            bookService.update(selected);
            refreshBooks();
        } catch (Exception ex) { showError(ex); }
    }

    @FXML
    public void onDeleteBook(ActionEvent e) {
        Book selected = booksTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        try {
            com.codeup.novabook.infra.HttpLogger.log("DELETE /books/" + selected.getId());
            bookService.delete(selected.getId());
            refreshBooks();
        } catch (Exception ex) { showError(ex); }
    }

    @FXML
    public void onImportBooksCsv(ActionEvent e) {
        try {
            // expects a file at src/main/sources/books.csv to respect your structure
            var path = Paths.get("src/main/sources/books.csv");
            try (FileReader r = new FileReader(path.toFile())) {
                int n = bookService.importFromCsv(r);
                showInfo("Imported " + n + " books");
            }
            refreshBooks();
        } catch (Exception ex) { showError(ex); }
    }

    @FXML
    public void onExportBooksCsv(ActionEvent e) {
        try {
            Path path = resolveExportPath("books_export.csv");
            try (FileWriter w = new FileWriter(path.toFile())) {
                bookService.exportToCsv(w);
            }
            showInfo("Exported books to " + path);
        } catch (Exception ex) { showError(ex); }
    }

    // Member CRUD
    @FXML
    public void onAddMember(ActionEvent e) {
        try {
            Member m = new Member();
            m.setName(memberNameField.getText());
            m.setRole(memberRoleChoice.getValue());
            m.setAccessLevel(memberAccessChoice.getValue());
            m.setActive(memberActiveCheck.isSelected());
            m.setDeleted(false);
            memberService.create(m);
            refreshMembers();
        } catch (Exception ex) { showError(ex); }
    }

    @FXML
    public void onUpdateMember(ActionEvent e) {
        Member selected = membersTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        try {
            selected.setName(memberNameField.getText());
            selected.setRole(memberRoleChoice.getValue());
            selected.setAccessLevel(memberAccessChoice.getValue());
            selected.setActive(memberActiveCheck.isSelected());
            memberService.update(selected);
            refreshMembers();
        } catch (Exception ex) { showError(ex); }
    }

    @FXML
    public void onDeleteMember(ActionEvent e) {
        Member selected = membersTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        try {
            memberService.softDelete(selected.getId());
            refreshMembers();
        } catch (Exception ex) { showError(ex); }
    }

    // Search/Import/Export Members
    @FXML
    public void onSearchMembers(ActionEvent e) {
        try {
            String q = memberSearchField.getText();
            if (q == null || q.isBlank()) {
                refreshMembers();
            } else {
                membersTable.setItems(FXCollections.observableArrayList(memberService.findByName(q)));
            }
        } catch (Exception ex) { showError(ex); }
    }

    @FXML
    public void onImportMembersCsv(ActionEvent e) {
        try {
            var path = Paths.get("src/main/sources/members.csv");
            try (FileReader r = new FileReader(path.toFile())) {
                int n = memberService.importFromCsv(r);
                showInfo("Imported " + n + " members");
            }
            refreshMembers();
        } catch (Exception ex) { showError(ex); }
    }

    @FXML
    public void onExportMembersCsv(ActionEvent e) {
        try {
            Path path = resolveExportPath("members_export.csv");
            try (FileWriter w = new FileWriter(path.toFile())) {
                memberService.exportToCsv(w);
            }
            showInfo("Exported members to " + path);
        } catch (Exception ex) { showError(ex); }
    }

    @FXML
    public void onBorrowBook(ActionEvent e) {
        com.codeup.novabook.infra.HttpLogger.log("POST /loans");
        try {
            Integer memberId = Integer.parseInt(memberIdField.getText());
            Integer bookId = Integer.parseInt(bookIdField.getText());
            LocalDate due = LocalDate.parse(dueDateField.getText());
            loanService.borrowBook(memberId, bookId, due);
            refreshLoans();
            refreshBooks();
        } catch (Exception ex) { showError(ex); }
    }

    @FXML
    public void onUpdateLoanDueDate(ActionEvent e) {
        Loan selected = loansTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        try {
            LocalDate due = LocalDate.parse(dueDateField.getText());
            selected.setDateDue(due);
            loanService.update(selected);
            refreshLoans();
        } catch (Exception ex) { showError(ex); }
    }

    @FXML
    public void onDeleteLoan(ActionEvent e) {
        Loan selected = loansTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        try {
            loanService.delete(selected.getId());
            refreshLoans();
        } catch (Exception ex) { showError(ex); }
    }

    @FXML
    public void onReturnLoan(ActionEvent e) {
        com.codeup.novabook.infra.HttpLogger.log("PATCH /loans/return");
        Loan selected = loansTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        try {
            loanService.returnBook(selected.getId());
            refreshLoans();
            refreshBooks();
        } catch (Exception ex) { showError(ex); }
    }

    @FXML
    public void onExportOverdueLoans(ActionEvent e) {
        try {
            Path out = resolveExportPath("prestamos_vencidos.csv");
            exportService.exportOverdueLoansCsv(out);
            showInfo("Exported overdue loans to " + out);
        } catch (Exception ex) { showError(ex); }
    }

    @FXML
    public void onExportAllLoansCsv(ActionEvent e) {
        try {
            Path out = resolveExportPath("loans_export.csv");
            try (FileWriter w = new FileWriter(out.toFile())) {
                LoanCsv.write(loanService.findAll(), w);
            }
            showInfo("Exported loans to " + out);
        } catch (Exception ex) { showError(ex); }
    }

    private void showError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
        alert.showAndWait();
    }
    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }

    // Resolve a safe, user-writable export directory under the user's home.
    private Path resolveExportPath(String fileName) {
        try {
            Path dir = Path.of(System.getProperty("user.home"), "Novabook", "exports");
            java.nio.file.Files.createDirectories(dir);
            return dir.resolve(fileName);
        } catch (Exception e) {
            // Fallback: current working directory
            return Path.of(fileName);
        }
    }
}
