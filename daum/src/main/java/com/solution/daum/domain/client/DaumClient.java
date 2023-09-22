package com.solution.daum.domain.client;

import com.solution.daum.domain.model.DaumBlogDocumentDto;
import com.solution.daum.domain.model.DaumBlogRs;
import domain.solution.core.model.controller.BlogSearchDto;
import domain.solution.core.model.controller.BlogSearchRs;
import domain.solution.core.model.controller.SortType;
import domain.solution.core.search.BlogStrategy;
import io.netty.handler.codec.http.HttpScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DaumClient implements BlogStrategy {

    private final WebClient webClient;

    private static final String API_KEY = "408848115a7af9b9806f8b2d9a0666a0";
    private static final String HOST = "dapi.kakao.com";
    private static final String PATH = "/v2/search/blog";

    @Override
    public BlogSearchRs findBlog(String keyword, SortType sortType, PageRequest pageRequest) {
        UriComponents uriComponents =
                getUriComponents(
                        keyword,
                        sortType.getDaumSortName(),
                        pageRequest.getPageNumber(),
                        pageRequest.getPageSize()
                );
        DaumBlogRs daumBlogRs = webClient.get()
                .uri(uriComponents.toUri())
                .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + API_KEY)
                .retrieve()
                .bodyToMono(DaumBlogRs.class)
                .block();

        // page 설정
        PageImpl<BlogSearchDto> daumBlogDocumentPage = getDaumBlogDocumentPage(daumBlogRs, pageRequest);
        return BlogSearchRs.of(daumBlogDocumentPage);
    }

    private PageImpl<BlogSearchDto> getDaumBlogDocumentPage(DaumBlogRs daumBlogRs, PageRequest pageRequest) {
        List<BlogSearchDto> blogSearchList = changeDocuments(daumBlogRs.getDocuments());
        return new PageImpl<>(blogSearchList, pageRequest, daumBlogRs.getMeta().getTotalCount());
    }

    public List<BlogSearchDto> changeDocuments(List<DaumBlogDocumentDto> documents) {
        return documents.stream()
                .map(DaumBlogDocumentDto::of)
                .collect(Collectors.toList());
    }

    public UriComponents getUriComponents(String keyword, String sortType, Integer page, Integer size) {
        return UriComponentsBuilder.newInstance()
                .scheme(HttpScheme.HTTPS.toString())
                .host(HOST)
                .path(PATH)
                .queryParam("sort", sortType)
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("query", keyword)
                .build();
    }

}
