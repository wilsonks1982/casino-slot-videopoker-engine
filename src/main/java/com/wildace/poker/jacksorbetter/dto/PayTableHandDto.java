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
package com.wildace.poker.jacksorbetter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * DTO representing a specific hand type and its payout structure in a pay table.
 */
@Schema(description = "Represents a payout rule for a specific poker hand in the pay table.")
public class PayTableHandDto {

    @Schema(
        description = "Unique identifier for the hand payout entry.",
        example = "1"
    )
    private Long id;

    @Schema(
        description = "Name of the poker hand (e.g. 'Royal Flush', 'Straight', 'Jacks or Better').",
        example = "Royal Flush"
    )
    private String name;

    @Schema(
        description = "List of payout amounts for this hand (index 0 = 1 coin bet, index 1 = 2 coins, etc).",
        example = "[250, 500, 750, 1000, 4000]"
    )
    private List<Integer> payouts;

    public PayTableHandDto() {}

    public PayTableHandDto(Long id, String name, List<Integer> payouts) {
        this.id = id;
        this.name = name;
        this.payouts = payouts;
    }

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
        return "PayTableHandDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", payouts=" + payouts +
                '}';
    }
}