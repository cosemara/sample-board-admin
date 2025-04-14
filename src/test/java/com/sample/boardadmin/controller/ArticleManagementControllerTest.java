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

@DisplayName("View 컨트롤러 - 게시글 관리")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleManagementController.class)
class ArticleManagementControllerTest {

    private final MockMvc mockMvc;

    public ArticleManagementControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @DisplayName("[view][GET] 게시글 관리 페이지 - 정상 호출")
    @Test
    void givenNoting_whenRequestingArticleManagementView_thenReturnsArticleManagementView() throws Exception {
        // Given

        // When & Then
        mockMvc.perform(get("/management/articles"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(view().name("management/articles"));
    }
}