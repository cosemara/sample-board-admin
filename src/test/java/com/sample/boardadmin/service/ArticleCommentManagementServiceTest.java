package com.sample.boardadmin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.boardadmin.dto.ArticleCommentDto;
import com.sample.boardadmin.dto.UserAccountDto;
import com.sample.boardadmin.dto.propertiees.ProjectProperties;
import com.sample.boardadmin.dto.response.ArticleCommentClientResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Disabled;
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
@DisplayName("비즈니스 로직 - 댓글 관리")
class ArticleCommentManagementServiceTest {


    @Disabled("실제 API 호출 결과 관찰용이므로 평상시엔 비활성화")
    @DisplayName("실제 API 호출 테스트")
    @SpringBootTest
    @Nested
    class RealApiTest {

        private final ArticleCommentManagementService sut;

        @Autowired
        public RealApiTest(ArticleCommentManagementService sut) {
            this.sut = sut;
        }

        @DisplayName("댓글 API를 호출하면, 댓글을 가져온다.")
        @Test
        void givenNothing_whenCallingCommentApi_thenReturnsCommentList() {
            // Given

            // When
            List<ArticleCommentDto> result = sut.getArticleComments();

            // Then
            System.out.println(result.stream().findFirst());
            assertThat(result).isNotNull();
        }
    }

    @DisplayName("API mocking 테스트")
    @EnableConfigurationProperties(ProjectProperties.class)
    @AutoConfigureWebClient(registerRestTemplate = true)
    @RestClientTest(ArticleCommentManagementService.class)
    @Nested
    class RestTemplateTest {

        private final ArticleCommentManagementService articleCommentManagementService;
        private final ProjectProperties projectProperties;
        private final MockRestServiceServer mockRestServiceServer;
        private final ObjectMapper objectMapper;

        @Autowired
        public RestTemplateTest(ArticleCommentManagementService articleCommentManagementService,
                                ProjectProperties projectProperties,
                                MockRestServiceServer mockRestServiceServer,
                                ObjectMapper objectMapper) {
            this.articleCommentManagementService = articleCommentManagementService;
            this.projectProperties = projectProperties;
            this.mockRestServiceServer = mockRestServiceServer;
            this.objectMapper = objectMapper;
        }

        @DisplayName("댓글 목록 API을 호출하면, 댓글들을 가져온다.")
        @Test
        void givenNothing_whenCallingCommentsApi_thenReturnsCommentList() throws Exception {
            // Given
            ArticleCommentDto expectedArticleComment = createArticleCommentDto("댓글");
            ArticleCommentClientResponse expectedResponse = ArticleCommentClientResponse.of(List.of(expectedArticleComment));
            mockRestServiceServer.expect(requestTo(projectProperties.board()
                                                                    .url()+ "/api/articleComments?size=10000"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(expectedResponse), MediaType.APPLICATION_JSON));

            // When
            List<ArticleCommentDto> result = articleCommentManagementService.getArticleComments();

            // Then
            assertThat(result).first()
                              .hasFieldOrPropertyWithValue("id", expectedArticleComment.id())
                              .hasFieldOrPropertyWithValue("content", expectedArticleComment.content())
                              .hasFieldOrPropertyWithValue("userAccount.nickname", expectedArticleComment.userAccount().nickname());
            mockRestServiceServer.verify();
        }

        @DisplayName("댓글 ID와 함께 댓글 API을 호출하면, 댓글을 가져온다.")
        @Test
        void givenCommentId_whenCallingCommentApi_thenReturnsComment() throws Exception {
            // Given
            Long articleCommentId = 1L;
            ArticleCommentDto expectedArticleComment = createArticleCommentDto("댓글");
            mockRestServiceServer
                .expect(requestTo(projectProperties.board().url() + "/api/articleComments/" + articleCommentId + "?projection=withUserAccount"))
                .andRespond(withSuccess(
                    objectMapper.writeValueAsString(expectedArticleComment),
                    MediaType.APPLICATION_JSON
                ));

            // When
            ArticleCommentDto result = articleCommentManagementService.getArticleComment(articleCommentId);

            // Then
            assertThat(result)
                .hasFieldOrPropertyWithValue("id", expectedArticleComment.id())
                .hasFieldOrPropertyWithValue("content", expectedArticleComment.content())
                .hasFieldOrPropertyWithValue("userAccount.nickname", expectedArticleComment.userAccount().nickname());
            mockRestServiceServer.verify();
        }

        @DisplayName("댓글 ID와 함께 댓글 삭제 API을 호출하면, 댓글을 삭제한다.")
        @Test
        void givenCommentId_whenCallingDeleteCommentApi_thenDeletesComment() throws Exception {
            // Given
            Long articleCommentId = 1L;
            mockRestServiceServer
                .expect(requestTo(projectProperties.board().url() + "/api/articleComments/" + articleCommentId))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withSuccess());

            // When
            articleCommentManagementService.deleteArticleComment(articleCommentId);

            // Then
            mockRestServiceServer.verify();
        }

        private ArticleCommentDto createArticleCommentDto(String content) {
            return ArticleCommentDto.of(
                1L,
                1L,
                createUserAccountDto(),
                null,
                content,
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