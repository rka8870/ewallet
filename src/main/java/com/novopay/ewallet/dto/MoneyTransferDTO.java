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
public class MoneyTransferDTO {

	private CredentialDTO credential;
	private String toUserName;
	private Double amount;
	private String transferType;
	private String description;
}
 

