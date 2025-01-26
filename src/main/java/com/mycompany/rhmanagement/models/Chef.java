/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rhmanagement.models;

/**
 *
 * @author farou
 */
public class Chef {
    private int idEmploye;
    private String username;
    private String email;
    private String lastName;
    private String firstName;
    private int departmentId;
    private String startDate;
    private String endDate;

    // Constructor
    public Chef(int idEmploye, String username, String email, String lastName, String firstName, int departmentId, String startDate, String endDate) {
        this.idEmploye = idEmploye;
        this.username = username;
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.departmentId = departmentId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public int getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(int idEmploye) {
        this.idEmploye = idEmploye;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}

