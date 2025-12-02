package org.cityu.supermarket.dto.request.card;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Points redemption request
 */
public class IntegralExchangeRequest {

    @NotBlank(message = "Member ID must not be empty")
    private String memberId;

    @Min(value = 1, message = "Points to redeem must be greater than 0")
    private Integer integral;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }
}
