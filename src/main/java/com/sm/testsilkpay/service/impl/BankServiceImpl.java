package com.sm.testsilkpay.service.impl;

import com.sm.testsilkpay.exception.BankAccountNotFoundException;
import com.sm.testsilkpay.exception.InsufficientFundsException;
import com.sm.testsilkpay.model.entity.BankAccount;
import com.sm.testsilkpay.model.entity.User;
import com.sm.testsilkpay.model.web.banking.AccountInfoResponse;
import com.sm.testsilkpay.model.web.banking.CreateAccountRequest;
import com.sm.testsilkpay.model.web.banking.TransferRequest;
import com.sm.testsilkpay.repository.BankAccountRepository;
import com.sm.testsilkpay.service.AuthService;
import com.sm.testsilkpay.service.BankService;
import com.sm.testsilkpay.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BankServiceImpl implements BankService {

  private final AuthService authService;
  private final UserService userService;

  private final BankAccountRepository bankAccountRepository;

  @Override
  public AccountInfoResponse createAccount(CreateAccountRequest createAccountRequest) {
    BankAccount account = new BankAccount();

    BigDecimal initialBalance = Optional.ofNullable(createAccountRequest)
                                        .map(CreateAccountRequest::getInitialBalance)
                                        .orElse(BigDecimal.ZERO);

    account.setBalance(initialBalance);

    User owner = getCurrentUser();

    account.setOwner(owner);

    BankAccount savedAccount = bankAccountRepository.save(account);

    return AccountInfoResponse.of(savedAccount.getId(), savedAccount.getBalance());
  }

  @Override
  public AccountInfoResponse getBalance(long accountId) {
    User owner = getCurrentUser();

    BankAccount account = bankAccountRepository.findByIdAndOwner(accountId, owner)
                                               .orElseThrow(() -> new BankAccountNotFoundException(accountId));

    return AccountInfoResponse.of(accountId, account.getBalance());
  }

  @Override
  @Transactional
  public AccountInfoResponse transfer(TransferRequest transferRequest) {
    User owner = getCurrentUser();

    BankAccount sourceAccount = bankAccountRepository.findByIdAndOwner(transferRequest.getFrom(), owner)
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

  private User getCurrentUser() {
    String username = authService.getAuthInfo().getPrincipal().toString();

    return (User) userService.loadUserByUsername(username);
  }

}
