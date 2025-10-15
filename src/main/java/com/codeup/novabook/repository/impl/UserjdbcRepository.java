package com.codeup.novabook.repository.impl;

import com.codeup.novabook.domain.AccessLevel;
import com.codeup.novabook.domain.User;
import com.codeup.novabook.domain.UserRole;
import com.codeup.novabook.exception.DatabaseException;
import com.codeup.novabook.jdbc.JdbcTemplateLight;
import com.codeup.novabook.jdbc.RowMapper;
import com.codeup.novabook.repository.IUserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserjdbcRepository implements IUserRepository {

    private final JdbcTemplateLight jdbc;
    private static final Logger logger = Logger.getLogger(UserjdbcRepository.class.getName());

    public UserjdbcRepository(JdbcTemplateLight jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<User> USER_MAPPER = rs -> {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setPhone(rs.getString("phone"));
        u.setRole(UserRole.valueOf(rs.getString("role")));
        u.setAccessLevel(AccessLevel.valueOf(rs.getString("access_level")));
        u.setActive(rs.getBoolean("active"));
        u.setDeleted(rs.getBoolean("deleted"));
        u.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        u.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return u;
    };

    @Override
    public User save(User user) throws DatabaseException {
        if (existsByEmail(user.getEmail())) {
            throw new DatabaseException("Email already exists: " + user.getEmail());
        }
        final String hashed = safeHash(user.getPassword());
        String sql = "INSERT INTO user (name, email, password, phone, role, access_level, active, deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int id = jdbc.insert(sql, ps -> {
            try {
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.setString(3, hashed);
                ps.setString(4, user.getPhone());
                ps.setString(5, user.getRole().name());
                ps.setString(6, user.getAccessLevel().name());
                ps.setBoolean(7, user.getActive());
                ps.setBoolean(8, user.getDeleted());
            } catch (SQLException e) {
                throw new RuntimeException("Error creating user", e);
            }
        });
        user.setId(id);
        user.setPassword(null); // do not keep raw password in memory
        return user;
    }

    @Override
    public User update(User user) throws DatabaseException {
        String sql = "UPDATE user SET name=?, email=?, password=?, phone=?, role=?, access_level=?, active=?, deleted=? WHERE id=?";
        int rows = jdbc.update(sql, ps -> {
            try {
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getPassword());
                ps.setString(4, user.getPhone());
                ps.setString(5, user.getRole().name());
                ps.setString(6, user.getAccessLevel().name());
                ps.setBoolean(7, user.getActive());
                ps.setBoolean(8, user.getDeleted());
                ps.setInt(9, user.getId());
            } catch (SQLException e) {
                throw new RuntimeException("Error updating user", e);
            }
        });
        if (rows != 1) {
            throw new DatabaseException("Failed to update user with id: " + user.getId());
        }
        return user;
    }

    @Override
    public boolean softDelete(Integer id) throws DatabaseException {
        String sql = "UPDATE user SET deleted = TRUE WHERE id = ?";
        int rows = jdbc.update(sql, ps -> {
            try { ps.setInt(1, id);} catch (SQLException e) { throw new RuntimeException(e);} });
        return rows > 0;
    }

    @Override
    public boolean hardDelete(Integer id) throws DatabaseException {
        String sql = "DELETE FROM user WHERE id = ?";
        int rows = jdbc.update(sql, ps -> {
            try { ps.setInt(1, id);} catch (SQLException e) { throw new RuntimeException(e);} });
        return rows > 0;
    }

    @Override
    public Optional<User> findById(Integer id) throws DatabaseException {
        String sql = "SELECT * FROM user WHERE id = ?";
        return jdbc.queryForObject(sql, ps -> { try { ps.setInt(1, id);} catch (SQLException e) { throw new RuntimeException(e);} }, USER_MAPPER);
    }

    @Override
    public Optional<User> findByEmail(String email) throws DatabaseException {
        String sql = "SELECT * FROM user WHERE email = ?";
        return jdbc.queryForObject(sql, ps -> { try { ps.setString(1, email);} catch (SQLException e) { throw new RuntimeException(e);} }, USER_MAPPER);
    }

    @Override
    public List<User> findAll() throws DatabaseException {
        return jdbc.query("SELECT * FROM user ORDER BY name", null, USER_MAPPER);
    }

    @Override
    public List<User> findAllActive() throws DatabaseException {
        return jdbc.query("SELECT * FROM user WHERE deleted = FALSE ORDER BY name", null, USER_MAPPER);
    }

    @Override
    public List<User> findByName(String name) throws DatabaseException {
        String sql = "SELECT * FROM user WHERE LOWER(name) LIKE LOWER(?) ORDER BY name";
        return jdbc.query(sql, ps -> { try { ps.setString(1, "%" + name + "%");} catch (SQLException e) { throw new RuntimeException(e);} }, USER_MAPPER);
    }

    @Override
    public List<User> findByRole(UserRole role) throws DatabaseException {
        String sql = "SELECT * FROM user WHERE role = ? ORDER BY name";
        return jdbc.query(sql, ps -> { try { ps.setString(1, role.name());} catch (SQLException e) { throw new RuntimeException(e);} }, USER_MAPPER);
    }

    @Override
    public Optional<User> authenticate(String email, String password) throws DatabaseException {
        Optional<User> db = findByEmail(email);
        if (db.isEmpty()) return Optional.empty();
        User u = db.get();
        boolean ok;
        try {
            ok = BCrypt.checkpw(password, u.getPassword());
        } catch (Exception ex) {
            logger.log(Level.WARNING, "BCrypt check failed, falling back to plain compare");
            ok = password.equals(u.getPassword());
        }
        return ok && Boolean.TRUE.equals(u.getActive()) && Boolean.FALSE.equals(u.getDeleted()) ? Optional.of(u) : Optional.empty();
    }

    @Override
    public boolean updateActiveStatus(Integer userId, Boolean active) throws DatabaseException {
        String sql = "UPDATE user SET active = ? WHERE id = ?";
        int rows = jdbc.update(sql, ps -> { try { ps.setBoolean(1, active); ps.setInt(2, userId);} catch (SQLException e) { throw new RuntimeException(e);} });
        return rows > 0;
    }

    @Override
    public boolean updatePassword(Integer userId, String newPassword) throws DatabaseException {
        final String hashed = safeHash(newPassword);
        String sql = "UPDATE user SET password = ? WHERE id = ?";
        int rows = jdbc.update(sql, ps -> { try { ps.setString(1, hashed); ps.setInt(2, userId);} catch (SQLException e) { throw new RuntimeException(e);} });
        return rows > 0;
    }

    private String safeHash(String raw) {
        try {
            return BCrypt.hashpw(raw, BCrypt.gensalt());
        } catch (Exception e) {
            return raw; // fallback
        }
    }

    @Override
    public boolean existsByEmail(String email) throws DatabaseException {
        String sql = "SELECT 1 FROM user WHERE email = ? LIMIT 1";
        List<Integer> rows = jdbc.query(sql, ps -> { try { ps.setString(1, email);} catch (SQLException e) { throw new RuntimeException(e);} }, rs -> 1);
        return !rows.isEmpty();
    }
}
