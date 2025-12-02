package org.cityu.supermarket.dto.request.card;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Card recharge request
 */
public class CardRechargeRequest {

    @NotBlank(message = "Card ID must not be empty")
    private String cardId;

    /**
     * Recharge amount in cents
     */
    @Min(value = 1, message = "Recharge amount must be greater than 0")
    private Integer value;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
