package com.charon.exceptions;

public class UserDataException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8893570875283551009L;
	public UserDataException(String message){ super(message);}
	public UserDataException(Throwable cause)  { super(cause); }
	public UserDataException(String message, Throwable cause)  { super(message, cause); }

}
