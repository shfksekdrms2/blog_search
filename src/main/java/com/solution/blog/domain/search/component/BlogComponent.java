package com.solution.blog.domain.search.component;

import com.solution.blog.domain.search.service.BlogSearchWordService;
import domain.solution.core.model.controller.BlogSearchRs;
import domain.solution.core.model.controller.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlogComponent {

    private final BlogSearchComponent blogSearchComponent;
    private final BlogSearchWordService blogSearchWordService;

    public BlogSearchRs searchBlog(String keyword, SortType sortType, Integer page, Integer size) {
        // 키워드 검색 로그 저장
        blogSearchWordService.create(keyword);

        // 블로그 조회
        PageRequest pageRequest = PageRequest.of(page, size);
        BlogSearchRs rs = blogSearchComponent.searchBlogRs(keyword, sortType, pageRequest);
        rs.setPageInfo(pageRequest);
        return rs;
    }
}
