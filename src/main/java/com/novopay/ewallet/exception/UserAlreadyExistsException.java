/**
 * 
 */
package com.novopay.ewallet.exception;

/**
 * @author Raunak Agarwal
 *
 */
public class UserAlreadyExistsException extends RuntimeException{

	public UserAlreadyExistsException(String message) {
		super(message);
	}
}
