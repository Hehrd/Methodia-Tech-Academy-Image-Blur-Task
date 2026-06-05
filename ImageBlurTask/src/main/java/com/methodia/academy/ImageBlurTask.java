package com.methodia.academy;

import com.methodia.academy.filter.BlurFilter;
import com.methodia.academy.input.provider.InputProvider;
import com.methodia.academy.util.FileUtil;

import java.awt.image.BufferedImage;

public class ImageBlurTask {
    private final String unblurredImageInputFilePath;
    private final String blurredImageOutputFilePath;
    private final BlurFilter blurFilter;

    public ImageBlurTask(InputProvider inputProvider) {
        if (inputProvider == null) {
            throw new IllegalArgumentException("Input provider cannot be null");
        }
        this.unblurredImageInputFilePath = inputProvider.getUnblurredImageInputFilePath();
        this.blurredImageOutputFilePath = inputProvider.getBlurredImageOutputFilePath();
        this.blurFilter = inputProvider.getBlurFilter();
    }

    public void runTask() {
        BufferedImage image = FileUtil.readImage(unblurredImageInputFilePath);
        blurFilter.applyBlur(image);
        FileUtil.writeImage(image, blurredImageOutputFilePath);
    }
}
