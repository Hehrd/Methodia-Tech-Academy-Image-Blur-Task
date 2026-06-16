package com.methodia.academy.validation;

import com.methodia.academy.filter.FilterDefinition;
import com.methodia.academy.filter.FilterRegistry;

public class InputValidator {
    public void validateFilterRegistry(FilterRegistry filterRegistry) {
        if (filterRegistry == null) {
            throw new IllegalArgumentException("Filter registry cannot be null");
        }
    }

    public void validateArgs(String[] args, int minimumLength) {
        if (args == null || args.length < minimumLength) {
            throw new IllegalArgumentException("At least image path and one filter are required");
        }
    }

    public void validateFilterDefinition(FilterDefinition definition, String filterName) {
        if (definition == null) {
            throw new IllegalArgumentException("Unknown filter: " + filterName);
        }
    }

    public void validateFilterParameters(String[] args, int nextIndex, String filterName) {
        if (nextIndex > args.length) {
            throw new IllegalArgumentException("Missing parameters for filter: " + filterName);
        }
    }
}
