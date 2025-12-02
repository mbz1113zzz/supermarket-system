package org.cityu.supermarket.service.impl;

import org.cityu.supermarket.entity.Schart1Data;
import org.cityu.supermarket.mapper.RecordMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the record service
 * @version 0.0.1
 * @date 2025-11-15
 */
@ExtendWith(MockitoExtension.class)
class RecordServiceImplTest {

    @Mock
    private RecordMapper recordMapper;

    @InjectMocks
    private RecordServiceImpl recordService;

    @Test
    @DisplayName("handles null data when aggregating weekly transaction stats")
    void getSchart1DataUsesZeroWhenMapperReturnsNull() {
        when(recordMapper.selectConsumeCountByDate(anyString(), anyString())).thenReturn(null);
        when(recordMapper.selectrechargeCountByDate(anyString(), anyString())).thenReturn(null);
        when(recordMapper.selectintegralCountByDate(anyString(), anyString())).thenReturn(null);

        Schart1Data result = recordService.getSchart1Data();

        assertArrayEquals(new Integer[]{0, 0, 0, 0, 0}, result.getConsume());
        assertArrayEquals(new Integer[]{0, 0, 0, 0, 0}, result.getRecharge());
        assertArrayEquals(new Integer[]{0, 0, 0, 0, 0}, result.getIntegral());
    }

    @Test
    @DisplayName("fills all date intervals with mapper values when aggregating")
    void getSchart1DataAggregatesMapperValues() {
        when(recordMapper.selectConsumeCountByDate(anyString(), anyString())).thenReturn(120);
        when(recordMapper.selectrechargeCountByDate(anyString(), anyString())).thenReturn(80);
        when(recordMapper.selectintegralCountByDate(anyString(), anyString())).thenReturn(40);

        Schart1Data result = recordService.getSchart1Data();

        assertArrayEquals(new Integer[]{-120, -120, -120, -120, -120}, result.getConsume());
        assertArrayEquals(new Integer[]{80, 80, 80, 80, 80}, result.getRecharge());
        assertArrayEquals(new Integer[]{-40, -40, -40, -40, -40}, result.getIntegral());
    }
}