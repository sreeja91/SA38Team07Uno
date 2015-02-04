package com.sa38.team07.model;


import java.io.Serializable;

import java.lang.Integer;
import java.lang.String;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


public  class Cards implements Serializable {


    
    private String category;


    
    private String color;


    
    private String imageUrl;


    
    private Integer cardID;


  
    private Integer score;

    public Cards(){

    }


   public String getCategory() {
        return this.category;
    }


  public void setCategory (String category) {
        this.category = category;
    }



   public String getColor() {
        return this.color;
    }


  public void setColor (String color) {
        this.color = color;
    }



   public String getImageUrl() {
        return this.imageUrl;
    }


  public void setImageUrl (String imageUrl) {
        this.imageUrl = imageUrl;
    }



   public Integer getCardID() {
        return this.cardID;
    }


  public void setCardID (Integer cardID) {
        this.cardID = cardID;
    }



   public Integer getScore() {
        return this.score;
    }


  public void setScore (Integer score) {
        this.score = score;
    }

}

