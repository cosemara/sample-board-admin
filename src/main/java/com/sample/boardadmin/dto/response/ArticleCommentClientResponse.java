package com.sample.boardadmin.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sample.boardadmin.dto.ArticleCommentDto;
import com.sample.boardadmin.dto.ArticleDto;
import java.util.List;

public record ArticleCommentClientResponse(
    @JsonProperty("_embedded") Embedded embedded,
    @JsonProperty("page") Page page
) {

    public static ArticleCommentClientResponse empty() {
        return new ArticleCommentClientResponse(
            new Embedded(List.of()),
            new Page(1, 0, 1, 0)
        );
    }

    public static ArticleCommentClientResponse of(List<ArticleCommentDto> articleComments) {
        return new ArticleCommentClientResponse(
            new Embedded(articleComments),
            new Page(articleComments.size(), articleComments.size(), 1, 0)
        );
    }

    public List<ArticleCommentDto> articleComments() { return this.embedded().articleComments(); }

    public record Embedded(List<ArticleCommentDto> articleComments) {}

    public record Page(
        int size,
        long totalElements,
        int totalPages,
        int number
    ) {}

}