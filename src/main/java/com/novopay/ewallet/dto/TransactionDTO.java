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
public class TransactionDTO {

	private Long transactionId;
	private String transactionType;
	private Double amount;
	private String Description; 
}
 

