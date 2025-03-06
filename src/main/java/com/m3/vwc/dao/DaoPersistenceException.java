package com.m3.vwc.dao;

public class DaoPersistenceException extends RuntimeException {
    public DaoPersistenceException(String message) {
        super(message);
    }

    public DaoPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}

