package com.methodia.academy.filter.blur;

import com.methodia.academy.filter.FilterDefinition;
import com.methodia.academy.filter.ImageFilter;

import java.awt.image.BufferedImage;
import java.util.List;

public class AverageBrightnessBlurFilter extends ImageFilter {
    private static final FilterDefinition<AverageBrightnessBlurFilter> FILTER_DEFINITION =
            new FilterDefinition<>(
                    "averagebrightnessblur",
                    List.of(FilterDefinition.positiveInt("radius")),
                    arguments -> new AverageBrightnessBlurFilter((Integer) arguments.getFirst())
            );
    private static final int DEFAULT_RADIUS = 5;

    private final int radius;

    public AverageBrightnessBlurFilter() {
        this(DEFAULT_RADIUS);
    }

    public AverageBrightnessBlurFilter(int radius) {
        super(FILTER_DEFINITION);
        if (radius < 1) {
            throw new IllegalArgumentException("Radius must be at least 1");
        }
        this.radius = radius;
    }

    public static FilterDefinition<AverageBrightnessBlurFilter> getDefinition() {
        return FILTER_DEFINITION;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] sourcePixels = image.getRGB(0, 0, width, height, null, 0, width);
        int[] resultPixels = new int[sourcePixels.length];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                resultPixels[y * width + x] = getBlurredBrightnessColor(sourcePixels, width, height, x, y);
            }
        }

        image.setRGB(0, 0, width, height, resultPixels, 0, width);
        return image;
    }

    private int getBlurredBrightnessColor(int[] pixels, int width, int height, int centerX, int centerY) {
        int startY = Math.max(0, centerY - radius);
        int endY = Math.min(height - 1, centerY + radius);
        int startX = Math.max(0, centerX - radius);
        int endX = Math.min(width - 1, centerX + radius);
        int sampleCount = 0;
        int alphaSum = 0;
        int brightnessSum = 0;

        for (int y = startY; y <= endY; y++) {
            int rowOffset = y * width;
            for (int x = startX; x <= endX; x++) {
                int pixel = pixels[rowOffset + x];
                int red = (pixel >>> 16) & 0xFF;
                int green = (pixel >>> 8) & 0xFF;
                int blue = pixel & 0xFF;
                alphaSum += (pixel >>> 24) & 0xFF;
                brightnessSum += (red + green + blue) / 3;
                sampleCount++;
            }
        }

        int averageAlpha = alphaSum / sampleCount;
        int averageBrightness = brightnessSum / sampleCount;
        return (averageAlpha << 24)
                | (averageBrightness << 16)
                | (averageBrightness << 8)
                | averageBrightness;
    }
}
