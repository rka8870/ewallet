/**
 * 
 */
package com.novopay.ewallet.dto;
/**
 * @author Raunak Agarwal
 *
 */

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PassBookDTO {

	private String userName;
	
	private Double balance;
	
	private List<TransactionDTO> transactions = new ArrayList<>();
	
}
 

