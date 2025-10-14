/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/package-info.java to edit this template
 */
/**
 * Repository layer interfaces for NovaBook application.
 * <p>
 * This package contains all repository interfaces that define the contract
 * for data access operations. These interfaces follow the Repository pattern
 * and provide abstraction over the actual data persistence mechanism.
 * </p>
 * 
 * <h2>Key Interfaces:</h2>
 * <ul>
 *   <li>{@link com.codeup.novabook.repository.IBookRepository} - Book data access operations</li>
 *   <li>{@link com.codeup.novabook.repository.ILoanRepository} - Loan data access operations</li>
 *   <li>{@link com.codeup.novabook.repository.IMemberRepository} - Member data access operations</li>
 *   <li>{@link com.codeup.novabook.repository.IUserRepository} - User data access operations</li>
 * </ul>
 * 
 * <h2>Design Pattern:</h2>
 * <p>
 * All repositories follow the Repository pattern, providing a collection-like
 * interface for accessing domain objects. This abstraction allows the business
 * logic layer to work with domain objects without knowing the details of how
 * data is persisted.
 * </p>
 * 
 * <h2>Exception Handling:</h2>
 * <p>
 * All repository methods may throw {@link com.codeup.novabook.exceptions.DatabaseException}
 * to indicate database-related errors during operations.
 * </p>
 * 
 * <h2>Implementation:</h2>
 * <p>
 * Concrete implementations of these interfaces can be found in the
 * {@code com.codeup.novabook.repository.jdbc} package, which provides
 * JDBC-based implementations for MySQL database.
 * </p>
 * 
 * @since 1.0
 * @version 1.0
 * @author NovaBook Team
 */
package com.codeup.novabook.repository;