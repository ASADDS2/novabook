/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.codeup.novabook.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Functional interface for mapping a ResultSet row to a domain object.
 * <p>
 * Implementations of this interface perform the actual work of mapping
 * each row to a result object. Used by {@link JdbcTemplateLight}.
 * </p>
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * RowMapper<User> userMapper = rs -> {
 *     User user = new User();
 *     user.setId(rs.getInt("id"));
 *     user.setName(rs.getString("name"));
 *     user.setEmail(rs.getString("email"));
 *     return user;
 * };
 * }</pre>
 * 
 * @param <T> the type of the result object
 * @author Coder
 * @version 1.0
 * @since 1.0
 * @see JdbcTemplateLight
 */
@FunctionalInterface
public interface RowMapper<T> {
    
    /**
     * Maps a single row from the ResultSet to an object.
     * 
     * @param rs the ResultSet positioned at the current row
     * @return the mapped object for the current row
     * @throws SQLException if a SQLException is encountered getting column values
     */
    T map(ResultSet rs) throws SQLException;
}
