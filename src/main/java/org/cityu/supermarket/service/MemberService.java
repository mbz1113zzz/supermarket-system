package org.cityu.supermarket.service;

import org.cityu.supermarket.common.vo.SupermarketResult;
import org.cityu.supermarket.entity.Member;
import org.cityu.supermarket.entity.MemberQuery;
import org.cityu.supermarket.entity.PageResult;


/**
 * Member service interface
 *
 * @version 0.0.1
 * @date 2025-11-01
 */
public interface MemberService {


    /**
     * Execute conditional search requested by the frontend.
     * @param query filter and pagination info
     * @return wrapped dataset for UI consumption
     */
    SupermarketResult<PageResult<Member>> getMemberData(MemberQuery query);

    /**
     * Register a new member.
     * @param member member payload
     * @return registration result
     */
    SupermarketResult<Member> memberRegister(Member member);

    /**
     * Remove a member profile.
     * @param memberId member identifier
     * @return deletion result
     */
    SupermarketResult<Void> delMember(String memberId);

    /**
     * Update member information.
     * @param member member payload
     * @return update result
     */
    SupermarketResult<Void> modifyMember(Member member);

    /**
     * Count registered members.
     * @return number of members
     */
    Long getMemberNum();

    /**
     * Retrieve latest registered members.
     * @param limit max number of entries
     * @return recent member list
     */
    java.util.List<Member> getRecentMembers(int limit);

}
