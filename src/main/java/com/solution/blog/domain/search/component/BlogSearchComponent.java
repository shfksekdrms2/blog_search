package com.solution.blog.domain.search.component;

import com.solution.blog.domain.search.component.strategy.ClientType;
import com.solution.daum.domain.client.DaumClient;
import com.solution.naver.domain.client.NaverClient;
import domain.solution.core.model.controller.BlogSearchRs;
import domain.solution.core.model.controller.SortType;
import domain.solution.core.search.FindBlogStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BlogSearchComponent {

    private final DaumClient daumClient;
    private final NaverClient naverClient;

    public static final Map<ClientType, FindBlogStrategy> map = new LinkedHashMap<>();

    @PostConstruct
    private void init() {
        map.put(ClientType.DAUM, daumClient);
        map.put(ClientType.NAVER, naverClient);
    }

    /**
     * find 순서 대로 찾기
     * daum -> naver 추가
     **/
    @Cacheable(value = "blogStore")
    public BlogSearchRs searchBlogRs(String keyword, SortType sortType, PageRequest pageRequest) {
        BlogSearchRs rs = new BlogSearchRs();
        for (FindBlogStrategy findBlogStrategy : map.values()) {
            try {
                rs = findBlogStrategy.findBlog(keyword, sortType, pageRequest);
                break;
            } catch (RuntimeException e) {
                System.out.println("api 요청 실패로 다음 요청 실행");
            }
        }
        return rs;
    }

}
