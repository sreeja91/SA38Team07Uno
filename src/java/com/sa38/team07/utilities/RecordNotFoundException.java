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
public class RecordNotFoundException extends Exception{
    
    public RecordNotFoundException(){
        super();
    }
    
    public RecordNotFoundException(String message){
        super(message);
    }
    
    public RecordNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
    
    public RecordNotFoundException(Throwable cause){
        super(cause);
    }
    
    
    
}
