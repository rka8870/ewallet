/**
 * 
 */
package com.novopay.ewallet.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Raunak Agarwal
 *
 */
@Data
@NoArgsConstructor
public class WalletDTO {

	@JsonIgnore
	private CredentialDTO credential;
	
	private String userName;
	private Double balance;
	
}
 

