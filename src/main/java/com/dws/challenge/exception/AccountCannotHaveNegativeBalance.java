package com.dws.challenge.exception;

public class AccountCannotHaveNegativeBalance extends RuntimeException {

  public AccountCannotHaveNegativeBalance(String message) {
    super(message);
  }
}
