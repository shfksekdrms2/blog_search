package com.solution.blog.domain.search.client;

import com.solution.naver.domain.client.NaverClient;
import domain.solution.core.model.controller.SortType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NaverClientTest {

    @InjectMocks
    private NaverClient naverClient;

    @Mock
    WebClient webClient;

    @Test
    @DisplayName("성공적으로 naver blog 검색")
    public void findBlog() {
        // given
        String keyword = "123";
        SortType sortType = SortType.RECENCY;
        int page = 1;
        int size = 10;

        PageRequest pageRequest = PageRequest.of(page, size);

        // when
        naverClient.findBlog(keyword, sortType, pageRequest);

        // then
        verify(webClient.get());
    }

}