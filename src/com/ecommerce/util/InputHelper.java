package com.ecommerce.util;

import java.util.Scanner;

public class InputHelper {

    private static final Scanner scanner = new Scanner(System.in);

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid number. Please enter a valid integer.");
            }
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid number. Please enter a valid decimal number.");
            }
        }
    }

    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
