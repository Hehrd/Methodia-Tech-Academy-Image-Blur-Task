package com.methodia.academy.filter;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class CropFilter extends ImageFilter {
    private static final FilterDefinition<CropFilter> FILTER_DEFINITION =
            new FilterDefinition<>(
                    "crop",
                    List.of(
                            FilterDefinition.nonNegativeInt("x"),
                            FilterDefinition.nonNegativeInt("y"),
                            FilterDefinition.positiveInt("width"),
                            FilterDefinition.positiveInt("height")
                    ),
                    arguments -> new CropFilter(
                            (Integer) arguments.get(0),
                            (Integer) arguments.get(1),
                            (Integer) arguments.get(2),
                            (Integer) arguments.get(3)
                    )
            );
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public CropFilter(int x, int y, int width, int height) {
        super(FILTER_DEFINITION);
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Crop position must be non-negative");
        }
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("Crop width and height must be positive");
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        if (x + width > image.getWidth() || y + height > image.getHeight()) {
            throw new IllegalArgumentException("Crop rectangle is outside the image bounds");
        }
        BufferedImage cropped = new BufferedImage(
                width,
                height,
                image.getType() == BufferedImage.TYPE_CUSTOM ? BufferedImage.TYPE_INT_ARGB : image.getType()
        );
        Graphics2D graphics = cropped.createGraphics();
        graphics.drawImage(image, 0, 0, width, height, x, y, x + width, y + height, null);
        graphics.dispose();
        return cropped;
    }

    public CropFilter() {
        this(0, 0, 1, 1);
    }

    public static FilterDefinition<CropFilter> getDefinition() {
        return FILTER_DEFINITION;
    }
}
