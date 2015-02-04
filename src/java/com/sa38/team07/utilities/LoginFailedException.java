/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sa38.team07.utilities;

/**
 *
 * @author gauri_000
 */
public class LoginFailedException extends Exception{
    
     public LoginFailedException(){
        super();
    }
    
    public LoginFailedException(String message){
        super(message);
    }
    
    public LoginFailedException(Throwable cause){
        super(cause);
    }
    
    public LoginFailedException(String message, Throwable cause){
        super(message, cause);
    }
    
    
}
