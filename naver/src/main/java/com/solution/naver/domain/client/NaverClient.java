package com.solution.naver.domain.client;

import com.solution.naver.domain.model.ItemDto;
import com.solution.naver.domain.model.NaverBlogRs;
import domain.solution.core.model.controller.BlogSearchDto;
import domain.solution.core.model.controller.BlogSearchRs;
import domain.solution.core.model.controller.SortType;
import domain.solution.core.search.BlogStrategy;
import io.netty.handler.codec.http.HttpScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NaverClient implements BlogStrategy {

    private final WebClient webClient;

    private static final String HOST = "openapi.naver.com";
    private static final String X_NAVER_CLIENT_ID_HEADER = "X-Naver-Client-Id";
    private static final String X_NAVER_CLIENT_ID_VALUE = "4TPmPdXYD1O0OuM8Lwr3";

    private static final String X_NAVER_CLIENT_SECRET_HEADER = "X-Naver-Client-Secret";
    private static final String X_NAVER_CLIENT_SECRET_VALUE = "wL7GYUsR13";

    @Override
    public BlogSearchRs findBlog(String keyword, SortType sortType, PageRequest pageRequest) {
        UriComponents uriComponents =
                getUriComponents(
                        keyword,
                        sortType.getNaverSortName(),
                        pageRequest.getPageNumber(),
                        pageRequest.getPageSize()
                );

        NaverBlogRs naverBlogRs = webClient.get()
                .uri(uriComponents.toUri())
                .header(X_NAVER_CLIENT_ID_HEADER, X_NAVER_CLIENT_ID_VALUE)
                .header(X_NAVER_CLIENT_SECRET_HEADER, X_NAVER_CLIENT_SECRET_VALUE)
                .retrieve()
                .bodyToMono(NaverBlogRs.class)
                .block();

        // page 설정
        PageImpl<BlogSearchDto> daumBlogDocumentPage = getNaverBlogDocumentPage(naverBlogRs, pageRequest);
        return BlogSearchRs.of(daumBlogDocumentPage);
    }

    private PageImpl<BlogSearchDto> getNaverBlogDocumentPage(NaverBlogRs naverBlogRs, PageRequest pageRequest) {
        List<BlogSearchDto> blogSearchDtoList = changeDocuments(naverBlogRs.getItems());
        return new PageImpl<>(blogSearchDtoList, pageRequest, naverBlogRs.getTotal());
    }

    public List<BlogSearchDto> changeDocuments(List<ItemDto> items) {
        return items.stream()
                .map(ItemDto::of)
                .collect(Collectors.toList());
    }

    public UriComponents getUriComponents(String keyword, String sortType, Integer page, Integer size) {
        return UriComponentsBuilder.newInstance()
                .scheme(HttpScheme.HTTPS.toString())
                .host(HOST)
                .path("/v1/search/blog.json")
                .queryParam("sort", sortType)
                .queryParam("start", page)
                .queryParam("display", size)
                .queryParam("query", keyword)
                .build();
    }

}
