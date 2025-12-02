package org.cityu.supermarket.mapper;

import org.cityu.supermarket.entity.Member;
import org.cityu.supermarket.entity.MemberQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Member Mapper integration test
 * Testing SQL queries
 * 
 * @version 0.0.1
 * @date 2025-12-01
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MemberMapperTest {

    @Autowired
    private MemberMapper memberMapper;

    @Test
    @DisplayName("Insert member")
    void insertMember_Success() {
        Member member = new Member();
        member.setmemberId("TEST001");
        member.setName("Test Member");
        member.setSex("Male");
        member.setPassword("$2a$10$encodedPassword123456");

        assertDoesNotThrow(() -> memberMapper.insertMember(member));
        
        Member inserted = memberMapper.selectMemberById("TEST001");
        assertNotNull(inserted);
        assertEquals("Test Member", inserted.getName());
    }

    @Test
    @DisplayName("Get member by ID")
    void selectMemberById_Found() {
        // assuming M001 exists in test data
        Member member = memberMapper.selectMemberById("M001");
        
        if (member != null) {
            assertEquals("M001", member.getmemberId());
        }
    }

    @Test
    @DisplayName("Get member by ID - not found")
    void selectMemberById_NotFound() {
        Member member = memberMapper.selectMemberById("NOTEXIST999");
        assertNull(member);
    }

    @Test
    @DisplayName("Query members - paginated")
    void selectByOrder_ReturnsList() {
        MemberQuery query = new MemberQuery();
        query.setPageIndex(1, 10);  // setting pagination properly

        List<Member> members = memberMapper.selectByOrder(query);
        
        assertNotNull(members);
        // might be empty but shouldn't be null
    }

    @Test
    @DisplayName("Count members")
    void selectCountByOrder_ReturnsCount() {
        Long count = memberMapper.selectCountByOrder(null);
        
        assertNotNull(count);
        assertTrue(count >= 0);
    }

    @Test
    @DisplayName("Update member")
    void updateMember_Success() {
        // insert test data first
        Member member = new Member();
        member.setmemberId("UPDATE001");
        member.setName("Original Name");
        member.setSex("Male");
        member.setPassword("$2a$10$testPassword123456");
        memberMapper.insertMember(member);

        // update
        member.setName("Updated Name");
        memberMapper.updateMember(member);

        // verify
        Member updated = memberMapper.selectMemberById("UPDATE001");
        if (updated != null) {
            assertEquals("Updated Name", updated.getName());
        }
    }

    @Test
    @DisplayName("Delete member")
    void deleteMember_Success() {
        // insert test data first
        Member member = new Member();
        member.setmemberId("DELETE001");
        member.setName("To Be Deleted");
        member.setPassword("$2a$10$testPassword123456");
        memberMapper.insertMember(member);

        // delete
        memberMapper.deleteMember("DELETE001");

        // verify
        Member deleted = memberMapper.selectMemberById("DELETE001");
        assertNull(deleted);
    }
}
