/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.domain;

/**
 * Enumeration defining user roles in the NovaBook system.
 * <p>
 * Roles represent the functional position of a user within the organization.
 * </p>
 * 
 * @author Coder
 * @version 1.0
 * @since 1.0
 * @see User
 * @see AccessLevel
 */
public enum UserRole {
    /** Regular system user */
    USER("User", "Standard user with basic permissions"),
    
    /** System administrator with full access */
    ADMIN("Administrator", "Full system access and configuration");
    
    private final String displayName;
    private final String description;
    
    /**
     * Constructs a UserRole with display information.
     * 
     * @param displayName human-readable role name
     * @param description detailed description of role responsibilities
     */
    UserRole(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    /**
     * Gets the human-readable display name.
     * 
     * @return display name for UI presentation
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Gets the detailed description of the role.
     * 
     * @return role description
     */
    public String getDescription() {
        return description;
    }
}