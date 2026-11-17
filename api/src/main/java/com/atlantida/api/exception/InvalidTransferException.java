// ./src/main/java/com/atlantida/api/exception/InvalidTransferException.java
package com.atlantida.api.exception;

public class InvalidTransferException extends RuntimeException {
    public InvalidTransferException(String message) {
        super(message);
    }
}