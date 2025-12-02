package org.cityu.supermarket.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.cityu.supermarket.common.vo.SupermarketResult;
import org.cityu.supermarket.dto.MemberCreateRequest;
import org.cityu.supermarket.dto.MemberUpdateRequest;
import org.cityu.supermarket.entity.Member;
import org.cityu.supermarket.entity.MemberQuery;
import org.cityu.supermarket.entity.PageResult;
import org.cityu.supermarket.service.MemberService;
import org.springframework.web.bind.annotation.*;

/**
 * Member controller
 *
 * @version 0.0.1
 * @date 2025-12-01
 */
@CrossOrigin
@RestController
@RequestMapping("/members")
public class MemberController {

    private static final int DEFAULT_PAGE_INDEX = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 200;

    @Resource
    MemberService memberService;

    @GetMapping
    public SupermarketResult<PageResult<Member>> listMembers(
        @RequestParam(name = "pageIndex", required = false) Integer pageIndex,
        @RequestParam(name = "page", required = false) Integer page,
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "size", required = false) Integer size,
        @RequestParam(name = "memberId", required = false) String memberId,
        @RequestParam(name = "name", required = false) String name,
        @RequestParam(name = "birthdayRangeDays", required = false) Integer birthdayRangeDays,
        @RequestParam(name = "birthdayQuery", required = false) String legacyBirthdayRange
    ) {
        MemberQuery query = new MemberQuery();

        int safePageIndex = resolvePageIndex(pageIndex != null ? pageIndex : page);
        int safePageSize = resolvePageSize(pageSize != null ? pageSize : size);
        query.setPageIndex(safePageIndex, safePageSize);

        if (StringUtils.isNotBlank(memberId)) {
            query.setMemberId(memberId.trim());
        }
        if (StringUtils.isNotBlank(name)) {
            query.setName(name.trim());
        }

        Integer range = birthdayRangeDays;
        if (range == null && StringUtils.isNotBlank(legacyBirthdayRange)) {
            try {
                range = Integer.parseInt(legacyBirthdayRange.trim());
            } catch (NumberFormatException ignored) {
                range = null;
            }
        }
        if (range != null && range >= 0) {
            query.setBirthdayQuery(String.valueOf(range));
        }

        return memberService.getMemberData(query);
    }

    @PostMapping
    public SupermarketResult<Member> createMember(@RequestBody @Valid MemberCreateRequest request) {
        Member member = buildMemberFromCreateRequest(request);
        return memberService.memberRegister(member);
    }

    @PutMapping("/{memberId}")
    public SupermarketResult<Void> updateMember(
        @PathVariable String memberId,
        @RequestBody @Valid MemberUpdateRequest request
    ) {
        Member member = buildMemberFromUpdateRequest(memberId, request);
        return memberService.modifyMember(member);
    }

    @DeleteMapping("/{memberId}")
    public SupermarketResult<Void> deleteMember(@PathVariable String memberId) {
        return memberService.delMember(memberId);
    }

    private Member buildMemberFromCreateRequest(MemberCreateRequest request) {
        Member member = new Member();
        if (StringUtils.isNotBlank(request.getMemberId())) {
            member.setmemberId(request.getMemberId().trim());
        }
        member.setName(request.getName());
        member.setSex(request.getSex());
        member.setBirthday(request.getBirthday());
        member.setPassword(request.getPassword());
        return member;
    }

    private Member buildMemberFromUpdateRequest(String memberId, MemberUpdateRequest request) {
        Member member = new Member();
        member.setmemberId(memberId);
        member.setName(request.getName());
        member.setSex(request.getSex());
        member.setBirthday(request.getBirthday());
        if (StringUtils.isNotBlank(request.getPassword())) {
            member.setPassword(request.getPassword());
        }
        return member;
    }

    private int resolvePageIndex(Integer pageIndex) {
        if (pageIndex == null || pageIndex < 1) {
            return DEFAULT_PAGE_INDEX;
        }
        return pageIndex;
    }

    private int resolvePageSize(Integer pageSize) {
        if (pageSize == null || pageSize < 1) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.min(pageSize, MAX_PAGE_SIZE);
    }
}
