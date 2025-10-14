/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.domain;

import java.time.LocalDateTime;

/**
 * Domain entity representing a library member.
 * 
 * <p>Members are users who can borrow books from the library.
 * They can have different roles (REGULAR or PREMIUM) and access levels.</p>
 * 
 * @author Coder
 * @version 1.0
 */
public class Member {
    
    private Integer id;
    private String name;
    private Boolean active;
    private Boolean deleted;
    private MemberRole role;
    private AccessLevel accessLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Default constructor.
     */
    public Member() {
        this.active = true;
        this.deleted = false;
    }

    /**
     * Constructor with required fields.
     * 
     * @param name Member name
     * @param role Member role
     * @param accessLevel Access level
     */
    public Member(String name, MemberRole role, AccessLevel accessLevel) {
        this.name = name;
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

    /**
     * Checks if the member is currently active and not deleted.
     * 
     * @return true if active and not deleted
     */
    public boolean isActiveAndNotDeleted() {
        return active && !deleted;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", active=" + active +
                ", deleted=" + deleted +
                ", role=" + role +
                ", accessLevel=" + accessLevel +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}