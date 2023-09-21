package com.solution.blog.domain.search.controller;

import com.solution.blog.domain.search.component.BlogComponent;
import domain.solution.core.model.controller.SortType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BlogSearchController.class)
class BlogSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    BlogComponent blogComponent;

    // 필수값만 넣고 조회 가능한지 확인
    @Test
    @DisplayName("정상 요청")
    public void requirementTest() throws Exception {
        // given
        String keyword = "123";

        // when, then
        mockMvc.perform(
                        get("/blog/search")
                                .param("keyword", keyword)
                ).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("필수값이 입력되지 않은 경우")
    public void requirementBlankTest() throws Exception {
        // given
        String keyword = "";

        // when, then
        mockMvc.perform(
                        get("/blog/search")
                                .param("keyword", keyword)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.code")
                                .value("ValidationException")
                )
                .andDo(print());

    }

    @Test
    @DisplayName("정렬 타입을 잘못 요청한 경우")
    public void sortTypeTest() throws Exception {
        // given
        String keyword = "내용";
        String sortType = "accuracy111";

        // when, then
        mockMvc.perform(
                        get("/blog/search")
                                .param("keyword", keyword)
                                .param("sortType", sortType)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.code")
                                .value("MethodArgumentTypeMismatchException")
                )
                .andDo(print());

    }

    // page 요청 범위가 벗어난 경우 - ValidationException 예외 발생
    @Test
    public void rangeOverTest() throws Exception {
        // given
        String keyword = "내용";
        String sortType = SortType.RECENCY.name();
        Integer page = 0;

        // when, then
        mockMvc.perform(
                        get("/blog/search")
                                .param("keyword", keyword)
                                .param("sortType", sortType)
                                .param("page", String.valueOf(page))
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.code")
                                .value("ValidationException")
                )
                .andDo(print());

    }

}