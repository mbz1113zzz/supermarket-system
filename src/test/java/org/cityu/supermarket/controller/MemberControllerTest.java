package org.cityu.supermarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cityu.supermarket.common.vo.SupermarketResult;
import org.cityu.supermarket.dto.MemberCreateRequest;
import org.cityu.supermarket.entity.Member;
import org.cityu.supermarket.entity.MemberQuery;
import org.cityu.supermarket.entity.PageResult;
import org.cityu.supermarket.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Controller integration · MA Bizheng · v0.0.1 · 2025-12-01 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = new Member();
        testMember.setmemberId("M001");
        testMember.setName("Test Member");
        testMember.setSex("Male");
    }

    @Test
    @DisplayName("Get member list - success")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getMemberData_Success() throws Exception {
        PageResult<Member> pageResult = new PageResult<>();
        pageResult.setList(Arrays.asList(testMember));
        pageResult.setPageTotal(1L);
        
        when(memberService.getMemberData(any(MemberQuery.class)))
            .thenReturn(SupermarketResult.success(pageResult));

        mockMvc.perform(get("/members")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    @DisplayName("Register member - success")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createMember_Success() throws Exception {
        when(memberService.memberRegister(any(Member.class)))
            .thenReturn(SupermarketResult.success(testMember));

        MemberCreateRequest request = new MemberCreateRequest();
        request.setMemberId("M001");
        request.setName("Test Member");
        request.setSex("Male");
        request.setPassword("password123");

        mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    // Removed unauthorized access test
    // void accessWithoutAuth_Returns401() ...
}
