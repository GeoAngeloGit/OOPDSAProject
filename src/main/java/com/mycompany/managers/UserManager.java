package com.mycompany.managers;

import com.mycompany.managers.*;
import com.mycompany.auxiliary.*;
import com.mycompany.models.*;
import com.mycompany.gui.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class UserManager {
    //list of users from Users model class
    private List<User> users;

    //default constructor
    public UserManager()
    {
        users = new ArrayList<>();
    }

    //load users from the Users.txt file containing all the data about the user
    public void loadUsers()
    {
        //set the filepath of the CSV txt file to InputStream
        InputStream is = getClass().getResourceAsStream("/database/Users.txt");
        //read the file using BufferedReader
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            //split each line by "," to store each field attribute to a variable
            while((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String username = parts[0];
                String password = parts[1];
                String role = parts[2];
                String department = parts[3];
                String filePath = parts[4];

                String status = null;
                if (parts.length >= 6) {
                    status = parts[5];
                }

                //add, to call the User Construcutor
                users.add(new User(username, password, role, department, filePath, status));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //authenticate the user using the username and its password
    public User authenticate(String username, String password) {
        //User u will contain the pair or username and password
        for (User u : users) {
            if (u.getUsername().equals(username) &&
                u.getPassword().equals(password)) 
            {
                
                // BLOCK DISABLED ACCOUNTS
                if (u.getStatus().equals("DISABLED")) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Your account has been disabled.\nPlease contact your department head.",
                        "Account Disabled",
                        JOptionPane.ERROR_MESSAGE
                    );
                return null;
            }
                return u;  // LOGIN SUCCESSFUL
            }
        }
        return null; // LOGIN FAILED
    }

    //get the list of users
    public List<User> getUsers()
    {
        return users;
    }

    public void setUserStatus(String username, boolean active) throws IOException {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                u.setStatus(active ? "ACTIVE" : "DISABLED");
                break;
            }
        }
        saveUsers(); // persist change
    }
    public void saveUsers() throws IOException {
        File file = new File("src/main/resources/database/Users.txt");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (User u : users) {
                bw.write(String.join(",",
                    u.getUsername(),
                    u.getPassword(),
                    u.getRole(),
                    u.getDepartment(),
                    u.getFilePath(),
                    u.getStatus()
                ));
                bw.newLine();
            }
        }
    }


}
