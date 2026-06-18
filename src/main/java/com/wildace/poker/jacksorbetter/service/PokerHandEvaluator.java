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
 * |  03/07/2025   | Wilson Sam   |     Updated     |  Added support for multi-hand evaluation
 * |  03/07/2025   | Wilson Sam   |     Updated     |  Added payout calculation
 * |  03/07/2025   | Wilson Sam   |     Updated     |  Added utility methods for hand evaluation
 * |  03/07/2025   | Wilson Sam   |     Updated     |  Added Jacks or Better evaluation
 * |  03/07/2025   | Wilson Sam   |     Updated     |  Added rank and suit mapping
 * **********************************************************************************************************************************************************************
 * */
package com.wildace.poker.jacksorbetter.service;

import com.wildace.poker.jacksorbetter.dto.CardDto;

import java.util.*;

public class PokerHandEvaluator {

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
    	/**
    	 * Logic:
    	 * - Get a dictionary of rank values - rankCount.
    	 * - Get a dictionary of suit counts - suitCount.
    	 * - A sorted rank values of the hand - rankValues.
    	 * 
    	 * Flush Logic:
    	 * - Check if any suit has 5 cards.
    	 * Straight Logic:
    	 * - Check if the sorted rank values form a consecutive sequence.
    	 * - Special case for Ace-low straight (2, 3, 4, 5, A).
    	 * Royal Flush Logic:
    	 * - Check if the hand is both a flush and a straight, with the lowest rank being 10.
    	 * Straight Flush Logic:
    	 * - Check if the hand is both a flush and a straight.
    	 * Four of a Kind Logic:
    	 * - Check if any rank has exactly 4 occurrences.
    	 * Full House Logic:
    	 * - Check if there is a combination of 3 of a kind and a pair.
    	 * Flush Logic:
    	 * - If flush is true, return "Flush".
    	 * Straight Logic:
    	 * - If straight is true, return "Straight".
    	 * Three of a Kind Logic:
    	 * - Check if any rank has exactly 3 occurrences.
    	 * Two Pair Logic:
    	 * - Check if there are exactly two ranks with 2 occurrences each.
    	 * Jacks or Better Logic:
    	 * - Check if there is at least one pair of Jacks, Queens, Kings, or Aces.
    	 * 
    	 * 
    	 */
        Map<String, Integer> rankCount = new HashMap<>();
        Map<String, Integer> suitCount = new HashMap<>();
        List<Integer> rankValues = new ArrayList<>();

        for (CardDto card : hand) {
            rankCount.merge(card.getRank(), 1, Integer::sum);// Count occurrences of each rank
            suitCount.merge(card.getSuit(), 1, Integer::sum);// Count occurrences of each suit
            rankValues.add(RANK_ORDER.get(card.getRank()));// Convert rank to its integer value
        }
        Collections.sort(rankValues);

        boolean flush = suitCount.values().stream().anyMatch(cnt -> cnt == 5);
        boolean straight = isStraight(rankValues);
        boolean royal = straight && rankValues.get(0) == 10;//

        if (flush && royal) return "Royal Flush";
        if (flush && straight) return "Straight Flush";
        if (hasOfAKind(rankCount, 4)) return "Four of a Kind";
        if (hasFullHouse(rankCount)) return "Full House";
        if (flush) return "Flush";
        if (straight) return "Straight";
        if (hasOfAKind(rankCount, 3)) return "Three of a Kind";
        if (hasTwoPair(rankCount)) return "Two Pair";
        if (hasJacksOrBetter(rankCount)) return "Jacks or Better";
        return "No Win";
    }

    public static int getPayout(String result) {
        switch (result) {
            case "Royal Flush":      return 800;
            case "Straight Flush":   return 50;
            case "Four of a Kind":   return 25;
            case "Full House":       return 9;
            case "Flush":            return 6;
            case "Straight":         return 4;
            case "Three of a Kind":  return 3;
            case "Two Pair":         return 2;
            case "Jacks or Better":  return 1;
            default:                 return 0;
        }
    }

    private static boolean isStraight(List<Integer> rankValues) {
        // Ace-low straight
        if (rankValues.equals(Arrays.asList(2, 3, 4, 5, 14))) return true;
        for (int i = 0; i < rankValues.size() - 1; i++) {
            if (rankValues.get(i) + 1 != rankValues.get(i + 1)) return false;
        }
        return true;
    }

    private static boolean hasOfAKind(Map<String, Integer> rankCount, int n) {
        return rankCount.values().stream().anyMatch(v -> v == n);
    }

    private static boolean hasFullHouse(Map<String, Integer> rankCount) {
        return rankCount.values().contains(3) && rankCount.values().contains(2);
    }

    private static boolean hasTwoPair(Map<String, Integer> rankCount) {
        return rankCount.values().stream().filter(v -> v == 2).count() == 2;
    }

    /**
     * 
     * Checks if the hand has Jacks or Better.
     * This means at least one pair of Jacks, Queens, Kings, or Aces.
     * Logic:
     * - Iterate through the rankCount map.
     * * - Check if any rank has exactly 2 occurrences (a pair).
     * * - Ensure the rank is one of J, Q, K, or A.
     * @param rankCount
     * @return
     */
    private static boolean hasJacksOrBetter(Map<String, Integer> rankCount) {
        return rankCount.entrySet().stream().anyMatch(e ->
                e.getValue() == 2 && ("JQKA".contains(e.getKey()))
        );
    }
}