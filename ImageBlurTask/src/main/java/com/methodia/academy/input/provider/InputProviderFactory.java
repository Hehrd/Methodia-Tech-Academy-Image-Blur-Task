package com.methodia.academy.input.provider;

import com.methodia.academy.util.ScannerHolder;

import java.util.function.Supplier;

public abstract class InputProviderFactory {

    public static InputProvider getInputProvider(String inputMode) {
        if (inputMode == null || inputMode.isBlank()) {
            return InputMode.SAMPLE.getInputProvider();
        }
        try {
            return InputMode.valueOf(inputMode.toUpperCase().trim()).getInputProvider();
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input mode specified. Defaulting to SAMPLE.");
            return InputMode.SAMPLE.getInputProvider();
        }
    }


    private enum InputMode {
        SAMPLE(() -> new SampleInputProvider()),
        MANUAL(() -> new ManualInputProvider(ScannerHolder.scanner));

        private final Supplier<InputProvider> inputProviderSupplier;

        InputMode(Supplier<InputProvider> inputProviderSupplier) {
            this.inputProviderSupplier = inputProviderSupplier;
        }

        public InputProvider getInputProvider() {
            return inputProviderSupplier.get();
        }
    }
}
