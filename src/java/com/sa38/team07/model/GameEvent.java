/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sa38.team07.model;

import com.sa38.team07.utilities.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.SseFeature;

/**
 *
 * @author gauri_000
 */
@RequestScoped
@Path("uno")
public class GameEvent {

    @EJB
    UnoGame unoGame;
    GameSession gameSession;
  
    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    @Path("game/{gameID}")
    public EventOutput newGame(@PathParam("gameID") String gameID, @QueryParam("playerName") String name) {
        System.out.println("Connection from--->" + name);
        EventOutput eo = new EventOutput();

        try {
            gameSession = unoGame.getGameSession(gameID);
        } catch (RecordNotFoundException rex) {
            System.out.println(rex.getMessage());
        }
        if (name.equals("Table")) {
            Table t = new Table(eo);
            t.setUsername(name);
            gameSession.add(t);
           
        } else {
            Participant p = new Participant(eo);
            p.setUsername(name);
            gameSession.add(p);

        }
        if (gameSession.canStart()) {
            gameSession.setStatus(Constant.STATUS_ON);
            gameSession.shuffle();
            gameSession.deal();
            gameSession.publishGameSetup(gameSession.getGameID()); 
          }
        return (eo);
    }

}
