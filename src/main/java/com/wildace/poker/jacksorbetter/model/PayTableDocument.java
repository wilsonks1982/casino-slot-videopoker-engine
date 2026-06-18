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
 * **********************************************************************************************************************************************************************
 * */
package com.wildace.poker.jacksorbetter.model;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Schema(
    description = "Represents a pay table document for Jacks or Better Poker, stored in MongoDB."
)
@Document(collection = "paytables")
public class PayTableDocument {

    @Schema(
        description = "MongoDB's unique object id",
        example = "60c72b2f9b1e8c1a4e4e6a2a",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    @Id
    private String mongoId; // MongoDB's unique object id

    @Schema(
        description = "Business ID for the pay table",
        example = "1"
    )
    @Field("id")
    private Long id; // Your business ID

    @ArraySchema(
        schema = @Schema(
            description = "List of payouts for each hand type."
        )
    )
    @Field("payouts")
    private List<PayTableHand> payouts;

    public PayTableDocument() {}

    public PayTableDocument(Long id, List<PayTableHand> payouts) {
        this.id = id;
        this.payouts = payouts;
    }

    // getters and setters...
    public String getMongoId() {
        return mongoId;
    }

    public void setMongoId(String mongoId) {
        this.mongoId = mongoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PayTableHand> getPayouts() {
        return payouts;
    }

    public void setPayouts(List<PayTableHand> payouts) {
        this.payouts = payouts;
    }

    @Override
    public String toString() {
        return "PayTableDocument{" +
                "mongoId='" + mongoId + '\'' +
                ", id=" + id +
                ", payouts=" + payouts +
                '}';
    }
}