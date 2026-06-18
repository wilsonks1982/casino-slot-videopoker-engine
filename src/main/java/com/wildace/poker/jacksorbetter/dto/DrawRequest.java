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

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request for drawing cards for all hands in a Jacks or Better game session.
 */
@Schema(description = "Request for drawing cards for all hands in a Jacks or Better game session. " +
        "The hold positions are applied to all hands based on the primary hand's card indexes.")
public class DrawRequest {

    @Schema(
        description = "Session ID as returned by the deal API.",
        example = "c1dd8ac1-1d8c-4f61-95b2-cccd0e8c9e64",
        required = true
    )
    private String sessionId;

    @Schema(
        description = "List of zero-based card indexes (0-4) to hold in the primary hand. " +
            "These indexes will be held in all hands; other cards will be replaced with new draws.",
        example = "[0, 2, 4]",
        required = true
    )
    private List<Integer> holds;

    public DrawRequest() {}

    public DrawRequest(String sessionId, List<Integer> holds) {
        this.sessionId = sessionId;
        this.holds = holds;
    }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public List<Integer> getHolds() { return holds; }
    public void setHolds(List<Integer> holds) { this.holds = holds; }
    
    @Override
    public String toString() {
		return "DrawRequest{" +
				"sessionId='" + sessionId + '\'' +
				", holds=" + holds +
				'}';
	}
}