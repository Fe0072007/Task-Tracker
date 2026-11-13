package com.tasktracker.model.Exceptions;

public class NotFoundException extends Exception{
    
    private String message;

    public NotFoundException() { }

    public NotFoundException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
