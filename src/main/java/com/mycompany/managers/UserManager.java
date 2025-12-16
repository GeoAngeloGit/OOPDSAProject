package com.mycompany.managers;

import com.mycompany.managers.*;
import com.mycompany.auxiliary.*;
import com.mycompany.models.*;
import com.mycompany.gui.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> users;

    public UserManager()
    {
        users = new ArrayList<>();
    }

    public void loadUsers()
    {
        InputStream is = getClass().getResourceAsStream("/database/Users.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String username = parts[0];
                String password = parts[1];
                String role = parts[2];
                String department = parts[3];
                String filePath = parts[4];

                users.add(new User(username, password, role, department, filePath));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User authenticate(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && 
                u.getPassword().equals(password)) {
                return u;  // LOGIN SUCCESSFUL
            }
        }
        return null; // LOGIN FAILED
    }

    public List<User> getUsers()
    {
        return users;
    }
}
