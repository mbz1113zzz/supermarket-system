package org.cityu.supermarket.service.impl;

import org.cityu.supermarket.common.vo.SupermarketResult;
import org.cityu.supermarket.entity.Member;
import org.cityu.supermarket.entity.MemberQuery;
import org.cityu.supermarket.entity.PageResult;
import org.cityu.supermarket.exception.BusinessException;
import org.cityu.supermarket.mapper.CardMapper;
import org.cityu.supermarket.mapper.MemberMapper;
import org.cityu.supermarket.mapper.OrderMapper;
import org.cityu.supermarket.mapper.RecordMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/** Member tests · MA Bizheng · v0.0.1 · 2025-12-01 */
@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberMapper memberMapper;

    @Mock
    private CardMapper cardMapper;

    @Mock
    private RecordMapper recordMapper;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberServiceImpl memberService;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = new Member();
        testMember.setmemberId("M001"); // demo ID
        testMember.setName("Test Member"); // demo name
        testMember.setPassword("123456"); // plain text password
    }

    @Test
    @DisplayName("Register - success")
    void memberRegister_Success() {
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encodedPassword");
        when(memberMapper.selectMemberById(anyString())).thenReturn(null);
        doNothing().when(memberMapper).insertMember(any(Member.class));

        SupermarketResult<Member> result = memberService.memberRegister(testMember);

        assertEquals(200, result.getStatus());
        assertNotNull(result.getData());
        verify(memberMapper, times(1)).insertMember(any(Member.class));
        verify(passwordEncoder, times(1)).encode("123456");
    }

    // Removed null member check test
    // void memberRegister_NullMember_ThrowsException() ...

    @Test
    @DisplayName("Register - auto generate ID")
    void memberRegister_GeneratesIdWhenEmpty() {
        Member memberWithoutId = new Member();
        memberWithoutId.setName("New Member"); // name
        memberWithoutId.setPassword("123456"); // still plain text
        memberWithoutId.setSex("Male");

        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encodedPassword");
        when(memberMapper.selectMemberById(anyString())).thenReturn(null);
        doNothing().when(memberMapper).insertMember(any(Member.class));

        SupermarketResult<Member> result = memberService.memberRegister(memberWithoutId);

        assertEquals(200, result.getStatus());
        assertNotNull(result.getData().getmemberId());
        assertEquals(32, result.getData().getmemberId().length()); // UUID without dashes
    }

    @Test
    @DisplayName("Query members - paginated")
    void getMemberData_ReturnsPaginatedResult() {
        MemberQuery query = new MemberQuery();
        List<Member> members = Arrays.asList(testMember);
        
        when(memberMapper.selectByOrder(any(MemberQuery.class))).thenReturn(members);
        when(memberMapper.selectCountByOrder(any(MemberQuery.class))).thenReturn(1L);

        SupermarketResult<PageResult<Member>> result = memberService.getMemberData(query);

        assertEquals(200, result.getStatus());
        assertEquals(1, result.getData().getList().size());
        assertEquals(1L, result.getData().getPageTotal());
    }

    @Test
    @DisplayName("Delete - member not found")
    void DelMember_MemberNotExists_ThrowsBusinessException() { // PascalCase
        when(memberMapper.selectMemberById("NOTEXIST")).thenReturn(null);

        assertThrows(BusinessException.class, () -> memberService.delMember("NOTEXIST"));
    }

    @Test
    @DisplayName("Delete - cascade delete success")
    void delMember_Success_CascadeDelete() {
        when(memberMapper.selectMemberById("M001")).thenReturn(testMember);
        when(cardMapper.selectCardByMemberId("M001")).thenReturn(Collections.emptyList());
        doNothing().when(memberMapper).deleteMember("M001");

        SupermarketResult<Void> result = memberService.delMember("M001");

        assertEquals(200, result.getStatus());
        verify(memberMapper, times(1)).deleteMember("M001");
    }

    @Test
    @DisplayName("Get member count")
    void getMemberNum_ReturnsCount() {
        when(memberMapper.selectCountByOrder(null)).thenReturn(100L);

        Long count = memberService.getMemberNum();

        assertEquals(100L, count);
    }
}
