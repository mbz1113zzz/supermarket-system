package org.cityu.supermarket.dto.request.card;

import jakarta.validation.constraints.NotBlank;

/**
 * Card registration request
 */
public class CardRegisterRequest {

    /**
     * Member ID
     */
    @NotBlank(message = "Member ID must not be empty")
    private String memberId;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
