package com.sm.testsilkpay.service;

import com.sm.testsilkpay.model.web.CreateAccountRequest;
import com.sm.testsilkpay.model.web.CreateAccountResponse;
import com.sm.testsilkpay.model.web.AccountInfoResponse;
import com.sm.testsilkpay.model.web.TransferRequest;

public interface BankService {

  CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest);

  AccountInfoResponse getBalance(long accountId);

  AccountInfoResponse transfer(TransferRequest transferRequest);

}
