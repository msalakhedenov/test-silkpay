package com.sm.testsilkpay.service;

import com.sm.testsilkpay.model.entity.User;
import com.sm.testsilkpay.model.web.banking.AccountInfoResponse;
import com.sm.testsilkpay.model.web.banking.CreateAccountRequest;
import com.sm.testsilkpay.model.web.banking.CreateAccountResponse;
import com.sm.testsilkpay.model.web.banking.TransferRequest;

public interface BankService {

  CreateAccountResponse createAccount(User user, CreateAccountRequest createAccountRequest);

  AccountInfoResponse getBalance(User user, long accountId);

  AccountInfoResponse transfer(User user, TransferRequest transferRequest);

}
