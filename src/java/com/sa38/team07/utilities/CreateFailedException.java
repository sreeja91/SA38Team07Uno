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
public class CreateFailedException extends Exception{
    
    public CreateFailedException(){
        super();
    }
    
    public CreateFailedException(String message){
        super(message);
    }
    
    public CreateFailedException(Throwable cause){
        super(cause);
    }
    
    public CreateFailedException(String message, Throwable cause){
        super(message, cause);
    }
    
    
}
