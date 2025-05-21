package com.dws.challenge.repository;

import com.dws.challenge.domain.Account;
import com.dws.challenge.domain.AccountTransferResult;
import com.dws.challenge.domain.TransferFunds;
import com.dws.challenge.exception.DuplicateAccountIdException;

import jakarta.validation.Valid;

public interface AccountsRepository {

  void createAccount(Account account) throws DuplicateAccountIdException;

  Account getAccount(String accountId);

  void clearAccounts();

  AccountTransferResult transferFunds(@Valid TransferFunds transferFunds);
}
