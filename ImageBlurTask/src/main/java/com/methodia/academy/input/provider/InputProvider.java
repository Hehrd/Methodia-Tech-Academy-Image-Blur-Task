package com.methodia.academy.input.provider;

import com.methodia.academy.filter.BlurFilter;

public interface InputProvider {
    String getUnblurredImageInputFilePath();
    String getBlurredImageOutputFilePath();
    BlurFilter getBlurFilter();
}
