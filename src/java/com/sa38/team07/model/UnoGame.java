/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sa38.team07.model;

import com.sa38.team07.utilities.*;
import com.sa38.team07.utilities.Constant;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author gauri_000
 */
@Singleton
public class UnoGame {

    Connection conn;
    /**get list of cards from db**/
         
    public List<Cards> selectAllCards() {
        List<Cards> cList = new ArrayList<>();
        String sql = "SELECT * FROM cards";
  
        try {
             conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sa38team07uno?zeroDateTimeBehavior=convertToNull", "root", "amogh0409");
            Statement smt = conn.createStatement();
            ResultSet rs = smt.executeQuery(sql);
            while(rs.next()){
                Cards card = new Cards();
                card.setCardID(rs.getInt("cardID"));
                card.setImageUrl(rs.getString("imageUrl"));
                card.setCategory(rs.getString("category"));
                card.setColor(rs.getString("color"));
                card.setScore(rs.getInt("score"));
                cList.add(card);
               }
           
        } catch (SQLException ex) {
          System.out.println(ex.getMessage());
        }finally{
            System.out.println("closing connection");
            try{
            conn.close();
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        
         return cList;
    }

    private final Map<String, GameSession> games = new HashMap<>();

    public UnoGame() {
    };

    /**
     * Create new Game Session*
     */
    @Lock(LockType.WRITE)
    public GameSession createGameSession(int maxPlayers, String status) {
        List<Deck> deck = initializeDeck();
        String gameID = UUID.randomUUID().toString();
        GameSession gameSession = new GameSession(gameID, maxPlayers, status, deck);
        games.put(gameID, gameSession);
        return gameSession;
    }

    /**
     * Get Game Session*
     */
    @Lock(LockType.READ)
    public GameSession getGameSession(String gameID) throws RecordNotFoundException {
        GameSession gameSession = games.get(gameID);
        if (gameSession == null) {
            throw new RecordNotFoundException(Constant.EXCEPTION_RECORD_NOT_FOUND);
        }
        return gameSession;
    }

    /**
     * Update the Game Status *
     */
    @Lock(LockType.WRITE)
    public void updateGameSessionStatus(String gameID, String status) {
        for (GameSession g : games.values()) {
            if (g.getGameID().equals(gameID)) {
                g.setStatus(status);
            }
        }
    }

    /**
     * Get list of all waiting gameSessions *
     */
    @Lock(LockType.READ)
    public List<GameSession> getAllWaitingSessions() {
        List<GameSession> gameList = new ArrayList<>();
        for (GameSession g : games.values()) {
            if (g.getStatus().equals(Constant.STATUS_WAITING)) {
                gameList.add(g);
            }
        }
        return gameList;
    }

    /**
     * Initialize the Deck*
     */
    private List<Deck> initializeDeck() {
        List<Deck> deck = new ArrayList<>();
        List<Cards> cList = selectAllCards();
        for (Cards c : cList) {
            Deck dc = new Deck();
            dc.setCardID(c.getCardID());
            dc.setImageUrl(c.getImageUrl());
            dc.setCategory(c.getCategory());
            dc.setColor(c.getColor());
            dc.setScore(c.getScore());
            dc.setOwner(Constant.DRAW_PILE);
            dc.setPlayedBy("");
            deck.add(dc);

        }
        return deck;
    }

}
