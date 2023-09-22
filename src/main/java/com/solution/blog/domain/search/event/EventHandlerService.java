package com.solution.blog.domain.search.event;

import com.solution.blog.domain.redis.RedisConstants;
import com.solution.blog.domain.search.entity.BlogSearchWordLog;
import com.solution.blog.repository.BlogSearchWordLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventHandlerService {

    private final BlogSearchWordLogRepository blogSearchWordLogRepository;

    private final StringRedisTemplate redisTemplate;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @EventListener
    public void create(String keyword) {
        // db 저장
        BlogSearchWordLog blogSearchWordLog = BlogSearchWordLog.create(keyword);
        blogSearchWordLogRepository.save(blogSearchWordLog);

        // redis 저장
        ZSetOperations<String, String> zSet = redisTemplate.opsForZSet();
        zSet.incrementScore(RedisConstants.REDIS_HITS_KEY, keyword, 1);
    }

}
