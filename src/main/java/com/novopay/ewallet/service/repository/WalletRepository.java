/**
 * 
 */
package com.novopay.ewallet.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.novopay.ewallet.entity.Wallet;

/**
 * @author User
 *
 */
@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long>{
	
	public Optional<Wallet> findByUser_UserName(String userName);
}
