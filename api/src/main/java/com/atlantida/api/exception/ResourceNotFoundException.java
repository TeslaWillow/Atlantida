// ./src/main/java/com/atlantida/api/exception/ResourceNotFoundException.java
package com.atlantida.api.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}