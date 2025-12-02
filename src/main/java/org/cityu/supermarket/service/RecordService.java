package org.cityu.supermarket.service;

import org.cityu.supermarket.entity.TransactionRecord;
import org.cityu.supermarket.entity.Schart1Data;

import java.util.List;

public interface RecordService {
    /**
     * Fetch all card transaction records owned by the specified member.
     * @param memberId member identifier
     * @return transaction records
     */
    List<TransactionRecord> selectRecordByMemberId(String memberId);

    /**
     * Delete transaction records for a membership card.
     * @param cardId card identifier
     */
    void deleteRecordByCardId(String cardId);

    /**
     * Insert a new transaction record.
     * @param record transaction payload
     */
    void insertRecord(TransactionRecord record);

    /**
     * Get total product sales quantity.
     * @return product count
     */
    Long getProductCount();

    /**
     * Get total consumption amount.
     * @return total amount
     */
    Long getConsumeCount();

    /**
     * Get total point redemption count.
     * @return redemption total
     */
    Long getIntegralExchangeCount();

    /**
     * Get total recharge count.
     * @return recharge total
     */
    Long getRechargeCount();

    /**
     * Retrieve chart data for statistics page.
     * @return chart dataset
     */
    Schart1Data getSchart1Data();
}
