package com.solution.blog.domain.search.component;

import domain.solution.core.model.controller.BlogSearchRs;
import domain.solution.core.model.controller.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlogComponent {

    private final BlogSearchComponent blogSearchComponent;

    private final ApplicationEventPublisher applicationEventPublisher;

    public BlogSearchRs searchBlog(String keyword, SortType sortType, Integer page, Integer size) {
        // 키워드 검색 로그 저장 - 이벤트 핸들러를 통한 비동기 처리
        applicationEventPublisher.publishEvent(keyword);

        // 블로그 조회
        PageRequest pageRequest = PageRequest.of(page, size);
        BlogSearchRs rs = blogSearchComponent.searchBlogRs(keyword, sortType, pageRequest);
        rs.setPageInfo(pageRequest);
        return rs;
    }
}
