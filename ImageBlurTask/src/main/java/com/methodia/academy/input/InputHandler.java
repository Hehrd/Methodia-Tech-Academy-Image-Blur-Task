package com.methodia.academy.input;

import com.methodia.academy.filter.FilterDefinition;
import com.methodia.academy.filter.FilterRegistry;
import com.methodia.academy.filter.ImageFilter;
import com.methodia.academy.validation.InputValidator;

import java.util.ArrayList;
import java.util.List;

public class InputHandler {
    private final FilterRegistry filterRegistry;
    private final InputValidator inputValidator;

    public InputHandler(FilterRegistry filterRegistry, InputValidator inputValidator) {
        if (inputValidator == null) {
            throw new IllegalArgumentException("Input validator cannot be null");
        }
        inputValidator.validateFilterRegistry(filterRegistry);
        this.filterRegistry = filterRegistry;
        this.inputValidator = inputValidator;
    }

    public String getInputImagePath(String[] args) {
        inputValidator.validateArgs(args, 2);
        return args[0];
    }

    public List<ImageFilter> getFilters(String[] args) {
        inputValidator.validateArgs(args, 2);
        List<ImageFilter> filters = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            String filterName = args[i];
            FilterDefinition<? extends ImageFilter> filterDefinition = filterRegistry.getFilterDefinition(filterName);
            inputValidator.validateFilterDefinition(filterDefinition, filterName);

            int nextIndex = i + 1 + filterDefinition.argumentCount();
            inputValidator.validateFilterParameters(args, nextIndex, filterName);

            List<String> filterArgs = new ArrayList<>(filterDefinition.argumentCount());
            for (int argumentIndex = i + 1; argumentIndex < nextIndex; argumentIndex++) {
                filterArgs.add(args[argumentIndex]);
            }

            ImageFilter imageFilter = filterDefinition.imageFilter(filterArgs);
            filters.add(imageFilter);
            i = nextIndex - 1;
        }
        return filters;
    }

    public void printUsage() {
        System.out.println("Usage:");
        System.out.println("java ... <image-path-or-url> <filter> <params> [<filter> <params> ...]");
        System.out.println("Available filters:");
        System.out.println("  averagebrightnessblur <radius>");
        System.out.println("  boxblur <kernel-size>");
        System.out.println("  gaussianblur <kernel-size>");
        System.out.println("  medianblur <radius>");
        System.out.println("  colorfilter <red|green|blue>");
        System.out.println("  crop <x> <y> <width> <height>");
    }

}
