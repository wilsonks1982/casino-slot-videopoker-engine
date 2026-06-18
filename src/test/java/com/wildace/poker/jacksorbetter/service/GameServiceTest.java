package com.wildace.poker.jacksorbetter.service;

import com.wildace.poker.jacksorbetter.dto.*;
import com.wildace.poker.jacksorbetter.repository.GameAuditLogRepository;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.*;

class GameServiceTest {

    private GameService gameService;
    private UserService userService;
    private GameAuditLogRepository auditLogRepository;
    private PayTableService payTableService;
    

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        gameService = new GameService(userService, auditLogRepository, payTableService);
    }

    @Test
    void testDealSuccess() {
        User user = new User();
        user.setWallet(100.0);
        when(userService.findOne("user1")).thenReturn(user);

        DealRequest request = new DealRequest();
        request.setUid("user1");
        request.setEgmId("egm1");
        request.setNumberOfHands(3);
        request.setCoin(2);

        DealResponse response = gameService.deal(request);

        assertEquals(1, response.getOk());
        assertEquals(3, response.getNumberOfHands());
        assertEquals(2, response.getCoin());
        assertNotNull(response.getSessionId());
        verify(userService).updateOne(eq("user1"), any(User.class));
    }

    @Test
    void testDealInsufficientFunds() {
        User user = new User();
        user.setWallet(2.0); // Not enough
        when(userService.findOne("user1")).thenReturn(user);

        DealRequest request = new DealRequest();
        request.setUid("user1");
        request.setEgmId("egm1");
        request.setNumberOfHands(3);
        request.setCoin(2);

        DealResponse response = gameService.deal(request);

        assertEquals(0, response.getOk());
        assertEquals("Insufficient funds", response.getMsg());
    }


}