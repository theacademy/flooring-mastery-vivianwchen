package com.m3.vwc.ui;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class UserIOImpl implements UserIO{

    Scanner sc = new Scanner(System.in);

    public void print(String prompt){
        System.out.println(prompt);
    }

    public String readString(String prompt){
        print(prompt);
        String message = sc.nextLine();
        return message;
    }

    public int readInt(String prompt, int min, int max) {
        print(prompt);
        int message = 0;
        boolean valid = false;

        while (!valid) {
            String input = sc.nextLine();
            try {
                message = Integer.parseInt(input);  // Try converting to integer
                if (message >= min && message <= max) {  // Check if within range
                    valid = true;  // Valid input
                } else {
                    System.out.println("Error: Please enter a number between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid input. Please enter a valid integer.");
            }
        }

        return message;  // Return valid integer input
    }
    public int readInt(String prompt){
        print(prompt);
        int input = sc.nextInt();
        sc.nextLine();
        return input;
    }
    public double readDouble(String prompt,double min){
        print(prompt);
        double message = sc.nextDouble();
        while (message < min){
            System.out.println("    Error: Please enter a number bigger than " + min);
            message = sc.nextDouble();
        }
        sc.nextLine();
        return message;
    }

}
