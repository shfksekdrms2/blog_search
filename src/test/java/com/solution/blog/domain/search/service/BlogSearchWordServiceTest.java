package com.solution.blog.domain.search.service;

import com.solution.blog.domain.redis.RedisConstants;
import com.solution.blog.domain.search.controller.model.BlogPopularKeywordDto;
import com.solution.blog.domain.search.controller.model.BlogPopularKeywordRs;
import com.solution.blog.repository.BlogSearchWordLogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BlogSearchWordServiceTest {

    @InjectMocks
    private BlogSearchWordService blogSearchWordService;

    @Mock
    BlogSearchWordLogRepository blogSearchWordLogRepository;

    @Mock
    StringRedisTemplate redisTemplate;

    @Mock
    ZSetOperations<String, String> zSet;

    @Test
    @DisplayName("키워드 조회 - redis")
    void findBlogPopularKeyword() {
        // given
        Set<ZSetOperations.TypedTuple<String>> typedTuples = new HashSet<>();
        typedTuples.add(new DefaultTypedTuple<>("222", 222.0));
        typedTuples.add(new DefaultTypedTuple<>("111", 111.0));

        List<BlogPopularKeywordDto> collect = Objects.requireNonNull(typedTuples)
                .stream()
                .map(BlogPopularKeywordDto::of)
                .collect(Collectors.toList());

        given(redisTemplate.opsForZSet()).willReturn(zSet);
        given(zSet.reverseRangeWithScores(RedisConstants.REDIS_HITS_KEY, 0, 9))
                .willReturn(typedTuples);

        // when
        BlogPopularKeywordRs blogPopularKeyword = blogSearchWordService.findBlogPopularKeyword();

        // then
        List<BlogPopularKeywordDto> popularKeywordTopTenList = blogPopularKeyword.getPopularKeywordTopTenList();
        for (int i = 0; i < popularKeywordTopTenList.size(); i++) {
            BlogPopularKeywordDto dto = popularKeywordTopTenList.get(i);
            BlogPopularKeywordDto givenDto = collect.get(i);
            assertThat(dto.getKeyword()).isEqualTo(givenDto.getKeyword());
            assertThat(dto.getCount()).isEqualTo(givenDto.getCount());
        }
    }

    @Test
    @DisplayName("키워드 조회 - redis 실패하여 db 조회")
    void findBlogPopularKeywordDB() {
        // given
        Set<ZSetOperations.TypedTuple<String>> typedTuples = new HashSet<>();
        typedTuples.add(new DefaultTypedTuple<>("222", 222.0));
        typedTuples.add(new DefaultTypedTuple<>("111", 111.0));

        List<BlogPopularKeywordDto> collect = Objects.requireNonNull(typedTuples)
                .stream()
                .map(BlogPopularKeywordDto::of)
                .collect(Collectors.toList());

        given(redisTemplate.opsForZSet()).willThrow(new RuntimeException());

        // when
        BlogPopularKeywordRs blogPopularKeyword = blogSearchWordService.findBlogPopularKeyword();

        // then
        List<BlogPopularKeywordDto> popularKeywordTopTenList = blogPopularKeyword.getPopularKeywordTopTenList();
        for (int i = 0; i < popularKeywordTopTenList.size(); i++) {
            BlogPopularKeywordDto dto = popularKeywordTopTenList.get(i);
            BlogPopularKeywordDto givenDto = collect.get(i);
            assertThat(dto.getKeyword()).isEqualTo(givenDto.getKeyword());
            assertThat(dto.getCount()).isEqualTo(givenDto.getCount());
        }
    }
}