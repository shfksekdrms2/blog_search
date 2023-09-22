package com.solution.blog.domain.search.event;

import com.solution.blog.repository.BlogSearchWordLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class EventHandlerServiceTest {

    @InjectMocks
    private EventHandlerService eventHandlerService;
    @Mock
    StringRedisTemplate redisTemplate;

    @Mock
    ZSetOperations<String, String> zSet;

    @Mock
    BlogSearchWordLogRepository blogSearchWordLogRepository;

    @Test
    void create() {
        // given
        String keyword = "블로그검색";

        given(redisTemplate.opsForZSet()).willReturn(zSet);

        // then, when
        eventHandlerService.create(keyword);

    }
}