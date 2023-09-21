package domain.solution.core.model.controller;

import lombok.Getter;

@Getter
public enum SortType {
    ACCURACY("정확도", "accuracy", "sim"),
    RECENCY("최신순", "recency", "date");

    private final String name;
    private final String daumSortName;
    private final String naverSortName;

    SortType(String name, String daumSortName, String naverSortName) {
        this.name = name;
        this.daumSortName = daumSortName;
        this.naverSortName = naverSortName;
    }
}
