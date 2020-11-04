/**
 * 
 */
package com.novopay.ewallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.novopay.ewallet.dto.CommisionChargeDTO;
import com.novopay.ewallet.dto.CredentialDTO;
import com.novopay.ewallet.dto.MoneyTransferDTO;
import com.novopay.ewallet.dto.PassBookDTO;
import com.novopay.ewallet.dto.WalletDTO;
import com.novopay.ewallet.service.TransactionService;

/**
 * @author Raunak Agarwal
 *
 */
@RestController
@RequestMapping(value = "/api/txns",consumes = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	
	/**
	 * This API is used to add money  by a given user by passing valid 
	 * credentials
	 * @param dto
	 * @return
	 */
	@PostMapping("/addMoney")
	public ResponseEntity<WalletDTO> addMonneyToWallet(@RequestBody @Validated MoneyTransferDTO dto) {
		return  new ResponseEntity<WalletDTO>(transactionService.addMonneyToWallet(dto), HttpStatus.OK);
	}
	
	/**
	 * This API is used to transfer money by a given user to some other user by passing valid 
	 * credentials
	 * @param dto
	 * @return
	 */
	@PostMapping("/transferMoney")
	public ResponseEntity<WalletDTO> transferMoney(@RequestBody @Validated MoneyTransferDTO dto) {
		return new ResponseEntity<WalletDTO>(transactionService.transferMoney(dto), HttpStatus.OK);
	}
	
	/**
	 * This API is used to check the status of a given transaction by passing valid 
	 * transactionId
	 * @param id
	 * @return
	 */
	@GetMapping("/status/{id}")
	public ResponseEntity<String> checkTransactionStatus(@PathVariable("id") Long id) {
		return  new ResponseEntity<String>(transactionService.checkTransactionStatus(id), HttpStatus.OK);
	}
	
	/**
	 * This API is used to reverse a given transaction by passing valid 
	 * transactionId
	 * @param id
	 * @return
	 */
	@PostMapping("/reverse/{id}")
	public ResponseEntity<WalletDTO> revereTransaction(@PathVariable("id") Long id) {
		return  new ResponseEntity<WalletDTO>(transactionService.revereTransaction(id), HttpStatus.OK);
	} 
	
	/**
	 * This API is used to calculate the commision and charges of a given user by passing valid 
	 * credentials
	 * @param dto
	 * @return
	 */
	@PostMapping("/commisionChargeCalc")
	public ResponseEntity<CommisionChargeDTO> calculateCommissonCharge(@RequestBody @Validated CredentialDTO dto) {
		return  new ResponseEntity<CommisionChargeDTO>(transactionService.calculateCommissonCharge(dto), HttpStatus.OK);
	}
	
	/**
	 * This API is used to generate passbook of a given user by passing valid 
	 * credentials
	 * @param dto
	 * @return
	 */
	@PostMapping("/passbook")
	public ResponseEntity<PassBookDTO> viewPassbook(@RequestBody @Validated CredentialDTO dto) {
		return  new ResponseEntity<PassBookDTO>(transactionService.viewPassbook(dto), HttpStatus.OK);
	}
	
}
 

