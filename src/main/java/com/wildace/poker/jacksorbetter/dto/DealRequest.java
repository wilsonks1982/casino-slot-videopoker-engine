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
 * |  02/07/2025   | Wilson Sam   |     Created     |  File Creation
 * |  03/07/2025   | Wilson Sam   |   Updated DTO   |  Added no-args and all-args constructors, equals, hashCode, toString
 * **********************************************************************************************************************************************************************
 * */
package com.wildace.poker.jacksorbetter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Schema(description = "Request body for performing a video poker deal")
public class DealRequest {

    @Schema(description = "Electronic Gaming Machine (EGM) identifier", example = "WAS-2001", required = true)
    private String egmId;

    @Schema(description = "User unique identifier (UID)", example = "53ce81df", required = true)
    private String uid;

    @Schema(
        description = "Number of hands to be dealt (must be 1-10)",
        allowableValues = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"},
        example = "5",
        required = true
    )
    private int numberOfHands;

    @Schema(
        description = "Coin selected for the deal (must be 1, 2, 3, 4, or 5)",
        allowableValues = {"1", "2", "3", "4", "5"},
        example = "1",
        required = true
    )
    private int coin;

    public DealRequest() {}

    public DealRequest(String egmId, String uid, int numberOfHands, int coin) {
        this.egmId = egmId;
        this.uid = uid;
        this.numberOfHands = numberOfHands;
        this.coin = coin;
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

    public int getNumberOfHands() {
        return numberOfHands;
    }

    public void setNumberOfHands(int numberOfHands) {
        this.numberOfHands = numberOfHands;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DealRequest)) return false;
        DealRequest that = (DealRequest) o;
        return numberOfHands == that.numberOfHands &&
                coin == that.coin &&
                Objects.equals(egmId, that.egmId) &&
                Objects.equals(uid, that.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(egmId, uid, numberOfHands, coin);
    }

    @Override
    public String toString() {
        return "DealRequest{" +
                "egmId='" + egmId + '\'' +
                ", uid='" + uid + '\'' +
                ", numberOfHands=" + numberOfHands +
                ", coin=" + coin +
                '}';
    }
}