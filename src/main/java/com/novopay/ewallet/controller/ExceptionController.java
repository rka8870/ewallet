/**
 * 
 */
package com.novopay.ewallet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.novopay.ewallet.exception.AuthenticationException;
import com.novopay.ewallet.exception.TransactionException;
import com.novopay.ewallet.exception.UserAlreadyExistsException;
import com.novopay.ewallet.exception.UserException;
import com.novopay.ewallet.exception.UserNotFoundException;
import com.novopay.ewallet.exception.WalletException;
import com.novopay.ewallet.exception.WrongUserCredentialsException;

/**
 * @author Raunak Agarwal
 *
 */
@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(value = UserAlreadyExistsException.class)
	public ResponseEntity<Object> userAlreadyExistException(UserAlreadyExistsException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.OK);
	}
	
	@ExceptionHandler(value = UserNotFoundException.class)
	public ResponseEntity<Object> userNotFoundException(UserNotFoundException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = WrongUserCredentialsException.class)
	public ResponseEntity<Object> wrongUserCredentialsException(WrongUserCredentialsException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = UserException.class)
	public ResponseEntity<Object> userException(UserException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.EXPECTATION_FAILED);
	}
	
	@ExceptionHandler(value = WalletException.class)
	public ResponseEntity<Object> walletException(WalletException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.EXPECTATION_FAILED);
	}
	
	@ExceptionHandler(value = TransactionException.class)
	public ResponseEntity<Object> transactionException(TransactionException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.EXPECTATION_FAILED);
	}
	
	@ExceptionHandler(value = AuthenticationException.class)
	public ResponseEntity<Object> authenticationException(AuthenticationException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
	}
	
}
 

