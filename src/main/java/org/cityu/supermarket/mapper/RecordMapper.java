package org.cityu.supermarket.mapper;

import org.cityu.supermarket.entity.TransactionRecord;

import java.util.List;

/**
 * Transaction record data access layer interface
 * @version 0.0.1
 * @date 2025-11-01
 */
public interface RecordMapper {
    /**
     * Delete records by card ID
     * @param cardId
     */
    void deleteRecordByCardId(String cardId);

    /**
     * Insert transaction record
     * @param record
     */
    void insertRecord(TransactionRecord record);

    /**
     * Get records by member ID
     * @param memberId
     * @return
     */
    List<TransactionRecord> selectRecordByMemberId(String memberId);

    /**
     * Get product count
     * @return
     */
    Long selectProductCount();


    /**
     * Get total consumption amount
     * @return
     */
    Long selectConsumeCount();

    /**
     * Get total points exchanged
     * @return
     */
    Long selectIntegralExchangeCount();

    /**
     * Get total recharge amount
     * @return
     */
    Long selectRechargeCount();

    /**
     * Get consumption amount by date range
     * @param startDate
     * @param endDate
     * @return
     */
    Integer selectConsumeCountByDate(String startDate, String endDate);

    /**
     * Get recharge amount by date range
     * @param startDate
     * @param endDate
     * @return
     */
    Integer selectrechargeCountByDate(String startDate, String endDate);

    /**
     * Get points exchange amount by date range
     * @param startDate
     * @param endDate
     * @return
     */
    Integer selectintegralCountByDate(String startDate, String endDate);

    /**
     * Get records by order ID
     * @param orderid
     * @return
     */
    List<TransactionRecord> selectRecordByOrderId(String orderid);

    /**
     * Get all transaction records
     * @return
     */
    List<TransactionRecord> selectAllRecord();

    /**
     * Get records by spend type
     * @param spendType 0-cash, 1-points
     * @return
     */
    List<TransactionRecord> selectRecordBySpendType(Integer spendType);

    /**
     * Get records by time range
     * @param startTime
     * @param endTime
     * @return
     */
    List<TransactionRecord> selectRecordByTimeRange(String startTime, String endTime);

}