/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.codeup.novabook.connection.ConnectionFactory;
import com.codeup.novabook.exception.DatabaseException;

/**
 * Lightweight JDBC template for simplified database operations.
 * <p>
 * This class provides a simplified interface for JDBC operations, handling
 * resource management, exception handling, and providing convenient
 * methods for common database operations.
 * </p>
 * <p>Key features:</p>
 * <ul>
 * <li>Automatic resource management (Connection, PreparedStatement, ResultSet)</li>
 * <li>Type-safe result mapping using {@link RowMapper}</li>
 * <li>Transaction support with rollback on exceptions</li>
 * <li>Parameterized queries to prevent SQL injection</li>
 * <li>Functional interfaces for flexible parameter binding</li>
 * </ul>
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * JdbcTemplateLight jdbc = new JdbcTemplateLight(connectionFactory);
 * 
 * // Query with parameters
 * List<User> users = jdbc.query(
 *     "SELECT * FROM users WHERE active = ?",
 *     ps -> ps.setBoolean(1, true),
 *     userMapper
 * );
 * 
 * // Query single object
 * Optional<User> user = jdbc.queryForObject(
 *     "SELECT * FROM users WHERE id = ?",
 *     ps -> ps.setInt(1, userId),
 *     userMapper
 * );
 * 
 * // Update with parameters
 * int rows = jdbc.update(
 *     "UPDATE users SET name = ? WHERE id = ?",
 *     ps -> {
 *         ps.setString(1, "New Name");
 *         ps.setInt(2, userId);
 *     }
 * );
 * }</pre>
 * 
 * @author Coder
 * @version 1.0
 * @since 1.0
 * @see ConnectionFactory
 * @see RowMapper
 */
public class JdbcTemplateLight {
    private final ConnectionFactory factory;
    
    /**
     * Constructs a JdbcTemplateLight with the specified connection factory.
     * 
     * @param factory the connection factory for obtaining database connections
     * @throws IllegalArgumentException if factory is null
     */
    public JdbcTemplateLight(ConnectionFactory factory) { 
        if (factory == null) {
            throw new IllegalArgumentException("ConnectionFactory cannot be null");
        }
        this.factory = factory; 
    }

    /**
     * Executes a query and maps the results using the provided RowMapper.
     * <p>
     * This method handles the complete lifecycle of a database query including
     * connection management, parameter binding, result set processing, and
     * resource cleanup.
     * </p>
     * 
     * @param <T> the type of objects to return
     * @param sql the SQL query to execute
     * @param binder a consumer to bind parameters to the PreparedStatement, can be null
     * @param mapper the RowMapper to convert ResultSet rows to objects
     * @return a list of mapped objects, never null but may be empty
     * @throws DatabaseException if a database error occurs
     */
    public <T> List<T> query(String sql, Consumer<PreparedStatement> binder, RowMapper<T> mapper) throws DatabaseException {
        try (Connection c = factory.open();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (binder != null) {
                binder.accept(ps);
            }
            try (ResultSet rs = ps.executeQuery()) {
                List<T> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(mapper.map(rs));
                }
                return out;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error executing query: " + sql, e);
        }
    }

    /**
     * Executes a query expecting a single result.
     * <p>
     * Useful for queries that should return at most one row (e.g., SELECT by ID).
     * If multiple rows are returned, only the first one is mapped.
     * </p>
     * 
     * @param <T> the type of object to return
     * @param sql the SQL query to execute
     * @param binder a consumer to bind parameters to the PreparedStatement, can be null
     * @param mapper the RowMapper to convert ResultSet row to object
     * @return an Optional containing the mapped object, or empty if no result found
     * @throws DatabaseException if a database error occurs
     */
    public <T> Optional<T> queryForObject(String sql, Consumer<PreparedStatement> binder, RowMapper<T> mapper) throws DatabaseException {
        List<T> results = query(sql, binder, mapper);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    /**
     * Executes an update statement (INSERT, UPDATE, DELETE).
     * <p>
     * This method handles parameter binding and resource cleanup for
     * data modification operations.
     * </p>
     * 
     * @param sql the SQL statement to execute
     * @param binder a consumer to bind parameters to the PreparedStatement, can be null
     * @return the number of rows affected by the statement
     * @throws DatabaseException if a database error occurs
     */
    public int update(String sql, Consumer<PreparedStatement> binder) throws DatabaseException {
        try (Connection c = factory.open();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (binder != null) {
                binder.accept(ps);
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error executing update: " + sql, e);
        }
    }

    /**
     * Executes an INSERT statement and returns the generated key.
     * <p>
     * Useful for getting the auto-generated ID after inserting a new record.
     * </p>
     * 
     * @param sql the INSERT SQL statement to execute
     * @param binder a consumer to bind parameters to the PreparedStatement, can be null
     * @return the generated key (typically an auto-increment ID)
     * @throws DatabaseException if a database error occurs or no key was generated
     */
    public int insert(String sql, Consumer<PreparedStatement> binder) throws DatabaseException {
        try (Connection c = factory.open();
             PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            if (binder != null) {
                binder.accept(ps);
            }
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DatabaseException("Insert failed, no rows affected");
            }
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new DatabaseException("Insert failed, no ID obtained");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error executing insert: " + sql, e);
        }
    }

    /**
     * Executes multiple operations within a single transaction.
     * <p>
     * This method provides transactional support by disabling auto-commit,
     * executing the callback, and then either committing on success or
     * rolling back on exception. The original auto-commit state is restored.
     * </p>
     * 
     * @param <T> the type of result returned by the callback
     * @param cb the callback containing the transactional operations
     * @return the result returned by the callback
     * @throws DatabaseException if any database error occurs, triggering rollback
     */
    public <T> T txExecute(SqlTxCallback<T> cb) throws DatabaseException {
        Connection c = null;
        boolean originalAutoCommit = true;
        
        try {
            c = factory.open();
            originalAutoCommit = c.getAutoCommit();
            c.setAutoCommit(false);
            
            T result = cb.doInTx(c);
            c.commit();
            return result;
            
        } catch (SQLException ex) {
            if (c != null) {
                try {
                    c.rollback();
                } catch (SQLException rollbackEx) {
                    throw new DatabaseException("Error during rollback", rollbackEx);
                }
            }
            throw new DatabaseException("Transaction failed and was rolled back", ex);
            
        } finally {
            if (c != null) {
                try {
                    c.setAutoCommit(originalAutoCommit);
                    c.close();
                } catch (SQLException e) {
                    throw new DatabaseException("Error closing connection", e);
                }
            }
        }
    }

    /**
     * Functional interface for transactional callback operations.
     * <p>
     * This interface defines a callback that can perform multiple database
     * operations within a single transaction. The connection is managed
     * by the calling {@link #txExecute(SqlTxCallback)} method.
     * </p>
     * 
     * @param <T> the type of result returned by the callback
     */
    @FunctionalInterface
    public interface SqlTxCallback<T> {
        
        /**
         * Performs transactional operations using the provided connection.
         * 
         * @param conn the database connection with auto-commit disabled
         * @return the result of the transactional operations
         * @throws SQLException if any database error occurs
         */
        T doInTx(Connection conn) throws SQLException;
    }
}