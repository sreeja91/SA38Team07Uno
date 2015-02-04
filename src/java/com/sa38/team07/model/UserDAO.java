package com.sa38.team07.model;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class UserDAO {
    
   public String addUser(String username,String password,String email) throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException
    {
       String result="";
    Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sa38team07uno?zeroDateTimeBehavior=convertToNull","root","amogh0409");
    
                
        String sql = "INSERT INTO logintable (username,password,email) VALUES (?, ?, ?)";
 
        PreparedStatement statement = cn.prepareStatement(sql);
        statement.setString(1,username);
        
        //password encryption
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        String text = password;

        md.update(text.getBytes("UTF-8")); // Change this to "UTF-16" if needed

        byte[] digest = md.digest();

        BigInteger bigInt = new BigInteger(1, digest);

        String output = bigInt.toString(16);
      
        statement.setString(2,output);
        statement.setString(3,email);
        
        String sql1="INSERT INTO grouptable(username,groupid) VALUES (?,?)";
        PreparedStatement statement1=cn.prepareStatement(sql1);
        statement1.setString(1,username);
        statement1.setString(2,"player");
      
        int rowsInserted = statement.executeUpdate();
        statement1.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("A new user was inserted successfully!");
        }
    
        return result;
    }
}
