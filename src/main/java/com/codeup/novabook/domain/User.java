/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.domain;

import java.time.LocalDateTime;

/**
 * Domain entity representing a system user.
 * 
 * <p>Users are administrators or staff members who manage the library system.
 * They have roles (USER or ADMIN) and access levels for different operations.</p>
 * 
 * @author Coder
 * @version 1.0
 */
public class User {
    
    private Integer id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private UserRole role;
    private AccessLevel accessLevel;
    private Boolean active;
    private Boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Default constructor.
     */
    public User() {
        this.active = true;
        this.deleted = false;
    }

    /**
     * Constructor with required fields.
     * 
     * @param name User name
     * @param email User email
     * @param password User password
     * @param phone User phone
     * @param role User role
     * @param accessLevel Access level
     */
    public User(String name, String email, String password, String phone, 
                UserRole role, AccessLevel accessLevel) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.accessLevel = accessLevel;
        this.active = true;
        this.deleted = false;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
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

    /**
     * Checks if the user is currently active and not deleted.
     * 
     * @return true if active and not deleted
     */
    public boolean isActiveAndNotDeleted() {
        return active && !deleted;
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
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}