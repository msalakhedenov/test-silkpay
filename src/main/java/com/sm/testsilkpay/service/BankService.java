package com.sm.testsilkpay.service;

import com.sm.testsilkpay.model.web.banking.AccountInfoResponse;
import com.sm.testsilkpay.model.web.banking.CreateAccountRequest;
import com.sm.testsilkpay.model.web.banking.TransferRequest;

public interface BankService {

  AccountInfoResponse createAccount(CreateAccountRequest createAccountRequest);

  AccountInfoResponse getBalance(long accountId);

  AccountInfoResponse transfer(TransferRequest transferRequest);

}
