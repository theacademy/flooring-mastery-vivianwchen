package com.m3.vwc.service;

public class InvalidInputException extends Exception{
    public InvalidInputException(String message) { super(message);}

    public InvalidInputException(String message, Throwable cause) { super(message, cause); }
}
