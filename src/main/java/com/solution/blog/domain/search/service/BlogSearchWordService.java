package com.solution.blog.domain.search.service;

import com.solution.blog.domain.redis.RedisConstants;
import com.solution.blog.domain.search.controller.model.BlogPopularKeywordDto;
import com.solution.blog.domain.search.controller.model.BlogPopularKeywordRs;
import com.solution.blog.domain.search.entity.BlogSearchWordLog;
import com.solution.blog.repository.BlogSearchWordLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlogSearchWordService {

    private final BlogSearchWordLogRepository blogSearchWordLogRepository;

    private final StringRedisTemplate redisTemplate;

    @Transactional
    public void create(String keyword) {
        // db 저장
        BlogSearchWordLog blogSearchWordLog = BlogSearchWordLog.create(keyword);
        blogSearchWordLogRepository.save(blogSearchWordLog);

        // redis 저장
        ZSetOperations<String, String> zSet = redisTemplate.opsForZSet();
        zSet.incrementScore(RedisConstants.REDIS_HITS_KEY, keyword, 1);
    }

    public BlogPopularKeywordRs findBlogPopularKeyword() {
        List<BlogPopularKeywordDto> popularKeywordTopTenList;
        try {
            // redis 조회
            ZSetOperations<String, String> zSet = redisTemplate.opsForZSet();
            Set<ZSetOperations.TypedTuple<String>> typedTuples =
                    zSet.reverseRangeWithScores(RedisConstants.REDIS_HITS_KEY, 0, 9);
            popularKeywordTopTenList =
                    Objects.requireNonNull(typedTuples)
                            .stream()
                            .map(BlogPopularKeywordDto::of)
                            .collect(Collectors.toList());
        } catch (Exception e) {
            // db 조회
            popularKeywordTopTenList = blogSearchWordLogRepository.findPopularKeywordTopTen();
        }

        BlogPopularKeywordRs rs = new BlogPopularKeywordRs();
        rs.setPopularKeywordTopTenList(popularKeywordTopTenList);
        return rs;
    }
}
