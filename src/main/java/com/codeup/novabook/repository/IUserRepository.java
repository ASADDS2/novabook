/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.codeup.novabook.repository;

import com.codeup.novabook.domain.User;
import com.codeup.novabook.domain.UserRole;
import com.codeup.novabook.exception.DatabaseException;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity operations.
 * Defines CRUD operations and custom queries for User management.
 * 
 * @author Coder
 * @version 1.0
 */
public interface IUserRepository {
    
    /**
     * Saves a new user to the database.
     * 
     * @param user User entity to save
     * @return The saved user with generated ID
     * @throws DatabaseException if a database error occurs
     */
    User save(User user) throws DatabaseException;
    
    /**
     * Updates an existing user in the database.
     * 
     * @param user User entity with updated data
     * @return The updated user
     * @throws DatabaseException if a database error occurs
     */
    User update(User user) throws DatabaseException;
    
    /**
     * Soft deletes a user by its ID.
     * 
     * @param id User ID to delete
     * @return true if deletion was successful, false otherwise
     * @throws DatabaseException if a database error occurs
     */
    boolean softDelete(Integer id) throws DatabaseException;
    
    /**
     * Permanently deletes a user by its ID.
     * 
     * @param id User ID to delete
     * @return true if deletion was successful, false otherwise
     * @throws DatabaseException if a database error occurs
     */
    boolean hardDelete(Integer id) throws DatabaseException;
    
    /**
     * Finds a user by its ID.
     * 
     * @param id User ID to search
     * @return Optional containing the user if found, empty otherwise
     * @throws DatabaseException if a database error occurs
     */
    Optional<User> findById(Integer id) throws DatabaseException;
    
    /**
     * Finds a user by email address.
     * 
     * @param email Email address to search
     * @return Optional containing the user if found, empty otherwise
     * @throws DatabaseException if a database error occurs
     */
    Optional<User> findByEmail(String email) throws DatabaseException;
    
    /**
     * Retrieves all users from the database (including deleted).
     * 
     * @return List of all users
     * @throws DatabaseException if a database error occurs
     */
    List<User> findAll() throws DatabaseException;
    
    /**
     * Retrieves all active users (not deleted).
     * 
     * @return List of active users
     * @throws DatabaseException if a database error occurs
     */
    List<User> findAllActive() throws DatabaseException;
    
    /**
     * Searches users by name (partial match, case-insensitive).
     * 
     * @param name Name or part of name to search
     * @return List of users matching the criteria
     * @throws DatabaseException if a database error occurs
     */
    List<User> findByName(String name) throws DatabaseException;
    
    /**
     * Finds users by role.
     * 
     * @param role User role (USER or ADMIN)
     * @return List of users with the specified role
     * @throws DatabaseException if a database error occurs
     */
    List<User> findByRole(UserRole role) throws DatabaseException;
    
    /**
     * Validates user credentials for authentication.
     * 
     * @param email User email
     * @param password User password (plain text, will be hashed for comparison)
     * @return Optional containing the user if credentials are valid, empty otherwise
     * @throws DatabaseException if a database error occurs
     */
    Optional<User> authenticate(String email, String password) throws DatabaseException;
    
    /**
     * Changes the active status of a user.
     * 
     * @param userId User ID to update
     * @param active New active status
     * @return true if update was successful, false otherwise
     * @throws DatabaseException if a database error occurs
     */
    boolean updateActiveStatus(Integer userId, Boolean active) throws DatabaseException;
    
    /**
     * Updates the password of a user.
     * 
     * @param userId User ID to update
     * @param newPassword New password (will be hashed)
     * @return true if update was successful, false otherwise
     * @throws DatabaseException if a database error occurs
     */
    boolean updatePassword(Integer userId, String newPassword) throws DatabaseException;
    
    /**
     * Checks if an email is already registered.
     * 
     * @param email Email to check
     * @return true if email exists, false otherwise
     * @throws DatabaseException if a database error occurs
     */
    boolean existsByEmail(String email) throws DatabaseException;
}