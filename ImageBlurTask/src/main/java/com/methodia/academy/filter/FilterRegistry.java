package com.methodia.academy.filter;

import com.methodia.academy.filter.blur.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FilterRegistry {
    private final Map<String, FilterDefinition<? extends ImageFilter>> filters = new HashMap<>();

    public FilterRegistry() {
        registerBuiltInFilters();
    }

    public void addFilterDefinition(FilterDefinition<? extends ImageFilter> filterDefinition) {
        filters.put(normalizeFilterName(filterDefinition.name()), filterDefinition);
    }

    public FilterDefinition<? extends ImageFilter> getFilterDefinition(String name) {
        return filters.get(normalizeFilterName(name));
    }

    private void registerBuiltInFilters() {
        addFilterDefinition(BoxBlurFilter.getDefinition());
        addFilterDefinition(AverageBrightnessBlurFilter.getDefinition());
        addFilterDefinition(GaussianBlurFilter.getDefinition());
        addFilterDefinition(MedianBlurFilter.getDefinition());
        addFilterDefinition(ColorComponentFilter.getDefinition());
        addFilterDefinition(CropFilter.getDefinition());
    }

    private String normalizeFilterName(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }
}
