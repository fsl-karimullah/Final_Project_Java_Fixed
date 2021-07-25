/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject.ui;

/**
 *
 * @author Faisal
 */
public class UserList {

    int UID;
    String Username, UserType, Nickname, Email;

    public UserList(int UID, String Username, String UserType, String Nickname, String Email) {
        this.UID = UID;
        this.Username = Username;
        this.UserType = UserType;
        this.Nickname = Nickname;
        this.Email = Email;
    }

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String UserType) {
        this.UserType = UserType;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String Nickname) {
        this.Nickname = Nickname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

}
