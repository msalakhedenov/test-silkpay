package com.sm.testsilkpay.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {

  public InsufficientFundsException(long accountId, BigDecimal transferAmount) {
    super("Insufficient funds on the account with ID = %d to transfer %s.".formatted(accountId, transferAmount));
  }

}
