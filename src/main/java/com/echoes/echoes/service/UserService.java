package com.echoes.echoes.service;

import com.echoes.echoes.database.UserDAO;
import com.echoes.echoes.model.User;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public void registerUser(User user) {

        if (userDAO.emailExists(user.getEmail())) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }

        userDAO.insertUser(user);
    }
}