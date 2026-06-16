package com.methodia.academy;

import com.methodia.academy.filter.FilterRegistry;
import com.methodia.academy.filter.ImageFilter;
import com.methodia.academy.input.InputHandler;
import com.methodia.academy.util.FileUtil;
import com.methodia.academy.validation.InputValidator;

import java.awt.image.BufferedImage;
import java.util.List;

public class Main {
    private static final String OUTPUT_DIR_PATH = "images/";

    public static void main(String[] args) {
        InputHandler inputHandler = new InputHandler(new FilterRegistry(), new InputValidator());
        if (args.length < 2) {
            inputHandler.printUsage();
            System.exit(1);
        }

        String inputImagePath = inputHandler.getInputImagePath(args);
        List<ImageFilter> filters = inputHandler.getFilters(args);
        BufferedImage image = FileUtil.readImage(inputImagePath);
        StringBuilder fileNameBuilder = new StringBuilder();
        for (ImageFilter filter : filters) {
            image = filter.apply(image);
            if (!fileNameBuilder.isEmpty()) {
                fileNameBuilder.append(' ');
            }
            fileNameBuilder.append(filter.getName());
        }
        String fileName = fileNameBuilder + "." + FileUtil.extractFormat(inputImagePath);
        FileUtil.writeImage(image, OUTPUT_DIR_PATH + fileName);
    }
}
