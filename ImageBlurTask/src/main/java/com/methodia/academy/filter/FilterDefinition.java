package com.methodia.academy.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public record FilterDefinition<T extends ImageFilter>(String name,
                                                      List<ArgumentParser<?>> argumentParsers,
                                                      Function<List<Object>, T> factory) {
    public FilterDefinition {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Filter name cannot be blank");
        }
        argumentParsers = List.copyOf(argumentParsers);
    }

    public int argumentCount() {
        return argumentParsers.size();
    }

    public ImageFilter imageFilter(List<String> args) {
        if (args.size() != argumentCount()) {
            throw new IllegalArgumentException("Filter " + name + " requires " + argumentCount() + " arguments, but got " + args.size());
        }
        List<Object> parsedArguments = new ArrayList<>(argumentParsers.size());
        for (int i = 0; i < argumentParsers.size(); i++) {
            parsedArguments.add(argumentParsers.get(i).parse(args.get(i)));
        }
        return factory.apply(parsedArguments);
    }

    public static ArgumentParser<Integer> positiveInt(String parameterName) {
        return value -> {
            int parsedValue = parseInt(value, parameterName);
            if (parsedValue < 1) {
                throw new IllegalArgumentException(parameterName + " must be a positive integer");
            }
            return parsedValue;
        };
    }

    public static ArgumentParser<Integer> nonNegativeInt(String parameterName) {
        return value -> {
            int parsedValue = parseInt(value, parameterName);
            if (parsedValue < 0) {
                throw new IllegalArgumentException(parameterName + " must be a non-negative integer");
            }
            return parsedValue;
        };
    }

    public static <T> ArgumentParser<T> value(String parameterName, Function<String, T> parser) {
        return rawValue -> {
            try {
                return parser.apply(rawValue);
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("Invalid value for " + parameterName + ": " + rawValue, e);
            }
        };
    }

    private static int parseInt(String value, String parameterName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(parameterName + " must be an integer", e);
        }
    }

    @FunctionalInterface
    public interface ArgumentParser<T> {
        T parse(String value);
    }
}
