package com.sm.testsilkpay.service;

import com.sm.testsilkpay.model.entity.User;
import com.sm.testsilkpay.model.web.AccountInfoResponse;
import com.sm.testsilkpay.model.web.CreateAccountRequest;
import com.sm.testsilkpay.model.web.CreateAccountResponse;
import com.sm.testsilkpay.model.web.TransferRequest;

public interface BankService {

  CreateAccountResponse createAccount(User user, CreateAccountRequest createAccountRequest);

  AccountInfoResponse getBalance(User user, long accountId);

  AccountInfoResponse transfer(User user, TransferRequest transferRequest);

}
