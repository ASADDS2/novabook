/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.domain;

/**
 * Enumeration defining member roles in the NovaBook system.
 * <p>
 * Member roles determine the type of membership and associated benefits.
 * </p>
 * 
 * @author Coder
 * @version 1.0
 * @since 1.0
 * @see Member
 */
public enum MemberRole {
    /** Regular member with standard benefits */
    REGULAR("Regular", "Standard membership with basic loan privileges"),
    
    /** Premium member with enhanced benefits */
    PREMIUM("Premium", "Premium membership with extended loan periods and priority access");
    
    private final String displayName;
    private final String description;
    
    /**
     * Constructs a MemberRole with display information.
     * 
     * @param displayName human-readable role name
     * @param description detailed description of role benefits
     */
    MemberRole(String displayName, String description) {
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
