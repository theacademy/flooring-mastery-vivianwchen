package com.m3.vwc.dao;

public class InvalidOptionException extends Exception {
    public InvalidOptionException(String message) {
        super(message);
    }

    public InvalidOptionException(String message, Throwable cause) {
        super(message, cause);
    }
}

