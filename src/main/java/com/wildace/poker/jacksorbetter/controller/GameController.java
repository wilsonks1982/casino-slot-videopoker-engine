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
 * |  03/07/2025   | Wilson Sam   |   Updated Logic |  Multi-hand support & extended DTOs for deal/draw
 * |  08/07/2025   | Wilson Sam   |   Documentation |  Swagger Documentation for clarity, maintainability, and REST best practices
 * |  08/07/2025   | Wilson Sam   |   Updated       |  Added game results endpoints
 * **********************************************************************************************************************************************************************
 * */
package com.wildace.poker.jacksorbetter.controller;

import com.wildace.poker.jacksorbetter.dto.DealRequest;
import com.wildace.poker.jacksorbetter.dto.DealResponse;
import com.wildace.poker.jacksorbetter.dto.DrawRequest;
import com.wildace.poker.jacksorbetter.dto.MultiHandDrawResultResponse;
import com.wildace.poker.jacksorbetter.dto.GameResultResponse;
import com.wildace.poker.jacksorbetter.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(
    name = "Jacks or Better Game API",
    description = "REST endpoints for Jacks or Better Video Poker game. Includes deal, draw and result retrieval operations."
)
@RestController
@RequestMapping("/api/v1/jacksorbetter/game")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }


    /**
     * Starts a new deal (initial hand) for the player.
     *
     * @param dealRequest The deal request payload.
     * @return The response containing the dealt hand and relevant metadata.
     */
    @Operation(
        summary = "Start a new deal for Jacks or Better Video Poker",
        description = "Initiates a new game deal and returns the initial hand and metadata."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deal successful", content = @Content(schema = @Schema(implementation = DealResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/deal")
    public DealResponse deal(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Deal request payload",
            required = true,
            content = @Content(schema = @Schema(implementation = DealRequest.class))
        )@RequestBody DealRequest dealRequest) {
    	System.out.println("DealRequest: " + dealRequest);

    	//Log the response for debugging
    	DealResponse response = gameService.deal(dealRequest);
    	System.out.println("DealResponse: " + response);
    	
        return response;
    }

    /**
     * Draws cards after the player selects which cards to hold.
     *
     * @param drawRequest The draw request payload.
     * @return The result of the draw including final hand(s), payout, and metadata.
     */
    @Operation(
        summary = "Draw cards after holding selected cards",
        description = "Completes the draw phase by replacing non-held cards and determines final hand(s) and payout."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Draw successful", content = @Content(schema = @Schema(implementation = MultiHandDrawResultResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content),
        @ApiResponse(responseCode = "404", description = "Session ID not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/draw")
    public MultiHandDrawResultResponse draw(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Draw request payload",
            required = true,
            content = @Content(schema = @Schema(implementation = DrawRequest.class))
        )
        @RequestBody DrawRequest drawRequest) {
    	//log the draw request for debugging
    	System.out.println("DrawRequest: " + drawRequest);
    	
    	//log the response for debugging
    	MultiHandDrawResultResponse response = gameService.draw(drawRequest.getSessionId(), drawRequest.getHolds());
    	System.out.println("DrawResponse: " + response);
        return response;
    }

    // ------------------- Game Results endpoints -------------------

    /**
     * Get all game results.
     */
    @Operation(
        summary = "Get all game results for Jacks or Better Video Poker",
        description = "Retrieves all historical game results for audit or analytics purposes."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of all game results", content = @Content(schema = @Schema(implementation = GameResultResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/results")
    public List<GameResultResponse> getAllResults() {
        return gameService.getAllResults();
    }

    /**
     * Get game results by unique user id.
     */
    @Operation(
        summary = "Get game results by unique user ID",
        description = "Retrieves all game results associated with a specific user."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of game results for the user", content = @Content(schema = @Schema(implementation = GameResultResponse.class))),
        @ApiResponse(responseCode = "404", description = "User ID not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/results/uid/{uid}")
    public List<GameResultResponse> getResultsByUid(
            @Parameter(description = "Unique user ID", required = true)
            @PathVariable String uid) {
        return gameService.getResultsByUid(uid);
    }

    /**
     * Get game results by EGM (Electronic Gaming Machine) ID.
     */
    @Operation(
        summary = "Get game results by EGM ID",
        description = "Retrieves all game results associated with a specific EGM (Electronic Gaming Machine) ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of game results for the EGM", content = @Content(schema = @Schema(implementation = GameResultResponse.class))),
        @ApiResponse(responseCode = "404", description = "EGM ID not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/results/egm/{egmId}")
    public List<GameResultResponse> getResultsByEgmId(
            @Parameter(description = "Electronic Gaming Machine ID", required = true)
            @PathVariable String egmId) {
        return gameService.getResultsByEgmId(egmId);
    }
}