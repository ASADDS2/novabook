/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.domain;

import java.time.LocalDateTime;

/**
 * Represents a library member.
 * <p>
 * Members can borrow books from the library. 
 * Corresponds to the 'member' table in the database.
 * </p>
 * 
 * @author Coder
 * @version 1.0
 * @since 1.0
 * @see Loan
 * @see MemberRole
 */
public class Member {
    
    private Integer id;
    private String name;
    private boolean active;
    private boolean deleted;
    private MemberRole role;
    private AccessLevel accessLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    
    /**
     * Default constructor.
     */
    public Member() {
        this.active = true;
        this.deleted = false;
        this.role = MemberRole.REGULAR;
        this.accessLevel = AccessLevel.READ_WRITE;
    }
    
    /**
     * Creates a new member with basic information.
     * 
     * @param name member's full name
     */
    public Member(String name) {
        this();
        this.name = name;
    }
    
    // Business logic methods
    
    /**
     * Checks if member can borrow books.
     * 
     * @return true if active and not deleted
     */
    public boolean canBorrow() {
        return active && !deleted;
    }
    
    /**
     * Checks if member has premium status.
     * 
     * @return true if role is PREMIUM
     */
    public boolean isPremium() {
        return this.role == MemberRole.PREMIUM;
    }
    
    /**
     * Soft deletes the member by setting deleted flag.
     */
    public void softDelete() {
        this.deleted = true;
        this.active = false;
        markAsUpdated();
    }
    
    /**
     * Updates the updatedAt timestamp to current time.
     */
    public void markAsUpdated() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public boolean isDeleted() {
        return deleted;
    }
    
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    public MemberRole getRole() {
        return role;
    }
    
    public void setRole(MemberRole role) {
        this.role = role;
    }
    
    public AccessLevel getAccessLevel() {
        return accessLevel;
    }
    
    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role=" + role +
                ", active=" + active +
                ", deleted=" + deleted +
                ", canBorrow=" + canBorrow() +
                '}';
    }
}