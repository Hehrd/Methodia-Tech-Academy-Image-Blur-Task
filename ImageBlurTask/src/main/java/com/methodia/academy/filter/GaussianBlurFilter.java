package com.methodia.academy.filter;

public class GaussianBlurFilter extends ConvolutionBlurFilter {
    private static final int DEFAULT_GAUSSIAN_KERNEL_SIZE = 3;

    public GaussianBlurFilter() {
        this(DEFAULT_GAUSSIAN_KERNEL_SIZE);
    }

    public GaussianBlurFilter(int kernelSize) {
        super(createGaussianKernel(kernelSize));
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
