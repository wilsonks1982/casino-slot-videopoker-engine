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
 * |  01/07/2025   | Wilson Sam   |     Created     |  File Creation
 * |  03/07/2025   | Wilson Sam   |   Updated DTO   |  Support for multi-hand, coin, and numberOfHands
 * |  03/07/2025   | Wilson Sam   |   Extended DTO  |  Added id, egmId, uid, wallet, statusCode, msg, ok
 * **********************************************************************************************************************************************************************
 * */
package com.wildace.poker.jacksorbetter.dto;

import java.util.List;

public class DealResponse {
    private String id;
    private String egmId;
    private String uid;
    private String sessionId;
    private List<List<String>> hands;
    private int coin;
    private int numberOfHands;
    private double wallet;
    private int statusCode;
    private String msg;
    private int ok;

    public DealResponse() {}

    public DealResponse(String id, String egmId, String uid, String sessionId, List<List<String>> hands,
                        int coin, int numberOfHands, double wallet, int statusCode, String msg, int ok) {
        this.id = id;
        this.egmId = egmId;
        this.uid = uid;
        this.sessionId = sessionId;
        this.hands = hands;
        this.coin = coin;
        this.numberOfHands = numberOfHands;
        this.wallet = wallet;
        this.statusCode = statusCode;
        this.msg = msg;
        this.ok = ok;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEgmId() {
        return egmId;
    }

    public void setEgmId(String egmId) {
        this.egmId = egmId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<List<String>> getHands() {
        return hands;
    }

    public void setHands(List<List<String>> hands) {
        this.hands = hands;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getNumberOfHands() {
        return numberOfHands;
    }

    public void setNumberOfHands(int numberOfHands) {
        this.numberOfHands = numberOfHands;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getOk() {
        return ok;
    }

    public void setOk(int ok) {
        this.ok = ok;
    }
    
    @Override
    public String toString() {
		return "DealResponse{" +
				"id='" + id + '\'' +
				", egmId='" + egmId + '\'' +
				", uid='" + uid + '\'' +
				", sessionId='" + sessionId + '\'' +
				", hands=" + hands +
				", coin=" + coin +
				", numberOfHands=" + numberOfHands +
				", wallet=" + wallet +
				", statusCode=" + statusCode +
				", msg='" + msg + '\'' +
				", ok=" + ok +
				'}';
	}
}