package com.solution.blog.domain.search.component.strategy;

import com.solution.daum.domain.client.DaumClient;
import com.solution.naver.domain.client.NaverClient;
import domain.solution.core.search.BlogStrategy;
import lombok.Getter;

@Getter
public enum ClientType {
    DAUM(DaumClient.class),
    NAVER(NaverClient.class)
    ;

    private final Class<? extends BlogStrategy> clazz;

    ClientType(Class<? extends BlogStrategy> clazz) {
        this.clazz = clazz;
    }
}
