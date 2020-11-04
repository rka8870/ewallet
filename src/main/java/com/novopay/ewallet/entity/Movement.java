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
public class Movement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long movementId;
	@ManyToOne
	private Transaction transaction;
	@ManyToOne
	private Wallet wallet;
	private Date entryDate;
	private String description;
	private Double debitedAmount;
	private Double creditedAmount;
	private Boolean deleted;
}
 

