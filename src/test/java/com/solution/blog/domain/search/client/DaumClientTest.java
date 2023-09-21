package com.solution.blog.domain.search.client;

import com.solution.daum.domain.client.DaumClient;
import com.solution.daum.domain.model.DaumBlogRs;
import domain.solution.core.model.controller.SortType;
import io.netty.handler.codec.http.HttpScheme;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DaumClientTest {

    @InjectMocks
    private DaumClient daumClient;

    @Mock
    WebClient webClient;

    private static final String API_KEY = "408848115a7af9b9806f8b2d9a0666a0";
    private static final String HOST = "dapi.kakao.com";
    private static final String PATH = "/v2/search/blog";

    @Test
    @DisplayName("성공적으로 daum blog 검색")
    public void findBlog() {
        // given
        String keyword = "123";
        SortType sortType = SortType.RECENCY;
        int page = 1;
        int size = 10;

        PageRequest pageRequest = PageRequest.of(page, size);

        UriComponents uriComponents =
                getUriComponents(
                        keyword,
                        sortType.getDaumSortName(),
                        pageRequest.getPageNumber(),
                        pageRequest.getPageSize()
                );

        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        DaumBlogRs daumBlogRs = new DaumBlogRs();
        given(webClient.get()).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.uri("/api/quotes")).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.retrieve()).willReturn(responseSpec);
        given(responseSpec.bodyToMono(DaumBlogRs.class));

        // when, then
        daumClient.findBlog(keyword, sortType, pageRequest);

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