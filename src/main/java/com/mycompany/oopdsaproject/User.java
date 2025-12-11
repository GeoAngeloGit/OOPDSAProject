/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oopdsaproject;

/**
 *
 * @author USER
 */
public class User {
    private String username;
    private String password;
    private String role;
    private String department;
    private String filePath;

    public User(String username, String password, String role, String department, String filePath) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.department = department;
        this.filePath = filePath;
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
        return "data/" + filePath;
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
}
