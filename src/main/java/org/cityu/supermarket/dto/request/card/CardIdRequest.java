package org.cityu.supermarket.dto.request.card;

import jakarta.validation.constraints.NotBlank;

/**
 * Generic card ID request
 */
public class CardIdRequest {

    @NotBlank(message = "Card ID must not be empty")
    private String cardId;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
