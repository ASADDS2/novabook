package com.codeup.novabook.infra;

import com.codeup.novabook.domain.User;

public final class SessionManager {
    private static volatile User currentUser;

    private SessionManager() {}

    public static synchronized void setCurrentUser(User user) {
        currentUser = user;
    }

    public static synchronized User getCurrentUser() {
        return currentUser;
    }

    public static synchronized boolean isAuthenticated() {
        return currentUser != null && Boolean.TRUE.equals(currentUser.getActive()) && !Boolean.TRUE.equals(currentUser.getDeleted());
    }

    public static synchronized void clear() { currentUser = null; }
}