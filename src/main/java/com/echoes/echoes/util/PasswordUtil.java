package com.echoes.echoes.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public final class PasswordUtil {

    private PasswordUtil() {
    }

    public static String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer()
                .verify(password.toCharArray(), hashedPassword);

        return result.verified;
    }
}