package com.charon.exceptions;

public class XMLValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2129599994753529648L;

	 XMLValidationException(String message){ super(message);}
	 XMLValidationException(Throwable cause)  { super(cause); }
	 XMLValidationException(String message, Throwable cause)  { super(message, cause); }

}
