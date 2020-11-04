/**
 * 
 */
package com.novopay.ewallet.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.novopay.ewallet.entity.Transaction;

/**
 * @author User
 *
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	
}
