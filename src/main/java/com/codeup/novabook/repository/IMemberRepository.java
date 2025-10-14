/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.codeup.novabook.repository;

import com.codeup.novabook.domain.Member;
import com.codeup.novabook.domain.MemberRole;
import com.codeup.novabook.exception.DatabaseException;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Member entity operations.
 * Defines CRUD operations and custom queries for Member management.
 * 
 * @author Coder
 * @version 1.0
 */
public interface IMemberRepository {
    
    /**
     * Saves a new member to the database.
     * 
     * @param member Member entity to save
     * @return The saved member with generated ID
     * @throws DatabaseException if a database error occurs
     */
    Member save(Member member) throws DatabaseException;
    
    /**
     * Updates an existing member in the database.
     * 
     * @param member Member entity with updated data
     * @return The updated member
     * @throws DatabaseException if a database error occurs
     */
    Member update(Member member) throws DatabaseException;
    
    /**
     * Soft deletes a member by its ID.
     * 
     * @param id Member ID to delete
     * @return true if deletion was successful, false otherwise
     * @throws DatabaseException if a database error occurs
     */
    boolean softDelete(Integer id) throws DatabaseException;
    
    /**
     * Permanently deletes a member by its ID.
     * 
     * @param id Member ID to delete
     * @return true if deletion was successful, false otherwise
     * @throws DatabaseException if a database error occurs
     */
    boolean hardDelete(Integer id) throws DatabaseException;
    
    /**
     * Finds a member by its ID.
     * 
     * @param id Member ID to search
     * @return Optional containing the member if found, empty otherwise
     * @throws DatabaseException if a database error occurs
     */
    Optional<Member> findById(Integer id) throws DatabaseException;
    
    /**
     * Retrieves all members from the database (including deleted).
     * 
     * @return List of all members
     * @throws DatabaseException if a database error occurs
     */
    List<Member> findAll() throws DatabaseException;
    
    /**
     * Retrieves all active members (not deleted).
     * 
     * @return List of active members
     * @throws DatabaseException if a database error occurs
     */
    List<Member> findAllActive() throws DatabaseException;
    
    /**
     * Searches members by name (partial match, case-insensitive).
     * 
     * @param name Name or part of name to search
     * @return List of members matching the criteria
     * @throws DatabaseException if a database error occurs
     */
    List<Member> findByName(String name) throws DatabaseException;
    
    /**
     * Finds members by role.
     * 
     * @param role Member role (REGULAR or PREMIUM)
     * @return List of members with the specified role
     * @throws DatabaseException if a database error occurs
     */
    List<Member> findByRole(MemberRole role) throws DatabaseException;
    
    /**
     * Finds active members by role.
     * 
     * @param role Member role (REGULAR or PREMIUM)
     * @return List of active members with the specified role
     * @throws DatabaseException if a database error occurs
     */
    List<Member> findActiveByRole(MemberRole role) throws DatabaseException;
    
    /**
     * Changes the active status of a member.
     * 
     * @param memberId Member ID to update
     * @param active New active status
     * @return true if update was successful, false otherwise
     * @throws DatabaseException if a database error occurs
     */
    boolean updateActiveStatus(Integer memberId, Boolean active) throws DatabaseException;
    
    /**
     * Checks if a member exists by ID.
     * 
     * @param id Member ID to check
     * @return true if exists, false otherwise
     * @throws DatabaseException if a database error occurs
     */
    boolean existsById(Integer id) throws DatabaseException;
}