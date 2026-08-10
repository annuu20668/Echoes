package com.echoes.echoes.database;

import com.echoes.echoes.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // =========================================================
    // INSERT USER
    // =========================================================

    public void insertUser(User user) {

        String sql =
                "INSERT INTO users (name, email, password_hash) " +
                        "VALUES (?, ?, ?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(
                    1,
                    user.getName()
            );

            preparedStatement.setString(
                    2,
                    user.getEmail()
            );

            preparedStatement.setString(
                    3,
                    user.getPasswordHash()
            );

            int rowsAffected =
                    preparedStatement.executeUpdate();

            System.out.println(
                    rowsAffected
                            + " user inserted successfully!"
            );

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to insert user.",
                    e
            );
        }
    }


    // =========================================================
    // GET USER BY EMAIL
    // =========================================================

    public User getUserByEmail(String email) {

        String sql =
                "SELECT * FROM users WHERE email = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(
                    1,
                    email
            );

            try (ResultSet resultSet =
                         preparedStatement.executeQuery()) {

                if (resultSet.next()) {

                    User user =
                            new User();

                    user.setUserId(
                            resultSet.getInt(
                                    "user_id"
                            )
                    );

                    user.setName(
                            resultSet.getString(
                                    "name"
                            )
                    );

                    user.setEmail(
                            resultSet.getString(
                                    "email"
                            )
                    );

                    user.setPasswordHash(
                            resultSet.getString(
                                    "password_hash"
                            )
                    );

                    return user;
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve user.",
                    e
            );
        }

        return null;
    }


    // =========================================================
    // CHECK EMAIL EXISTS
    // =========================================================

    public boolean emailExists(String email) {

        String sql =
                "SELECT COUNT(*) FROM users WHERE email = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(
                    1,
                    email
            );

            try (ResultSet resultSet =
                         preparedStatement.executeQuery()) {

                if (resultSet.next()) {

                    return resultSet.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to check email.",
                    e
            );
        }

        return false;
    }


    // =========================================================
    // UPDATE USER
    // =========================================================

    public boolean updateUser(
            int userId,
            String name,
            String email) {

        String sql = """
                UPDATE users
                SET name = ?,
                    email = ?
                WHERE user_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(
                    1,
                    name
            );

            preparedStatement.setString(
                    2,
                    email
            );

            preparedStatement.setInt(
                    3,
                    userId
            );

            int rowsAffected =
                    preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to update user.",
                    e
            );
        }
    }
}