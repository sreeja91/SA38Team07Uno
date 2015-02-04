package com.sa38.team07.model;


import java.io.Serializable;

import java.lang.String;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


public  class Grouptable implements Serializable {
   
    
    
    private String username;


    
    private String groupid;

    
    
    
    private Logintable logintable;

    public Grouptable(){

    }


   public String getUsername() {
        return this.username;
    }


  public void setUsername (String username) {
        this.username = username;
    }



   public String getGroupid() {
        return this.groupid;
    }


  public void setGroupid (String groupid) {
        this.groupid = groupid;
    }



   public Logintable getLogintable() {
        return this.logintable;
    }


  public void setLogintable (Logintable logintable) {
        this.logintable = logintable;
    }

   
}


  