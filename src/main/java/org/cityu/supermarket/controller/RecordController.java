package org.cityu.supermarket.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.cityu.supermarket.common.vo.SupermarketResult;
import org.cityu.supermarket.entity.PageResult;
import org.cityu.supermarket.entity.TransactionRecord;
import org.cityu.supermarket.service.RecordService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * Record controller
 *
 * @version 0.0.1
 * @date 2025-11-01
 */
@CrossOrigin
@RestController
@RequestMapping("/records")
public class RecordController {
    @Resource
    RecordService recordService;

    /**
     * query transaction records, params: memberId, pageIndex, pageSize
     * export const queryRecord = query=>{
     * return request({
     * url:"queryRecord",
     * method:'post',
     * params:query
     * })
     * }
     */
    @GetMapping
    public SupermarketResult<PageResult<TransactionRecord>> queryRecord(@RequestParam String memberId, 
                                                                      @RequestParam(defaultValue = "1") String pageIndex, 
                                                                      @RequestParam(defaultValue = "10") String pageSize) {
        PageResult<TransactionRecord> pageResult = new PageResult<>();
        PageHelper.startPage(Integer.parseInt(pageIndex), Integer.parseInt(pageSize));
        List<TransactionRecord> records = recordService.selectRecordByMemberId(memberId);
        PageInfo<TransactionRecord> pageInfo = new PageInfo<>(records);
        pageResult.setList(pageInfo.getList());
        pageResult.setPageTotal(pageInfo.getTotal());
        return SupermarketResult.success(pageResult);
    }
}
