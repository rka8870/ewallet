/**
 * 
 */
package com.novopay.ewallet.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novopay.ewallet.dto.CommisionChargeDTO;
import com.novopay.ewallet.dto.CredentialDTO;
import com.novopay.ewallet.dto.MoneyTransferDTO;
import com.novopay.ewallet.dto.PassBookDTO;
import com.novopay.ewallet.dto.TransactionDTO;
import com.novopay.ewallet.dto.WalletDTO;
import com.novopay.ewallet.entity.Movement;
import com.novopay.ewallet.entity.Transaction;
import com.novopay.ewallet.entity.Wallet;
import com.novopay.ewallet.enums.TransactionStatus;
import com.novopay.ewallet.enums.TransactionType;
import com.novopay.ewallet.exception.TransactionException;
import com.novopay.ewallet.exception.WalletException;
import com.novopay.ewallet.service.repository.MovementRepository;
import com.novopay.ewallet.service.repository.TransactionRepository;
import com.novopay.ewallet.service.repository.WalletRepository;

/**
 * @author Raunak Agarwal
 *
 */
@Service
@Transactional
public class TransactionService {

	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private MovementRepository movementRepository;
	
	@Autowired
	private WalletRepository walletRepository;
	
	public WalletDTO addMonneyToWallet(MoneyTransferDTO dto) {
		authenticationService.authenticateUser(dto.getCredential());
		try {
		Wallet wallet = walletService.getWalletDetail(dto.getCredential().getUserName());
		Transaction txn = new Transaction();
		txn.setAmountCredited(dto.getAmount());
		txn.setAmountDebited(new Double(0));
		txn.setDeleted(Boolean.FALSE);
		txn.setDescription(dto.getDescription());
		txn.setTransactionStatus(TransactionStatus.STARTED.name());
		txn.setTransactionType(TransactionType.SELF_TRANSFER.name());
		txn.setTransactionDate(getCurrentDate());
		txn.setToWallet(wallet);
		txn = addTransaction(txn);
		addMovement(txn);
		wallet.setAmount(wallet.getAmount()+dto.getAmount());
		wallet = walletRepository.save(wallet);
		return  prepareWalletDTO(wallet);
		}
		catch(WalletException e) {
			throw new TransactionException("Self Transaction Failed");
		}
	}
	
	public WalletDTO transferMoney(MoneyTransferDTO dto) {
		authenticationService.authenticateUser(dto.getCredential());
		try {
			Wallet fromWallet = walletService.getWalletDetail(dto.getCredential().getUserName());
			Wallet toWallet = walletService.getWalletDetail(dto.getToUserName());
			if(!isAmountTransferable(fromWallet, dto.getAmount())){
				throw new TransactionException("Insufficient balance for transaction to proceed");
			}
			Transaction txn = new Transaction();
			txn.setAmountCredited(dto.getAmount());
			txn.setAmountDebited(dto.getAmount());
			txn.setDeleted(Boolean.FALSE);
			txn.setDescription(dto.getDescription());
			txn.setTransactionStatus(TransactionStatus.STARTED.name());
			txn.setTransactionType(TransactionType.WALLET_TRANSFER.name());
			txn.setToWallet(toWallet);
			txn.setFromWallet(fromWallet);
			txn.setTransactionDate(getCurrentDate());
			txn = addTransaction(txn);
			addMovement(txn);
			fromWallet.setAmount(fromWallet.getAmount()-dto.getAmount());
			toWallet.setAmount(toWallet.getAmount()+dto.getAmount());
			List<Wallet> wallets = new ArrayList<>();
			wallets.add(fromWallet);
			wallets.add(toWallet);
			wallets = walletRepository.saveAll(wallets);
			return  prepareWalletDTO(fromWallet);
			}
			catch(WalletException e) {
				throw new TransactionException("Self Transaction Failed");
			}
	}
	
	
	public String checkTransactionStatus(Long transactionId) {
		Optional<Transaction> optionalTxn = transactionRepository.findById(transactionId);
		optionalTxn.orElseThrow(()->{
			throw new TransactionException("Transcation Id is Invalid");
		});
		return optionalTxn.get().getTransactionStatus();
	}
	
	public WalletDTO revereTransaction(Long transactionId) {
		Optional<Transaction> optionalTxn = transactionRepository.findById(transactionId);
		try {
		optionalTxn.orElseThrow(()->{
			throw new TransactionException("Transcation Id is Invalid");
		});
		Transaction txn = optionalTxn.get();
		if(txn.getTransactionType().equals(TransactionType.SELF_TRANSFER.name())) {
			throw new TransactionException("Reverse Transansaction is not implemented for Self Transactions");
		}
		if(txn.getTransactionStatus().equals(TransactionStatus.REVERTED.name())) {
			throw new TransactionException("Transaction is already reverted");
		}
		if(!isAmountTransferable(txn.getToWallet(), txn.getAmountCredited())){
			throw new TransactionException("Insufficient balance for transaction to proceed");
		}
		txn.setTransactionStatus(TransactionStatus.REVERTED.name());
		transactionRepository.save(txn);
		Transaction rev = prepareTransaction(txn);
		rev = addTransaction(rev);
		addMovement(rev);
		Wallet to = rev.getToWallet();
		Wallet from = rev.getFromWallet();
		to.setAmount(to.getAmount()+txn.getAmountCredited());
		from.setAmount(from.getAmount()-txn.getAmountDebited());
		walletRepository.save(to);
		return prepareWalletDTO(walletRepository.save(to));
		}
		catch(WalletException e) {
			throw new TransactionException("Reverse Transaction Failed");
		}
	} 
	
	private Transaction prepareTransaction(Transaction txn) {
		Transaction rev = new Transaction();
		rev.setAmountCredited(txn.getAmountCredited());
		rev.setAmountDebited(txn.getAmountDebited());
		rev.setDeleted(Boolean.FALSE);
		rev.setDescription("This is a reverted transaction aganist transaction Id : " + txn.getTransactionId());
		rev.setFromWallet(txn.getToWallet());
		rev.setToWallet(txn.getFromWallet());
		rev.setTransactionDate(getCurrentDate());
		rev.setTransactionStatus(TransactionStatus.STARTED.name());
		rev.setTransactionType(TransactionType.WALLET_TRANSFER.name());
		return rev;
	}

	public CommisionChargeDTO calculateCommissonCharge(CredentialDTO dto) {
		authenticationService.authenticateUser(dto);
		CommisionChargeDTO com = new CommisionChargeDTO();
		List<Movement> movements = movementRepository.findByWallet_User_UserNameOrderByEntryDateDesc(
				dto.getUserName());
		if(!movements.isEmpty()) {
			movements.stream().filter(x->x.getCreditedAmount()!=null)
			.forEach(y->{
				addCreditedAmount(com,y.getCreditedAmount());
			});
			movements.stream().filter(x->x.getDebitedAmount()!=null)
			.forEach(y->{
				addDebitedAmount(com,y.getDebitedAmount());
			});
		}
		com.setUsername(dto.getUserName());
		com.setCharge(com.getCharge()*0.002);
		com.setComision(com.getComision()*0.0005);
		return com;
	}
	
	public PassBookDTO viewPassbook(CredentialDTO dto) {
		authenticationService.authenticateUser(dto);
		List<Movement> movements = movementRepository.findByWallet_User_UserNameOrderByEntryDateDesc(
				dto.getUserName());
		PassBookDTO passbook = new PassBookDTO();
		Optional<Wallet> wallet = walletRepository.findByUser_UserName(dto.getUserName());
		wallet.orElseThrow(()->{
			throw new TransactionException("Wallet not associated with the given username : "+ dto.getUserName());
		});
		passbook.setBalance(wallet.get().getAmount());
		passbook.setUserName(dto.getUserName());
		List<TransactionDTO> txns = passbook.getTransactions();
		passbook.getTransactions();
		if(!movements.isEmpty()) {
			movements.forEach(x->getPassbookEntry(txns,x));
		}
		return passbook;
	}
	
	private void getPassbookEntry(List<TransactionDTO> txns, Movement mov) {
		TransactionDTO dto = new TransactionDTO();
		dto.setDescription(mov.getDescription());
		dto.setTransactionId(mov.getTransaction().getTransactionId());
		if(mov.getCreditedAmount()!=null) {
			dto.setAmount(mov.getCreditedAmount());
			dto.setTransactionType("CREDIT");
		}
		else if(mov.getDebitedAmount()!=null) {
			dto.setAmount(mov.getDebitedAmount());
			dto.setTransactionType("DEBIT");
		}
		txns.add(dto);
	}

	private void addCreditedAmount(CommisionChargeDTO dto, Double amount) {
		dto.setComision(dto.getComision()+amount);
	}

	private void addDebitedAmount(CommisionChargeDTO dto, Double amount) {
		dto.setCharge(dto.getCharge()+amount);
	}
	
	private WalletDTO prepareWalletDTO(Wallet wallet) {
		WalletDTO dto = new WalletDTO();
		dto.setBalance(wallet.getAmount());
		dto.setUserName(wallet.getUser().getUserName());
		return dto;
	}

	private List<Movement> addMovement(Transaction txn) {
		List<Movement> movements = new ArrayList<>();
		if(txn.getTransactionType().equals(TransactionType.SELF_TRANSFER.name())) {
			// Single Movement Record is Inserted
			Movement mov = new Movement();
			mov.setCreditedAmount(txn.getAmountCredited());
			mov.setDeleted(Boolean.FALSE);
			mov.setDescription("Amount added to the wallet");
			mov.setEntryDate(txn.getTransactionDate());
			mov.setTransaction(txn);
			mov.setWallet(txn.getToWallet());
			movements.add(movementRepository.save(mov));
		}
		else if(txn.getTransactionType().equals(TransactionType.WALLET_TRANSFER.name())) {
			// Two Movement Records are inserted one each for both wallets
			Movement mov1 = new Movement();
			Movement mov2 = new Movement();
			mov1.setDebitedAmount(txn.getAmountDebited());
			mov1.setDeleted(Boolean.FALSE);
			mov1.setDescription("Amount debited from the wallet to user: "+ txn.getToWallet().getUser().getUserName());
			mov1.setEntryDate(txn.getTransactionDate());
			mov1.setTransaction(txn);
			mov1.setWallet(txn.getFromWallet());
			mov2.setCreditedAmount(txn.getAmountCredited());
			mov2.setDeleted(Boolean.FALSE);
			mov2.setDescription("Amount credited to the wallet by user: " + txn.getFromWallet().getUser().getUserName());
			mov2.setEntryDate(txn.getTransactionDate());
			mov2.setTransaction(txn);
			mov2.setWallet(txn.getToWallet());
			movements.add(movementRepository.save(mov1));
			movements.add(movementRepository.save(mov2));
		}
		
		return movements;
		
	}

	private Transaction addTransaction(Transaction txn) {
		txn.setTransactionStatus(TransactionStatus.COMPLETED.name());
		return transactionRepository.save(txn);
	}
	
	private Boolean isAmountTransferable(Wallet wallet, Double amount) {
		return wallet.getAmount()>=amount;
	}
	
	private Date getCurrentDate() {
		Date date = new Date();
		return date;
	}
	
}
 

