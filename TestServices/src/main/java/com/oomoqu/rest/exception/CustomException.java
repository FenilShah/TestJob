package com.oomoqu.rest.exception;


public class CustomException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	 
    public CustomException() {
        super();
    }
 
    public CustomException(String message) {
        super(message);
        this.message = message;
    }
 
    public CustomException(Throwable cause) {
        super(cause);
    }
 
    @Override
    public String toString() {
        return message;
    }
 
    @Override
    public String getMessage() {
        return message;
    }
	
}
