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
 * |     Date      |     Name     |          Change          |      Details
 * |  03/07/2025   | Wilson Sam   | Multi-hand Draw Response |  DTO for multi-hand draw result, extended with meta fields and Swagger annotations
 * |  30/07/2025   | Wilson Sam   |  Draw Response           |  Added results, payouts, and holds for each hand
 * **********************************************************************************************************************************************************************
 * */
package com.wildace.poker.jacksorbetter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class MultiHandDrawResultResponse {
    @Schema(description = "Unique result id", example = "b3d2f1ab-ec44-47d9-8a42-bb24c4cfa3e1")
    private String id;

    @Schema(description = "Electronic Gaming Machine (EGM) identifier", example = "WAS-2001")
    private String egmId;

    @Schema(description = "User unique identifier (UID)", example = "53ce81df")
    private String uid;

    @Schema(description = "Session id for the game", example = "e2e1c4a4-9c6e-4fba-a9c1-2e5c4d3a2f1b")
    private String sessionId;

    @Schema(description = "Coin selected for the deal (must be 1, 2, 3, 4, or 5)", example = "1")
    private int coin;

    @Schema(description = "Number of hands dealt (must be 1-10)", example = "5")
    private int numberOfHands;

    @Schema(description = "Current wallet value after game", example = "3000.0")
    private double wallet;

    @Schema(description = "Status code of the response", example = "200")
    private int statusCode;

    @Schema(description = "Response message", example = "Draw successful")
    private String msg;

    @Schema(description = "OK flag (1 for success, 0 for failure)", example = "1")
    private int ok;

    @Schema(description = "All hands after draw. Each hand is a list of 5 cards.")
    private List<List<String>> hands;

    @Schema(description = "Holds for each hand (1 for hold, 0 for discard). Each list corresponds to a hand.")
    private List<Integer> holds;         // For draw
    
    @Schema(description = "Poker result for each hand (e.g., Pair, Three of a Kind, etc.)")
    private List<String> results;

    @Schema(description = "Payout for each hand according to result in order")
    private List<Integer> payouts;

    @Schema(description = "Total amount bet", example = "500.0")
    private double betAmount;

    @Schema(description = "Total amount won", example = "1000.0")
    private double winAmount;

    @Schema(description = "User's credit before the game", example = "2500.0")
    private double oldCredit;

    @Schema(description = "User's credit after the game", example = "3000.0")
    private double newCredit;

    @Schema(description = "Game start timestamp (yyyy-MM-dd HH:mm:ss)", example = "2025-06-03 13:30:00")
    private String gameStart;

    @Schema(description = "Game end timestamp (yyyy-MM-dd HH:mm:ss)", example = "2025-06-03 13:30:05")
    private String gameEnd;

    public MultiHandDrawResultResponse() {}

    public MultiHandDrawResultResponse(
            String id,
            String egmId,
            String uid,
            String sessionId,
            int coin,
            int numberOfHands,
            double wallet,
            int statusCode,
            String msg,
            int ok,
            List<List<String>> hands,
            List<String> results,
            List<Integer> payouts,
            List<Integer> holds,
            double betAmount,
            double winAmount,
            double oldCredit,
            double newCredit,
            String gameStart,
            String gameEnd
    ) {
        this.id = id;
        this.egmId = egmId;
        this.uid = uid;
        this.sessionId = sessionId;
        this.coin = coin;
        this.numberOfHands = numberOfHands;
        this.wallet = wallet;
        this.statusCode = statusCode;
        this.msg = msg;
        this.ok = ok;
        this.hands = hands;
        this.results = results;
        this.payouts = payouts;
        this.holds = holds;
        this.betAmount = betAmount;
        this.winAmount = winAmount;
        this.oldCredit = oldCredit;
        this.newCredit = newCredit;
        this.gameStart = gameStart;
        this.gameEnd = gameEnd;
    }

    // Getters and setters for all fields

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEgmId() { return egmId; }
    public void setEgmId(String egmId) { this.egmId = egmId; }
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public int getCoin() { return coin; }
    public void setCoin(int coin) { this.coin = coin; }
    public int getNumberOfHands() { return numberOfHands; }
    public void setNumberOfHands(int numberOfHands) { this.numberOfHands = numberOfHands; }
    public double getWallet() { return wallet; }
    public void setWallet(double wallet) { this.wallet = wallet; }
    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public int getOk() { return ok; }
    public void setOk(int ok) { this.ok = ok; }
    public List<List<String>> getHands() { return hands; }
    public void setHands(List<List<String>> hands) { this.hands = hands; }
    public List<String> getResults() { return results; }
    public void setResults(List<String> results) { this.results = results; }
    public List<Integer> getPayouts() { return payouts; }
    public void setPayouts(List<Integer> payouts) { this.payouts = payouts; }
    public List<Integer> getHolds() { return holds; }
    public void setHolds(List<Integer> holds) { this.holds = holds; }
    public double getBetAmount() { return betAmount; }
    public void setBetAmount(double betAmount) { this.betAmount = betAmount; }
    public double getWinAmount() { return winAmount; }
    public void setWinAmount(double winAmount) { this.winAmount = winAmount; }
    public double getOldCredit() { return oldCredit; }
    public void setOldCredit(double oldCredit) { this.oldCredit = oldCredit; }
    public double getNewCredit() { return newCredit; }
    public void setNewCredit(double newCredit) { this.newCredit = newCredit; }
    public String getGameStart() { return gameStart; }
    public void setGameStart(String gameStart) { this.gameStart = gameStart; }
    public String getGameEnd() { return gameEnd; }
    public void setGameEnd(String gameEnd) { this.gameEnd = gameEnd; }
    
    @Override
    public String toString() {
		return "MultiHandDrawResultResponse{" +
				"id='" + id + '\'' +
				", egmId='" + egmId + '\'' +
				", uid='" + uid + '\'' +
				", sessionId='" + sessionId + '\'' +
				", coin=" + coin +
				", numberOfHands=" + numberOfHands +
				", wallet=" + wallet +
				", statusCode=" + statusCode +
				", msg='" + msg + '\'' +
				", ok=" + ok +
				", hands=" + hands +
				", results=" + results +
				", payouts=" + payouts +
				", holds=" + holds +
				", betAmount=" + betAmount +
				", winAmount=" + winAmount +
				", oldCredit=" + oldCredit +
				", newCredit=" + newCredit +
				", gameStart='" + gameStart + '\'' +
				", gameEnd='" + gameEnd + '\'' +
				'}';
	}
}