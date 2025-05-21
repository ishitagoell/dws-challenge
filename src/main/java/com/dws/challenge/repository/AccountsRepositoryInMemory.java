package com.dws.challenge.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Repository;

import com.dws.challenge.domain.Account;
import com.dws.challenge.domain.AccountTransferResult;
import com.dws.challenge.domain.TransferFunds;
import com.dws.challenge.exception.AccountCannotHaveNegativeBalance;
import com.dws.challenge.exception.AccountDoesNotExistException;
import com.dws.challenge.exception.DuplicateAccountIdException;

import jakarta.validation.Valid;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public void createAccount(Account account) throws DuplicateAccountIdException {
        Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
        if (previousAccount != null) {
            throw new DuplicateAccountIdException(
                    "Account id " + account.getAccountId() + " already exists!");
        }
    }

    @Override
    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    @Override
    public void clearAccounts() {
        accounts.clear();
    }

	@Override
	public AccountTransferResult transferFunds(@Valid TransferFunds transferFunds) {
		String fromAccId = transferFunds.getFromAccountId();
		String toAccId = transferFunds.getToAccountId();
		
		Account fromAccount = accounts.get(fromAccId);
		Account toAccount = accounts.get(toAccId);
		
		if(fromAccount == null || toAccount == null) {
			throw new AccountDoesNotExistException("Account id " + fromAccId + " or" + toAccId + " does not exist!");
		}
		Object lock1 = fromAccId.compareTo(toAccId) < 0? fromAccount : toAccount; // 101 < 102 ? true=(l1:A, l2:B); false=(l1:B,l2:A)
		Object lock2 = fromAccId.compareTo(toAccId) < 0? toAccount : fromAccount;
		synchronized (lock1) {
			synchronized (lock2) {
				BigDecimal transferAmt = transferFunds.getTransferAmount();
				if(transferAmt.compareTo(BigDecimal.ZERO) < 0) {
					 throw new IllegalArgumentException("Transfer amount must be positive");
				}
				
				BigDecimal balance = fromAccount.getBalance().subtract(transferAmt);
				if(balance.compareTo(BigDecimal.ZERO) < 0) {
					throw new AccountCannotHaveNegativeBalance("Insufficient balance= " + fromAccount);
				}
				
				BigDecimal fromBalance = fromAccount.getBalance();
				BigDecimal toBalance = toAccount.getBalance();
				
				fromAccount.setBalance(fromBalance.subtract(transferAmt));
				toAccount.setBalance(toBalance.add(transferAmt));
			}
		}
		
		return new AccountTransferResult(fromAccount, toAccount);
	}

}
