/**
 * 
 */
package com.novopay.ewallet.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novopay.ewallet.dto.UserDTO;
import com.novopay.ewallet.entity.User;
import com.novopay.ewallet.exception.UserAlreadyExistsException;
import com.novopay.ewallet.exception.UserException;
import com.novopay.ewallet.exception.UserNotFoundException;
import com.novopay.ewallet.exception.WrongUserCredentialsException;
import com.novopay.ewallet.service.repository.UserRepository;
import com.novopay.ewallet.utility.EncryptionUtility;

/**
 * @author User
 *
 */
@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;	
	
	@Autowired
	private WalletService walletService;
	
	private EncryptionUtility encryptionUtility= new EncryptionUtility();
	
	public User createUser(UserDTO dto) {
		Optional<User> existingUser = userRepository.findByUserName(dto.getUserName());	
		if(existingUser.isPresent()) {
			throw new UserAlreadyExistsException("Username : "+ dto.getUserName() + " already exists");
		}
		User user = new User();
		user.setUserName(dto.getUserName());
		user.setPhoneNo(dto.getPhoneNo());
		if(!isPasswordMatch(dto.getPassword(),dto.getConfirmPassword())) {
			throw new UserException("Passwords entered did not match");
		}
		user.setPassword(encryptionUtility.getEncodedPassword(dto.getPassword()));
		user.setApproved(Boolean.TRUE);
		user.setDeleted(Boolean.FALSE);
		user = userRepository.save(user);
		walletService.createWallet(user);
		return user;
	}

	private boolean isPasswordMatch(String password, String confirmPassword) {
		return password.equals(confirmPassword);
	}

	public User getUser(UserDTO dto) {
		Optional<User> optionalUser = userRepository.findByUserName(dto.getUserName());
		optionalUser.orElseThrow(()->{
				throw new UserNotFoundException("Username : "+dto.getUserName() + " not found");
			});
		User user = optionalUser.get();
		if(!encryptionUtility.isPasswordMatch(dto.getPassword(), user.getPassword())) {
			throw new WrongUserCredentialsException("Entered Password Did Not Match");
		}
		return user; 
	}
	
}
