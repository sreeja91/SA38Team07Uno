/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sa38.team07.model;

import org.glassfish.jersey.media.sse.OutboundEvent;

/**
 *
 * @author gauri_000
 */
public class GameSetupTask implements Runnable{
    private GameSession gameSession;
   

    public GameSetupTask(GameSession gameSession) {
        this.gameSession = gameSession;
        
    }        

    @Override
    public void run() {   
        gameSession.publishGameSetup(gameSession.getGameID());
    }    
    
}
