/**
 * 
 */
package com.novopay.ewallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novopay.ewallet.dto.CredentialDTO;
import com.novopay.ewallet.dto.UserDTO;
import com.novopay.ewallet.entity.User;
import com.novopay.ewallet.exception.AuthenticationException;

/**
 * @author Raunak Agarwal
 *
 */
@Service
public class AuthenticationService {

	@Autowired
	private UserService userService;
	
	public boolean authenticateUser(CredentialDTO dto) {
		try{
			UserDTO userDTO = new UserDTO();
			userDTO.setUserName(dto.getUserName());
			userDTO.setPassword(dto.getPassword());
			User user = userService.getUser(userDTO);
			return true;
		}
		catch (Exception e) {
			throw new AuthenticationException("Wrong Credentials(User Authentication faliure)");
		}
	}
	
}
 

