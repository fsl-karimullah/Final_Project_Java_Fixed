package finalproject.ui;


import java.sql.Connection;
import java.sql.DriverManager;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Faisal
 */

public class MySqlConnection {
    public static Connection getConnection() throws Exception{
        String dbroot = "jdbc:mysql://";
        String hostName = "localhost:3306/"; 
        String dbName = "simpeg";
        String dbUrl = dbroot + hostName + dbName;
        
        String hostUsername = "root";
        String hostPassword = "";
        
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = (Connection)DriverManager.getConnection(dbUrl, hostUsername, hostPassword);
        
        return con;
    }  
}
