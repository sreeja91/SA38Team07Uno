package com.sa38.team07.model;


import java.io.Serializable;

import java.lang.String;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


public  class Logintable implements Serializable {


    
  
    private String username;


    
  
    private String email;


   
  
    private String password;


   
    private Collection<Grouptable> grouptableCollection;

    public Logintable(){

    }


   public String getUsername() {
        return this.username;
    }


  public void setUsername (String username) {
        this.username = username;
    }



   public String getEmail() {
        return this.email;
    }


  public void setEmail (String email) {
        this.email = email;
    }



   public String getPassword() {
        return this.password;
    }


  public void setPassword (String password) {
        this.password = password;
    }



   public Collection<Grouptable> getGrouptableCollection() {
        return this.grouptableCollection;
    }


  public void setGrouptableCollection (Collection<Grouptable> grouptableCollection) {
        this.grouptableCollection = grouptableCollection;
    }

}

