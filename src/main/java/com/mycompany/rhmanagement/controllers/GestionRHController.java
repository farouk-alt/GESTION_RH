package com.mycompany.rhmanagement.controllers;

import com.mycompany.rhmanagement.DatabaseConnection;
import com.mycompany.rhmanagement.models.Admin;
import com.mycompany.rhmanagement.models.Employee;
import com.mycompany.rhmanagement.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestionRHController {
    
    // login pour les trois role (ADMIN,CHEF,EMPLOYE)
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
    
    //-------------------------------Admin------------------------------------//
    //-------------------------------Admin Profile----------------------------//
    // Admin Consultation
    public static Admin getAdminDetails(int adminId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = DatabaseConnection.getConnection();

            String query = "SELECT id_employe, nom_utilisateur, email, nom_employe, prenom_employe " +
                           "FROM employe WHERE id_employe = ? AND admin = TRUE";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, adminId);

            rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id_employe");
                String username = rs.getString("nom_utilisateur");
                String email = rs.getString("email");
                String nom = rs.getString("nom_employe");
                String prenom = rs.getString("prenom_employe");

                return new Admin(id, username, email, nom, prenom);
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
    //admin infos modifictaion 
    public static boolean updateAdminInfo(int adminId, String newUsername, String newEmail, String newNom, String newPrenom) {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = DatabaseConnection.getConnection();

            String query = "UPDATE employe SET nom_utilisateur = ?, email = ?, nom_employe = ?, prenom_employe = ? WHERE id_employe = ? AND admin = TRUE";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, newUsername);
            stmt.setString(2, newEmail);
            stmt.setString(3, newNom);
            stmt.setString(4, newPrenom);
            stmt.setInt(5, adminId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
            }
        }
    }
    
    //---------------------------------Employee----------------------------//
    // pour que l'admin ajouter un employe
    public static boolean addEmployee(String username, String password, String email, String nom, String prenom, boolean isAdmin, int soldeConge) {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = DatabaseConnection.getConnection();

            String query = "INSERT INTO employe (nom_utilisateur, mot_de_passe, email, nom_employe, prenom_employe, admin, solde_conge, date_creation) VALUES (?, ?, ?, ?, ?, ?, ?, CURDATE())";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.setString(4, nom);
            stmt.setString(5, prenom);
            stmt.setBoolean(6, isAdmin);
            stmt.setInt(7, soldeConge);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
            }
        }
    }
    
    //modifier un employee
    public static boolean updateEmployeeInfo(int employeeId, String newUsername, String newEmail, String newNom, String newPrenom, int newSoldeConge) {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = DatabaseConnection.getConnection();

            String query = "UPDATE employe SET nom_utilisateur = ?, email = ?, nom_employe = ?, prenom_employe = ?, solde_conge = ? WHERE id_employe = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, newUsername);
            stmt.setString(2, newEmail);
            stmt.setString(3, newNom);
            stmt.setString(4, newPrenom);
            stmt.setInt(5, newSoldeConge);
            stmt.setInt(6, employeeId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
            }
        }
    }
    
    //suuprimer un employee
    public static boolean deleteEmployee(int employeeId) {
       Connection connection = null;
       PreparedStatement stmt = null;
       PreparedStatement stmt1 = null;

       try {
           connection = DatabaseConnection.getConnection();
           connection.setAutoCommit(false);

           String query1 = "DELETE FROM demande_conge WHERE id_employe = ?";
           stmt1 = connection.prepareStatement(query1);
           stmt1.setInt(1, employeeId);
           stmt1.executeUpdate();

           String query = "DELETE FROM employe WHERE id_employe = ?";
           stmt = connection.prepareStatement(query);
           stmt.setInt(1, employeeId);
           int rowsAffected = stmt.executeUpdate();

           connection.commit(); 
           return rowsAffected > 0;

       } catch (SQLException e) {
           if (connection != null) {
               try {
                   connection.rollback(); // Rollback transaction on error
               } catch (SQLException rollbackEx) {
                   rollbackEx.printStackTrace();
               }
           }

           return false;
       } finally {
           try {
               if (stmt1 != null) stmt1.close();
               if (stmt != null) stmt.close();
               if (connection != null) connection.close();
           } catch (SQLException e) {


           }
       }
   }

    public static Employee getEmployeeInfo(int employeeId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = DatabaseConnection.getConnection();

            String query = "SELECT id_employe, nom_utilisateur, email, nom_employe, prenom_employe, solde_conge FROM employe WHERE id_employe = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, employeeId);

            rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id_employe");
                String username = rs.getString("nom_utilisateur");
                String email = rs.getString("email");
                String nom = rs.getString("nom_employe");
                String prenom = rs.getString("prenom_employe");
                int soldeConge = rs.getInt("solde_conge");

                return new Employee(id, username, email, nom, prenom, soldeConge);
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