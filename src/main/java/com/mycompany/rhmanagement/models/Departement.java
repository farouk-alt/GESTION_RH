/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rhmanagement.models;

/**
 *
 * @author farou
 */
public class Departement {
    private int id;
    private String nom_departement;

    public Departement(int id, String nom_departement) {
        this.id = id;
        this.nom_departement = nom_departement;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom_departement() {
        return nom_departement;
    }

    public void setNom_departement(String nom_departement) {
        this.nom_departement = nom_departement;
    }
    
    
}
