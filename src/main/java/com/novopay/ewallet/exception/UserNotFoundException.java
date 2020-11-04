/**
 * 
 */
package com.novopay.ewallet.exception;

/**
 * @author Raunak Agarwal
 *
 */
public class UserNotFoundException extends RuntimeException{

	public UserNotFoundException(String message) {
		super(message);
	}
	
}
