package com.methodia.academy.filter.blur;

import com.methodia.academy.filter.FilterDefinition;

import java.util.List;

public class BoxBlurFilter extends ConvolutionBlurFilter {
    private static final FilterDefinition<BoxBlurFilter> FILTER_DEFINITION =
            new FilterDefinition<>(
                    "boxblur",
                    List.of(FilterDefinition.positiveInt("kernel-size")),
                    arguments -> new BoxBlurFilter((Integer) arguments.getFirst())
            );
    private static final int DEFAULT_BOX_KERNEL_SIZE = 3;

    public BoxBlurFilter() {
        this(DEFAULT_BOX_KERNEL_SIZE);
    }

    public BoxBlurFilter(int kernelSize) {
        super(FILTER_DEFINITION, createBoxKernel(kernelSize));
    }

    public static FilterDefinition<BoxBlurFilter> getDefinition() {
        return FILTER_DEFINITION;
    }

    private static double[][] createBoxKernel(int size) {
        double[][] kernel = new double[size][size];
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                kernel[row][column] = 1;
            }
        }
        return kernel;
    }

}
