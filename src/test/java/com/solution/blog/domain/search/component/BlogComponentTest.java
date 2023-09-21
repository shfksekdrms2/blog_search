package com.solution.blog.domain.search.component;

import com.solution.blog.domain.search.component.strategy.ClientType;
import com.solution.blog.domain.search.service.BlogSearchWordService;
import com.solution.daum.domain.client.DaumClient;
import com.solution.naver.domain.client.NaverClient;
import domain.solution.core.model.controller.BlogSearchRs;
import domain.solution.core.model.controller.SortType;
import domain.solution.core.search.FindBlogStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BlogComponentTest {

    @InjectMocks
    private BlogComponent blogComponent;

    @Mock
    BlogSearchWordService blogSearchWordService;

    @Mock
    DaumClient daumClient;

    @Mock
    NaverClient naverClient;

    private void init() {
        Map<ClientType, FindBlogStrategy> map = BlogComponent.map;
        map.put(ClientType.DAUM, daumClient);
        map.put(ClientType.NAVER, naverClient);
    }

    @Test
    void searchBlog() {
        // given
        String keyword = "111";
        SortType sortType = SortType.ACCURACY;
        int page = 1;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        init();
        given(daumClient.findBlog(keyword, sortType, pageRequest)).willReturn(new BlogSearchRs());

        // when
        BlogSearchRs blogSearchRs = blogComponent.searchBlog(keyword, sortType, page, size);

        // then
//        todo BlogSearchRs 끼리 맞는지 확인

    }

    @Test
    @DisplayName("다음 요청 실패시 네이버 요청")
    void searchBlogNaver() {
        // given
        String keyword = "111";
        SortType sortType = SortType.ACCURACY;
        int page = 1;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        init();
        given(daumClient.findBlog(keyword, sortType, pageRequest)).willThrow(new RuntimeException());
        given(naverClient.findBlog(keyword, sortType, pageRequest)).willReturn(new BlogSearchRs());

        // when
        BlogSearchRs blogSearchRs = blogComponent.searchBlog(keyword, sortType, page, size);

        // then
//        todo BlogSearchRs 끼리 맞는지 확인

    }
}