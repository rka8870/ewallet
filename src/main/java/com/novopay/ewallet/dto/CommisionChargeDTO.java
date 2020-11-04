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
public class CommisionChargeDTO {
	@JsonIgnore
	private CredentialDTO credential;
	private String username;
	private Double comision = new Double(0);
	private Double charge = new Double(0);;
}
 

