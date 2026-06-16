package com.methodia.academy.filter.blur;

import com.methodia.academy.filter.FilterDefinition;

import java.util.List;

public class GaussianBlurFilter extends ConvolutionBlurFilter {
    private static final FilterDefinition<GaussianBlurFilter> FILTER_DEFINITION =
            new FilterDefinition<>(
                    "gaussianblur",
                    List.of(FilterDefinition.positiveInt("kernel-size")),
                    arguments -> new GaussianBlurFilter((Integer) arguments.getFirst())
            );
    private static final int DEFAULT_GAUSSIAN_KERNEL_SIZE = 3;

    public GaussianBlurFilter() {
        this(DEFAULT_GAUSSIAN_KERNEL_SIZE);
    }

    public GaussianBlurFilter(int kernelSize) {
        super(FILTER_DEFINITION, createGaussianKernel(kernelSize));
    }

    public static FilterDefinition<GaussianBlurFilter> getDefinition() {
        return FILTER_DEFINITION;
    }

    private static double[][] createGaussianKernel(int size) {
        double[][] kernel = new double[size][size];
        int center = size / 2;
        double sigma = size / 6.0;
        double twoSigmaSquare = 2 * sigma * sigma;
        double sum = 0;

        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                int x = column - center;
                int y = row - center;
                kernel[row][column] = Math.exp(-(x * x + y * y) / twoSigmaSquare);
                sum += kernel[row][column];
            }
        }

        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                kernel[row][column] /= sum;
            }
        }

        return kernel;
    }
}
