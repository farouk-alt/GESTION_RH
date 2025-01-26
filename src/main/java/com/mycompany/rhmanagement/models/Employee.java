package com.mycompany.rhmanagement.models;

public class Employee {
    private int id;
    private String username;
    private String email;
    private String nom;
    private String prenom;
    private int soldeConge;

    public Employee(int id, String username, String email, String nom, String prenom, int soldeConge) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.soldeConge = soldeConge;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public int getSoldeConge() {
        return soldeConge;
    }

}