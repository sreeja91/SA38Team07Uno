/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sa38.team07.model;

/**
 *
 * @author gauri_000
 */
public class Deck extends Cards {

    private String owner;
    private String playedBy;
    
    public Deck(){};

   public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPlayedBy() {
        return playedBy;
    }

    public void setPlayedBy(String playedBy) {
        this.playedBy = playedBy;
    }


}
