package com.dws.challenge.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dws.challenge.domain.Account;
import com.dws.challenge.domain.TransferFunds;
import com.dws.challenge.exception.AccountCannotHaveNegativeBalance;
import com.dws.challenge.exception.AccountDoesNotExistException;
import com.dws.challenge.exception.DuplicateAccountIdException;
import com.dws.challenge.service.AccountsService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountsController {

  private final AccountsService accountsService;

  @Autowired
  public AccountsController(AccountsService accountsService) {
    this.accountsService = accountsService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
    log.info("Creating account {}", account);

    try {
    this.accountsService.createAccount(account);
    } catch (DuplicateAccountIdException daie) {
      return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping(path = "/{accountId}")
  public Account getAccount(@PathVariable String accountId) {
    log.info("Retrieving account for id {}", accountId);
    return this.accountsService.getAccount(accountId);
  }
  
  @PostMapping("/fundsTransfer")
  public ResponseEntity<Object> trasnferFunds(@RequestBody @Valid TransferFunds transferFunds) {
	  log.info("Initiating Funds Transfer from account={} to account={}, Amount={}", 
			  transferFunds.getFromAccountId(), transferFunds.getToAccountId(), transferFunds.getTransferAmount());
	  try {
		  this.accountsService.transferFunds(transferFunds);  
	  } catch(AccountDoesNotExistException | AccountCannotHaveNegativeBalance ex) {
		  return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	  }
	  
	  return new  ResponseEntity<>("Funds Transferred Successfully!", HttpStatus.CREATED);
	  
  }

}
