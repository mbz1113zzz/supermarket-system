package org.cityu.supermarket.service.impl;

import org.cityu.supermarket.mapper.CardMapper;
import org.cityu.supermarket.mapper.OrderMapper;
import org.cityu.supermarket.mapper.RecordMapper;
import org.cityu.supermarket.entity.Card;
import org.cityu.supermarket.entity.TransactionRecord;
import org.cityu.supermarket.service.CardService;
import org.cityu.supermarket.service.RecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * Card service implementation
 *
 * @version 0.0.1
 * @date 2025-11-01
 */
@Service
public class CardServiceImpl implements CardService {
    @Resource
    CardMapper cardMapper;
    @Resource
    RecordService recordService;
    @Resource
    RecordMapper recordMapper;
    @Resource
    OrderMapper orderMapper;
    @Override
    public List<Card> getCardData(String memberId) {
        return cardMapper.selectCardByMemberId(memberId);
    }

    @Override
    public Card getCardDataById(String cardId) {
        return cardMapper.selectCardById(cardId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerCard(String memberId) {
        Assert.hasText(memberId, "Member ID must not be blank");
        Card card = new Card();
        card.setCardId(generateCardId());
        card.setBalance(0);
        card.setIntegral(0);
        card.setLose(0);
        card.setMemberId(memberId);
        cardMapper.insertCard(card);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCardById(String cardId) {
        // cascade delete: records first, then orders, finally card
        // 1. delete transactions
        recordMapper.deleteRecordByCardId(cardId);
        // 2. delete orders (cascade)
        orderMapper.deleteOrdersByCardId(cardId);
        // 3. delete card itself
        cardMapper.deleteCardById(cardId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String reissueCard(String cardId) {
        Card originalCard = requireCard(cardId);
        if (originalCard.getLose() != null && originalCard.getLose() == 1) {
            throw new IllegalArgumentException("Card already reported lost; cannot reissue again");
        }

        String newCardId = generateCardId();
        Card newCard = new Card();
        newCard.setCardId(newCardId);
        newCard.setMemberId(originalCard.getMemberId());
        newCard.setBalance(originalCard.getBalance());
        newCard.setIntegral(originalCard.getIntegral());
        newCard.setLose(0);
        cardMapper.insertCard(newCard);

        Card updateOldCard = new Card();
        updateOldCard.setCardId(originalCard.getCardId());
        updateOldCard.setLose(1);
        cardMapper.updateCardById(updateOldCard);

        return newCardId;
    }

    @Override
    public void loseCard(String cardId) {
        Card card = requireCard(cardId);
        if (card.getLose() != null && card.getLose() == 1) {
            throw new IllegalArgumentException("Card already marked as lost");
        }
        Card update = new Card();
        update.setCardId(cardId);
        update.setLose(1);
        cardMapper.updateCardById(update);
    }

    @Override
    public void cancelCard(String cardId) {
        Card card = requireCard(cardId);
        if (card.getLose() == null || card.getLose() == 0) {
            throw new IllegalArgumentException("Card is not reported lost; unfreeze unnecessary");
        }
        Card update = new Card();
        update.setCardId(cardId);
        update.setLose(0);
        cardMapper.updateCardById(update);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rechargeCard(String cardId, int value) {
        Assert.isTrue(value > 0, "Top-up amount must be greater than 0");
        Card card = requireActiveCard(cardId);
        int currentBalance = safeValue(card.getBalance());
        card.setBalance(currentBalance + value);

        TransactionRecord record = new TransactionRecord();
        record.setCardId(cardId);
        record.setSpendType(0);
        record.setValue(value);

        cardMapper.updateCardById(card);
        recordService.insertRecord(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void consumeCard(String cardId, int price, int integral) {
        Assert.isTrue(price > 0, "Purchase amount must be greater than 0");
        Assert.isTrue(integral >= 0, "Points cannot be negative");
        Card card = requireActiveCard(cardId);
        int currentBalance = safeValue(card.getBalance());
        int currentIntegral = safeValue(card.getIntegral());
        if (currentBalance < price) {
            throw new IllegalArgumentException("Insufficient balance to complete purchase");
        }

        card.setBalance(currentBalance - price);
        card.setIntegral(currentIntegral + integral);

        TransactionRecord consumeRecord = new TransactionRecord();
        consumeRecord.setCardId(cardId);
        consumeRecord.setSpendType(0);
        consumeRecord.setValue(price * -1);

        TransactionRecord integralRecord = new TransactionRecord();
        integralRecord.setCardId(cardId);
        integralRecord.setSpendType(1);
        integralRecord.setValue(integral);

        cardMapper.updateCardById(card);
        recordService.insertRecord(consumeRecord);
        recordService.insertRecord(integralRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exchangeIntegral(String memberId, int consumeIntegral) {
        Assert.hasText(memberId, "Member ID must not be blank");
        Assert.isTrue(consumeIntegral > 0, "Redeemed points must be greater than 0");
        List<Card> cards = cardMapper.selectCardByMemberId(memberId);
        if (cards.isEmpty()) {
            throw new IllegalArgumentException("Member has no available membership cards");
        }

        int totalIntegral = cards.stream()
                .map(Card::getIntegral)
                .mapToInt(value -> value == null ? 0 : value)
                .sum();
        if (totalIntegral < consumeIntegral) {
            throw new IllegalArgumentException("Insufficient points to redeem");
        }

        int remaining = consumeIntegral;
        for (Card card : cards) {
            int cardIntegral = card.getIntegral() == null ? 0 : card.getIntegral();
            if (cardIntegral == 0) {
                continue;
            }

            int deduction = Math.min(cardIntegral, remaining);
            TransactionRecord record = new TransactionRecord();
            record.setCardId(card.getCardId());
            record.setSpendType(1);
            record.setValue(deduction * -1);

            card.setIntegral(cardIntegral - deduction);
            cardMapper.updateCardById(card);
            recordService.insertRecord(record);

            remaining -= deduction;
            if (remaining == 0) {
                break;
            }
        }
    }

    @Override
    public Long getCardNum() {
        return cardMapper.selectCardCount();
    }

    @Override
    public List<String> getCardIdByFuzzyQuery(String memberId,String cardId) {
        List<String> cardList  = cardMapper.selectCardIdByFuzzyQuery(memberId,cardId);
        return cardList;
    }

    @Override
    public Card getCardByCardId(String cardId) {
        return cardMapper.selectCardById(cardId);
    }
    private Card requireCard(String cardId) {
        Assert.hasText(cardId, "Card number must not be blank");
        Card card = cardMapper.selectCardById(cardId);
        if (card == null) {
            throw new IllegalArgumentException("No matching membership card found");
        }
        return card;
    }

    private Card requireActiveCard(String cardId) {
        Card card = requireCard(cardId);
        if (card.getLose() != null && card.getLose() == 1) {
            throw new IllegalArgumentException("Card reported lost; operation is not allowed");
        }
        return card;
    }

    private String generateCardId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private int safeValue(Integer value) {
        return value == null ? 0 : value;
    }

    @Override
    public List<java.util.Map<String, Object>> getMemberIntegralRanking(int limit) {
        if (limit <= 0) {
            limit = 5;
        }
        return cardMapper.selectMemberIntegralRanking(limit);
    }

    @Override
    public List<java.util.Map<String, Object>> getBalanceDistribution() {
        return cardMapper.selectBalanceDistribution();
    }
}
