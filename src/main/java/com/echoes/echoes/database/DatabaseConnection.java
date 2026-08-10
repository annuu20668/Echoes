package com.echoes.echoes.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            System.getenv().getOrDefault(
                    "ECHOES_DB_URL",
                    "jdbc:mysql://localhost:3306/echoesdb"
            );

    private static final String USERNAME =
            System.getenv().getOrDefault(
                    "ECHOES_DB_USERNAME",
                    "root"
            );

    private static final String PASSWORD =
            System.getenv("ECHOES_DB_PASSWORD");

    public static Connection getConnection() throws SQLException {

        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new SQLException(
                    "ECHOES_DB_PASSWORD environment variable is not configured."
            );
        }

        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}