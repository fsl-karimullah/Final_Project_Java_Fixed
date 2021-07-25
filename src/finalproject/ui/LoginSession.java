package finalproject.ui;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Faisal
 */
public class LoginSession {
    public static int UID;
    public static String UserType;
    public static String Nickname;
    public static boolean isLoggedIn = false;

    public static int getUID() {
        return UID;
    }

    public static void setUID(int UID) {
        LoginSession.UID = UID;
    }

    public static String getUserType() {
        return UserType;
    }

    public static void setUserType(String UserType) {
        LoginSession.UserType = UserType;
    }

    public static String getNickname() {
        return Nickname;
    }

    public static void setNickname(String Nickname) {
        LoginSession.Nickname = Nickname;
    }

    public static boolean isIsLoggedIn() {
        return isLoggedIn;
    }

    public static void setIsLoggedIn(boolean isLoggedIn) {
        LoginSession.isLoggedIn = isLoggedIn;
    }
    
}
