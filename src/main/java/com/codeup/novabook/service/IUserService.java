package com.codeup.novabook.service;

import com.codeup.novabook.domain.User;
import com.codeup.novabook.domain.UserRole;
import com.codeup.novabook.exception.DatabaseException;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    User create(User user) throws DatabaseException;
    User update(User user) throws DatabaseException;
    boolean softDelete(Integer id) throws DatabaseException;
    boolean hardDelete(Integer id) throws DatabaseException;

    Optional<User> findById(Integer id) throws DatabaseException;
    Optional<User> findByEmail(String email) throws DatabaseException;
    List<User> findAll() throws DatabaseException;
    List<User> findAllActive() throws DatabaseException;
    List<User> findByName(String name) throws DatabaseException;
    List<User> findByRole(UserRole role) throws DatabaseException;

    Optional<User> authenticate(String email, String password) throws DatabaseException;
    boolean updateActiveStatus(Integer userId, Boolean active) throws DatabaseException;
    boolean updatePassword(Integer userId, String newPassword) throws DatabaseException;

}
