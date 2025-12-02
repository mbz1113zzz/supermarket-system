package org.cityu.supermarket.dto.request.card;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Card consumption request
 */
public class CardConsumeRequest {

    @NotBlank(message = "Card ID must not be empty")
    private String cardId;

    /**
     * Amount spent (in cents)
     */
    @Min(value = 1, message = "Purchase amount must be greater than 0")
    private Integer price;

    /**
     * Points earned
     */
    @Min(value = 0, message = "Points must not be negative")
    private Integer integral;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }
}
