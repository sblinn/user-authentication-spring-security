package com.sblinn.userlogin.dao;

public class InvalidEmailException extends Exception {

    public InvalidEmailException() {
        super();
    }

    public InvalidEmailException(String errorMessage) {
        super(errorMessage);
    }
    
}
