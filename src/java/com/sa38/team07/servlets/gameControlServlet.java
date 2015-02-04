/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sa38.team07.servlets;

import com.sa38.team07.model.*;
import com.sa38.team07.utilities.Constant;
import com.sa38.team07.utilities.RecordNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.sse.OutboundEvent;

/**
 *
 * @author gauri_000
 */
@WebServlet("/gameControl")
public class gameControlServlet extends HttpServlet {

    @EJB
    UnoGame unoGame;
    @Resource(mappedName = "concurrent/unoThreadpool")
    private ManagedScheduledExecutorService svc;
    @Resource(mappedName = "concurrent/unoThreadpool")
    private ManagedScheduledExecutorService mse;
    @Resource(mappedName = "concurrent/unoThreadpool")
    private ManagedScheduledExecutorService svc1;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String cmd = req.getParameter("cmd");
        switch (cmd) {
            case Constant.NEW_GAME: {

                int maxPlayers = Integer.parseInt(req.getParameter("maxPlayers"));
                String status = Constant.STATUS_WAITING;
                GameSession gameSession = unoGame.createGameSession(maxPlayers, status);
                JsonObjectBuilder gameJson = Json.createObjectBuilder();
                gameJson.add("gameID", gameSession.getGameID());
                res.setStatus(HttpServletResponse.SC_OK);
                res.setContentType("application/json");
                try (JsonWriter writer = Json.createWriter(res.getOutputStream());) {
                    writer.write(gameJson.build());
                    break;
                }

            }
            case Constant.DRAW_CARD: {
                /**
                 * get username from session
                 */
                String userName = req.getParameter("userName");
                String gameID = req.getParameter("gameID");
                try {
                    GameSession gameSession = unoGame.getGameSession(gameID);
                    Deck card = gameSession.drawCard(userName);
                    JsonObjectBuilder cardJson = Json.createObjectBuilder();
                    cardJson.add("cardID", card.getCardID())
                            .add("imageUrl", card.getImageUrl())
                            .add("cmd", "DrawCard");
                    res.setStatus(HttpServletResponse.SC_OK);
                    res.setContentType("application/json");
                    try (JsonWriter writer = Json.createWriter(res.getOutputStream());) {
                        writer.write(cardJson.build());
                    }
                } catch (RecordNotFoundException rex) {
                    System.out.println(rex.getMessage());
                }
                break;
            }
            case Constant.UNDO: {
                String gameID = req.getParameter("gameID");
                String userName = req.getParameter("userName");
                try {
                    GameSession gameSession = unoGame.getGameSession(gameID);
                    Deck card = gameSession.undoMove(userName);
                    if (!(card == null)) {
                        JsonObjectBuilder cardJson = Json.createObjectBuilder();
                        cardJson.add("cardID", card.getCardID())
                                .add("imageUrl", card.getImageUrl())
                                .add("cmd", "Undo");
                        res.setContentType("application/json");
                        try (JsonWriter writer = Json.createWriter(res.getOutputStream());) {
                            writer.write(cardJson.build());
                            
                            /*Creating outbound event for table*/
                            Deck topcard = gameSession.getDiscardPileCard();
                            JsonObject topCardJson = Json.createObjectBuilder()
                                    .add("cmd", "Undo")
                                    .add("cardID", topcard.getCardID())
                                    .add("imageUrl", topcard.getImageUrl())
                                    .add("who", userName).build();

                            OutboundEvent event = new OutboundEvent.Builder()
                                    .mediaType(MediaType.APPLICATION_JSON_TYPE)
                                    .name(gameID)
                                    .data(JsonObject.class, topCardJson)
                                    .build();
                            BroadcastTask t = new BroadcastTask(gameSession, event);
                            svc.schedule(t, 200, TimeUnit.MILLISECONDS);
                            res.setStatus(HttpServletResponse.SC_OK);
                        }
                    } 

                } catch (RecordNotFoundException rex) {
                    System.out.println("error");
                }
                break;
            }
            case Constant.UNO: {
                String gameID = req.getParameter("gameID");
                String userName = req.getParameter("userName");
                try {
                    GameSession gameSession = unoGame.getGameSession(gameID);
                    boolean uno = gameSession.declareUno(userName);
                    if (uno) {
                        JsonObject unoJson = Json.createObjectBuilder()
                                .add("cmd", "Uno")
                                .add("gameID", gameID)
                                .add("userName", userName)
                                .add("msg", "Player: " + userName + " clicked UNO!").build();

                        OutboundEvent event = new OutboundEvent.Builder()
                                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                                .name(gameID)
                                .data(JsonObject.class, unoJson)
                                .build();
                        BroadcastTask t = new BroadcastTask(gameSession, event);
                        mse.schedule(t, 200, TimeUnit.MILLISECONDS);
                        res.setStatus(HttpServletResponse.SC_OK);

                    } else {
                        JsonObjectBuilder unoInvalidJson = Json.createObjectBuilder();
                        unoInvalidJson.add("msg", "Oops!! Invalid move!");
                        res.setStatus(HttpServletResponse.SC_OK);
                        res.setContentType("application/json");
                        try (JsonWriter writer = Json.createWriter(res.getOutputStream());) {
                            writer.write(unoInvalidJson.build());
                        }
                    }
                } catch (RecordNotFoundException rex) {
                    System.out.println("error");
                }
                break;
            }
            case Constant.SELECT_WAITING_GAMES: {

                
                List<GameSession> gameResults = unoGame.getAllWaitingSessions();
                JsonArrayBuilder jsonGameArray = Json.createArrayBuilder();
                for (GameSession g : gameResults) {
                    JsonObjectBuilder gameJson = Json.createObjectBuilder();
                    gameJson.add("gameID", g.getGameID());
                    gameJson.add("maxPlayers", g.getMaxPlayers());
                    gameJson.add("currPlayers", g.getCurrPlayers());
                    gameJson.add("status", g.getStatus());
                    jsonGameArray.add(gameJson);
                }
                res.setStatus(HttpServletResponse.SC_OK);
                res.setContentType("application/json");

                try (JsonWriter writer = Json.createWriter(res.getOutputStream());) {
                    writer.writeArray(jsonGameArray.build());
                }
                break;
            }
            case Constant.GET_USER:{
                String userName = req.getRemoteUser();
                JsonObjectBuilder userJson = Json.createObjectBuilder();
                userJson.add("userName", userName);
                 res.setStatus(HttpServletResponse.SC_OK);
                res.setContentType("application/json");

                try (JsonWriter writer = Json.createWriter(res.getOutputStream());) {
                    writer.write(userJson.build());
                }
                break;

            }
            case Constant.GET_SCORE: {
              
                String gameID = req.getParameter("gameID");
                try {
                    GameSession gameSession = unoGame.getGameSession(gameID);
                    Map<String, Integer> scores = gameSession.calculateScore();
                    JsonArrayBuilder scoreList = Json.createArrayBuilder();

                    for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                        JsonObjectBuilder scoreJson = Json.createObjectBuilder();
                        String uName = entry.getKey();
                        int sc = entry.getValue();
                        scoreJson.add("userName", uName)
                                .add("score", sc);
                        scoreList.add(scoreJson);
                    }
                    JsonArray scoreArray = scoreList.build();
                    res.setStatus(HttpServletResponse.SC_OK);
                    res.setContentType("application/json");
                    try (JsonWriter writer = Json.createWriter(res.getOutputStream());) {
                        writer.writeArray(scoreArray);
                    }
                } catch (RecordNotFoundException rex) {
                    System.out.println(rex.getMessage());
                }
                break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String cmd = req.getParameter("cmd");
        switch (cmd) {
            case Constant.PLAY_CARD: {
                String imageUrl = req.getParameter("imageUrl");
                String userName = req.getParameter("userName");
                String gameID = req.getParameter("gameID");
                try {
                    GameSession gameSession = unoGame.getGameSession(gameID);
                    gameSession.playCard(userName, imageUrl);
                    Deck topcard = gameSession.getDiscardPileCard();
                    JsonObject topCardJson = Json.createObjectBuilder()
                            .add("cmd", "PlayCard")
                            .add("cardID", topcard.getCardID())
                            .add("imageUrl", topcard.getImageUrl())
                            .add("playedBy", topcard.getPlayedBy()).build();

                    OutboundEvent event = new OutboundEvent.Builder()
                            .mediaType(MediaType.APPLICATION_JSON_TYPE)
                            .name(gameID)
                            .data(JsonObject.class, topCardJson)
                            .build();

                    BroadcastTask t = new BroadcastTask(gameSession, event);
                    svc1.schedule(t, 200, TimeUnit.MILLISECONDS);
                    res.setStatus(HttpServletResponse.SC_ACCEPTED);

                    /**
                     * check if game over *
                     */
                    if (gameSession.gameOver()) {
                        gameSession.setStatus(Constant.STATUS_OVER);
                        gameSession.publishGameOver(gameID);
                           
                    }
                } catch (RecordNotFoundException rex) {
                    System.out.println("Record not found"+rex.getMessage());
                }
                break;
            }

        }

    }

}
