package finalproject.ui;


import finalproject.ui.Login;
import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Faisal
 */
public class Logout {
    public static void logOut(JFrame context, Login loginScreen){
        LoginSession.isLoggedIn = false;
        context.setVisible(false);
        loginScreen.setVisible(true);
    }
}
