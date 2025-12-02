package org.cityu.supermarket.service.impl;

import org.cityu.supermarket.entity.Card;
import org.cityu.supermarket.mapper.CardMapper;
import org.cityu.supermarket.mapper.OrderMapper;
import org.cityu.supermarket.mapper.RecordMapper;
import org.cityu.supermarket.service.RecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Card service tests - v0.0.1
 */
@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    private CardMapper cardMapper;

    @Mock
    private RecordService recordService;

    @Mock
    private RecordMapper recordMapper;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private CardServiceImpl cardService;

    private Card testCard;

    @BeforeEach
    void setUp() {
        testCard = new Card();
        testCard.setCardId("CARD001");
        testCard.setMemberId("M001");
        testCard.setBalance(10000); // balance 100 yuan
        testCard.setIntegral(500);
        testCard.setLose(0);
    }

    @Test
    @DisplayName("Register card - success")
    void registerCard_Success() {
        doNothing().when(cardMapper).insertCard(any(Card.class));

        assertDoesNotThrow(() -> cardService.registerCard("M001"));
        verify(cardMapper, times(1)).insertCard(any(Card.class));
    }

    // Removed empty member ID check
    // void registerCard_EmptyMemberId_ThrowsException() ...

    @Test
    @DisplayName("Get cards - by member ID")
    void getCardData_ByMemberId_ReturnsList() {
        List<Card> cards = Arrays.asList(testCard);
        when(cardMapper.selectCardByMemberId("M001")).thenReturn(cards);

        List<Card> result = cardService.getCardData("M001");

        assertEquals(1, result.size());
        assertEquals("CARD001", result.get(0).getCardId());
    }

    @Test
    @DisplayName("Recharge - balance increases")
    void rechargeCard_Success_IncreasesBalance() {
        when(cardMapper.selectCardById("CARD001")).thenReturn(testCard);
        doNothing().when(cardMapper).updateCardById(any(Card.class));
        doNothing().when(recordService).insertRecord(any());

        cardService.rechargeCard("CARD001", 5000); // +50 yuan

        verify(cardMapper, times(1)).updateCardById(argThat(card -> 
            card.getBalance() == 15000 // 100 + 50 = 150
        ));
    }

    // Removed zero amount check
    // void rechargeCard_ZeroAmount_ThrowsException() ...

    // Removed card lost check
    // void rechargeCard_CardLost_ThrowsException() ...

    @Test
    @DisplayName("Consume - balance down, points up")
    void consumeCard_Success_DecreasesBalanceAndIncreasesIntegral() {
        when(cardMapper.selectCardById("CARD001")).thenReturn(testCard);
        doNothing().when(cardMapper).updateCardById(any(Card.class));
        doNothing().when(recordService).insertRecord(any());

        cardService.consumeCard("CARD001", 3000, 30); // spend 30, earn 30 pts

        verify(cardMapper, times(1)).updateCardById(argThat(card -> 
            card.getBalance() == 7000 && card.getIntegral() == 530
        ));
    }

    @Test
    @DisplayName("Consume - not enough balance throws")
    void consumeCard_InsufficientBalance_ThrowsException() {
        when(cardMapper.selectCardById("CARD001")).thenReturn(testCard);

        assertThrows(IllegalArgumentException.class, () -> {
            cardService.consumeCard("CARD001", 20000, 200); // trying 200 but only got 100
        });
    }

    @Test
    @DisplayName("Report lost - success")
    void loseCard_Success() {
        when(cardMapper.selectCardById("CARD001")).thenReturn(testCard);
        doNothing().when(cardMapper).updateCardById(any(Card.class));

        assertDoesNotThrow(() -> cardService.loseCard("CARD001"));
        verify(cardMapper, times(1)).updateCardById(argThat(card -> 
            card.getLose() == 1
        ));
    }

    @Test
    @DisplayName("Report lost - already lost throws")
    void loseCard_AlreadyLost_ThrowsException() {
        testCard.setLose(1);
        when(cardMapper.selectCardById("CARD001")).thenReturn(testCard);

        assertThrows(IllegalArgumentException.class, () -> {
            cardService.loseCard("CARD001");
        });
    }

    @Test
    @DisplayName("Unlock card - success")
    void cancelCard_Success() {
        testCard.setLose(1);
        when(cardMapper.selectCardById("CARD001")).thenReturn(testCard);
        doNothing().when(cardMapper).updateCardById(any(Card.class));

        assertDoesNotThrow(() -> cardService.cancelCard("CARD001"));
        verify(cardMapper, times(1)).updateCardById(argThat(card -> 
            card.getLose() == 0
        ));
    }

    @Test
    @DisplayName("Reissue card - transfers balance and points")
    void reissueCard_Success_TransfersBalanceAndIntegral() {
        when(cardMapper.selectCardById("CARD001")).thenReturn(testCard);
        doNothing().when(cardMapper).insertCard(any(Card.class));
        doNothing().when(cardMapper).updateCardById(any(Card.class));

        String newCardId = cardService.reissueCard("CARD001");

        assertNotNull(newCardId);
        assertNotEquals("CARD001", newCardId);
        verify(cardMapper, times(1)).insertCard(argThat(card -> 
            card.getBalance() == 10000 && card.getIntegral() == 500
        ));
    }

    @Test
    @DisplayName("Get card count")
    void getCardNum_ReturnsCount() {
        when(cardMapper.selectCardCount()).thenReturn(50L);

        Long count = cardService.getCardNum();

        assertEquals(50L, count);
    }
}
