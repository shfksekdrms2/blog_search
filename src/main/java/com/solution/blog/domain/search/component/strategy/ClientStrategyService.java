package com.solution.blog.domain.search.component.strategy;

import domain.solution.core.search.BlogStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClientStrategyService {

    private final ApplicationContext applicationContext;

    public static final Map<ClientType, Class<? extends BlogStrategy>> map = new LinkedHashMap<>();

    static {
        for (ClientType clientType : ClientType.values()) {
            map.put(clientType, clientType.getClazz());
        }
    }

    public BlogStrategy findService(ClientType clientType) {
        Class<? extends BlogStrategy> serviceBeanName = map.get(clientType);
        return applicationContext.getBean(serviceBeanName);
    }
}
