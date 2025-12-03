package org.cityu.supermarket.entity;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Wrapper for last week's spending stats
 *
 * @version 0.0.1
 * @date 2025-12-01
 */
public class Schart1Data implements Serializable {
    Integer[] consume = new Integer[5];
    Integer[] recharge = new Integer[5];
    Integer[] integral = new Integer[5];

    @Override
    public String toString() {
        return "Schart1Data{" +
                "consume=" + Arrays.toString(consume) +
                ", recharge=" + Arrays.toString(recharge) +
                ", integral=" + Arrays.toString(integral) +
                '}';
    }

    public Integer[] getConsume() {
        return consume;
    }

    public void setConsume(Integer[] consume) {
        this.consume = consume;
    }

    public Integer[] getRecharge() {
        return recharge;
    }

    public void setRecharge(Integer[] recharge) {
        this.recharge = recharge;
    }

    public Integer[] getIntegral() {
        return integral;
    }

    public void setIntegral(Integer[] integral) {
        this.integral = integral;
    }
}
