package com.sm.testsilkpay.controller;

import com.sm.testsilkpay.model.web.banking.AccountInfoResponse;
import com.sm.testsilkpay.model.web.banking.CreateAccountRequest;
import com.sm.testsilkpay.model.web.banking.CreateAccountResponse;
import com.sm.testsilkpay.model.web.banking.TransferRequest;
import com.sm.testsilkpay.service.BankService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/account")
public class BankAccountController {

  private final BankService bankService;

  @PostMapping
  public CreateAccountResponse createAccount(@RequestBody(required = false) CreateAccountRequest createAccountRequest) {
    return bankService.createAccount(createAccountRequest);
  }

  @GetMapping("/{accountId}")
  public AccountInfoResponse getBalance(@PathVariable long accountId) {
    return bankService.getBalance(accountId);
  }

  @PostMapping("/transfer")
  public AccountInfoResponse transfer(@RequestBody TransferRequest transferRequest) {
    return bankService.transfer(transferRequest);
  }

}
