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
 * |  28/07/2025   | Wilson Sam   |     Created     |  Deuces Wild Hand Evaluator
 * |  12/08/2025   | Wilson Sam   |     Updated     |  Updated Wild Royal Flush logic
 * **********************************************************************************************************************************************************************
 */

package com.wildace.poker.jacksorbetter.service;

import com.wildace.poker.jacksorbetter.dto.CardDto;

import java.util.*;

/**
 * Evaluates a Deuces Wild Poker Hand.
 * This evaluator assumes that Deuces (2s) are wild and can substitute for any card.
 * Hand Ranking (typical for Deuces Wild):
 *  - Natural Royal Flush (No wilds)
 *  - Four Deuces
 *  - Wild Royal Flush
 *  - Five of a Kind
 *  - Straight Flush
 *  - Four of a Kind
 *  - Full House
 *  - Flush
 *  - Straight
 *  - Three of a Kind
 */
public class DeucesWildPokerHandEvaluator {

    private static final Map<String, Integer> RANK_ORDER = createRankOrder();

    private static Map<String, Integer> createRankOrder() {
        Map<String, Integer> order = new HashMap<>();
        order.put("2", 2);   order.put("3", 3);   order.put("4", 4);
        order.put("5", 5);   order.put("6", 6);   order.put("7", 7);
        order.put("8", 8);   order.put("9", 9);   order.put("10", 10);
        order.put("J", 11);  order.put("Q", 12);  order.put("K", 13); order.put("A", 14);
        return order;
    }

    public static String evaluateHand(List<CardDto> hand) {
        // Count wilds (deuces) and non-wild cards
        List<CardDto> wilds = new ArrayList<>();
        List<CardDto> nonWilds = new ArrayList<>();
        for (CardDto card : hand) {
            if (card.getRank().equals("2")) wilds.add(card);
            else nonWilds.add(card);
        }
        int numWilds = wilds.size();

        // Shortcut: Four Deuces
        if (numWilds == 4) return "Four Deuces";

        // Count non-wild ranks and suits
        Map<String, Integer> rankCount = new HashMap<>();
        Map<String, Integer> suitCount = new HashMap<>();
        List<Integer> rankValues = new ArrayList<>();
        for (CardDto card : nonWilds) {
            rankCount.merge(card.getRank(), 1, Integer::sum);
            suitCount.merge(card.getSuit(), 1, Integer::sum);
            rankValues.add(RANK_ORDER.get(card.getRank()));
        }
        Collections.sort(rankValues);

        // HAND RANK CHECKS (HIGH TO LOW):

        // 1. Natural Royal Flush (no wilds)
        if (numWilds == 0 && isRoyalFlush(nonWilds)) return "Natural Royal Flush";
        
        // 2. Wild Royal Flush (with wilds)
        if (canMakeRoyalFlush(nonWilds, numWilds)) return "Wild Royal Flush";

        // 3. Five of a Kind
        if (canMakeOfAKind(rankCount, numWilds, 5)) return "Five of a Kind";

        // 4. Straight Flush
        if (canMakeStraightFlush(nonWilds, numWilds)) return "Straight Flush";

        // 5. Four of a Kind
        if (canMakeOfAKind(rankCount, numWilds, 4)) return "Four of a Kind";

        // 6. Full House
        if (canMakeFullHouse(rankCount, numWilds)) return "Full House";

        // 7. Flush
        if (canMakeFlush(nonWilds, numWilds)) return "Flush";

        // 8. Straight
        if (canMakeStraight(rankValues, numWilds)) return "Straight";

        // 9. Three of a Kind
        if (canMakeOfAKind(rankCount, numWilds, 3)) return "Three of a Kind";

        return "No Win";
    }

    public static int getPayout(String result) {
        switch (result) {
            case "Natural Royal Flush": return 800;
            case "Four Deuces":        return 200;
            case "Wild Royal Flush":   return 25;
            case "Five of a Kind":     return 15;
            case "Straight Flush":     return 9;
            case "Four of a Kind":     return 5;
            case "Full House":         return 3;
            case "Flush":              return 2;
            case "Straight":           return 2;
            case "Three of a Kind":    return 1;
            default:                   return 0;
        }
    }

    // Utility methods

    private static boolean isRoyalFlush(List<CardDto> hand) {
        // Check for 10, J, Q, K, A all same suit, no wilds
        if (hand.size() != 5) return false;
        Set<String> required = new HashSet<>(Arrays.asList("10", "J", "Q", "K", "A"));
        Set<String> ranks = new HashSet<>();
        String suit = hand.get(0).getSuit();
        for (CardDto card : hand) {
            if (!card.getSuit().equals(suit)) return false;
            ranks.add(card.getRank());
        }
        return ranks.equals(required);
    }

    private static boolean canMakeRoyalFlush(List<CardDto> nonWilds, int numWilds) {
        if (nonWilds.size() + numWilds != 5) return false;
        Set<String> needed = new HashSet<>(Arrays.asList("10", "J", "Q", "K", "A"));
        Set<String> have = new HashSet<>();
        Map<String, Integer> suitCount = new HashMap<>();
        for (CardDto card : nonWilds) {
            have.add(card.getRank());
            suitCount.merge(card.getSuit(), 1, Integer::sum);
        }
        needed.removeAll(have);
        boolean canFill = needed.size() <= numWilds;
        boolean oneSuit = suitCount.size() == 1;// All cards must be of the same suit
        return canFill && oneSuit;
    }

    private static boolean canMakeOfAKind(Map<String, Integer> rankCount, int wilds, int n) {
        // Try to make n of a kind using wilds
        for (Map.Entry<String, Integer> e : rankCount.entrySet()) {
            if (e.getValue() + wilds >= n) return true;
        }
        // All wilds scenario (for Five of a Kind)
        return wilds >= n;
    }

    private static boolean canMakeFullHouse(Map<String, Integer> rankCount, int wilds) {
        // Try to make 3 of a kind and 2 of a kind using wilds
        List<Integer> counts = new ArrayList<>(rankCount.values());
        counts.sort(Collections.reverseOrder());
        // Try all combinations
        // Try making 3+2 (using wilds optimally)
        for (int i = 0; i < counts.size(); i++) {
            int neededForThree = Math.max(0, 3 - counts.get(i));
            for (int j = 0; j < counts.size(); j++) {
                if (i == j) continue;
                int neededForPair = Math.max(0, 2 - counts.get(j));
                int totalNeeded = neededForThree + neededForPair;
                if (totalNeeded <= wilds) return true;
            }
        }
        // 2 ranks only: fill both with wilds
        if (counts.size() == 1 && wilds >= 2) return true;
        // All wilds
        return wilds >= 5;
    }

    private static boolean canMakeFlush(List<CardDto> nonWilds, int wilds) {
        if (nonWilds.isEmpty()) return wilds >= 5;// All wilds can make a flush
        Map<String, Integer> suitCount = new HashMap<>();
        for (CardDto card : nonWilds) suitCount.merge(card.getSuit(), 1, Integer::sum);// Count occurrences of each suit
        for (int count : suitCount.values()) {
            if (count + wilds >= 5) return true;
        }
        return false;
    }

    private static boolean canMakeStraight(List<Integer> nonWildRanks, int wilds) {
        if (nonWildRanks.isEmpty()) return wilds >= 5;
        Set<Integer> unique = new HashSet<>(nonWildRanks);
        // Try all possible starting points for a 5-card straight
        for (int low = 2; low <= 10; low++) {
            int needed = 0;
            for (int i = 0; i < 5; i++) {
                if (!unique.contains(low + i)) needed++;
            }
            if (needed <= wilds) return true;
        }
        // Ace-low straight (A, 2, 3, 4, 5)
        int neededAceLow = 0;
        for (int v : Arrays.asList(2, 3, 4, 5, 14)) {
            if (!unique.contains(v)) neededAceLow++;
        }
        return neededAceLow <= wilds;
    }

    private static boolean canMakeStraightFlush(List<CardDto> nonWilds, int wilds) {
        // Group by suit
        Map<String, List<Integer>> suitRanks = new HashMap<>();
        for (CardDto card : nonWilds) {
            suitRanks.computeIfAbsent(card.getSuit(), k -> new ArrayList<>()).add(RANK_ORDER.get(card.getRank()));
        }
        for (List<Integer> ranks : suitRanks.values()) {
            if (canMakeStraight(ranks, wilds)) return true;
        }
        // All wilds = straight flush
        return nonWilds.isEmpty() && wilds >= 5;
    }
}