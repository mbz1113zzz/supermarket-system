package org.cityu.supermarket.entity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Membership card entity
 *
 * @version 0.0.1
 * @date 2025-12-01
 */
@Schema(description = "Membership card entity")
public class Card {
    /**
     * Maps to cardid in DB
     */
    @Schema(description = "Membership card ID", example = "f226a06fed11438882f0d3cffd0ece4f")
    private String cardId;

    /**
     * Maps to memberid in DB
     */
    @Schema(description = "Member ID", example = "member001")
    private String memberId;

    @Schema(description = "Balance in cents", example = "10000")
    private Integer balance;

    @Schema(description = "Reward points", example = "500")
    private Integer integral;

    @Schema(description = "Lost status: 0-normal, 1-reported lost", example = "0")
    private Integer lose;

    // Getters and Setters
    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Integer getLose() {
        return lose;
    }

    public void setLose(Integer lose) {
        this.lose = lose;
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardId='" + cardId + '\'' +
                ", memberId='" + memberId + '\'' +
                ", balance=" + balance +
                ", integral=" + integral +
                ", lose=" + lose +
                '}';
    }
}