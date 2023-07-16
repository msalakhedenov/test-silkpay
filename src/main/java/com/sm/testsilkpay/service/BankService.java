package com.sm.testsilkpay.service;

import com.sm.testsilkpay.model.web.banking.AccountInfoResponse;
import com.sm.testsilkpay.model.web.banking.CreateAccountRequest;
import com.sm.testsilkpay.model.web.banking.TransferRequest;

import java.math.BigDecimal;
import java.util.List;

public interface BankService {

  List<AccountInfoResponse> findAccounts();

  AccountInfoResponse createAccount(CreateAccountRequest createAccountRequest);

  AccountInfoResponse getBalance(long accountId);

  AccountInfoResponse setBalance(long accountId, BigDecimal balance);

  AccountInfoResponse transfer(TransferRequest transferRequest);

}
