package com.methodia.academy.filter;

import java.awt.image.BufferedImage;
import java.util.function.Supplier;

public interface BlurFilter {
    void applyBlur(BufferedImage image);

    enum BlurAlgorithm {
        MEDIAN(MedianBlurFilter::new),
        GAUSSIAN(GaussianBlurFilter::new),
        BOX(BoxBlurFilter::new);

        private final Supplier<BlurFilter> filterSupplier;

        BlurAlgorithm(Supplier<BlurFilter> filterSupplier) {
            this.filterSupplier = filterSupplier;
        }

        public BlurFilter createFilter() {
            return filterSupplier.get();
        }
    }
}
