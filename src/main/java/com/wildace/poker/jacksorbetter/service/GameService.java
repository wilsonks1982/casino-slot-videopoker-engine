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
 * |  02/07/2025   | Wilson Sam   | Use UserService |  Fetch and update user info and wallet from UserService
 * |  02/07/2025   | Wilson Sam   |   Updated Logic |  Support for full MultiHandDrawResultResponse fields
 * |  03/07/2025   | Wilson Sam   |  Multi-deck     |  Use separate deck for each hand in multi-hand mode
 * |  03/07/2025   | Wilson Sam   |  Add Logging    |  Added slf4j logging, especially for hold/draw debugging
 * |  03/07/2025   | Wilson Sam   | Primary Copy    |  On draw, copy held cards from primary hand to all hands
 * |  03/07/2025   | Wilson Sam   |  Deep Copy Decks|  Ensure decks are deep copied to avoid shared mutable state
 * |  04/07/2025   | Wilson Sam   |  Audit Logging  |  Added GameAuditLogRepository for audit logs
 * |  04/07/2025   | Wilson Sam   |  Scheduled Task |  Added cleanup task to remove old sessions after 30 mins
 * |  04/07/2025   | Wilson Sam   |  Error Handling |  Improved error handling and response messages
 * |  04/07/2025   | Wilson Sam   |  Date Formatting|  Use consistent date formatting for gameStart/gameEnd 
 * |  04/07/2025   | Wilson Sam   |  Session Expiry |  Added session expiry logic to cleanup old games 
 * |  08/07/2025   | Wilson Sam   |  Game Results   |  Added results endpoints for querying game history
 * |  08/07/2025   | Wilson Sam   |  Game Result    |  Added GameResultResponse for game results
 * |  08/07/2025   | Wilson Sam   |  Game Result    |  Added getAllResults, getResultsByUid, getResultsByEgmId methods
 * |  30/07/2025   | Wilson Sam   |  payTableService|  Added payTableService for taking payouts from pay table
 * |  30/07/2025   | Wilson Sam   |  Draw Response  |  Added resultList in Response for multi-hand draw
 * **********************************************************************************************************************************************************************
 * */
package com.wildace.poker.jacksorbetter.service;

import com.wildace.poker.jacksorbetter.dto.*;

import com.wildace.poker.jacksorbetter.dto.GameAuditLog;
import com.wildace.poker.jacksorbetter.repository.GameAuditLogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GameService provides deal and draw logic for multi-hand Jacks or Better, with correct hold/copy logic.
 */
@Service
public class GameService {

    private static final Logger log = LoggerFactory.getLogger(GameService.class);

    private final UserService userService;
    private final GameAuditLogRepository auditLogRepository;
    private final PayTableService payTableService;



    @Autowired
    public GameService(
        UserService userService, 
        GameAuditLogRepository auditLogRepository,
        PayTableService payTableService
    ) {
        this.userService = userService;
        this.auditLogRepository = auditLogRepository;
        this.payTableService = payTableService;
    }

    /**
     * Internal state for each game session.
     * - Only the primary hand (index 0) is drawn at deal; others are empty.
     * - All decks are shuffled and stored; only the primary deck has 47 cards after deal, others still have 52.
     */
    private static class GameState {
        List<List<CardDto>> hands;      // Only primary hand populated in deal, others empty
        List<Deque<CardDto>> decks;     // One deck per hand
        String egmId;
        String uid;
        int coin;
        int numberOfHands;
        double wallet;
        double oldCredit;
        String id;
        String gameStart;
        double betAmount;
        long lastAccessed = System.currentTimeMillis();
    }

    // In-memory storage for active games, keyed by sessionId
    private final Map<String, GameState> games = new java.util.concurrent.ConcurrentHashMap<>();

    private static final double DENOMINATOR = 1.0; // For bet calculations, each coin is worth 1 unit
    private static final int HAND_SIZE = 5;
    
    private static final int MIN_HANDS = 1;
    private static final int MAX_HANDS = 10;
    private static final int MIN_COINS = 1;
    private static final int MAX_COINS = 5;
    
    private static final long SESSION_EXPIRY_MINS = 30;

    
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final int STATUS_OK = 200;
    private static final int STATUS_BAD_REQUEST = 400;
    private static final int STATUS_SERVER_ERROR = 500;

    
    /**
     * Generates a unique primary key using current timestamp.
     */
    public static String generatePrimaryKey() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.ofHoursMinutes(5,30));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        return now.format(formatter);
    }

    /**
     * Returns current timestamp in standard format.
     */
    private String now() {
		return LocalDateTime.now(ZoneOffset.ofHoursMinutes(5,30)).format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    /**
     * Fills in a DealResponse with error details.
     */
    private DealResponse errorResponse(DealResponse response, String msg, int statusCode) {
        response.setOk(0);
        response.setMsg(msg);
        response.setStatusCode(statusCode);
        return response;
    }

    
    /**
	 * Deep copies the decks to ensure each game state is independent.
	 * This is necessary to avoid shared mutable state between games.
	 */
    private List<List<CardDto>> deepCopyDecks(List<Deque<CardDto>> decks) {
        return decks.stream()
            .map(deck -> new ArrayList<>(deck))
            .collect(Collectors.toList());// Convert Deque to List for easier handling
    }
    
    
    /**
     * Handles the DEAL request.
     * - Only the primary hand (index 0) is drawn from its deck.
     * - All other hands remain empty, but their decks are shuffled and stored.
     * - The user's wallet is debited for the bet.
     *
     * @param dealRequest The incoming deal request.
     * @return A DealResponse containing the primary hand and session info.
     */
    public DealResponse deal(DealRequest dealRequest) {
        int numberOfHands = dealRequest.getNumberOfHands();
        int coin = dealRequest.getCoin();
        String egmId = dealRequest.getEgmId();
        String uid = dealRequest.getUid();
        DealResponse response = new DealResponse();
        if (uid == null || uid.isEmpty())
            return errorResponse(response, "Invalid request: User ID is required", STATUS_BAD_REQUEST);
        if (egmId == null || egmId.isEmpty())
            return errorResponse(response, "Invalid request: EGM ID is required", STATUS_BAD_REQUEST);

        if (numberOfHands < MIN_HANDS || numberOfHands > MAX_HANDS)
            return errorResponse(response, "Invalid request: Number of hands must be between 1 and 10", STATUS_BAD_REQUEST);
        if (coin < MIN_COINS || coin > MAX_COINS)
            return errorResponse(response, "Invalid request: Coin value must be between 1 and 5", STATUS_BAD_REQUEST);

        String sessionId = UUID.randomUUID().toString();
        String id = generatePrimaryKey();

        // Get user info from UserService
        User user = userService.findOne(uid);
        if (user == null) return errorResponse(response, "User not found", STATUS_BAD_REQUEST);

        double oldCredit = user.getWallet();
        double betAmount = numberOfHands * coin * DENOMINATOR; // 1 per coin per hand
        if (oldCredit < betAmount) return errorResponse(response, "Insufficient funds", STATUS_BAD_REQUEST);

        double wallet = oldCredit - betAmount;
        String gameStart = now();

        // Update the user's wallet in the database
        user.setWallet(wallet);
        userService.updateOne(uid, user);

        List<List<CardDto>> hands = new ArrayList<>();
        List<Deque<CardDto>> decks = new ArrayList<>();
        
        List<List<CardDto>> decksBefore = deepCopyDecks(decks); // before any cards are drawn

        for (int h = 0; h < numberOfHands; h++) {
            Deque<CardDto> deck = generateShuffledDeck();
            if (h == 0) {
                // Draw first 5 cards for primary hand
                List<CardDto> hand = new ArrayList<>();
                for (int i = 0; i < HAND_SIZE; i++) {
                    hand.add(deck.pollFirst());
                }
                hands.add(hand);
            } else {
                // All other hands: leave empty, to be filled at draw
                hands.add(new ArrayList<>());
            }
            decks.add(deck); // All decks: primary deck has 47 cards, others 52
        }

        List<List<CardDto>> decksAfter = deepCopyDecks(decks);  // after deal (primary deck has 47 cards)

        // Store meta info for draw
        GameState state = new GameState();
        state.hands = hands;
        state.decks = decks;
        state.egmId = egmId;
        state.uid = uid;
        state.coin = coin;
        state.numberOfHands = numberOfHands;
        state.wallet = wallet;
        state.oldCredit = oldCredit;
        state.id = id;
        state.gameStart = gameStart;
        state.betAmount = betAmount;
        state.lastAccessed = System.currentTimeMillis();

        games.put(sessionId, state);

        // Only send the primary hand (index 0) to client
        List<List<CardDto>> primaryHandOnly = new ArrayList<>();
        primaryHandOnly.add(hands.get(0));

        response.setId(id);
        response.setEgmId(egmId);
        response.setUid(uid);
        response.setSessionId(sessionId);
        response.setHands(primaryHandOnly.stream()
			.map(hand -> hand.stream()
				.map(CardDto::toString)
				.collect(Collectors.toList()))
			.collect(Collectors.toList()));
        
        response.setCoin(coin);
        response.setNumberOfHands(numberOfHands);
        response.setWallet(wallet);
        response.setStatusCode(200);
        response.setMsg("Deal successful");
        response.setOk(1);

        log.info("Deal: sessionId={}, uid={}, egmId={}, numberOfHands={}, coin={}, wallet(before)={}, wallet(after)={}",
            sessionId, uid, egmId, numberOfHands, coin, oldCredit, wallet);

        // Save the game state in the map
        GameAuditLog dealAudit = new GameAuditLog();
        dealAudit.setSessionId(sessionId);
        dealAudit.setUserId(uid);
        dealAudit.setEgmId(egmId);
        dealAudit.setAction("deal");
        dealAudit.setGameStart(gameStart);
        dealAudit.setGameEnd(now());
        dealAudit.setHands(primaryHandOnly); // Only primary hand
        dealAudit.setDecksBefore(decksBefore);
        dealAudit.setDecksAfter(decksAfter);
        dealAudit.setBetAmount(betAmount);
        dealAudit.setOldCredit(oldCredit);
        dealAudit.setNewCredit(wallet);
        dealAudit.setCoin(coin);
        dealAudit.setNumberOfHands(numberOfHands);
        auditLogRepository.save(dealAudit);
        
        return response;
    }

    /**
     * Handles the DRAW request.
     * - For each hand:
     *   - For hold positions, copy the card from the primary hand's position in that index.
     *   - For non-hold positions, draw a new card from the corresponding hand's deck.
     * - The user's wallet is updated with winnings.
     *
     * @param sessionId The session ID, as returned by deal.
     * @param holds List of held card indexes for the primary hand (applies to all hands).
     * @return MultiHandDrawResultResponse with results for all hands and meta fields.
     */
	public MultiHandDrawResultResponse draw(String sessionId, List<Integer> holds) {
	    GameState state = games.get(sessionId);
	    if (state == null) {
	        log.error("Draw: Invalid sessionId={}", sessionId);
	        throw new IllegalArgumentException("Invalid sessionId");
	    }
	
	    List<List<CardDto>> currentHands = state.hands;
	    List<Deque<CardDto>> decks = state.decks;
	
	    Set<Integer> holdSet = holds == null ? Collections.emptySet() : new HashSet<>(holds);
	
	    // Always refer to the primary hand, which has 5 initial cards
	    List<CardDto> primaryHand = currentHands.get(0);
	
	    List<List<CardDto>> newHands = new ArrayList<>();
	    List<String> resultList = new ArrayList<>();
	    List<Integer> payoutList = new ArrayList<>();
	
	    List<List<CardDto>> decksBefore = deepCopyDecks(decks); // before any cards are drawn
	
	    double winAmount = 0.0;
	    for (int handIndex = 0; handIndex < state.numberOfHands; handIndex++) {
	        List<CardDto> newHand = new ArrayList<>();
	        Deque<CardDto> deck = decks.get(handIndex);
	
	        // Remove held cards from secondary hand decks before drawing
	        if (handIndex != 0 && !holdSet.isEmpty()) {
	            for (int holdIdx : holdSet) {
	                deck.remove(primaryHand.get(holdIdx));
	            }
	        }
	
	        for (int i = 0; i < HAND_SIZE; i++) {
	            if (holdSet.contains(i)) {
	                // Copy from primary hand, regardless of which hand this is
	                newHand.add(primaryHand.get(i));
	                log.trace("Draw: handIndex={}, cardIndex={}, heldCard={}", handIndex, i, primaryHand.get(i));
	            } else {
	                // Draw from THIS hand's deck
	                if (deck.isEmpty()) {
	                    log.error("Draw: Deck is empty for handIndex={}", handIndex);
	                    throw new IllegalStateException("Deck is empty for hand " + handIndex);
	                }
	                CardDto drawn = deck.pollFirst();
	                newHand.add(drawn);
	                log.trace("Draw: handIndex={}, cardIndex={}, drawnCard={}", handIndex, i, drawn);
	            }
	        }
	        newHands.add(newHand);
	
	        String result = PokerHandEvaluator.evaluateHand(newHand);
	        int payout = payTableService.getPayoutForHand(result, state.coin);
	
	        resultList.add(result);
	        payoutList.add(payout);
	        winAmount += payout;
	        log.debug("Draw: handIndex={}, newHand(after hold/draw)={}", handIndex, newHand);
	    }
	
	    List<List<CardDto>> decksAfter = deepCopyDecks(decks);  // after draw
	
	    String gameEnd = now();
	    double newCredit = state.oldCredit - state.betAmount + winAmount;
	
	    // Update the user's wallet in the database (win is credited)
	    User user = userService.findOne(state.uid);
	    if (user != null) {
	        user.setWallet(newCredit);
	        userService.updateOne(state.uid, user);
	    }
	
	    // Save the completed hands for reference (optional)
	    state.hands = newHands;
	    state.lastAccessed = System.currentTimeMillis();
	
	    MultiHandDrawResultResponse response = new MultiHandDrawResultResponse();
	    response.setId(state.id);
	    response.setEgmId(state.egmId);
	    response.setUid(state.uid);
	    response.setSessionId(sessionId);
	    response.setCoin(state.coin);
	    response.setNumberOfHands(state.numberOfHands);
	    response.setWallet(newCredit); // wallet = updated credit after draw
	    response.setStatusCode(200);
	    response.setMsg("Draw successful");
	    response.setOk(1);
	    response.setHands(newHands.stream()
	        .map(hand -> hand.stream()
	            .map(CardDto::toString)
	            .collect(Collectors.toList()))
	        .collect(Collectors.toList()));
	
	    response.setHolds(holds);
	    response.setPayouts(payoutList);
	    response.setResults(resultList);
	    response.setBetAmount(state.betAmount);
	    response.setWinAmount(winAmount);
	    response.setOldCredit(state.oldCredit);
	    response.setNewCredit(newCredit);
	    response.setGameStart(state.gameStart);
	    response.setGameEnd(gameEnd);
	
	    // Optionally update wallet in state for future rounds
	    state.wallet = newCredit;
	
	    log.info("Draw: sessionId={}, uid={}, betAmount={}, winAmount={}, wallet(before)={}, wallet(after)={}",
	        sessionId, state.uid, state.betAmount, winAmount, state.oldCredit, newCredit);
	    log.debug("Draw: sessionId={}, newHands={}, results={}, payouts={} holds={}", sessionId, newHands, resultList, payoutList, holds);
	
	    // Save audit log for the draw action
	    GameAuditLog drawAudit = new GameAuditLog();
	    drawAudit.setSessionId(sessionId);
	    drawAudit.setUserId(state.uid);
	    drawAudit.setEgmId(state.egmId);
	    drawAudit.setAction("draw");
	    drawAudit.setGameStart(state.gameStart);
	    drawAudit.setGameEnd(gameEnd);
	    drawAudit.setHands(newHands);
	    drawAudit.setDecksBefore(decksBefore);
	    drawAudit.setDecksAfter(decksAfter);
	    drawAudit.setHolds(holds);
	    drawAudit.setPayouts(payoutList);
	    drawAudit.setResults(resultList);
	    drawAudit.setBetAmount(state.betAmount);
	    drawAudit.setWinAmount(winAmount);
	    drawAudit.setOldCredit(state.oldCredit);
	    drawAudit.setNewCredit(newCredit);
	    drawAudit.setCoin(state.coin);
	    drawAudit.setNumberOfHands(state.numberOfHands);
	    auditLogRepository.save(drawAudit);
	
	    return response;
	}
    
    // --- Game Results endpoints ---

    /**
     * Get all game results (across all users, for all time).
     */
    public List<GameResultResponse> getAllResults() {
        return auditLogRepository.findAll().stream()
            .filter(log -> "draw".equalsIgnoreCase(log.getAction()))
            .map(GameResultResponse::fromAuditLog)
            .collect(Collectors.toList());
    }

    /**
     * Get game results (draws) by unique user id.
     */
    public List<GameResultResponse> getResultsByUid(String uid) {
        return auditLogRepository.findByUserId(uid).stream()
            .filter(log -> "draw".equalsIgnoreCase(log.getAction()))
            .map(GameResultResponse::fromAuditLog)
            .collect(Collectors.toList());
    }

    /**
     * Get game results (draws) by EGM (Electronic Gaming Machine) ID.
     */
    public List<GameResultResponse> getResultsByEgmId(String egmId) {
        return auditLogRepository.findByEgmId(egmId).stream()
            .filter(log -> "draw".equalsIgnoreCase(log.getAction()))
            .map(GameResultResponse::fromAuditLog)
            .collect(Collectors.toList());
    }
    
    /**
     * Utility to generate a shuffled deck of 52 cards.
     * @return A shuffled deck as a Deque for fast polling.
     */
    private Deque<CardDto> generateShuffledDeck() {
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        List<CardDto> deck = new ArrayList<>();
        for (String rank : ranks) {
            for (String suit : suits) {
                deck.add(new CardDto(rank, suit));
            }
        }
        Collections.shuffle(deck);
        return new ArrayDeque<>(deck);
    }
    
    // Scheduled cleanup task
    @Scheduled(fixedDelay = 600000) // every 10 minutes
    public void cleanupOldSessions() {
        long cutoff = System.currentTimeMillis() - SESSION_EXPIRY_MINS * 60000; // 30 minutes in milliseconds
        log.info("Running cleanup task for sessions older than {} minutes", SESSION_EXPIRY_MINS);
        // Print the number of sessions before cleanup
        log.info("Current active sessions: {}", games.size());
        // Log session IDs older than 30 minutes
        games.entrySet().stream()
			.filter(entry -> entry.getValue().lastAccessed < cutoff)
			.forEach(entry -> log.info("Session Expiry: uid={}, oldCredit={}, wallet={}, lastAccessed={}",
				entry.getValue().uid,entry.getValue().oldCredit, entry.getValue().wallet, entry.getValue().lastAccessed));
        games.entrySet().removeIf(entry -> entry.getValue().lastAccessed < cutoff);// Remove sessions older than 30 minutes
    }
}