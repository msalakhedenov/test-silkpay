package com.sm.testsilkpay.service.impl;

import com.sm.testsilkpay.exception.BankAccountNotFoundException;
import com.sm.testsilkpay.exception.InsufficientFundsException;
import com.sm.testsilkpay.model.entity.BankAccount;
import com.sm.testsilkpay.model.entity.User;
import com.sm.testsilkpay.model.web.AccountInfoResponse;
import com.sm.testsilkpay.model.web.CreateAccountRequest;
import com.sm.testsilkpay.model.web.CreateAccountResponse;
import com.sm.testsilkpay.model.web.TransferRequest;
import com.sm.testsilkpay.repository.BankAccountRepository;
import com.sm.testsilkpay.service.BankService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class BankServiceImpl implements BankService {

  private final BankAccountRepository bankAccountRepository;

  @Override
  public CreateAccountResponse createAccount(User user, CreateAccountRequest createAccountRequest) {
    BankAccount account = new BankAccount();

    BigDecimal initialBalance = createAccountRequest.getInitialBalance();

    if (initialBalance != null) {
      account.setBalance(initialBalance);
    }

    account.setOwner(user);

    BankAccount savedAccount = bankAccountRepository.save(account);

    return CreateAccountResponse.of(savedAccount.getId());
  }

  @Override
  public AccountInfoResponse getBalance(User user, long accountId) {
    BankAccount account = bankAccountRepository.findByIdAndOwner(accountId, user)
                                               .orElseThrow(() -> new BankAccountNotFoundException(accountId));

    return AccountInfoResponse.of(accountId, account.getBalance());
  }

  @Override
  @Transactional
  public AccountInfoResponse transfer(User user, TransferRequest transferRequest) {
    BankAccount sourceAccount = bankAccountRepository.findByIdAndOwner(transferRequest.getFrom(), user)
                                                     .orElseThrow(() -> new BankAccountNotFoundException(transferRequest.getFrom()));

    if (sourceAccount.getBalance().compareTo(transferRequest.getAmount()) < 0) {
      throw new InsufficientFundsException(sourceAccount.getId(), transferRequest.getAmount());
    }

    BankAccount destinationAccount = bankAccountRepository.findById(transferRequest.getTo())
                                                          .orElseThrow(() -> new BankAccountNotFoundException(transferRequest.getTo()));

    sourceAccount.setBalance(sourceAccount.getBalance().subtract(transferRequest.getAmount()));
    destinationAccount.setBalance(destinationAccount.getBalance().add(transferRequest.getAmount()));

    bankAccountRepository.save(destinationAccount);

    BankAccount savedSourceAccount = bankAccountRepository.save(sourceAccount);

    return AccountInfoResponse.of(savedSourceAccount.getId(), savedSourceAccount.getBalance());
  }

}
