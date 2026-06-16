package com.methodia.academy.filter;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Locale;

public class ColorComponentFilter extends ImageFilter {
    private static final FilterDefinition<ColorComponentFilter> FILTER_DEFINITION =
            new FilterDefinition<>(
                    "colorfilter",
                    List.of(FilterDefinition.value("component", ColorComponent::fromName)),
                    arguments -> new ColorComponentFilter((ColorComponent) arguments.getFirst())
            );
    private final ColorComponent component;

    public ColorComponentFilter(ColorComponent component) {
        super(FILTER_DEFINITION);
        if (component == null) {
            throw new IllegalArgumentException("Color component cannot be null");
        }
        this.component = component;
    }

    public ColorComponentFilter() {
        super(FILTER_DEFINITION);
        this.component = ColorComponent.RED;
    }

    public static FilterDefinition<ColorComponentFilter> getDefinition() {
        return FILTER_DEFINITION;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);

        for (int index = 0; index < pixels.length; index++) {
            int pixel = pixels[index];
            int alpha = (pixel >>> 24) & 0xFF;
            int red = component == ColorComponent.RED ? (pixel >>> 16) & 0xFF : 0;
            int green = component == ColorComponent.GREEN ? (pixel >>> 8) & 0xFF : 0;
            int blue = component == ColorComponent.BLUE ? pixel & 0xFF : 0;
            pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
        }

        image.setRGB(0, 0, width, height, pixels, 0, width);
        return image;
    }

    public enum ColorComponent {
        RED,
        GREEN,
        BLUE;

        public static ColorComponent fromName(String name) {
            try {
                return valueOf(name.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Unsupported color component: " + name);
            }
        }
    }
}
