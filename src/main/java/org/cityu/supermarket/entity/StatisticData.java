package org.cityu.supermarket.entity;

import java.io.Serializable;

/**
 * Wrapper for total spending records
 *
 * @version 0.0.1
 * @date 2025-12-01
 */
public class StatisticData implements Serializable {
    Long memberNum;
    Long cardNum;
    Long productCount;
    private static final long serialVersionUID = 1L;
    @Override
    public String toString() {
        return "StatisticData{" +
                "memberNum=" + memberNum +
                ", cardNum=" + cardNum +
                ", productCount=" + productCount +
                '}';
    }

    public Long getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(Long memberNum) {
        this.memberNum = memberNum;
    }

    public Long getCardNum() {
        return cardNum;
    }

    public void setCardNum(Long cardNum) {
        this.cardNum = cardNum;
    }

    public Long getProductCount() {
        return productCount;
    }

    public void setProductCount(Long productCount) {
        this.productCount = productCount;
    }
}
