/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany;

import com.mycompany.managers.*;
import com.mycompany.gui.*;

/**
 *
 * @author USER
 */
public class OOPDSAProject {

    public static void main(String[] args) {
        UserManager userManager = new UserManager();
        userManager.loadUsers();

        GUILogin loginForm = new GUILogin(userManager);
        loginForm.setVisible(true);
    }
}
