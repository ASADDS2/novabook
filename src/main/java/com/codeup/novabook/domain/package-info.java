/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/package-info.java to edit this template
 */
/**
 * Domain layer containing the core business entities of the NovaBook library system.
 * <p>
 * This package contains Plain Old Java Objects (POJOs) that represent the fundamental
 * concepts and business logic of the library management system. These entities map
 * directly to database tables and encapsulate business rules and behaviors.
 * </p>
 * 
 * <h2>Core Entities</h2>
 * <ul>
 * <li>{@link com.codeup.novabook.domain.User} - System users (staff and administrators)</li>
 * <li>{@link com.codeup.novabook.domain.Member} - Library members who can borrow books</li>
 * <li>{@link com.codeup.novabook.domain.Book} - Books available in the library catalog</li>
 * <li>{@link com.codeup.novabook.domain.Loan} - Book loan transactions between members and books</li>
 * </ul>
 * 
 * <h2>Enumerations</h2>
 * <ul>
 * <li>{@link com.codeup.novabook.domain.UserRole} - Roles for system users (USER, ADMIN)</li>
 * <li>{@link com.codeup.novabook.domain.MemberRole} - Membership types (REGULAR, PREMIUM)</li>
 * <li>{@link com.codeup.novabook.domain.AccessLevel} - Access control levels (READ_ONLY, READ_WRITE, MANAGE)</li>
 * </ul>
 * 
 * <h2>Design Principles</h2>
 * <p>
 * Domain entities follow these principles:
 * </p>
 * <ul>
 * <li><b>Encapsulation:</b> Business logic is contained within the entities</li>
 * <li><b>Immutability awareness:</b> Enums are used for fixed value sets</li>
 * <li><b>Validation:</b> Entities validate their state through business methods</li>
 * <li><b>Rich domain model:</b> Entities are not just data containers but include behavior</li>
 * <li><b>Framework independence:</b> No dependencies on infrastructure or presentation layers</li>
 * </ul>
 * 
 * <h2>Database Mapping</h2>
 * <p>
 * Entities in this package correspond to the following database tables:
 * </p>
 * <ul>
 * <li>{@link com.codeup.novabook.domain.User} ↔ {@code users}</li>
 * <li>{@link com.codeup.novabook.domain.Member} ↔ {@code member}</li>
 * <li>{@link com.codeup.novabook.domain.Book} ↔ {@code book}</li>
 * <li>{@link com.codeup.novabook.domain.Loan} ↔ {@code loan}</li>
 * </ul>
 * 
 * <h2>Usage Example</h2>
 * <pre>{@code
 * // Create a new user
 * User user = new User("John Doe", "john@example.com", "hashedPassword", "1234567890");
 * user.setRole(UserRole.ADMIN);
 * user.setAccessLevel(AccessLevel.MANAGE);
 * 
 * // Check permissions
 * if (user.canManage()) {
 *     // Perform administrative operations
 * }
 * 
 * // Create a book
 * Book book = new Book("978-0134685991", "Effective Java", "Joshua Bloch", 5);
 * 
 * // Check availability
 * if (book.isAvailable()) {
 *     book.decreaseStock(); // Loan the book
 * }
 * 
 * // Create a loan
 * Loan loan = new Loan(memberId, bookId, LocalDate.now().plusDays(14));
 * 
 * // Check if overdue
 * if (loan.isOverdue()) {
 *     long daysOverdue = loan.getDaysOverdue();
 *     // Handle overdue logic
 * }
 * }</pre>
 * 
 * <h2>Relationship Overview</h2>
 * <pre>
 * User                    Member
 *   |                       |
 *   | manages               | borrows
 *   |                       |
 *   └──────> System <───────┴──────> Loan ──────> Book
 * </pre>
 * 
 * @author Coder
 * @version 1.0
 * @since 1.0
 * @see com.codeup.novabook.repository
 * @see com.codeup.novabook.service
 */
package com.codeup.novabook.domain;