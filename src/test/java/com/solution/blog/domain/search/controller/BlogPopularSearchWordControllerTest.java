package com.solution.blog.domain.search.controller;

import com.solution.blog.domain.search.service.BlogSearchWordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BlogPopularSearchWordController.class)
class BlogPopularSearchWordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    BlogSearchWordService blogSearchWordService;

    @Test
    void findBlogPopularKeyword() throws Exception {
        // given
        // none

        // when, then
        mockMvc.perform(
                        get("/blog/popular/keyword")
                ).andExpect(status().isOk())
                .andDo(print());

    }
}