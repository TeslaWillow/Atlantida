// ./src/main/java/com/atlantida/api/exception/InsufficientFundsException.java
package com.atlantida.api.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}