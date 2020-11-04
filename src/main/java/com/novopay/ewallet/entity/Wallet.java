/**
 * 
 */
package com.novopay.ewallet.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Raunak Agarwal
 *
 */
@Entity
@NoArgsConstructor
@Data
public class Wallet {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long walletId;
	private Double amount;
	@OneToOne
	private User user;
	private Boolean deleted;
}
