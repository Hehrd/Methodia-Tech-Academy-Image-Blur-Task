package com.methodia.academy.filter;

import java.awt.image.BufferedImage;

import static java.lang.Double.sum;
import static java.lang.Math.clamp;

abstract class ConvolutionBlurFilter implements BlurFilter {
    private final double[][] kernel;
    private final double kernelWeight;
    private final int kernelCenterX;
    private final int kernelCenterY;

    protected ConvolutionBlurFilter(double[][] kernel) {
        this.kernel = copyKernel(kernel);
        this.kernelWeight = getKernelWeight(kernel);
        this.kernelCenterY = kernel.length / 2;
        this.kernelCenterX = kernel[0].length / 2;
    }

    @Override
    public void applyBlur(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] sourcePixels = image.getRGB(0, 0, width, height, null, 0, width);
        int[] resultPixels = new int[sourcePixels.length];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                resultPixels[y * width + x] = getConvolutedColor(sourcePixels, width, height, x, y);
            }
        }

        image.setRGB(0, 0, width, height, resultPixels, 0, width);
    }

    private int getConvolutedColor(int[] pixels, int width, int height, int centerX, int centerY) {
        double alpha = 0;
        double red = 0;
        double green = 0;
        double blue = 0;

        for (int kernelY = 0; kernelY < kernel.length; kernelY++) {
            int pixelY = clamp(centerY + kernelY - kernelCenterY, 0, height - 1);
            int rowOffset = pixelY * width;
            for (int kernelX = 0; kernelX < kernel[kernelY].length; kernelX++) {
                int pixelX = clamp(centerX + kernelX - kernelCenterX, 0, width - 1);
                int pixel = pixels[rowOffset + pixelX];
                double weight = kernel[kernelY][kernelX];
                alpha += ((pixel >>> 24) & 0xFF) * weight;
                red += ((pixel >>> 16) & 0xFF) * weight;
                green += ((pixel >>> 8) & 0xFF) * weight;
                blue += (pixel & 0xFF) * weight;
            }
        }

        int alphaValue = clampColor((int) Math.round(alpha / kernelWeight));
        int redValue = clampColor((int) Math.round(red / kernelWeight));
        int greenValue = clampColor((int) Math.round(green / kernelWeight));
        int blueValue = clampColor((int) Math.round(blue / kernelWeight));

        return (alphaValue << 24)
                | (redValue << 16)
                | (greenValue << 8)
                | blueValue;
    }

    private static double[][] copyKernel(double[][] sourceKernel) {
        double[][] copiedKernel = new double[sourceKernel.length][];
        for (int rowIndex = 0; rowIndex < sourceKernel.length; rowIndex++) {
            copiedKernel[rowIndex] = sourceKernel[rowIndex].clone();
        }
        return copiedKernel;
    }

    private static int clampColor(int value) {
        return clamp(value, 0, 255);
    }

    private static double getKernelWeight(double[][] kernel) {
        double weight = 0;
        for (double[] row : kernel) {
            for (double value : row) {
                weight += value;
            }
        }
        return weight;
    }

}
