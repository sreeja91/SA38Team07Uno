/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sa38.team07.model;

import org.glassfish.jersey.media.sse.EventOutput;

/**
 *
 * @author gauri_000
 */
public class Participant {
    
    private String username;
    private EventOutput eo;
        
    public Participant(EventOutput eo) {
        this.eo = eo;
    }
    
    public String getUsername() {
        return username;
    }

     public void setUsername(String username) {
        this.username = username;
    }

    
    public EventOutput getEo() {
        return eo;
    }

    
    public void setEo(EventOutput eo) {
        this.eo = eo;
    }
    
   
}
