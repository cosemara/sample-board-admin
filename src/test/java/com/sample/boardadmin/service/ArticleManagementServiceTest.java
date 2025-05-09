package com.sample.boardadmin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.boardadmin.domain.constant.RoleType;
import com.sample.boardadmin.dto.ArticleDto;
import com.sample.boardadmin.dto.UserAccountDto;
import com.sample.boardadmin.dto.propertiees.ProjectProperties;
import com.sample.boardadmin.dto.response.ArticleClientResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;

@ActiveProfiles("test")
@DisplayName("비즈니스 로직 - 게시글 관리")
class ArticleManagementServiceTest {


    //    @Disabled("실제 API 호출 결과 관찰용이므로 평상시엔 비활성화")
    @DisplayName("실제 API 호출 테스트")
    @SpringBootTest
    @Nested
    class RealApiTest {

        private final ArticleManagementService sut;

        @Autowired
        public RealApiTest(ArticleManagementService sut) {
            this.sut = sut;
        }

        @DisplayName("게시글 API를 호출하면, 게시글을 가져온다.")
        @Test
        void givenNothing_whenCallingArticleApi_thenReturnsArticleList() {
            // Given

            // When
            List<ArticleDto> result = sut.getArticles();

            // Then
            System.out.println(result.stream().findFirst());
            assertThat(result).isNotNull();
        }
    }

    @DisplayName("API mocking 테스트")
    @EnableConfigurationProperties(ProjectProperties.class)
    @AutoConfigureWebClient(registerRestTemplate = true)
    @RestClientTest(ArticleManagementService.class)
    @Nested
    class RestTemplateTest {

        private final ArticleManagementService articleManagementService;
        private final ProjectProperties projectProperties;
        private final MockRestServiceServer mockRestServiceServer;
        private final ObjectMapper objectMapper;

        @Autowired
        public RestTemplateTest(ArticleManagementService articleManagementService,
                                ProjectProperties projectProperties,
                                MockRestServiceServer mockRestServiceServer,
                                ObjectMapper objectMapper) {
            this.articleManagementService = articleManagementService;
            this.projectProperties = projectProperties;
            this.mockRestServiceServer = mockRestServiceServer;
            this.objectMapper = objectMapper;
        }

        @DisplayName("게시글 목록 API를 호출하면, 게시글들을 가져온다.")
        void givenNothing_whenCallingArticlesApi_thenReturnsArticleList() throws Exception {
            // Given
            ArticleDto expectedArticle = createArticleDto("제목", "글");
            ArticleClientResponse expectedResponse = ArticleClientResponse.of(List.of(expectedArticle));
            mockRestServiceServer.expect(requestTo(projectProperties.board()
                                                                    .url()+ "/api/articles?size=10000"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(expectedResponse), MediaType.APPLICATION_JSON));

            // When
            List<ArticleDto> result = articleManagementService.getArticles();

            // Then
            assertThat(result).first()
                              .hasFieldOrPropertyWithValue("id", expectedArticle.id())
                              .hasFieldOrPropertyWithValue("title", expectedArticle.title())
                              .hasFieldOrPropertyWithValue("content", expectedArticle.content())
                              .hasFieldOrPropertyWithValue("userAccount.nickname", expectedArticle.userAccount().nickname());
            mockRestServiceServer.verify();
        }

        @DisplayName("게시글 ID와 함께 게시글 API을 호출하면, 게시글을 가져온다.")
        @Test
        void givenArticleId_whenCallingArticleApi_thenReturnsArticle() throws Exception {
            // Given
            Long articleId = 1L;
            ArticleDto expectedArticle = createArticleDto("게시판", "글");
            mockRestServiceServer
                .expect(requestTo(projectProperties.board().url() + "/api/articles/" + articleId))
                .andRespond(withSuccess(
                    objectMapper.writeValueAsString(expectedArticle),
                    MediaType.APPLICATION_JSON
                ));

            // When
            ArticleDto result = articleManagementService.getArticle(articleId);

            // Then
            assertThat(result)
                .hasFieldOrPropertyWithValue("id", expectedArticle.id())
                .hasFieldOrPropertyWithValue("title", expectedArticle.title())
                .hasFieldOrPropertyWithValue("content", expectedArticle.content())
                .hasFieldOrPropertyWithValue("userAccount.nickname", expectedArticle.userAccount().nickname());
            mockRestServiceServer.verify();
        }

        @DisplayName("게시글 ID와 함께 게시글 삭제 API을 호출하면, 게시글을 삭제한다.")
        @Test
        void givenArticleId_whenCallingDeleteArticleApi_thenDeletesArticle() throws Exception {
            // Given
            Long articleId = 1L;
            mockRestServiceServer
                .expect(requestTo(projectProperties.board().url() + "/api/articles/" + articleId))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withSuccess());

            // When
            articleManagementService.deleteArticle(articleId);

            // Then
            mockRestServiceServer.verify();
        }

        private ArticleDto createArticleDto(String title, String content) {
            return ArticleDto.of(
                1L,
                createUserAccountDto(),
                title,
                content,
                null,
                LocalDateTime.now(),
                "Uno",
                LocalDateTime.now(),
                "Uno"
            );
        }

        private UserAccountDto createUserAccountDto() {
            return UserAccountDto.of(
                "unoTest",
                "uno-test@email.com",
                "uno-test",
                "test memo"
            );
        }
    }
}