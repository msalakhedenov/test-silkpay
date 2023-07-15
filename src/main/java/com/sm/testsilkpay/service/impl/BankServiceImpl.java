package com.sm.testsilkpay.service.impl;

import com.sm.testsilkpay.model.web.AccountInfoResponse;
import com.sm.testsilkpay.model.web.CreateAccountRequest;
import com.sm.testsilkpay.model.web.CreateAccountResponse;
import com.sm.testsilkpay.model.web.TransferRequest;
import com.sm.testsilkpay.service.BankService;
import org.springframework.stereotype.Service;

@Service
public class BankServiceImpl implements BankService {

  @Override
  public CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest) {
    return null;
  }

  @Override
  public AccountInfoResponse getBalance(long accountId) {
    return null;
  }

  @Override
  public AccountInfoResponse transfer(TransferRequest transferRequest) {
    return null;
  }

}
