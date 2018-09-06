package com.bj.exception;

public class SessionTimeoutException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public SessionTimeoutException(String message){
	    super(message);
	}

    public SessionTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
