package com.mycompany.rhmanagement.controllers;

import com.mycompany.rhmanagement.DatabaseConnection;
import com.mycompany.rhmanagement.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestionRHController {
    public static User login(String username, String password) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = DatabaseConnection.getConnection();

            String query = "SELECT id_employe, admin FROM employe WHERE nom_utilisateur = ? AND mot_de_passe = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int employeId = rs.getInt("id_employe");
                boolean isAdmin = rs.getBoolean("admin");

                if (isAdmin) {
                    return new User(employeId, username, "Admin");
                }

                query = "SELECT * FROM chef WHERE id_employe = ?";
                stmt = connection.prepareStatement(query);
                stmt.setInt(1, employeId);
                rs = stmt.executeQuery();

                if (rs.next()) {
                    return new User(employeId, username, "Chef");
                }

                return new User(employeId, username, "Employee");
            } else {
                return null;
            }

        } catch (SQLException e) {
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
            }
        }
    }
}