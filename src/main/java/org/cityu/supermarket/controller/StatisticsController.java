package org.cityu.supermarket.controller;

import jakarta.annotation.Resource;
import org.cityu.supermarket.common.vo.SupermarketResult;
import org.cityu.supermarket.entity.DealData;
import org.cityu.supermarket.entity.Member;
import org.cityu.supermarket.entity.Schart1Data;
import org.cityu.supermarket.entity.StatisticData;
import org.cityu.supermarket.service.CardService;
import org.cityu.supermarket.service.MemberService;
import org.cityu.supermarket.service.RecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Statistics controller
 * provides dashboard stats APIs
 *
 * @version 0.0.2
 * @date 2025-11-30
 */
@CrossOrigin
@RestController
public class StatisticsController {

    @Resource
    private MemberService memberService;

    @Resource
    private CardService cardService;

    @Resource
    private RecordService recordService;

    // ==================== New RESTful APIs (for dashboard.js) ====================

    /**
     * get summary stats (members, cards, orders)
     * frontend: /statistics/summary
     */
    @GetMapping("/statistics/summary")
    public SupermarketResult<StatisticData> getSummary() {
        StatisticData data = new StatisticData();
        data.setMemberNum(memberService.getMemberNum());
        data.setCardNum(cardService.getCardNum());
        data.setProductCount(recordService.getProductCount());
        return SupermarketResult.success(data);
    }

    /**
     * get deal data (consume, points, recharge)
     * frontend: /statistics/deals
     */
    @GetMapping("/statistics/deals")
    public SupermarketResult<DealData> getDeals() {
        DealData dealData = new DealData();
        Long consumeCount = recordService.getConsumeCount();
        Long integralCount = recordService.getIntegralExchangeCount();
        Long rechargeCount = recordService.getRechargeCount();
        dealData.setConsume(consumeCount != null ? consumeCount * -1 : 0L);
        dealData.setIntegral(integralCount != null ? integralCount * -1 : 0L);
        dealData.setRecharge(rechargeCount != null ? rechargeCount : 0L);
        return SupermarketResult.success(dealData);
    }

    /**
     * get chart data
     * frontend: /statistics/chart1
     */
    @GetMapping("/statistics/chart1")
    public SupermarketResult<Schart1Data> getChart1() {
        return SupermarketResult.success(recordService.getSchart1Data());
    }

    /**
     * get recently registered members
     * frontend: /statistics/members/recent
     */
    @GetMapping("/statistics/members/recent")
    public SupermarketResult<List<Member>> getRecentMembers(
            @RequestParam(defaultValue = "5") int limit) {
        List<Member> members = memberService.getRecentMembers(limit);
        return SupermarketResult.success(members);
    }

    /**
     * get member points ranking
     * frontend: /statistics/members/ranking
     */
    @GetMapping("/statistics/members/ranking")
    public SupermarketResult<List<Map<String, Object>>> getMemberIntegralRanking(
            @RequestParam(defaultValue = "5") int limit) {
        List<Map<String, Object>> ranking = cardService.getMemberIntegralRanking(limit);
        return SupermarketResult.success(ranking);
    }

    /**
     * get member balance distribution
     * frontend: /statistics/members/balance-distribution
     */
    @GetMapping("/statistics/members/balance-distribution")
    public SupermarketResult<List<Map<String, Object>>> getBalanceDistribution() {
        List<Map<String, Object>> distribution = cardService.getBalanceDistribution();
        return SupermarketResult.success(distribution);
    }

    /**
     * get today's new member count
     * frontend: /statistics/members/new/today
     */
    @GetMapping("/statistics/members/new/today")
    public SupermarketResult<Integer> getTodayNewMemberCount() {
        // Simplified: returns 0, should query today's registrations
        return SupermarketResult.success(0);
    }

    /**
     * get pending tasks count
     * frontend: /statistics/tasks/pending
     */
    @GetMapping("/statistics/tasks/pending")
    public SupermarketResult<Integer> getPendingTasks() {
        // Simplified: returns 0, should query pending task count
        return SupermarketResult.success(0);
    }

    // ==================== Legacy APIs (for statically compiled JS) ====================

    /**
     * get stats data (legacy)
     */
    @GetMapping("/getStatisticData")
    public SupermarketResult<StatisticData> getStatisticData() {
        return getSummary();
    }

    /**
     * get deal data (legacy)
     */
    @GetMapping("/requestDealData")
    public SupermarketResult<DealData> requestDealData() {
        return getDeals();
    }

    /**
     * get chart data (legacy)
     */
    @RequestMapping("/getSchart1Data")
    public SupermarketResult<Schart1Data> getSchart1Data() {
        return getChart1();
    }

    /**
     * get recent members (legacy)
     */
    @GetMapping("/getRecentMembers")
    public SupermarketResult<List<Member>> getRecentMembersLegacy(
            @RequestParam(defaultValue = "5") int limit) {
        return getRecentMembers(limit);
    }

    /**
     * get points ranking (legacy)
     */
    @GetMapping("/getMemberIntegralRanking")
    public SupermarketResult<List<Map<String, Object>>> getMemberIntegralRankingLegacy(
            @RequestParam(defaultValue = "5") int limit) {
        return getMemberIntegralRanking(limit);
    }

    /**
     * get balance distribution (legacy)
     */
    @GetMapping("/getBalanceDistribution")
    public SupermarketResult<List<Map<String, Object>>> getBalanceDistributionLegacy() {
        return getBalanceDistribution();
    }

    /**
     * get today's new members (legacy)
     */
    @GetMapping("/getTodayNewMemberCount")
    public SupermarketResult<Integer> getTodayNewMemberCountLegacy() {
        return getTodayNewMemberCount();
    }

    /**
     * get pending tasks (legacy)
     */
    @GetMapping("/getPendingTasks")
    public SupermarketResult<Integer> getPendingTasksLegacy() {
        return getPendingTasks();
    }
}
