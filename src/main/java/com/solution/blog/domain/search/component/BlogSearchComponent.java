package com.solution.blog.domain.search.component;

import com.solution.blog.domain.search.component.strategy.ClientStrategyService;
import com.solution.blog.domain.search.component.strategy.ClientType;
import domain.solution.core.model.controller.BlogSearchRs;
import domain.solution.core.model.controller.SortType;
import domain.solution.core.search.BlogStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class BlogSearchComponent {

    private final ClientStrategyService clientStrategyService;

    /**
     * find 순서 대로 찾기
     * daum -> naver 추가
     **/
    @Cacheable(value = "blogStore")
    public BlogSearchRs searchBlogRs(String keyword, SortType sortType, PageRequest pageRequest) {
        BlogSearchRs rs = new BlogSearchRs();
        for (Map.Entry<ClientType, Class<? extends BlogStrategy>> entry : ClientStrategyService.map.entrySet()) {
            BlogStrategy service = clientStrategyService.findService(entry.getKey());
            try {
                rs = service.findBlog(keyword, sortType, pageRequest);
                break;
            } catch (RuntimeException e) {
                System.out.println("api 요청 실패로 다음 요청 실행");
            }
        }
        return rs;
    }

}
