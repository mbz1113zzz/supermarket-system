package org.cityu.supermarket.mapper;

import org.apache.ibatis.annotations.Param;
import org.cityu.supermarket.entity.Member;
import java.util.List;

import org.cityu.supermarket.entity.MemberQuery;

/**
 * Member mapper interface
 *
 * @version 0.0.1
 * @date 2025-11-01
 */
public interface MemberMapper {
    /**
     * Search by query rules
     * @param query
     * @return Matching rows
     */
    List<Member> selectByOrder(MemberQuery query);

    /**
     * Search by query rules, return count
     * @return Count under current query rules
     */
    Long selectCountByOrder(MemberQuery query);

    /**
     * Add a member to the table
     * @param member
     */
    void insertMember(Member member);

    /**
     * Delete a member
     * @param memberId
     * @return
     */
    void deleteMember(String memberId);

    /**
     * Update member info by member ID
     * @param member
     */
    void updateMember(Member member);

    /**
     * Get a member by ID
     * @param memberId Member ID
     * @return Member info
     */
    Member selectMemberById(String memberId);

    /**
     * Update member password
     * @param memberId Member ID
     * @param password New password
     */
    void updateMemberPassword(@Param("memberId") String memberId, @Param("password") String password);

    /**
     * Get recently registered members
     * @param limit Number to fetch
     * @return List of recent members
     */
    List<Member> selectRecentMembers(@Param("limit") int limit);

}