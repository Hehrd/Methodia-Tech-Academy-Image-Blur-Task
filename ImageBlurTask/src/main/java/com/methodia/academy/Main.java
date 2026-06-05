package com.methodia.academy;

import com.methodia.academy.input.provider.InputProvider;
import com.methodia.academy.input.provider.InputProviderFactory;
import com.methodia.academy.util.ScannerHolder;

public class Main {
    public static void main(String[] args) {
        ScannerHolder.init(System.in);
        String inputMode = args.length > 0 ? args[0] : null;
        InputProvider inputProvider = InputProviderFactory.getInputProvider(inputMode);
        ImageBlurTask imageBlurTask = new ImageBlurTask(inputProvider);

        imageBlurTask.runTask();

        ScannerHolder.close();
    }
}
