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
public class BroadcastTask implements Runnable{
    
    private GameSession gameSession;
    private OutboundEvent evt;

    public BroadcastTask(GameSession gameSession, OutboundEvent evt) {
        this.gameSession = gameSession;
        this.evt = evt;
    }        

    @Override
    public void run() {   
        gameSession.publishToTable(evt);
    }    
}
