/**
 * 
 */
package com.novopay.ewallet.utility;

import org.jasypt.util.password.StrongPasswordEncryptor;

/**
 * @author Raunak Agarwal
 *
 */
public class EncryptionUtility {

	private static StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
	
	public String getEncodedPassword(String password) { 
		return encryptor.encryptPassword(password);
	}

	public Boolean isPasswordMatch(String rawPassword, String encodedPassword) {
		return encryptor.checkPassword(rawPassword, encodedPassword);
	}

}
 

