package com.solution.blog.domain.search.component;

import com.solution.blog.domain.search.service.BlogSearchWordService;
import domain.solution.core.model.controller.BlogSearchDto;
import domain.solution.core.model.controller.BlogSearchRs;
import domain.solution.core.model.controller.SortType;
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
class BlogComponentTest {

    @InjectMocks
    private BlogComponent blogComponent;

    @Mock
    BlogSearchWordService blogSearchWordService;

    @Mock
    BlogSearchComponent blogSearchComponent;

    @Test
    public void searchBlog() {
        // given
        String keyword = "111";
        SortType sortType = SortType.ACCURACY;
        int page = 1;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        BlogSearchRs givenBlogSearchRs = new BlogSearchRs();
        List<BlogSearchDto> documents = getBlogSearchList();
        givenBlogSearchRs.setDocuments(documents);

        given(blogSearchComponent.searchBlogRs(keyword, sortType, pageRequest)).willReturn(givenBlogSearchRs);

        // when
        BlogSearchRs blogSearchRs = blogComponent.searchBlog(keyword, sortType, page, size);

        // then
        List<BlogSearchDto> whenBlogSearList = blogSearchRs.getDocuments();
        for (int i = 0; i < whenBlogSearList.size(); i++) {
            BlogSearchDto whenDto = whenBlogSearList.get(i);
            BlogSearchDto blogSearchDto = documents.get(i);
            assertThat(whenDto.getTitle()).isEqualTo(blogSearchDto.getTitle());
        }
    }

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
}