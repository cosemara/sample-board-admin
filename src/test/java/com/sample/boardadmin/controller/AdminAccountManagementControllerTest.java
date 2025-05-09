package com.sample.boardadmin.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.sample.boardadmin.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("View 컨트롤러 - 어드민 사용자 계정 관리")
@Import(SecurityConfig.class)
@WebMvcTest(AdminAccountManagementController.class)
class AdminAccountManagementControllerTest {

    private final MockMvc mockMvc;

    public AdminAccountManagementControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @DisplayName("[view][GET] 어드민 사용자 관리 페이지 - 정상 호출")
    @Test
    void givenNoting_whenRequestingAdminUserManagementView_thenReturnsAdminUserManagementView() throws Exception {
        // Given

        // When & Then
        mockMvc.perform(get("/admin/members"))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(view().name("admin/members"));
    }
}