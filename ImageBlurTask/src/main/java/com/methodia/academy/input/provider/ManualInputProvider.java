package com.methodia.academy.input.provider;

import com.methodia.academy.filter.BlurFilter;
import com.methodia.academy.filter.BoxBlurFilter;
import com.methodia.academy.filter.GaussianBlurFilter;
import com.methodia.academy.filter.MedianBlurFilter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Supplier;

public class ManualInputProvider implements InputProvider {
    private final Scanner scanner;
    private final Map<BlurFilter.BlurAlgorithm, Supplier<BlurFilter>> blurFilterSuppliers;

    public ManualInputProvider(Scanner scanner) {
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner cannot be null");
        }
        this.scanner = scanner;
        this.blurFilterSuppliers = initializeBlurFilterSuppliers();
    }

    @Override
    public String getUnblurredImageInputFilePath() {
        System.out.println("Please enter the input file path for the unblurred image:");
        return readRequiredInput();
    }

    @Override
    public String getBlurredImageOutputFilePath() {
        System.out.println("Please enter the output file path for the blurred image:");
        return readRequiredInput();
    }

    @Override
    public BlurFilter getBlurFilter() {
        System.out.println("Please select a blur algorithm: MEDIAN, GAUSSIAN, or BOX.");
        do {
            String input = scanner.nextLine().trim();
            try {
                BlurFilter.BlurAlgorithm algorithm =
                        BlurFilter.BlurAlgorithm.valueOf(input.toUpperCase(Locale.ROOT));
                return blurFilterSuppliers.get(algorithm).get();
            } catch (IllegalArgumentException ignored) {
                System.out.println("Invalid input. Please enter MEDIAN, GAUSSIAN, or BOX.");
            }
        } while (true);
    }

    private BlurFilter readMedianFilter() {
        System.out.println("Please enter median blur radius (positive integer):");
        return new MedianBlurFilter(readPositiveInt());
    }

    private BlurFilter readGaussianFilter() {
        System.out.println("Please enter Gaussian blur kernel size (odd integer >= 3).");
        int kernelSize = readOddPositiveInt();
        do {
            if (kernelSize < 3) {
                System.out.println("Kernel size must be at least 3.");
                kernelSize = readOddPositiveInt();
                continue;
            }
            return new GaussianBlurFilter(kernelSize);
        } while (true);
    }

    private BlurFilter readBoxFilter() {
        System.out.println("Please enter box blur kernel size (odd integer >= 3).");
        int kernelSize = readOddPositiveInt();
        do {
            if (kernelSize < 3) {
                System.out.println("Kernel size must be at least 3.");
                kernelSize = readOddPositiveInt();
                continue;
            }
            return new BoxBlurFilter(kernelSize);
        } while (true);

    }



    private int readPositiveInt() {
        System.out.println("Please enter a positive integer:");
        do {
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Please enter a positive integer.");
        } while (true);
    }

    private int readOddPositiveInt() {
        System.out.println("Please enter an odd positive integer:");
        do {
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value > 0 && value % 2 != 0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Please enter an odd positive integer.");
        } while (true);
    }

    private String readRequiredInput() {
        do {
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Please try again.");
        } while (true);
    }

    private Map<BlurFilter.BlurAlgorithm, Supplier<BlurFilter>> initializeBlurFilterSuppliers() {
        Map<BlurFilter.BlurAlgorithm, Supplier<BlurFilter>> handlers = new HashMap<>();
        handlers.put(BlurFilter.BlurAlgorithm.MEDIAN, this::readMedianFilter);
        handlers.put(BlurFilter.BlurAlgorithm.GAUSSIAN, this::readGaussianFilter);
        handlers.put(BlurFilter.BlurAlgorithm.BOX, this::readBoxFilter);
        return handlers;
    }
}
