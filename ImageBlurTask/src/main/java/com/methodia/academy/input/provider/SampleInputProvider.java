package com.methodia.academy.input.provider;

import com.methodia.academy.filter.BlurFilter;
import com.methodia.academy.filter.BoxBlurFilter;
import com.methodia.academy.filter.GaussianBlurFilter;
import com.methodia.academy.filter.MedianBlurFilter;

public class SampleInputProvider implements InputProvider {
    private static final String UNBLURRED_IMAGE_INPUT_FILE_PATH = "unblurred.jpg";
    private static final String BLURRED_IMAGE_OUTPUT_FILE_PATH = "blurred.jpg";

    @Override
    public String getUnblurredImageInputFilePath() {
        return UNBLURRED_IMAGE_INPUT_FILE_PATH;
    }

    @Override
    public String getBlurredImageOutputFilePath() {
        return BLURRED_IMAGE_OUTPUT_FILE_PATH;
    }

    @Override
    public BlurFilter getBlurFilter() {
        return new MedianBlurFilter();
    }
}
