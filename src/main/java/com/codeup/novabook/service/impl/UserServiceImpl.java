package com.codeup.novabook.service.impl;

import com.codeup.novabook.domain.User;
import com.codeup.novabook.domain.UserRole;
import com.codeup.novabook.exception.DatabaseException;
import com.codeup.novabook.repository.IUserRepository;
import com.codeup.novabook.service.IUserService;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements IUserService {

    private final IUserRepository repo;

    public UserServiceImpl(IUserRepository repo) {
        this.repo = repo;
    }

    @Override
    public User create(User user) throws DatabaseException { return repo.save(user); }

    @Override
    public User update(User user) throws DatabaseException { return repo.update(user); }

    @Override
    public boolean softDelete(Integer id) throws DatabaseException { return repo.softDelete(id); }

    @Override
    public boolean hardDelete(Integer id) throws DatabaseException { return repo.hardDelete(id); }

    @Override
    public Optional<User> findById(Integer id) throws DatabaseException { return repo.findById(id); }

    @Override
    public Optional<User> findByEmail(String email) throws DatabaseException { return repo.findByEmail(email); }

    @Override
    public List<User> findAll() throws DatabaseException { return repo.findAll(); }

    @Override
    public List<User> findAllActive() throws DatabaseException { return repo.findAllActive(); }

    @Override
    public List<User> findByName(String name) throws DatabaseException { return repo.findByName(name); }

    @Override
    public List<User> findByRole(UserRole role) throws DatabaseException { return repo.findByRole(role); }

    @Override
    public Optional<User> authenticate(String email, String password) throws DatabaseException { return repo.authenticate(email, password); }

    @Override
    public boolean updateActiveStatus(Integer userId, Boolean active) throws DatabaseException { return repo.updateActiveStatus(userId, active); }

    @Override
    public boolean updatePassword(Integer userId, String newPassword) throws DatabaseException { return repo.updatePassword(userId, newPassword); }
}
