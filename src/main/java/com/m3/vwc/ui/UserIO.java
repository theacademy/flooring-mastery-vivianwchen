package com.m3.vwc.ui;

public interface UserIO {
    void print(String prompt);

   String readString(String prompt);

    int readInt(String prompt, int min, int max);

    int readInt(String prompt);

    double readDouble(String prompt, double min);

}
