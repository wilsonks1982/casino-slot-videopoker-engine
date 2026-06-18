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
 * **********************************************************************************************************************************************************************
 * */
package com.wildace.poker.jacksorbetter.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object for a playing card")
public class CardDto {
    private String rank;
    private String suit;

    public CardDto() {}
    public CardDto(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }
    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }
    public String getSuit() { return suit; }
    public void setSuit(String suit) { this.suit = suit; }
    
    @Override
    public String toString() {
    	//Take suit's first character and concatenate with rank
		return suit.toLowerCase().charAt(0) + rank;
	}
}