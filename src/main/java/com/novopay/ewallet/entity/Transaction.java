/**
 * 
 */
package com.novopay.ewallet.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Raunak Agarwal
 *
 */
@Entity
@NoArgsConstructor
@Data
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transactionId;
	@ManyToOne
	private Wallet fromWallet;
	@ManyToOne
	private Wallet toWallet;
	private Double amountDebited;
	private Double amountCredited;
	private String description;
	private Date transactionDate;
	private String transactionStatus;
	private String TransactionType;
	private Boolean deleted;
}
