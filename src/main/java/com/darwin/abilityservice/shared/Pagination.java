package com.darwin.abilityservice.shared;

import lombok.Getter;

@Getter
public final class Pagination {
    public static final String PAGE_PARAM = "page";
    public static final String SIZE_PARAM = "size";
    public static final String SORT_ASCENDING = "sortAscending";
    public static final String SORT_PROPERTY = "sortProperty";

    public static final String DEFAULT_PAGE = "0";
    public static final String DEFAULT_SIZE = "10";
    public static final String DEFAULT_ASCENDING = "1";

    public static final String ASCENDING_TRUE = "1";

    public static final String DEFAULT_SORT_PROPERTY_FOR_ABILITY = "name";

    private Pagination() {
    }
}
