package com.mycompany.rhmanagement.controllers;

import com.mycompany.rhmanagement.DatabaseConnection;
import com.mycompany.rhmanagement.models.Admin;
import com.mycompany.rhmanagement.models.Chef;
import com.mycompany.rhmanagement.models.Demande;
import com.mycompany.rhmanagement.models.DemandeEmploye;
import com.mycompany.rhmanagement.models.Departement;
import com.mycompany.rhmanagement.models.Employee;
import com.mycompany.rhmanagement.models.User;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    
    public static List<Employee> getAllEmployees() {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = DatabaseConnection.getConnection();
            List<Employee> employees = new ArrayList<>();
            String query = "SELECT * FROM employe";
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_employe");
                String username = rs.getString("nom_utilisateur");
                String email = rs.getString("email");
                String nom = rs.getString("nom_employe");
                String prenom = rs.getString("prenom_employe");
                int soldeConge = rs.getInt("solde_conge");

                Employee employee = new Employee(id, username, email, nom, prenom, soldeConge);
                employees.add(employee);
            }

            return employees;

        } catch (SQLException e) {
            return new ArrayList<>();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                
            }
        }
    }
    
    //----------------------------Departements-------------------------//
    //get all the dpeartements
    public static List<Departement> getAllDepartments() {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Departement> departments = new ArrayList<>();

        try {
            connection = DatabaseConnection.getConnection();

            String query = "SELECT * FROM departement";
            stmt = connection.prepareStatement(query);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_departement");
                String nomDepartement = rs.getString("nom_departement");
                
                Departement dep = new Departement(id,nomDepartement);

                departments.add(dep);
            }
        } catch (SQLException e) {
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
            }
        }

        return departments;
    }
    
    //ajouter departement
        public static boolean addDepartment(String departmentName) {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = DatabaseConnection.getConnection();

            String query = "INSERT INTO departement (nom_departement) VALUES (?)";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, departmentName);

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
        // consulter un departement specifique
        public static Departement getDepartmentDetails(int departmentId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = DatabaseConnection.getConnection();

            String query = "SELECT d.id_departement, d.nom_departement, e.nom_employe , e.prenom_employe FROM departement d JOIN Employe e "
                    + "ON d.id_departement = e.id_departement"
                    + "JOIN Chef c ON c.id_employe = e.id_employe WHERE d.id_departement = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, departmentId);

            rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id_departement");
                String name = rs.getString("nom_departement");

                return new Departement(id, name);
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


        // modifier un departement
        public static boolean updateDepartment(int departmentId, String newDepartmentName) {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = DatabaseConnection.getConnection();

            String query = "UPDATE departement SET nom_departement = ? WHERE id_departement = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, newDepartmentName);
            stmt.setInt(2, departmentId);

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
        
    // supprimer un departement
    public static boolean deleteDepartment(int departmentId) {
    Connection connection = null;
    PreparedStatement stmt = null;

    try {
        connection = DatabaseConnection.getConnection();

        String query = "DELETE FROM departement WHERE id_departement = ?";
        stmt = connection.prepareStatement(query);
        stmt.setInt(1, departmentId);

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
    
    //---------------------------------Chef--------------------------------------//
    public static boolean addChef(
    int departmentId, 
    String firstName, 
    String lastName, 
    String email, 
    String username, 
    String password, 
    int soldeConge, 
    String startDate, 
    String endDate
    ) {
        Connection connection = null;
        PreparedStatement stmtEmploye = null;
        PreparedStatement stmtChef = null;

        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false); 

            String queryEmploye = "INSERT INTO employe (nom_utilisateur, mot_de_passe, email, nom_employe, prenom_employe, solde_conge, admin, date_creation) " +
                                  "VALUES (?, ?, ?, ?, ?, ?, FALSE, CURDATE())";
            stmtEmploye = connection.prepareStatement(queryEmploye, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtEmploye.setString(1, username);
            stmtEmploye.setString(2, password);
            stmtEmploye.setString(3, email);
            stmtEmploye.setString(4, lastName);
            stmtEmploye.setString(5, firstName);
            stmtEmploye.setInt(6, soldeConge);

            int rowsAffectedEmploye = stmtEmploye.executeUpdate();

            if (rowsAffectedEmploye == 0) {
                connection.rollback();
                return false;
            }

            ResultSet generatedKeys = stmtEmploye.getGeneratedKeys();
            if (!generatedKeys.next()) {
                connection.rollback();
                return false;
            }
            int employeeId = generatedKeys.getInt(1);

            String queryChef = "INSERT INTO chef (id_departement, id_employe, date_debut, date_fin) VALUES (?, ?, ?, ?)";
            stmtChef = connection.prepareStatement(queryChef);
            stmtChef.setInt(1, departmentId);
            stmtChef.setInt(2, employeeId);
            stmtChef.setString(3, startDate);
            stmtChef.setString(4, endDate);

            int rowsAffectedChef = stmtChef.executeUpdate();

            if (rowsAffectedChef == 0) {
                connection.rollback();
                return false;
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                }
            }
            return false;
        } finally {
            try {
                if (stmtEmploye != null) stmtEmploye.close();
                if (stmtChef != null) stmtChef.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
            }
        }
    }
    
    public static Chef getChefDetails(int employeeId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = DatabaseConnection.getConnection();

            String query = "SELECT e.id_employe, e.nom_utilisateur, e.email, e.nom_employe, e.prenom_employe, c.id_departement, c.date_debut, c.date_fin " +
                           "FROM employe e JOIN chef c ON e.id_employe = c.id_employe WHERE e.id_employe = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, employeeId);

            rs = stmt.executeQuery();

            
            if (rs.next()) {
                int idEmploye = rs.getInt("id_employe");
                String username = rs.getString("nom_utilisateur");
                String email = rs.getString("email");
                String lastName = rs.getString("nom_employe");
                String firstName = rs.getString("prenom_employe");
                int departmentId = rs.getInt("id_departement");
                String startDate = rs.getString("date_debut");
                String endDate = rs.getString("date_fin");

                
                return new Chef(idEmploye, username, email, lastName, firstName, departmentId, startDate, endDate);
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



     public static boolean updateChef(
        int employeeId, 
        String newUsername, 
        String newEmail, 
        String newLastName, 
        String newFirstName, 
        int newSoldeConge, 
        int newDepartmentId, 
        String newStartDate, 
        String newEndDate
    ) {
    Connection connection = null;
    PreparedStatement stmtEmploye = null;
    PreparedStatement stmtChef = null;

    try {
        connection = DatabaseConnection.getConnection();
        connection.setAutoCommit(false);

        
        String queryEmploye = "UPDATE employe SET nom_utilisateur = ?, email = ?, nom_employe = ?, prenom_employe = ?, solde_conge = ? WHERE id_employe = ?";
        stmtEmploye = connection.prepareStatement(queryEmploye);
        stmtEmploye.setString(1, newUsername);
        stmtEmploye.setString(2, newEmail);
        stmtEmploye.setString(3, newLastName);
        stmtEmploye.setString(4, newFirstName);
        stmtEmploye.setInt(5, newSoldeConge);
        stmtEmploye.setInt(6, employeeId);

        int rowsAffectedEmploye = stmtEmploye.executeUpdate();

        if (rowsAffectedEmploye == 0) {
            connection.rollback();
            return false;
        }

        String queryChef = "UPDATE chef SET id_departement = ?, date_debut = ?, date_fin = ? WHERE id_employe = ?";
        stmtChef = connection.prepareStatement(queryChef);
        stmtChef.setInt(1, newDepartmentId);
        stmtChef.setString(2, newStartDate);
        stmtChef.setString(3, newEndDate);
        stmtChef.setInt(4, employeeId);

        int rowsAffectedChef = stmtChef.executeUpdate();

        if (rowsAffectedChef == 0) {
            connection.rollback();
            return false;
        }

        connection.commit();
        return true;

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                }
            }
            return false;
        } finally {
            try {
                if (stmtEmploye != null) stmtEmploye.close();
                if (stmtChef != null) stmtChef.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
            }
        }
    }
     
     
    public static boolean deleteChef(int employeeId) {
    Connection connection = null;
    PreparedStatement stmtChef = null;
    PreparedStatement stmtEmploye = null;

    try {
        connection = DatabaseConnection.getConnection();
        connection.setAutoCommit(false);

        String queryChef = "DELETE FROM chef WHERE id_employe = ?";
        stmtChef = connection.prepareStatement(queryChef);
        stmtChef.setInt(1, employeeId);
        stmtChef.executeUpdate();

        
        String queryEmploye = "DELETE FROM employe WHERE id_employe = ?";
        stmtEmploye = connection.prepareStatement(queryEmploye);
        stmtEmploye.setInt(1, employeeId);
        int rowsAffected = stmtEmploye.executeUpdate();

        if (rowsAffected == 0) {
            connection.rollback();
            return false;
        }

        connection.commit();
        return true;

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(); 
                } catch (SQLException rollbackEx) {
                }
            }
            return false;
        } finally {
            try {
                if (stmtChef != null) stmtChef.close();
                if (stmtEmploye != null) stmtEmploye.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
            }
        }
    }

    
    //------------------------------------------Historique demande de congés--------------------------//
        public static List<Employee> getEmployesByDepartement(int departementId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Employee> employes = new ArrayList<>();

        try {
            connection = DatabaseConnection.getConnection();

            String query = "SELECT e.id_employe, e.nom_employe, e.prenom_employe, e.email, e.nom_utilisateur " +
                           "FROM employe e " +
                           "WHERE e.id_departement = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, departementId);

            rs = stmt.executeQuery();

            while (rs.next()) {
                int idEmploye = rs.getInt("id_employe");
                String nom = rs.getString("nom_employe");
                String prenom = rs.getString("prenom_employe");
                String email = rs.getString("email");
                String nomUtilisateur = rs.getString("nom_utilisateur");
                int solde = rs.getInt("solde_conge");

                Employee employe = new Employee(idEmploye, nomUtilisateur,nom, prenom, email, solde);
                employes.add(employe);
            }
        } catch (SQLException e) {
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
            }
        }
        return employes;
    }

        
    //--------------------------------------------------Chef de departement-----------------------------------//
    //demande en cours ordonnée par dateMiseAJour
    
        
    public static List<DemandeEmploye> getPendingRequestsByPriority(int departmentId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<DemandeEmploye> demandes = new ArrayList<>();

        try {
            connection = DatabaseConnection.getConnection();

            String query = "SELECT d.id_demande, d.date_debut, d.date_fin, d.motif, d.etat, d.dateMiseAjour, " +
                           "e.nom_employe, e.prenom_employe " +
                           "FROM demande_conge d JOIN employe e ON d.id_employe = e.id_employe " +
                           "WHERE e.id_departement = ? AND d.etat = 'en attente' " +
                           "ORDER BY d.dateMiseAjour ASC";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, departmentId);

            rs = stmt.executeQuery();

            while (rs.next()) {
                Date startDate = rs.getDate("date_debut");
                Date endDate = rs.getDate("date_fin");
                String motive = rs.getString("motif");
                String state = rs.getString("etat");
                Date updateDate = rs.getDate("dateMiseAjour");
                String lastName = rs.getString("nom_employe");
                String firstName = rs.getString("prenom_employe");

                DemandeEmploye demande = new DemandeEmploye(startDate, endDate, motive, state, updateDate, lastName, firstName);
                demandes.add(demande);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return demandes;
    }
     //accpeter la demande 
    public static boolean accepterDemande(int idDemande) {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = DatabaseConnection.getConnection();
            String query = "UPDATE demande_conge SET etat = ?, dateMiseAjour = NOW() WHERE id_demande = ?";
            stmt = connection.prepareStatement(query);

            stmt.setString(1, "approuvée");
            stmt.setInt(2, idDemande);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Refuser une demande
    public static boolean refuserDemande(int idDemande) {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = DatabaseConnection.getConnection();
            String query = "UPDATE demande_conge SET etat = ?, dateMiseAjour = NOW() WHERE id_demande = ?";
            stmt = connection.prepareStatement(query);

            stmt.setString(1, "refusée");
            stmt.setInt(2, idDemande);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


     //historique des demandes
     public static List<DemandeEmploye> getDemandesApprouvéeOuRefusée() {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<DemandeEmploye> demandes = new ArrayList<>();

        try {
            connection = DatabaseConnection.getConnection();

            String query = "SELECT d.date_debut, d.date_fin, d.motif, d.etat, d.dateMiseAjour " +
                           "e.nom_employe , e.prenom_employe FROM demande d JOIN employe e ON e.id_employe = d.id_employe"
                          + " WHERE etat != ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, "en attente");

            rs = stmt.executeQuery();

            while (rs.next()) {
                Date dateDebut = rs.getDate("date_debut");
                Date dateFin = rs.getDate("date_fin");
                String motif = rs.getString("motif");
                String etat = rs.getString("etat");
                Date dateMiseAjour = rs.getDate("dateMiseAjour");
                String nom_emp = rs.getString("nom_employe");
                String prenom_emp = rs.getString("prenom_employe");

                DemandeEmploye demande = new DemandeEmploye(dateDebut, dateFin, motif, etat, dateMiseAjour,nom_emp,prenom_emp);
                demandes.add(demande);
            }
        } catch (SQLException e) {            
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
            }
        }
        return demandes;
    }
     
     //recupere les employes de departement
    public static List<Employee> getEmployeesByDepartment(int departmentId, int chefId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Employee> employees = new ArrayList<>();

        try {
            connection = DatabaseConnection.getConnection();

            String checkChefQuery = "SELECT * FROM chef WHERE id_departement = ? AND id_employe = ? AND CURDATE() BETWEEN date_debut AND date_fin";
            stmt = connection.prepareStatement(checkChefQuery);
            stmt.setInt(1, departmentId);
            stmt.setInt(2, chefId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String query = "SELECT e.id_employe, e.nom_utilisateur, e.email, e.nom_employe, e.prenom_employe, e.solde_conge " +
                               "FROM employe e WHERE e.id_departement = ?";
                stmt = connection.prepareStatement(query);
                stmt.setInt(1, departmentId);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id_employe");
                    String username = rs.getString("nom_utilisateur");
                    String email = rs.getString("email");
                    String firstName = rs.getString("nom_employe");
                    String lastName = rs.getString("prenom_employe");
                    int soldeConge = rs.getInt("solde_conge");

                    Employee employee = new Employee(id, username, email, firstName, lastName, soldeConge);
                    employees.add(employee);
                }
            }

            return employees;

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
    
    //---------------------employe--------------------//
    //profil employee already decalred (getEmployeeInfo ,updateEmployeeInfo)
    
    // Mes demandes 
    public static List<Demande> getAllLeaveRequests(int employeeId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Demande> demandes = new ArrayList<>();

        try {
            connection = DatabaseConnection.getConnection();

            String query = "SELECT id_demande, date_debut, date_fin, motif, etat, dateMiseAjour " +
                           "FROM demande WHERE id_employe = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, employeeId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int idDemande = rs.getInt("id_demande");
                int idEmploye = rs.getInt("id_employe");
                Date startDate = rs.getDate("date_debut");
                Date endDate = rs.getDate("date_fin");
                String motive = rs.getString("motif");
                String state = rs.getString("etat");
                Date updateDate = rs.getDate("dateMiseAjour");

                Demande demande = new Demande(idDemande,idEmploye, startDate, endDate, motive, state, updateDate);
                demandes.add(demande);
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

        return demandes;
    }

    //envoyer une demande de conge avec le 50% condition
    public static boolean sendLeaveRequest(int employeeId, Date startDate, Date endDate, String motive, int departmentId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        PreparedStatement stmtCheck = null;
        ResultSet rs = null;

        try {
            connection = DatabaseConnection.getConnection();

            String queryCheck = "SELECT COUNT(*) AS total FROM employe WHERE id_departement = ?";
            stmtCheck = connection.prepareStatement(queryCheck);
            stmtCheck.setInt(1, departmentId);
            rs = stmtCheck.executeQuery();

            int totalEmployees = 0;
            if (rs.next()) {
                totalEmployees = rs.getInt("total");
            }

            String queryCount = "SELECT COUNT(*) AS onLeave FROM demande_conge dc " +
                                "JOIN employe e ON e.id_employe = dc.id_employe " +
                                "WHERE e.id_departement = ? AND dc.etat = 'approuvée' " +
                                "AND (dc.date_debut <= ? AND dc.date_fin >= ?)";
            stmtCheck = connection.prepareStatement(queryCount);
            stmtCheck.setInt(1, departmentId);
            stmtCheck.setDate(2, endDate);
            stmtCheck.setDate(3, startDate);

            rs = stmtCheck.executeQuery();

            int onLeave = 0;
            if (rs.next()) {
                onLeave = rs.getInt("onLeave");
            }

            if (onLeave >= totalEmployees / 2) {
                return false;
            }

            String insertQuery = "INSERT INTO demande_conge (id_employe, date_debut, date_fin, motif, etat, dateMiseAjour) " +
                                 "VALUES (?, ?, ?, ?, 'en attente', CURDATE())";
            stmt = connection.prepareStatement(insertQuery);
            stmt.setInt(1, employeeId);
            stmt.setDate(2, startDate);
            stmt.setDate(3, endDate);
            stmt.setString(4, motive);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmtCheck != null) stmtCheck.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
            }
        }
    }
    
    
    //reintialiser le solde de conge pour chaque debut de l'année 

    public static boolean resetLeaveBalances() {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {

            Calendar calendar = Calendar.getInstance();
            int currentMonth = calendar.get(Calendar.MONTH) + 1;
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);


            if (currentMonth == 1 && currentDay == 1) {
                connection = DatabaseConnection.getConnection();

                String query = "UPDATE employe SET solde_conge = ?";
                stmt = connection.prepareStatement(query);
                stmt.setInt(1, 18);

                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            } else {
                System.out.println("It's not the start of the year. No action taken.");
                return false;
            }

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


    
    
    



}