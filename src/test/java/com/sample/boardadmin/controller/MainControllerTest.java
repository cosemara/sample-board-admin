package com.sample.boardadmin.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.sample.boardadmin.config.SecurityConfig;
import com.sample.boardadmin.config.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("View 컨트롤러 - 루트페이지")
@Import(TestSecurityConfig.class)
@WebMvcTest(MainController.class)
class MainControllerTest {

    private final MockMvc mockMvc;

    public MainControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @DisplayName("[view][GET] 루트 페이지 - 정상 호출")
    @Test
    void givenNoting_whenRequestingMainView_thenReturnsMainView() throws Exception {
        // Given

        // When & Then
        mockMvc.perform(get("/"))
               .andExpect(status().isOk())
               .andExpect(view().name("forward:/management/articles"))
               .andExpect(forwardedUrl("/management/articles"));
    }
}