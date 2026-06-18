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
 * DTO representing a Pay Table document, including its database ID and the list of payouts for each hand type.
 */
@Schema(description = "Pay Table document containing all payout rules for Jacks or Better. Each pay table has an ID and a list of hand payout rules.")
public class PayTableDocumentDto {

    @Schema(
        description = "Unique identifier for the pay table document.",
        example = "1"
    )
    private Long id;

    @Schema(
        description = "List of payout rules for different hand types in the pay table.",
        implementation = PayTableHandDto.class
    )
    private List<PayTableHandDto> payouts;

    public PayTableDocumentDto() {}

    public PayTableDocumentDto(Long id, List<PayTableHandDto> payouts) {
        this.id = id;
        this.payouts = payouts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PayTableHandDto> getPayouts() {
        return payouts;
    }

    public void setPayouts(List<PayTableHandDto> payouts) {
        this.payouts = payouts;
    }

    @Override
    public String toString() {
        return "PayTableDocumentDto{" +
                "id=" + id +
                ", payouts=" + payouts +
                '}';
    }
}