package com.methodia.academy.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FileUtil {
    public static File readFile(String filePath) {
        File file = new File(filePath);
        if (!file.isFile()) {
            throw new IllegalArgumentException("File does not exist: " + filePath);
        }
        return file;
    }

    public static BufferedImage readImage(String filePath) {
        try {
            BufferedImage image = ImageIO.read(readFile(filePath));
            if (image == null) {
                throw new IllegalArgumentException("Unsupported image format: " + filePath);
            }
            return image;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read image: " + filePath, e);
        }
    }

    public static void writeImage(BufferedImage image, String outputPath) {
        String format = extractFormat(outputPath);
        try {
            if (!ImageIO.write(image, format, new File(outputPath))) {
                throw new IllegalArgumentException("Unsupported output image format: " + outputPath);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to write image: " + outputPath, e);
        }
    }

    private static String extractFormat(String outputPath) {
        int dotIndex = outputPath.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == outputPath.length() - 1) {
            throw new IllegalArgumentException("Output file must have an extension: " + outputPath);
        }
        return outputPath.substring(dotIndex + 1);
    }
}
