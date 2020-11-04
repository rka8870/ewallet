/**
 * 
 */
package com.novopay.ewallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.novopay.ewallet.dto.UserDTO;
import com.novopay.ewallet.entity.User;
import com.novopay.ewallet.service.UserService;

/**
 * @author Raunak Agarwal
 *
 */
@RestController
@RequestMapping(value = "/api/users",consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

	@Autowired
	private UserService userService;
	
	/**
	 * This API is used to add user by a given user by passing valid 
	 * inputs in user dto
	 * @param dto
	 * @return
	 */
	@PostMapping("/add")
	public ResponseEntity<User> createUser(@RequestBody UserDTO dto) {
		return new ResponseEntity<User>(userService.createUser(dto), HttpStatus.OK);
	}
	
	/**
	 * This API is used to login and get details of a given user by passing valid 
	 * credentials
	 * @param dto
	 * @return
	 */
	@PostMapping("/login")
	public ResponseEntity<User> loginUser(@RequestBody UserDTO dto) {
		return new ResponseEntity<User>(userService.getUser(dto), HttpStatus.OK);
	}
	
}
