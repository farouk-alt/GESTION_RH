/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rhmanagement.models;

/**
 *
 * @author farou
 */
import java.sql.Date;

public class DemandeEmploye {
    private Date dateDebut;
    private Date dateFin;
    private String motif;
    private String etat;
    private Date dateMiseAjour;
    private String nom_employe;
    private String prenom_employe;

    public DemandeEmploye(Date dateDebut, Date dateFin, String motif, String etat, Date dateMiseAjour, String nom_employe, String prenom_employe) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.motif = motif;
        this.etat = etat;
        this.dateMiseAjour = dateMiseAjour;
        this.nom_employe = nom_employe;
        this.prenom_employe = prenom_employe;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Date getDateMiseAjour() {
        return dateMiseAjour;
    }

    public void setDateMiseAjour(Date dateMiseAjour) {
        this.dateMiseAjour = dateMiseAjour;
    }

    public String getNom_employe() {
        return nom_employe;
    }

    public void setNom_employe(String nom_employe) {
        this.nom_employe = nom_employe;
    }

    public String getPrenom_employe() {
        return prenom_employe;
    }

    public void setPrenom_employe(String prenom_employe) {
        this.prenom_employe = prenom_employe;
    }

   
}

