package com.sm.testsilkpay.exception;

public class BankAccountNotFoundException extends RuntimeException {

  public BankAccountNotFoundException(long accountId) {
    super("Bank account with ID = %d is not found".formatted(accountId));
  }

}
