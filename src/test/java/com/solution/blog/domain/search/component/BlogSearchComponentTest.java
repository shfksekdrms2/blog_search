package com.solution.blog.domain.search.component;

import com.solution.blog.domain.search.component.strategy.ClientStrategyService;
import com.solution.blog.domain.search.component.strategy.ClientType;
import com.solution.daum.domain.client.DaumClient;
import com.solution.naver.domain.client.NaverClient;
import domain.solution.core.model.controller.BlogSearchDto;
import domain.solution.core.model.controller.BlogSearchRs;
import domain.solution.core.model.controller.SortType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BlogSearchComponentTest {

    @Mock
    DaumClient daumClient;

    @Mock
    NaverClient naverClient;

    @InjectMocks
    private BlogSearchComponent blogSearchComponent;

    @Mock
    ClientStrategyService clientStrategyService;

    private static List<BlogSearchDto> getBlogSearchList() {
        List<BlogSearchDto> documents = new ArrayList<>();
        BlogSearchDto dto = new BlogSearchDto();
        dto.setTitle("title");
        dto.setContents("contents");
        dto.setUrl("url");
        dto.setBlogName("blogName");
        dto.setThumbnail("thumbnail");
        dto.setPostDateTime(LocalDateTime.now());
        documents.add(dto);
        return documents;
    }

    @Test
    @DisplayName("다음 블로그 요청")
    void searchBlog() {
        // given
        String keyword = "111";
        SortType sortType = SortType.ACCURACY;
        int page = 1;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        BlogSearchRs givenBlogSearchRs = new BlogSearchRs();
        List<BlogSearchDto> documents = getBlogSearchList();
        givenBlogSearchRs.setDocuments(documents);

        given(clientStrategyService.findService(ClientType.DAUM)).willReturn(daumClient);
        given(daumClient.findBlog(keyword, sortType, pageRequest)).willReturn(givenBlogSearchRs);

        // when
        BlogSearchRs blogSearchRs = blogSearchComponent.searchBlogRs(keyword, sortType, pageRequest);

        // then
        List<BlogSearchDto> whenBlogSearList = blogSearchRs.getDocuments();
        for (int i = 0; i < whenBlogSearList.size(); i++) {
            BlogSearchDto whenDto = whenBlogSearList.get(i);
            BlogSearchDto blogSearchDto = documents.get(i);
            assertThat(whenDto.getTitle()).isEqualTo(blogSearchDto.getTitle());
        }
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
        BlogSearchRs givenBlogSearchRs = new BlogSearchRs();
        List<BlogSearchDto> documents = getBlogSearchList();
        givenBlogSearchRs.setDocuments(documents);


        given(clientStrategyService.findService(ClientType.DAUM)).willReturn(daumClient);
        given(clientStrategyService.findService(ClientType.NAVER)).willReturn(naverClient);

        given(daumClient.findBlog(keyword, sortType, pageRequest)).willThrow(new RuntimeException());
        given(naverClient.findBlog(keyword, sortType, pageRequest)).willReturn(givenBlogSearchRs);

        // when
        BlogSearchRs blogSearchRs = blogSearchComponent.searchBlogRs(keyword, sortType, pageRequest);

        // then
        List<BlogSearchDto> whenBlogSearList = blogSearchRs.getDocuments();
        for (int i = 0; i < whenBlogSearList.size(); i++) {
            BlogSearchDto whenDto = whenBlogSearList.get(i);
            BlogSearchDto blogSearchDto = documents.get(i);
            assertThat(whenDto.getTitle()).isEqualTo(blogSearchDto.getTitle());
        }
    }
}