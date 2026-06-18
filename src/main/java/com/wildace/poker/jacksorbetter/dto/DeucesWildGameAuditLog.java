/*
 * Copyright 2024 Wildace Private Limited - All Rights Reserved
 *
 * Licensed under Wildace Software License Agreement ("License").
 * You may not use this file except in compliance with the License.
 *
 * NOTICE
 * ALL INFORMATION CONTAINED HEREIN IS, AND REMAINS THE PROPERTY OF WILDACE PRIVATE LIMITED.
 * THE INTELLECTUAL AND TECHNICAL CONCEPTS CONTAINED HEREIN ARE PROPRIETARY TO WILDACE PRIVATE LIMITED AND ARE PROTECTED BY TRADE SECRET OR COPYRIGHT LAW.
 * DISSEMINATION OF THIS INFORMATION OR REPRODUCTION OF THIS MATERIAL IS STRICTLY FORBIDDEN UNLESS PRIOR WRITTEN PERMISSION IS OBTAINED FROM WILDACE PRIVATE LIMITED.
 * **********************************************************************************************************************************************************************
 * Change History
 * **********************************************************************************************************************************************************************
 * |     Date      |     Name     |      Change     |      Details
 * |  28/07/2025   | Wilson Sam   |     Created     |  File Creation
 * |  30/07/2025   | Wilson Sam   |     Updated      |  Added new fields for Deuces Wild game audit log
 * **********************************************************************************************************************************************************************
 * */
package com.wildace.poker.jacksorbetter.dto;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "poker_dw_results")
public class DeucesWildGameAuditLog {
    @Id
    private String id;
    private String sessionId;
    private String userId;
    private String egmId;
    private String action; // "deal" or "draw"
    private String gameStart;
    private String gameEnd;
    private List<List<CardDto>> hands;
    private List<List<CardDto>> decksBefore;
    private List<List<CardDto>> decksAfter;
    private List<Integer> holds;         // For draw
    private List<Integer> payouts;       // For draw
    private List<String> results;        // For results of the game
    private double betAmount;
    private double winAmount;
    private double oldCredit;
    private double newCredit;
    private int coin;
    private int numberOfHands;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEgmId() { return egmId; }
    public void setEgmId(String egmId) { this.egmId = egmId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getGameStart() { return gameStart; }	
    public void setGameStart(String gameStart) { this.gameStart = gameStart; }
    
    public String getGameEnd() { return gameEnd; }
    public void setGameEnd(String gameEnd) { this.gameEnd = gameEnd; }
    
    public List<List<CardDto>> getHands() { return hands; }
    public void setHands(List<List<CardDto>> hands) { this.hands = hands; }
    
    public List<List<CardDto>> getDecksBefore() { return decksBefore; }
    public void setDecksBefore(List<List<CardDto>> decksBefore) { this.decksBefore = decksBefore; }
    
    public List<List<CardDto>> getDecksAfter() { return decksAfter; }
    public void setDecksAfter(List<List<CardDto>> decksAfter) { this.decksAfter = decksAfter; }

    public List<Integer> getHolds() { return holds; }
    public void setHolds(List<Integer> holds) { this.holds = holds; }

    public List<Integer> getPayouts() { return payouts; }
    public void setPayouts(List<Integer> payouts) { this.payouts = payouts; }

    public List<String> getResults() { return results; }
    public void setResults(List<String> results) { this.results = results; }
    
    public double getBetAmount() { return betAmount; }
    public void setBetAmount(double betAmount) { this.betAmount = betAmount; }

    public double getWinAmount() { return winAmount; }
    public void setWinAmount(double winAmount) { this.winAmount = winAmount; }

    public double getOldCredit() { return oldCredit; }
    public void setOldCredit(double oldCredit) { this.oldCredit = oldCredit; }

    public double getNewCredit() { return newCredit; }
    public void setNewCredit(double newCredit) { this.newCredit = newCredit; }

    public int getCoin() { return coin; }
    public void setCoin(int coin) { this.coin = coin; }

    public int getNumberOfHands() { return numberOfHands; }
    public void setNumberOfHands(int numberOfHands) { this.numberOfHands = numberOfHands; }
}