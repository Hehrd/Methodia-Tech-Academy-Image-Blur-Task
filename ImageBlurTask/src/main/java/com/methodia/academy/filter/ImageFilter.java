package com.methodia.academy.filter;

import java.awt.image.BufferedImage;

public abstract class ImageFilter {
    private final FilterDefinition<? extends ImageFilter> filterDefinition;

    protected ImageFilter(FilterDefinition<? extends ImageFilter> filterDefinition) {
        if (filterDefinition == null) {
            throw new IllegalArgumentException("Filter definition cannot be null");
        }
        this.filterDefinition = filterDefinition;
    }

    public abstract BufferedImage apply(BufferedImage image);

    public FilterDefinition<? extends ImageFilter> getFilterDefinition() {
        return filterDefinition;
    }

    public String getName() {
        return filterDefinition.name();
    }

}
