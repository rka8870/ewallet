/**
 * 
 */
package com.novopay.ewallet.service.repository;
/**
 * @author Raunak Agarwal
 *
 */

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.novopay.ewallet.entity.Movement;

public interface MovementRepository extends JpaRepository<Movement, Long>{

	public List<Movement> findByTransaction_TransactionId(Long transactionId);
	
	public List<Movement> findByWallet_User_UserNameOrderByEntryDateDesc(String userName);
	
}
 

