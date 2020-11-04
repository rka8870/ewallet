/**
 * 
 */
package com.novopay.ewallet.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Raunak Agarwal
 *
 */
@Data
@NoArgsConstructor
public class UserDTO {
	
	
	private String userName;
	private String phoneNo;
	private String password;
	private String confirmPassword;
	private Double balance;
}
