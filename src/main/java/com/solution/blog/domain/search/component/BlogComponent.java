package com.solution.blog.domain.search.component;

import com.solution.blog.domain.search.component.strategy.ClientType;
import com.solution.blog.domain.search.service.BlogSearchWordService;
import com.solution.daum.domain.client.DaumClient;
import com.solution.naver.domain.client.NaverClient;
import domain.solution.core.model.controller.BlogSearchRs;
import domain.solution.core.model.controller.SortType;
import domain.solution.core.search.FindBlogStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BlogComponent {

    private final DaumClient daumClient;
    private final NaverClient naverClient;

    private final BlogSearchWordService blogSearchWordService;

    public static final Map<ClientType, FindBlogStrategy> map = new LinkedHashMap<>();

    @PostConstruct
    private void init() {
        map.put(ClientType.DAUM, daumClient);
        map.put(ClientType.NAVER, naverClient);
    }

    public BlogSearchRs searchBlog(String keyword, SortType sortType, Integer page, Integer size) {
        // 키워드 검색 로그 저장
        blogSearchWordService.create(keyword);

        // 블로그 조회
        PageRequest pageRequest = PageRequest.of(page, size);
        BlogSearchRs rs = searchBlogRs(keyword, sortType, pageRequest);
        rs.setPageInfo(pageRequest);
        return rs;
    }

    /**
     * find 순서 대로 찾기
     * daum -> naver 추가
     **/
    private BlogSearchRs searchBlogRs(String keyword, SortType sortType, PageRequest pageRequest) {
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
