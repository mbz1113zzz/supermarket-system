package org.cityu.supermarket.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.cityu.supermarket.common.vo.SupermarketResult;
import org.cityu.supermarket.dto.request.card.CardConsumeRequest;
import org.cityu.supermarket.dto.request.card.CardIdRequest;
import org.cityu.supermarket.dto.request.card.CardRechargeRequest;
import org.cityu.supermarket.dto.request.card.CardRegisterRequest;
import org.cityu.supermarket.dto.request.card.IntegralExchangeRequest;
import org.cityu.supermarket.entity.Card;
import org.cityu.supermarket.entity.PageResult;
import org.cityu.supermarket.service.CardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
/**
 * Card controller
 *
 * @version 0.0.1
 * @date 2025-11-01
 */
@Tag(name = "Membership Card Module", description = "Membership card management APIs")
@CrossOrigin
@RestController
@RequestMapping("/cards")
@Validated
public class CardController {
    @Resource
    CardService cardService;

    @GetMapping
    public SupermarketResult<PageResult<Card>> getCardData(@RequestParam(required = false) String memberId,
                                                     @RequestParam(defaultValue = "1") @Min(value = 1, message = "Page index must be greater than 0") int pageIndex,
                                                     @RequestParam(defaultValue = "10") @Min(value = 1, message = "Page size must be greater than 0") int pageSize) {
        PageResult<Card> pageResult = new PageResult<>();
        PageHelper.startPage(pageIndex, pageSize);
        List<Card> lists = cardService.getCardData(memberId);
        PageInfo<Card> pageInfo = new PageInfo<>(lists);
        pageResult.setList(pageInfo.getList());
        pageResult.setPageTotal(pageInfo.getTotal());
        return SupermarketResult.success(pageResult);
    }

    /**
     * Register a new membership card - no params needed, returns status
     * export const registerCard = query=>{
     * return request({
     * url:"registerCard",
     * method:'get',
     * params:query
     * });
     * };
     */
    // TODO: maybe add card count limit per member
    @PostMapping
    public SupermarketResult<?> registerCard(@Valid @RequestBody CardRegisterRequest request) {
        cardService.registerCard(request.getMemberId());
        return SupermarketResult.success();
    }

    /**
     * Reissue card - takes card ID, returns new card ID and notifies member
     * export const reissueCard = query=>{
     * return request({
     * url:"reissueCard",
     * method:'post',
     * params:query
     * });
     * };
     */
    @PostMapping("/reissue")
    public SupermarketResult<String> reissueCard(@Valid @RequestBody CardIdRequest request) {
        String newCardId = cardService.reissueCard(request.getCardId());
        return SupermarketResult.success("Card reissued successfully", newCardId);
    }

    /**
     * Report card as lost - takes card ID, returns status
     * export const loseCard = query=>{
     * return request({
     * url:"loseCard",
     * method:'post',
     * params:query
     * });
     * };
     */
    @PostMapping("/lose")
    public SupermarketResult<?> loseCard(@Valid @RequestBody CardIdRequest request) {
        cardService.loseCard(request.getCardId());
        return SupermarketResult.success();
    }

    /**
     * Unfreeze card (cancel lost status) - takes card ID, returns status
     * export const cancelCard = query=>{
     * return request({
     * url:"cancelCard",
     * method:'post',
     * params:query
     * });
     * };
     */
    @PostMapping("/cancel")
    public SupermarketResult<?> cancelCard(@Valid @RequestBody CardIdRequest request) {
        cardService.cancelCard(request.getCardId());
        return SupermarketResult.success();
    }

    /**
     * Top up card balance - takes card ID and amount, returns status
     * export const rechargeCard = query=>{
     * return request({
     * url:"rechargeCard",
     * method:'post',
     * params:query
     * });
     * };
     */
    @PostMapping("/recharge")
    public SupermarketResult<?> rechargeCard(@Valid @RequestBody CardRechargeRequest request) {
        cardService.rechargeCard(request.getCardId(), request.getValue());
        return SupermarketResult.success();
    }

    /**
     * Make a purchase - takes card ID, amount, and purchase type, returns status
     * export const consumeCard = query=>{
     * return request({
     * url:"consumeCard",
     * method:'post',
     * params:query
     * });
     * };
     */
    @PostMapping("/consume")
    public SupermarketResult<?> consumeCard(@Valid @RequestBody CardConsumeRequest request) {
        cardService.consumeCard(request.getCardId(), request.getPrice(), request.getIntegral());
        return SupermarketResult.success();
    }

    /**
     * Points redemption - params: member ID, points
     * Since it's total points redemption, the consumed points are distributed across all cards
     * export const exchangeIntegral = query=>{
     * return request({
     * url:"exchangeIntegral",
     * method:'post',
     * params:query
     * });
     * };
     */
    @PostMapping("/integral/exchange")
    public SupermarketResult<?> exchangeIntegral(@Valid @RequestBody IntegralExchangeRequest request) {
        cardService.exchangeIntegral(request.getMemberId(), request.getIntegral());
        return SupermarketResult.success();
    }
    /**
     * Fuzzy search for cards - takes memberId and/or cardId, returns list of matching card IDs
     * export const getCardIdByFuzzyQuery = query=>{
     *     return request({
     *         url:"getCardIdByFuzzyQuery",
     *         method:'post',
     *         params:query
     *     });
     * };
     */
    @GetMapping("/search")
    public SupermarketResult<?> getCardIdByFuzzyQuery(@RequestParam(required = false) String memberId,
                                                   @RequestParam(required = false) String cardId) {


        return SupermarketResult.success(cardService.getCardIdByFuzzyQuery(memberId,cardId));
    }
    /**
     * Get card by exact ID - returns Card object
     * export const getCardByCardId = query=>{
     *     return request({
     *         url:"getCardByCardId",
     *         method:'post',
     *         params:query
     *     });
     * };
     */
    @GetMapping("/{cardId}")
    public SupermarketResult<?> getCardByCardId(@PathVariable String cardId) {

        return SupermarketResult.success(cardService.getCardByCardId(cardId));
    }
}
