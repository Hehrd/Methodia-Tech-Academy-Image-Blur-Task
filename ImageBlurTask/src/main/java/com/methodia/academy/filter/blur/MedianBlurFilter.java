package com.methodia.academy.filter.blur;

import com.methodia.academy.filter.FilterDefinition;
import com.methodia.academy.filter.ImageFilter;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class MedianBlurFilter extends ImageFilter {
    private static final FilterDefinition<MedianBlurFilter> FILTER_DEFINITION =
            new FilterDefinition<>(
                    "medianblur",
                    List.of(FilterDefinition.positiveInt("radius")),
                    arguments -> new MedianBlurFilter((Integer) arguments.getFirst())
            );
    private static final int DEFAULT_RADIUS = 5;
    private static final int COLOR_RANGE = 256;

    private final int radius;
    private final Buffer buffer;

    public MedianBlurFilter() {
        this(DEFAULT_RADIUS);
    }

    public MedianBlurFilter(int radius) {
        super(FILTER_DEFINITION);
        if (radius < 1) {
            throw new IllegalArgumentException("Radius must be at least 1");
        }
        this.radius = radius;
        this.buffer = new Buffer();
    }

    public static FilterDefinition<MedianBlurFilter> getDefinition() {
        return FILTER_DEFINITION;
    }

    public BufferedImage apply(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] sourcePixels = image.getRGB(0, 0, width, height, null, 0, width);
        int[] resultPixels = new int[sourcePixels.length];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                resultPixels[y * width + x] = getMedianColor(sourcePixels, width, height, x, y);
            }
        }

        image.setRGB(0, 0, width, height, resultPixels, 0, width);
        return image;
    }

    private int getMedianColor(int[] pixels, int width, int height, int centerX, int centerY) {
        int[] alphaHistogram = buffer.alphaHistogram();
        int[] redHistogram = buffer.redHistogram();
        int[] greenHistogram = buffer.greenHistogram();
        int[] blueHistogram = buffer.blueHistogram();
        Arrays.fill(alphaHistogram, 0);
        Arrays.fill(redHistogram, 0);
        Arrays.fill(greenHistogram, 0);
        Arrays.fill(blueHistogram, 0);

        int sampleCount = 0;
        int startY = Math.max(0, centerY - radius);
        int endY = Math.min(height - 1, centerY + radius);
        int startX = Math.max(0, centerX - radius);
        int endX = Math.min(width - 1, centerX + radius);

        for (int y = startY; y <= endY; y++) {
            int rowOffset = y * width;
            for (int x = startX; x <= endX; x++) {
                int pixel = pixels[rowOffset + x];
                alphaHistogram[(pixel >>> 24) & 0xFF]++;
                redHistogram[(pixel >>> 16) & 0xFF]++;
                greenHistogram[(pixel >>> 8) & 0xFF]++;
                blueHistogram[pixel & 0xFF]++;
                sampleCount++;
            }
        }

        return (findMedian(alphaHistogram, sampleCount) << 24)
                | (findMedian(redHistogram, sampleCount) << 16)
                | (findMedian(greenHistogram, sampleCount) << 8)
                | findMedian(blueHistogram, sampleCount);
    }

    private int findMedian(int[] histogram, int sampleCount) {
        int targetIndex = sampleCount / 2;
        int cumulativeCount = 0;
        for (int colorValue = 0; colorValue < COLOR_RANGE; colorValue++) {
            cumulativeCount += histogram[colorValue];
            if (cumulativeCount > targetIndex) {
                return colorValue;
            }
        }
        throw new IllegalStateException("Unable to determine median color");
    }


    private record Buffer(int[] redHistogram, int[] greenHistogram, int[] blueHistogram, int[] alphaHistogram) {
        private Buffer() {
            this(new int[COLOR_RANGE], new int[COLOR_RANGE], new int[COLOR_RANGE], new int[COLOR_RANGE]);
        }
    }
}
