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
public class UpdateFailedException extends Exception{
    
   
    public UpdateFailedException(){
        super();
    }
    
    public UpdateFailedException(String message){
        super(message);
    }
    
    public UpdateFailedException(String message, Throwable cause){
        super(message, cause);
    }
    
    public UpdateFailedException(Throwable cause){
        super(cause);
    }
}

    
