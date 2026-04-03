package Filtering;

import Filtering.Rules.DateFilter;

import java.util.LinkedHashMap;
import java.util.Map;

public class FilterFactory {
    private final Map<String, IFilter> filters = new LinkedHashMap<>();

    public void register(String filterName, IFilter filter) {
        filters.put(filterName, filter);
    }

    public Map<String, IFilter> getFilters() {
        return filters;
    }

    public IFilter createFilterChain() {
        IFilter firstFilter = null;
        IFilter currentFilter = null;

        for (IFilter filter : filters.values()) {
            if (firstFilter == null) {
                firstFilter = filter;
                currentFilter = filter;
            } else {
                currentFilter = currentFilter.setNext(filter);
            }
        }

        return firstFilter;
    }
}
