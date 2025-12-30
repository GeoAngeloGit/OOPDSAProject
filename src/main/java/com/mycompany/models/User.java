/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.models;

/**
 *
 * @author USER
 */
public class User {
    //attributes
    private String username;
    private String password;
    private String role;
    private String department;
    private String filePath;
    private String status;

    //user constructor
    public User(String username, String password, String role, String department, String filePath, String status) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.department = department;
        this.filePath = filePath;
        this.status = status;
    }

    //getters and setters
    public String getUsername()
    {
        return username;
    }
    public String getPassword()
    {
        return password;
    }
    public String getRole()
    {
        return role;
    }
    public String getDepartment()
    {
        return department;
    }
    public String getFilePath()
    {
        return filePath;
    }
    public String getStatus() {
        return status;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }
    public void setRole(String role)
    {
        this.role = role;
    }
    public void setDepartment(String department)
    {
        this.department = department;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
