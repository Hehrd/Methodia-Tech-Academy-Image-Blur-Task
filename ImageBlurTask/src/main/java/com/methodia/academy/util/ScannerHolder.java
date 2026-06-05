package com.methodia.academy.util;

import java.io.InputStream;
import java.util.Scanner;

public class ScannerHolder {
    public static Scanner scanner;
    public static void init(InputStream inputStream) {
        scanner = new Scanner(inputStream);
    }
    public static void close() {
        if (scanner != null) {
            scanner.close();
            scanner = null;
        }
    }
}
