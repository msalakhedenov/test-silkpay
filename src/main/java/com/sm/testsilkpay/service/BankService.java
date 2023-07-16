package com.sm.testsilkpay.service;

import com.sm.testsilkpay.model.web.banking.AccountInfoResponse;
import com.sm.testsilkpay.model.web.banking.CreateAccountRequest;
import com.sm.testsilkpay.model.web.banking.CreateAccountResponse;
import com.sm.testsilkpay.model.web.banking.TransferRequest;

public interface BankService {

  CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest);

  AccountInfoResponse getBalance(long accountId);

  AccountInfoResponse transfer(TransferRequest transferRequest);

}
