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
 * |  28/07/2025   | Wilson Sam   |     Created     |  File Creation
 * **********************************************************************************************************************************************************************
 * */
package com.wildace.poker.jacksorbetter.repository;

import com.wildace.poker.jacksorbetter.dto.DeucesWildGameAuditLog;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeucesWildGameAuditLogRepository extends MongoRepository<DeucesWildGameAuditLog, String> {
    // You can add custom query methods here if needed
    List<DeucesWildGameAuditLog> findByUserId(String userId);
    List<DeucesWildGameAuditLog> findByEgmId(String egmId);
}