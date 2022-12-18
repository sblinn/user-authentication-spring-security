package com.sblinn.userlogin.dao;

public class InvalidUsernameException extends Exception {

    public InvalidUsernameException() {
        super();
    }

    public InvalidUsernameException(String errorMessage) {
        super(errorMessage);
    }
    
}
