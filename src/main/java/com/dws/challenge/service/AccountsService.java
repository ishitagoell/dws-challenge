package com.dws.challenge.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.domain.AccountTransferResult;
import com.dws.challenge.domain.TransferFunds;
import com.dws.challenge.repository.AccountsRepository;

import jakarta.validation.Valid;
import lombok.Getter;

@Service
public class AccountsService {

  @Getter
  private final AccountsRepository accountsRepository;
  
  @Getter
  private NotificationService notificationService;

  @Autowired
  public AccountsService(AccountsRepository accountsRepository, NotificationService notificationService) {
    this.accountsRepository = accountsRepository;
    this.notificationService = notificationService;
  }

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }

  public void transferFunds(@Valid TransferFunds transferFunds) {
	AccountTransferResult accountsTransferRes = this.accountsRepository.transferFunds(transferFunds);
	if(accountsTransferRes != null) {
		BigDecimal transferAmt = transferFunds.getTransferAmount();
		this.notificationService.notifyAboutTransfer(accountsTransferRes.getFromAccount(), "Account debited from your account= " + transferAmt);
		this.notificationService.notifyAboutTransfer(accountsTransferRes.getToAccount(), "Account credited to your account= " + transferAmt);
	}
		
  }
}
