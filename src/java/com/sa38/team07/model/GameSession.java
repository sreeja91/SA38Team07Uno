/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sa38.team07.model;

import com.sa38.team07.utilities.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import javax.annotation.*;
import javax.ejb.*;
import javax.enterprise.context.*;
import javax.json.Json;
import javax.json.*;
import javax.persistence.*;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.sse.OutboundEvent;

/**
 *
 * @author gauri_000
 */
public class GameSession {

    private String gameID;
    private int maxPlayers;
    private int currPlayers;
    private String status;
    private Integer index = 0;

    Map<String, Participant> participantsMap = new HashMap<>();
    List<Deck> deck = new ArrayList<>();
    Deque<Deck> discardPile = new ArrayDeque<Deck>();
    Deque<Deck> drawPile = new ArrayDeque<Deck>();
    List<Deck> playersHand = new ArrayList<>();
    Map<Participant, OutboundEvent> eventMap = new HashMap<>();
    Map<String, Integer> scoreMap = new HashMap<>();
    Deck topcard;

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCurrPlayers() {
        return currPlayers;
    }

    public void setCurrPlayers(int currPlayers) {
        this.currPlayers = currPlayers;
    }

    /**
     * Constructor *
     */
    public GameSession(String gameID, int maxPlayers, String status, List<Deck> deck) {
        this.gameID = gameID;
        this.maxPlayers = maxPlayers;
        this.currPlayers = 0;
        this.status = status;
        this.deck = deck;
    }

    /**
     * Add Participants to the List*
     */
    @Lock(LockType.WRITE)
    public void add(Participant p) {
        participantsMap.put(p.getUsername(), p);
        if (!(p.getUsername().equals("Table"))) {
            setCurrPlayers(getCurrPlayers() + 1);
            /**
             * display joined player on table*
             */
            JsonObject joinJason = Json.createObjectBuilder()
                    .add("cmd", "PlayerJoin")
                    .add("userName", p.getUsername()).build();
            OutboundEvent evt = new OutboundEvent.Builder()
                    .mediaType(MediaType.APPLICATION_JSON_TYPE)
                    .data(JsonObject.class, joinJason)
                    .name(gameID)
                    .build();
            publishToTable(evt);
        }
        System.out.println("Added" + p.getUsername());

    }

    /**
     * Can Start*
     */
    public boolean canStart() {
        boolean result;
        System.out.println(participantsMap.size());
        if ((participantsMap.size() - 1) == maxPlayers) {
            System.out.println("true");
            result = true;
        } else {
            result = false;
        }
        return result;

    }

    /**
     * Shuffle deck*
     */
    public void shuffle() {
        Collections.shuffle(deck);
    }

    /**
     * Deal* Changing Ownership of the cards in the deck
     */
    public void deal() {
        for (Participant p : participantsMap.values()) {
            if (!(p.getUsername().equals(Constant.TABLE))) {
                for (int j = 0; j < 7; j++) {
                    deck.get(index).setOwner(p.getUsername());
                    index++;
                }
            }
        }
        deck.get(index).setOwner(Constant.DISCARD_PILE);

        for (Deck d : deck) {

            switch (d.getOwner()) {

                case Constant.DISCARD_PILE: {
                    discardPile.push(d);
                    break;
                }
                case Constant.DRAW_PILE: {
                    drawPile.push(d);
                    break;
                }
                default: {
                    playersHand.add(d);
                    System.out.println(d.getCardID() + d.getOwner());
                    break;
                }
            }
        }
    }

    /**
     * Build outbound event for table*
     */
    public void createTableSetupEvent(String gameID) {
        topcard = drawPile.pop();
        JsonObject tableJason = Json.createObjectBuilder()
                .add("cmd", "setupTable")
                .add("cardID", topcard.getCardID())
                .add("imageUrl", topcard.getImageUrl()).build();

        OutboundEvent evt = new OutboundEvent.Builder()
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(JsonObject.class, tableJason)
                .name(gameID)
                .build();
        eventMap.put(participantsMap.get("Table"), evt);
    }

    /**
     * Create outbound event for Players *
     */
    public void createPlayerSetupEvent(String GameID) {

        for (Participant p : participantsMap.values()) {

            if (!(p.getUsername().equals("Table"))) {
                JsonArrayBuilder imgList = Json.createArrayBuilder();

                for (Deck d : playersHand) {
                    if (p.getUsername().equals(d.getOwner())) {
                        JsonObjectBuilder imageJson = Json.createObjectBuilder();
                        imageJson.add("cardID", d.getCardID())
                                .add("imageUrl", d.getImageUrl());
                        imgList.add(imageJson);
                    }
                }
                JsonArray imageList = imgList.build();
                OutboundEvent evt = new OutboundEvent.Builder()
                        .mediaType(MediaType.APPLICATION_JSON_TYPE)
                        .name(gameID)
                        .data(JsonArray.class, imageList)
                        .build();
                eventMap.put(p, evt);
            }

        }
    }

    /**
     * Publish the Deal to client *
     */
    @Lock(LockType.READ)
    public void publishGameSetup(String gameID) {
        createTableSetupEvent(gameID);
        createPlayerSetupEvent(gameID);

        for (Map.Entry<Participant, OutboundEvent> entry : eventMap.entrySet()) {
            Participant p = entry.getKey();
            OutboundEvent event = entry.getValue();

            try {
                p.getEo().write(event);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * Play Card *
     */
    public void playCard(String userName, String imageUrl) {
        Iterator<Deck> i = playersHand.iterator();
        while (i.hasNext()) {
            Deck c = i.next();
            if ((c.getImageUrl().equals(imageUrl)) && (c.getOwner().equals(userName))) {
                c.setOwner(Constant.DISCARD_PILE);
                c.setPlayedBy(userName);
                discardPile.push(c);
                i.remove();
            }
        }

    }

    /**
     * Get top card from discard pile*
     */
    public Deck getDiscardPileCard() {
        Deck topcard = discardPile.peek();
        return topcard;
    }

    /**
     * publish the output to table *
     */
    public void publishToTable(OutboundEvent event) {
        Participant p = participantsMap.get("Table");
        try {
            p.getEo().write(event);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Draw card from Deck *
     */
    public Deck drawCard(String userName) {
        Deck topcard = drawPile.pop();
        topcard.setOwner(userName);
        playersHand.add(topcard);
        return topcard;
    }

    /**
     * undo move *
     */
    public Deck undoMove(String userName) {
        Deck topcard = discardPile.peek();
        if (topcard.getPlayedBy().equals(userName)) {
            topcard = discardPile.pop();
            topcard.setOwner(userName);
            topcard.setPlayedBy("");
            playersHand.add(topcard);
            return topcard;
        } else {
            return null;
        }
    }

    /**
     * BroadCast UNO *
     *
     */
    public boolean declareUno(String userName) {
        int count = 0;
        boolean result;
        for (Deck card : playersHand) {
            if (card.getOwner().equals(userName)) {
                count++;
            }
        }
        if (count == 1) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    /**
     * Check if game over *
     */
    public boolean gameOver() {
        boolean result = false;
        for (Participant p : participantsMap.values()) {
            int count = 0;
            if (!(p.getUsername().equals("Table"))) {
                for (Deck d : playersHand) {
                    if (d.getOwner().equals(p.getUsername())) {
                        count++;
                    }
                }
                if (count == 0) {
                    result = true;
                    break;
                }

            }
        }
        return result;
    }

    /**
     * calculate Score*
     */
    public Map<String, Integer> calculateScore() {
        for (Participant p : participantsMap.values()) {
            int score = 0;
            if (!(p.getUsername().equals("Table"))) {
                for (Deck d : playersHand) {
                    if (!(d.getOwner().equals(p.getUsername()))) {
                        score += d.getScore();
                    }
                }
                scoreMap.put(p.getUsername(), score);
            }
        }
        return scoreMap;
    }

    public void publishGameOver(String gameID) {
        JsonObject overJson = Json.createObjectBuilder()
                .add("cmd", "GameOver")
                .add("msg", "Game Over!!").build();

        OutboundEvent evt = new OutboundEvent.Builder()
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .name(gameID)
                .data(JsonObject.class, overJson)
                .build();
        /**
         * write outbound event to participants*
         */
        for (Participant p : participantsMap.values()) {
            try {
                p.getEo().write(evt);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }
}
