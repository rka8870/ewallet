package com.novopay.ewallet;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.novopay.ewallet.dto.CommisionChargeDTO;
import com.novopay.ewallet.dto.CredentialDTO;
import com.novopay.ewallet.dto.MoneyTransferDTO;
import com.novopay.ewallet.dto.PassBookDTO;
import com.novopay.ewallet.dto.UserDTO;
import com.novopay.ewallet.dto.WalletDTO;
import com.novopay.ewallet.entity.User;
import com.novopay.ewallet.enums.TransactionStatus;
import com.novopay.ewallet.enums.TransactionType;
import com.novopay.ewallet.service.TransactionService;
import com.novopay.ewallet.service.UserService;

@SpringBootTest
class EwalletApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TransactionService transactionService;
	
	
	@Test
	public void userServiceTest() {
	
		//Create User
		UserDTO dto = createUserDTO();
		dto.setUserName("rka");
		User user = userService.createUser(dto);
		assertEquals(user.getUserName(), dto.getUserName());
		assertEquals(user.getPhoneNo(), dto.getPhoneNo());
		
		//Login user
		user = userService.getUser(dto);
		assertEquals(user.getUserName(), dto.getUserName());
		assertEquals(user.getPhoneNo(), dto.getPhoneNo());
		
		
	}
	
	@Test
	public void transactionServiceTest() {
		UserDTO dto1 = createUserDTO();
		UserDTO dto2 = createUserDTO();
		dto2.setUserName("rahul");
		User user1 = userService.createUser(dto1);
		User user2 = userService.createUser(dto2);
		
		//add money
		MoneyTransferDTO mt = createMoneyTransferDTO("raunak","abc");
		mt.setTransferType(TransactionType.SELF_TRANSFER.name());
		WalletDTO wdto = transactionService.addMonneyToWallet(mt);
		assertEquals(wdto.getUserName(), mt.getCredential().getUserName());
		assertEquals(wdto.getBalance(), mt.getAmount());
		
		//transfer money
		mt.setToUserName("rahul");
		mt.setAmount(new Double(500));
		mt.setTransferType(TransactionType.WALLET_TRANSFER.name());
		WalletDTO wdto2 = transactionService.transferMoney(mt);
		assertEquals(wdto2.getUserName(), mt.getCredential().getUserName());
		assertEquals(wdto2.getBalance(), new Double(500));
		
		//passbook
		CredentialDTO cdto = createCredentialDTO("raunak", "abc"); 
		PassBookDTO pdto = transactionService.viewPassbook(cdto);
		assertEquals(pdto.getUserName(), cdto.getUserName());
		assertEquals(pdto.getBalance(), new Double(500));
		assertEquals(pdto.getTransactions().get(0).getAmount(), new Double(500));
		assertEquals(pdto.getTransactions().get(0).getTransactionType(), "DEBIT");
		
		//calculate Commision and Charge
		CommisionChargeDTO ccdto = transactionService.calculateCommissonCharge(cdto);
		assertEquals(pdto.getUserName(), cdto.getUserName());
		assertEquals(pdto.getBalance(), new Double(500));
		assertEquals(ccdto.getCharge(), new Double(500*0.002));
		assertEquals(ccdto.getComision(), new Double(1000*0.0005));
		
		
		//checktransactionStatus
		Long transactionId = pdto.getTransactions().get(0).getTransactionId();
		String status = transactionService.checkTransactionStatus(transactionId);
		assertEquals(status, TransactionStatus.COMPLETED.name());
		
		//reverse transaction
		
		WalletDTO wdto3 = transactionService.revereTransaction(transactionId);
		assertEquals(wdto3.getUserName(), cdto.getUserName());
		assertEquals(wdto3.getBalance(), new Double(1000));
		
	}
	
	private UserDTO createUserDTO() {
		UserDTO dto = new UserDTO();
		dto.setUserName("raunak");
		dto.setConfirmPassword("abc");
		dto.setPassword("abc");
		dto.setPhoneNo("8101755845");
		return dto;
	}
	
	private  MoneyTransferDTO createMoneyTransferDTO(String userName, String password) {
		MoneyTransferDTO mt = new MoneyTransferDTO(); 
		mt.setCredential(createCredentialDTO(userName,password));
		mt.setAmount(new Double(1000));
		return mt;
	}

	private CredentialDTO createCredentialDTO(String userName, String password) {
		CredentialDTO dto = new CredentialDTO();
		dto.setPassword(password);;
		dto.setUserName(userName);
		return dto;
	}
}
