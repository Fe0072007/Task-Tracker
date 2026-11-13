package com.tasktracker.model.Exceptions;
    
public class EmptyException extends Exception{
    
    private String message;

    public EmptyException() { }

    public EmptyException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}

