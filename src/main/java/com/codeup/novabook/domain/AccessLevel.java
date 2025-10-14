/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.domain;

/**
 * Enumeration defining granular access levels for system operations.
 * <p>
 * This enum provides fine-grained control over what operations users can perform
 * within the NovaBook system, complementing the {@link UserRole} enum.
 * </p>
 * <p>Example usage:</p>
 * <pre>{@code
 * User user = new User("john_doe", "password123", "John", "Doe");
 * user.setAccessLevel(AccessLevel.READ_WRITE);
 * 
 * if (user.getAccessLevel() == AccessLevel.MANAGE) {
 *     // Allow management operations
 * }
 * }</pre>
 * 
 * @author Coder
 * @version 1.0
 * @since 1.0
 * @see User
 * @see UserRole
 */
public enum AccessLevel {
    /** Can only view data, no modifications allowed */
    READ_ONLY,
    
    /** Can view and modify own data */
    READ_WRITE,
    
    /** Full management capabilities including other users' data */
    MANAGE
}