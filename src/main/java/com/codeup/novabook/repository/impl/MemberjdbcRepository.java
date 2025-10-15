/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.repository.impl;

import com.codeup.novabook.domain.AccessLevel;
import com.codeup.novabook.domain.Member;
import com.codeup.novabook.domain.MemberRole;
import com.codeup.novabook.exception.DatabaseException;
import com.codeup.novabook.jdbc.JdbcTemplateLight;
import com.codeup.novabook.jdbc.RowMapper;
import com.codeup.novabook.repository.IMemberRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JDBC implementation of the Member repository interface.
 * 
 * <p>This repository provides data access operations for {@link Member} entities using
 * JDBC through the {@link JdbcTemplateLight} abstraction. It handles member management
 * including registration, updates, and soft delete functionality.</p>
 * 
 * <p><strong>Key Features:</strong></p>
 * <ul>
 *   <li><strong>Soft Delete</strong> - Members are marked as deleted but not removed from database</li>
 *   <li><strong>Role Management</strong> - Supports REGULAR and PREMIUM member types</li>
 *   <li><strong>Access Control</strong> - Tracks member access levels</li>
 * </ul>
 * 
 * @author Coder
 * @version 1.0
 * @since 1.0
 */
public class MemberjdbcRepository implements IMemberRepository {
    
    private final JdbcTemplateLight jdbc;
    private static final Logger logger = Logger.getLogger(MemberjdbcRepository.class.getName());

    /**
     * Constructs a new MemberjdbcRepository with the specified JDBC template.
     * 
     * @param jdbc the JDBC template for database operations
     * @throws NullPointerException if jdbc is null
     */
    public MemberjdbcRepository(JdbcTemplateLight jdbc) { 
        this.jdbc = jdbc; 
    }

    // Row MAPPER
    private static final RowMapper<Member> MEMBER_MAPPER = rs -> {
        Member member = new Member(
            rs.getString("name"),
            MemberRole.valueOf(rs.getString("role")),
            AccessLevel.valueOf(rs.getString("access_level"))
        );
        member.setId(rs.getInt("id"));
        member.setActive(rs.getBoolean("active"));
        member.setDeleted(rs.getBoolean("deleted"));
        member.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        member.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return member;
    };

    @Override
    public Member save(Member member) throws DatabaseException {
        String sql = "INSERT INTO member (name, active, deleted, role, access_level) VALUES (?, ?, ?, ?, ?)";
        try {
            int rows = jdbc.update(sql, ps -> {
                try {
                    ps.setString(1, member.getName());
                    ps.setBoolean(2, member.getActive());
                    ps.setBoolean(3, member.getDeleted());
                    ps.setString(4, member.getRole().name());
                    ps.setString(5, member.getAccessLevel().name());
                } catch (SQLException e) {
                    throw new RuntimeException("Error creating member", e);
                }
            });
            if (rows != 1) {
                throw new DatabaseException("Failed to create member");
            }

            return findByName(member.getName()).stream()
                    .findFirst()
                    .orElseThrow(() -> new DatabaseException("Failed to retrieve created member"));
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error creating member", e);
            throw e;
        }
    }

    @Override
    public Member update(Member member) throws DatabaseException {
        String sql = "UPDATE member SET name=?, active=?, deleted=?, role=?, access_level=? WHERE id=?";
        try {
            int rows = jdbc.update(sql, ps -> {
                try {
                    ps.setString(1, member.getName());
                    ps.setBoolean(2, member.getActive());
                    ps.setBoolean(3, member.getDeleted());
                    ps.setString(4, member.getRole().name());
                    ps.setString(5, member.getAccessLevel().name());
                    ps.setInt(6, member.getId());
                } catch (SQLException e) {
                    throw new RuntimeException("Error updating member", e);
                }
            });
            if (rows != 1) {
                throw new DatabaseException("Failed to update member with id: " + member.getId());
            }
            return member;
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error updating member", e);
            throw e;
        }
    }

    @Override
    public boolean softDelete(Integer id) throws DatabaseException {
        String sql = "UPDATE member SET deleted = TRUE WHERE id = ?";
        try {
            int rows = jdbc.update(sql, ps -> {
                try {
                    ps.setInt(1, id);
                } catch (SQLException e) {
                    throw new RuntimeException("Error soft deleting member", e);
                }
            });
            return rows > 0;
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error soft deleting member", e);
            throw e;
        }
    }

    @Override
    public boolean hardDelete(Integer id) throws DatabaseException {
        String sql = "DELETE FROM member WHERE id = ?";
        try {
            int rows = jdbc.update(sql, ps -> {
                try {
                    ps.setInt(1, id);
                } catch (SQLException e) {
                    throw new RuntimeException("Error deleting member", e);
                }
            });
            return rows > 0;
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error deleting member", e);
            throw e;
        }
    }

    @Override
    public Optional<Member> findById(Integer id) throws DatabaseException {
        String sql = "SELECT * FROM member WHERE id = ?";
        try {
            List<Member> members = jdbc.query(sql, ps -> {
                try {
                    ps.setInt(1, id);
                } catch (SQLException e) {
                    throw new RuntimeException("Error setting parameters", e);
                }
            }, MEMBER_MAPPER);
            return members.isEmpty() ? Optional.empty() : Optional.of(members.get(0));
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error finding member by id", e);
            throw e;
        }
    }

    @Override
    public List<Member> findAll() throws DatabaseException {
        String sql = "SELECT * FROM member ORDER BY name";
        try {
            logger.log(Level.INFO, "Member list executed");
            return jdbc.query(sql, null, MEMBER_MAPPER);
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error executing list members: {0}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Member> findAllActive() throws DatabaseException {
        String sql = "SELECT * FROM member WHERE deleted = FALSE ORDER BY name";
        try {
            logger.log(Level.INFO, "Active member list executed");
            return jdbc.query(sql, null, MEMBER_MAPPER);
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error executing active members list: {0}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Member> findByName(String name) throws DatabaseException {
        String sql = "SELECT * FROM member WHERE LOWER(name) LIKE LOWER(?) ORDER BY name";
        try {
            List<Member> list = jdbc.query(sql, ps -> {
                try { 
                    ps.setString(1, "%" + name + "%"); 
                } catch (SQLException e) { 
                    throw new RuntimeException("Error searching members by name", e); 
                }
            }, MEMBER_MAPPER);
            logger.log(Level.INFO, "Member search by name executed: {0}", name);
            return list;
        } catch (DatabaseException e) { 
            logger.log(Level.SEVERE, "Error executing search by name: {0}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Member> findByRole(MemberRole role) throws DatabaseException {
        String sql = "SELECT * FROM member WHERE role = ? ORDER BY name";
        try {
            List<Member> list = jdbc.query(sql, ps -> {
                try { 
                    ps.setString(1, role.name()); 
                } catch (SQLException e) { 
                    throw new RuntimeException("Error searching members by role", e); 
                }
            }, MEMBER_MAPPER);
            logger.log(Level.INFO, "Member search by role executed: {0}", role);
            return list;
        } catch (DatabaseException e) { 
            logger.log(Level.SEVERE, "Error executing search by role: {0}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Member> findActiveByRole(MemberRole role) throws DatabaseException {
        String sql = "SELECT * FROM member WHERE role = ? AND deleted = FALSE ORDER BY name";
        try {
            List<Member> list = jdbc.query(sql, ps -> {
                try { 
                    ps.setString(1, role.name()); 
                } catch (SQLException e) { 
                    throw new RuntimeException("Error searching active members by role", e); 
                }
            }, MEMBER_MAPPER);
            logger.log(Level.INFO, "Active member search by role executed: {0}", role);
            return list;
        } catch (DatabaseException e) { 
            logger.log(Level.SEVERE, "Error executing search active by role: {0}", e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean updateActiveStatus(Integer memberId, Boolean active) throws DatabaseException {
        String sql = "UPDATE member SET active = ? WHERE id = ?";
        try {
            int rows = jdbc.update(sql, ps -> {
                try {
                    ps.setBoolean(1, active);
                    ps.setInt(2, memberId);
                } catch (SQLException e) {
                    throw new RuntimeException("Error updating member active status", e);
                }
            });
            return rows > 0;
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error updating member active status", e);
            throw e;
        }
    }

    @Override
    public boolean existsById(Integer id) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM member WHERE id = ?";
        try {
            List<Integer> count = jdbc.query(sql, ps -> {
                try { 
                    ps.setInt(1, id); 
                } catch (SQLException e) { 
                    throw new RuntimeException("Error checking member existence", e); 
                }
            }, rs -> rs.getInt(1));
            logger.log(Level.INFO, "Member exists check executed: {0}", id);
            return count.stream().findFirst().orElse(0) > 0;
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error executing member exists check: {0}", e.getMessage());
            throw e;
        }
    }
}