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
 * |  02/07/2025   | Wilson Sam   |     Updated     |  Added method to save pay table document
 * |  02/07/2025   | Wilson Sam   |     Updated     |  Added method to fetch all pay tables
 * |  02/07/2025   | Wilson Sam   |     Updated     |  Added method to fetch pay table by ID
 * |  02/07/2025   | Wilson Sam   |     Updated     |  Added method to delete pay table document
 * |  03/07/2025   | Wilson Sam   |     Updated     |  Added @PostConstruct annotation to initPayTable method
 * |  03/07/2025   | Wilson Sam   |     Updated     |  Added initPayTable method to initialize default pay table
 * |  03/07/2025   | Wilson Sam   |     Updated     |  Added JSON string for default pay table
 * |  03/07/2025   | Wilson Sam   |     Updated     |  Added ObjectMapper to convert JSON to PayTableDocument
 * |  03/07/2025   | Wilson Sam   |     Updated     |  Added check to avoid re-initialization if pay table already exists
 * |  30/07/2025   | Wilson Sam   |     Updated     |  Added method to get payout for a given hand type and coin count
 * **********************************************************************************************************************************************************************
 * */
package com.wildace.poker.jacksorbetter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildace.poker.jacksorbetter.model.PayTableDocument;
import com.wildace.poker.jacksorbetter.model.PayTableHand;
import com.wildace.poker.jacksorbetter.repository.PayTableDocumentRepository;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PayTableService {
    private static final Logger logger = LoggerFactory.getLogger(PayTableService.class);
    private static final String PAY_TABLE_DOCUMENT_JSON = """
    		{
			    "id": 1,
			    "payouts": [
			        {
			            "id": 1,
			            "name": "Royal Flush",
			            "payouts": [250, 500, 750, 1000, 4000]
			        },
			        {
			            "id": 2,
			            "name": "Straight Flush",
			            "payouts": [50, 100, 150, 200, 250]
			        },
			        {
			            "id": 3,
			            "name": "Four of a Kind",
			            "payouts": [25, 50, 75, 100, 125]
			        },
			        {
			            "id": 4,
			            "name": "Full House",
			            "payouts": [9, 18, 27, 36, 45]
			        },
			        {
			            "id": 5,
			            "name": "Flush",
			            "payouts": [6, 12, 18, 24, 30]
			        },
			        {
			            "id": 6,
			            "name": "Straight",
			            "payouts": [4, 8, 12, 16, 20]
			        },
			        {
			            "id": 7,
			            "name": "Three of a Kind",
			            "payouts": [3, 6, 9, 12, 15]
			        },
			        {
			            "id": 8,
			            "name": "Two Pair",
			            "payouts": [2, 4, 6, 8, 10]
			        },
			        {
			            "id": 9,
			            "name": "Jacks or Better",
			            "payouts": [1, 2, 3, 4, 5]
			        }
			    ]
			}
			""";

    private final PayTableDocumentRepository payTableDocumentRepository;

    public PayTableService(PayTableDocumentRepository payTableDocumentRepository) {
        this.payTableDocumentRepository = payTableDocumentRepository;
    }

    public List<PayTableDocument> getAllPayTables() {
        logger.debug("Fetching all pay tables");
        return payTableDocumentRepository.findAll();
    }

    public Optional<PayTableDocument> getPayTableById(Long id) {
        logger.debug("Fetching pay table by id: {}", id);
        return payTableDocumentRepository.findById(id);
    }

    /**
     * Get the payout for a given hand type and coin count.
     * @param handType The evaluated hand type (e.g., "Royal Flush", "Two Pair", etc.)
     * @param coin Number of coins bet (1-based, max 5).
     * @return The payout for the hand, or 0 if no match.
     */
    public int getPayoutForHand(String handType, int coin) {
        // For simplicity, always use paytable with id=1
        Optional<PayTableDocument> opt = payTableDocumentRepository.findById(1L);
        if (opt.isEmpty()) return 0;
        PayTableDocument payTable = opt.get();
        if (coin < 1 || coin > 5) return 0;
        for (PayTableHand entry : payTable.getPayouts()) {
            if (entry.getName().equalsIgnoreCase(handType)) {
                // coin is 1-based, payouts is 0-based
                return entry.getPayouts().get(coin - 1);
            }
        }
        return 0;
    }
    
    public PayTableDocument savePayTable(PayTableDocument document) {
        logger.info("Saving pay table: {}", document);
        return payTableDocumentRepository.save(document);
    }

    public void deletePayTable(Long id) {
        logger.warn("Deleting pay table with id: {}", id);
        payTableDocumentRepository.deleteById(id);
    }
    /**
	 * Initializes the pay table with default data if it is empty.
	 * This method is called after the service is constructed.
	 */
    @PostConstruct
    public void initPayTable() {
		if (payTableDocumentRepository.count() == 0) {
			logger.info("Initializing pay table with default data");
			
			ObjectMapper objectMapper = new ObjectMapper();

			// Convert JSON string to PayTableDocument object
			// This is a simple way to initialize the pay table with default values
			try {
				PayTableDocument defaultPayTable = objectMapper.readValue(PAY_TABLE_DOCUMENT_JSON, PayTableDocument.class);
				defaultPayTable.setId(1L); // Ensure the ID is set correctly
				payTableDocumentRepository.save(defaultPayTable);
				logger.info("Default pay table initialized successfully");
			} catch (Exception e) {
				logger.error("Error initializing default pay table", e);
			}
		} else {
			logger.info("Pay table already initialized");
		}
	}
}