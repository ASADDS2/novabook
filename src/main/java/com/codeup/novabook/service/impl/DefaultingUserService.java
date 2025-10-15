package com.codeup.novabook.service.impl;

import com.codeup.novabook.domain.User;
import com.codeup.novabook.domain.UserRole;
import com.codeup.novabook.service.IUserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Decorator to set default properties when creating a user
 * without changing the base service logic.
 */
public class DefaultingUserService implements IUserService {

    private final IUserService delegate;

    public DefaultingUserService(IUserService delegate) {
        this.delegate = delegate;
    }

    @Override
    public User create(User user) {
        if (user.getRole() == null) user.setRole(UserRole.USER); // map ASISTENTE -> USER
        if (user.getActive() == null) user.setActive(true);
        if (user.getCreatedAt() == null) user.setCreatedAt(LocalDateTime.now());
        return delegate.create(user);
    }

    @Override
    public User update(User user) { return delegate.update(user); }

    @Override
    public boolean softDelete(Integer id) { return delegate.softDelete(id); }

    @Override
    public boolean hardDelete(Integer id) { return delegate.hardDelete(id); }

    @Override
    public Optional<User> findById(Integer id) { return delegate.findById(id); }

    @Override
    public Optional<User> findByEmail(String email) { return delegate.findByEmail(email); }

    @Override
    public List<User> findAll() { return delegate.findAll(); }

    @Override
    public List<User> findAllActive() { return delegate.findAllActive(); }

    @Override
    public List<User> findByName(String name) { return delegate.findByName(name); }

    @Override
    public List<User> findByRole(UserRole role) { return delegate.findByRole(role); }

    @Override
    public Optional<User> authenticate(String email, String password) { return delegate.authenticate(email, password); }

    @Override
    public boolean updateActiveStatus(Integer userId, Boolean active) { return delegate.updateActiveStatus(userId, active); }

    @Override
    public boolean updatePassword(Integer userId, String newPassword) { return delegate.updatePassword(userId, newPassword); }
}
