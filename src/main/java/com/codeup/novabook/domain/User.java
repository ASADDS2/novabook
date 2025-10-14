/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.domain;

import java.time.LocalDateTime;

/**
 * Represents a system user (staff/admin).
 * <p>
 * Users have credentials and access levels to manage the library system.
 * Corresponds to the 'users' table in the database.
 * </p>
 * 
 * @author Coder
 * @version 1.0
 * @since 1.0
 * @see UserRole
 * @see AccessLevel
 */
public class User {
    
    private Integer id;
    private String name;
    private String email;
    private String password;        // Should be hashed in production
    private String phone;
    private UserRole role;
    private AccessLevel accessLevel;
    private boolean active;
    private boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    
    /**
     * Default constructor.
     * Initializes user as active and not deleted.
     */
    public User() {
        this.active = true;
        this.deleted = false;
        this.role = UserRole.USER;
        this.accessLevel = AccessLevel.READ_WRITE;
    }
    
    /**
     * Creates a new user with basic information.
     * 
     * @param name user's full name
     * @param email user's email address
     * @param password user password (should be hashed before storage)
     * @param phone user's phone number
     */
    public User(String name, String email, String password, String phone) {
        this();
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }
    
    // Business logic methods
    
    /**
     * Updates the updatedAt timestamp to current time.
     */
    public void markAsUpdated() {
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Checks if user can perform management operations.
     * 
     * @return true if access level is MANAGE
     */
    public boolean canManage() {
        return this.accessLevel == AccessLevel.MANAGE;
    }
    
    /**
     * Checks if user can modify data.
     * 
     * @return true if access level is READ_WRITE or MANAGE
     */
    public boolean canWrite() {
        return this.accessLevel == AccessLevel.READ_WRITE || 
               this.accessLevel == AccessLevel.MANAGE;
    }
    
    /**
     * Checks if user can only read data.
     * 
     * @return true if access level is READ_ONLY
     */
    public boolean isReadOnly() {
        return this.accessLevel == AccessLevel.READ_ONLY;
    }
    
    /**
     * Checks if user account is active and can login.
     * 
     * @return true if user is active and not deleted
     */
    public boolean canLogin() {
        return active && !deleted;
    }
    
    /**
     * Checks if user is an administrator.
     * 
     * @return true if role is ADMIN
     */
    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }
    
    /**
     * Soft deletes the user by setting deleted flag.
     */
    public void softDelete() {
        this.deleted = true;
        this.active = false;
        markAsUpdated();
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public AccessLevel getAccessLevel() {
        return accessLevel;
    }
    
    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
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
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                ", accessLevel=" + accessLevel +
                ", active=" + active +
                ", deleted=" + deleted +
                '}';
    }
}
