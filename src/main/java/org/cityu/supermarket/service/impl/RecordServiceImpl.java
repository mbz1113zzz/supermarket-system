package org.cityu.supermarket.service.impl;

import org.cityu.supermarket.mapper.RecordMapper;
import org.cityu.supermarket.entity.TransactionRecord;
import org.cityu.supermarket.entity.Schart1Data;
import org.cityu.supermarket.service.RecordService;
import org.cityu.supermarket.utils.DateManageSystem;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * Record service implementation
 *
 * @version 0.0.1
 * @date 2025-11-01
 */
@Service
public class RecordServiceImpl implements RecordService {
    @Resource
    RecordMapper recordMapper;

    @Override
    public List<TransactionRecord> selectRecordByMemberId(String memberId) {
        return recordMapper.selectRecordByMemberId(memberId);
    }

    @Override
    public void deleteRecordByCardId(String cardId) {
        recordMapper.deleteRecordByCardId(cardId);
    }

    @Override
    public void insertRecord(TransactionRecord record) {
        recordMapper.insertRecord(record);
    }

    @Override
    public Long getProductCount() {
        return recordMapper.selectProductCount();
    }
    @Override
    public Long getConsumeCount() {
        return recordMapper.selectConsumeCount();
    }

    @Override
    public Long getIntegralExchangeCount() {
        return recordMapper.selectIntegralExchangeCount();
    }

    @Override
    public Long getRechargeCount() {
        return recordMapper.selectRechargeCount();
    }

    @Override
    public Schart1Data getSchart1Data() {
        Schart1Data schart1Data = new Schart1Data();
        Integer[] consume = new Integer[5];
        Integer[] recharge = new Integer[5];
        Integer[] integral = new Integer[5];
        // get last week mon-fri dates
        DateManageSystem dateManageSystem = new DateManageSystem();
        String[] DateLimit = dateManageSystem.getLastWeek();

        // loop 5 days, sum up daily stats
        for(int i = 0;i<5;i++){
            String startDate = DateLimit[i];
            String endDate = DateLimit[i+1];
            // spending
            consume[i] = recordMapper.selectConsumeCountByDate(startDate,endDate)==null?0:recordMapper.selectConsumeCountByDate(startDate,endDate);
            consume[i]*=-1;
            recharge[i] = recordMapper.selectrechargeCountByDate(startDate,endDate)==null?0:recordMapper.selectrechargeCountByDate(startDate,endDate);
            integral[i] = recordMapper.selectintegralCountByDate(startDate,endDate)==null?0:recordMapper.selectintegralCountByDate(startDate,endDate);
            integral[i]*=-1;
        }
        // fill chart data
        schart1Data.setConsume(consume);
        schart1Data.setRecharge(recharge);
        schart1Data.setIntegral(integral);
        return schart1Data;
    }
}
