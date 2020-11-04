/**
 * 
 */
package com.novopay.ewallet.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novopay.ewallet.entity.User;
import com.novopay.ewallet.entity.Wallet;
import com.novopay.ewallet.exception.WalletException;
import com.novopay.ewallet.service.repository.WalletRepository;

/**
 * @author Raunak Agarwal
 *
 */
@Service
public class WalletService {
	
	@Autowired
	private WalletRepository walletRepository;
	
	public Wallet createWallet(User user) {
		Wallet wallet = new Wallet();
		wallet.setAmount(new Double(0));
		wallet.setUser(user);
		return walletRepository.save(wallet);
	}
	
	public Wallet getWalletDetail(User user) {
		Optional<Wallet> optionalwallet = walletRepository.findByUser_UserName(user.getUserName());
		optionalwallet.orElseThrow(()->{
			throw new WalletException("Wallet Not Associated with the user: " + user.getUserName());
		});
		return optionalwallet.get();
	}
	
	public Wallet getWalletDetail(String userName) {
		Optional<Wallet> optionalwallet = walletRepository.findByUser_UserName(userName);
		optionalwallet.orElseThrow(()->{
			throw new WalletException("Wallet Not Associated with the user: " + userName);
		});
		return optionalwallet.get();
	}
	
}
 

