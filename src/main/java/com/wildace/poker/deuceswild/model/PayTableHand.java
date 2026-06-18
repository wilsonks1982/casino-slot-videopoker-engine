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
 * |  30/07/2025   | Wilson Sam   |     Created     |  File Creation
 * **********************************************************************************************************************************************************************
 * */
package com.wildace.poker.deuceswild.model;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Schema(
    description = "Represents a single poker hand entry in the pay table, including its payouts."
)
public class PayTableHand {

    @Schema(
        description = "Unique business ID for the poker hand.",
        example = "1"
    )
    @Field("id")
    private Long id;

    @Schema(
        description = "Name of the poker hand (e.g., 'Royal Flush', 'Straight').",
        example = "Royal Flush"
    )
    @Field("name")
    private String name;

    @ArraySchema(
        schema = @Schema(
            description = "Payouts for this hand depending on the number of coins bet.",
            example = "[250, 500, 750, 1000, 4000]"
        )
    )
    @Field("payouts")
    private List<Integer> payouts;

    public PayTableHand() {}

    public PayTableHand(Long id, String name, List<Integer> payouts) {
        this.id = id;
        this.name = name;
        this.payouts = payouts;
    }

    // getters and setters...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getPayouts() {
        return payouts;
    }

    public void setPayouts(List<Integer> payouts) {
        this.payouts = payouts;
    }

    @Override
    public String toString() {
        return "PayTableHand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", payouts=" + payouts +
                '}';
    }
}