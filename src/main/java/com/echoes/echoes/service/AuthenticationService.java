package com.echoes.echoes.service;

import com.echoes.echoes.database.UserDAO;
import com.echoes.echoes.model.User;
import com.echoes.echoes.util.PasswordUtil;

public class AuthenticationService {

    private final UserDAO userDAO = new UserDAO();

    public User login(String email, String password) {

        User user = userDAO.getUserByEmail(email);

        if (user == null) {
            return null;
        }

        if (PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            return user;
        }

        return null;
    }
}