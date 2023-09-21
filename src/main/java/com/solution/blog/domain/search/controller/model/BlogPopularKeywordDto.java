package com.solution.blog.domain.search.controller.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.ZSetOperations;

@Setter
@Getter
public class BlogPopularKeywordDto {

    private String keyword;

    private Long count;

    public static BlogPopularKeywordDto of(ZSetOperations.TypedTuple<String> typedTuples) {
        BlogPopularKeywordDto dto = new BlogPopularKeywordDto();
        dto.setKeyword(typedTuples.getValue());
        dto.setCount(Math.round(typedTuples.getScore()));
        return dto;
    }

}
