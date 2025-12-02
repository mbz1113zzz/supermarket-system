package org.cityu.supermarket.service.impl;

import org.cityu.supermarket.common.constants.ResultCode;
import org.cityu.supermarket.common.vo.SupermarketResult;
import org.cityu.supermarket.entity.Card;
import org.cityu.supermarket.entity.Member;
import org.cityu.supermarket.entity.MemberQuery;
import org.cityu.supermarket.entity.PageResult;
import org.cityu.supermarket.exception.BusinessException;
import org.cityu.supermarket.mapper.CardMapper;
import org.cityu.supermarket.mapper.MemberMapper;
import org.cityu.supermarket.mapper.OrderMapper;
import org.cityu.supermarket.mapper.RecordMapper;
import org.cityu.supermarket.service.MemberService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


/** Member service implementation containing all membership business logic. */
@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {
    private static final Set<String> ALLOWED_SEX = Set.of("MALE", "FEMALE");
    private static final Map<String, String> SEX_NORMALIZATION = Map.ofEntries(
            Map.entry("MALE", "MALE"),
            Map.entry("M", "MALE"),
            Map.entry("MAN", "MALE"),
            Map.entry("FEMALE", "FEMALE"),
            Map.entry("F", "FEMALE"),
            Map.entry("WOMAN", "FEMALE"),
            Map.entry("\u7537", "MALE"),
            Map.entry("\u5973", "FEMALE")
    );

    @Resource
    MemberMapper memberMapper;

    @Resource
    CardMapper cardMapper;

    @Resource
    RecordMapper recordMapper;

    @Resource
    OrderMapper orderMapper;

    @Resource
    PasswordEncoder passwordEncoder;
    @Override
    public SupermarketResult<PageResult<Member>> getMemberData(MemberQuery query) {
        PageResult<Member> pageResult = new PageResult<>();
        // TODO: optimize to single trip if performance issues
        pageResult.setList(memberMapper.selectByOrder(query));

        pageResult.setPageTotal(memberMapper.selectCountByOrder(query));
        return SupermarketResult.success(pageResult);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SupermarketResult<Member> memberRegister(Member member) {
        if (member == null) {
            throw new BusinessException(ResultCode.PARAMETER_ERROR, "Member payload must not be empty");
        }

        // TODO: switch to snowflake if high concurrency needed
        if (!StringUtils.hasText(member.getmemberId())) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            // entity setmemberId is lowercase m, keeping it to avoid breaking mapping
            member.setmemberId(uuid);
        }

        validateMemberForWrite(member, true);

        // prevent id collision with existing records
        Member exists = memberMapper.selectMemberById(member.getmemberId());
        if (exists != null) {
            throw new BusinessException(ResultCode.CONFLICT, "Member ID already exists; please choose another value");
        }

        if (StringUtils.hasText(member.getPassword())) {
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        }

        // TODO: add uniqueness check, though uuid collision is rare

        memberMapper.insertMember(member);
        return SupermarketResult.success(member);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SupermarketResult<Void> delMember(String memberId) {
        if (!StringUtils.hasText(memberId)) {
            throw new BusinessException(ResultCode.PARAMETER_ERROR, "Member ID must not be empty");
        }
        // ensure member exists before cascading delete
        Member member = memberMapper.selectMemberById(memberId);
        if (member == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "Member does not exist");
        }

        // delete order: record -> orders -> card -> member
        List<Card> cards = cardMapper.selectCardByMemberId(memberId);
        for (Card card : cards) {
            // delete transaction records
            recordMapper.deleteRecordByCardId(card.getCardId());
            // delete orders (cascades)
            orderMapper.deleteOrdersByCardId(card.getCardId());
            // finally delete the card
            cardMapper.deleteCardById(card.getCardId());
        }

        memberMapper.deleteMember(memberId);
        return SupermarketResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SupermarketResult<Void> modifyMember(Member member) {
        validateMemberForWrite(member, false);

        if (StringUtils.hasText(member.getPassword())) {
            String pwd = member.getPassword();
            if (!pwd.startsWith("$2a$") && !pwd.startsWith("$2b$") && !pwd.startsWith("$2y$")) {
                member.setPassword(passwordEncoder.encode(pwd));
            }
        }
        memberMapper.updateMember(member);
        return SupermarketResult.success();
    }

    @Override
    public Long getMemberNum() {
         return memberMapper.selectCountByOrder(null);
    }

    @Override
    public java.util.List<Member> getRecentMembers(int limit) {
        if (limit <= 0) {
            limit = 5;
        }
        return memberMapper.selectRecentMembers(limit);
    }

    private void validateMemberForWrite(Member member, boolean creating) {
        if (member == null) {
            throw new BusinessException(ResultCode.PARAMETER_ERROR, "Member payload must not be empty");
        }
        if (!StringUtils.hasText(member.getName())) {
            throw new BusinessException(ResultCode.PARAMETER_ERROR, "Member name must not be empty");
        }
        if (!creating && !StringUtils.hasText(member.getmemberId())) {
            throw new BusinessException(ResultCode.PARAMETER_ERROR, "Member ID must not be empty when updating");
        }
        if (StringUtils.hasText(member.getSex())) {
            String normalizedSex = normalizeSex(member.getSex());
            if (normalizedSex == null || !ALLOWED_SEX.contains(normalizedSex)) {
                throw new BusinessException(ResultCode.PARAMETER_ERROR, "Sex must be MALE or FEMALE");
            }
            member.setSex(normalizedSex);
        }
        if (StringUtils.hasText(member.getPassword()) && member.getPassword().length() < 6) {
            throw new BusinessException(ResultCode.PARAMETER_ERROR, "Password length must be at least 6 characters");
        }
        if (creating && !StringUtils.hasText(member.getPassword())) {
            throw new BusinessException(ResultCode.PARAMETER_ERROR, "Password must not be empty when creating a member");
        }
    }

    private String normalizeSex(String rawSex) {
        String trimmed = rawSex.trim();
        String upper = trimmed.toUpperCase();
        String normalized = SEX_NORMALIZATION.get(upper);
        if (normalized != null) {
            return normalized;
        }
        return SEX_NORMALIZATION.get(trimmed);
    }
}
