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
public class DeleteFailedException extends Exception {
     
    public DeleteFailedException(){
        super();
    }
    
    public DeleteFailedException(String message){
        super(message);
    }
    
    public DeleteFailedException(String message, Throwable cause){
        super(message, cause);
    }
    
    public DeleteFailedException(Throwable cause){
        super(cause);
    }
    
    
}
