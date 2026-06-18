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
 * |  03/07/2025   | Wilson Sam   |   Updated Logic |  Added CRUD operations for pay tables
 * |  08/07/2025   | Wilson Sam   |   Documentation |  Swagger Documentation for clarity, maintainability, and REST best practices
 * **********************************************************************************************************************************************************************
 * */
package com.wildace.poker.jacksorbetter.controller;

import com.wildace.poker.jacksorbetter.model.PayTableDocument;
import com.wildace.poker.jacksorbetter.service.PayTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Jacks Or Better PayTable API", description = "Operations related to pay tables for Jacks Or Better Poker Game")
@RestController
@RequestMapping("/api/v1/jacksorbetter/paytable")
public class PayTableController {
    private final PayTableService payTableService;

    public PayTableController(PayTableService payTableService) {
        this.payTableService = payTableService;
    }

    @Operation(summary = "Get all pay tables", description = "Retrieve a list of all pay tables.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public List<PayTableDocument> getAllPayTables() {
        return payTableService.getAllPayTables();
    }

    @Operation(summary = "Get pay table by ID", description = "Retrieve a specific pay table by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pay table found"),
        @ApiResponse(responseCode = "404", description = "Pay table not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PayTableDocument> getPayTableById(
            @Parameter(description = "ID of the pay table to retrieve") @PathVariable Long id) {
        return payTableService.getPayTableById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new pay table", description = "Add a new pay table to the database.")
    @ApiResponse(responseCode = "200", description = "Pay table created successfully")
    @PostMapping
    public ResponseEntity<PayTableDocument> createPayTable(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "PayTableDocument object to be created")
            @RequestBody PayTableDocument payTableDocument) {
        PayTableDocument savedDocument = payTableService.savePayTable(payTableDocument);
        return ResponseEntity.ok(savedDocument);
    }

    @Operation(summary = "Update an existing pay table", description = "Update a pay table by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pay table updated successfully"),
        @ApiResponse(responseCode = "404", description = "Pay table not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PayTableDocument> updatePayTable(
            @Parameter(description = "ID of the pay table to update") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Updated PayTableDocument object")
            @RequestBody PayTableDocument payTableDocument) {
        payTableDocument.setId(id);
        PayTableDocument updatedDocument = payTableService.savePayTable(payTableDocument);
        return ResponseEntity.ok(updatedDocument);
    }

    @Operation(summary = "Delete a pay table", description = "Delete a pay table by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pay table deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Pay table not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayTable(
            @Parameter(description = "ID of the pay table to delete") @PathVariable Long id) {
        payTableService.deletePayTable(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "API version", description = "Get the version of this API.")
    @ApiResponse(responseCode = "200", description = "API version retrieved successfully")
    @GetMapping("/version")
    public ResponseEntity<String> version() {
        return ResponseEntity.ok("PayTable API Version 1.0");
    }
}