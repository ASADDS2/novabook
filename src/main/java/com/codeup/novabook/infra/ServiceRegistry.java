package com.codeup.novabook.infra;

import com.codeup.novabook.connection.ConnectionFactory;
import com.codeup.novabook.infra.config.AppConfig;
import com.codeup.novabook.jdbc.JdbcTemplateLight;
import com.codeup.novabook.repository.IBookRepository;
import com.codeup.novabook.repository.ILoanRepository;
import com.codeup.novabook.repository.IMemberRepository;
import com.codeup.novabook.repository.IUserRepository;
import com.codeup.novabook.repository.impl.BookjdbcRepository;
import com.codeup.novabook.repository.impl.LoanjdbcRepository;
import com.codeup.novabook.repository.impl.MemberjdbcRepository;
import com.codeup.novabook.repository.impl.UserjdbcRepository;
import com.codeup.novabook.service.IBookService;
import com.codeup.novabook.service.ILoanService;
import com.codeup.novabook.service.IMemberService;
import com.codeup.novabook.service.IUserService;
import com.codeup.novabook.service.ExportService;
import com.codeup.novabook.service.FineCalculator;
import com.codeup.novabook.service.impl.BookServiceImpl;
import com.codeup.novabook.service.impl.LoanServiceImpl;
import com.codeup.novabook.service.impl.MemberServiceImpl;
import com.codeup.novabook.service.impl.UserServiceImpl;
import com.codeup.novabook.service.impl.DefaultingUserService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ServiceRegistry {
    private static ServiceRegistry INSTANCE;

    private final AppConfig config;
    private final JdbcTemplateLight jdbc;

    private final IBookRepository bookRepo;
    private final IMemberRepository memberRepo;
    private final ILoanRepository loanRepo;
    private final IUserRepository userRepo;

    private final IBookService bookService;
    private final IMemberService memberService;
    private final ILoanService loanService;
    private final IUserService userService;
    private final ExportService exportService;

    private ServiceRegistry() {
        this.config = new AppConfig();
        ConnectionFactory factory = new ConnectionFactory(config);
        this.jdbc = new JdbcTemplateLight(factory);

        this.bookRepo = new BookjdbcRepository(jdbc);
        this.memberRepo = new MemberjdbcRepository(jdbc);
        this.loanRepo = new LoanjdbcRepository(jdbc);
        this.userRepo = new UserjdbcRepository(jdbc);

        // Configure logging
        LogConfig.configure();

        // Preflight DB connectivity (non-fatal)
        try {
            var c = factory.open();
            try { c.close(); } catch (Exception ignore) {}
            Logger.getLogger(ServiceRegistry.class.getName()).info("Database connection preflight: OK");
        } catch (Exception ex) {
            Logger.getLogger(ServiceRegistry.class.getName()).log(Level.WARNING, "Database connection preflight failed: " + ex.getMessage(), ex);
        }

        // Config-based services
        ConfigService conf = new ConfigService();
        int diasPrestamo = conf.getInt("diasPrestamo", 7);
        long multaPorDia = conf.getLong("multaPorDia", 1500);
        FineCalculator fineCalculator = new FineCalculator(diasPrestamo, multaPorDia);

        this.bookService = new BookServiceImpl(bookRepo);
        this.memberService = new MemberServiceImpl(memberRepo);
        this.loanService = new LoanServiceImpl(loanRepo, bookRepo, memberRepo, jdbc, fineCalculator);
        this.userService = new DefaultingUserService(new UserServiceImpl(userRepo));
        this.exportService = new ExportService(loanRepo);

        // Seed default admin (non-fatal if DB not ready)
        try { seedDefaultAdmin(); } catch (Exception ex) {
            Logger.getLogger(ServiceRegistry.class.getName()).log(Level.WARNING, "Default admin seed failed: " + ex.getMessage());
        }
    }

    private void seedDefaultAdmin() {
        try {
            List<com.codeup.novabook.domain.User> admins = userService.findByRole(com.codeup.novabook.domain.UserRole.ADMIN);
            boolean hasAdmin = admins != null && !admins.isEmpty();
            if (!hasAdmin) {
                com.codeup.novabook.domain.User u = new com.codeup.novabook.domain.User();
                u.setName("Administrador");
                u.setEmail("admin@novabook.local");
                u.setPassword("Admin123!");
                u.setPhone("");
                u.setRole(com.codeup.novabook.domain.UserRole.ADMIN);
                u.setAccessLevel(com.codeup.novabook.domain.AccessLevel.MANAGE);
                userService.create(u);
                Logger.getLogger(ServiceRegistry.class.getName()).info("Default admin created: admin@novabook.local");
            }
        } catch (Exception ex) {
            // swallow to avoid crashing UI
            Logger.getLogger(ServiceRegistry.class.getName()).log(Level.WARNING, "Could not seed admin: " + ex.getMessage());
        }
    }

    public static synchronized ServiceRegistry getInstance() {
        if (INSTANCE == null) INSTANCE = new ServiceRegistry();
        return INSTANCE;
    }

    public IBookService bookService() { return bookService; }
    public IMemberService memberService() { return memberService; }
    public ILoanService loanService() { return loanService; }
    public IUserService userService() { return userService; }
    public ExportService exportService() { return exportService; }
}
