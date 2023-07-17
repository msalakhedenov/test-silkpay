package com.sm.testsilkpay.service.impl;

import com.sm.testsilkpay.exception.BankAccountNotFoundException;
import com.sm.testsilkpay.exception.InsufficientFundsException;
import com.sm.testsilkpay.model.entity.BankAccount;
import com.sm.testsilkpay.model.entity.Transaction;
import com.sm.testsilkpay.model.entity.User;
import com.sm.testsilkpay.model.web.banking.AccountInfoResponse;
import com.sm.testsilkpay.model.web.banking.CreateAccountRequest;
import com.sm.testsilkpay.model.web.banking.TransferRequest;
import com.sm.testsilkpay.repository.BankAccountRepository;
import com.sm.testsilkpay.repository.TransactionRepository;
import com.sm.testsilkpay.service.AuthService;
import com.sm.testsilkpay.service.BankService;
import com.sm.testsilkpay.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BankServiceImpl implements BankService {

  private final AuthService authService;
  private final UserService userService;

  private final BankAccountRepository bankAccountRepository;
  private final TransactionRepository transactionRepository;

  @Override
  public List<AccountInfoResponse> findAccounts() {
    User owner = getCurrentUser();

    log.debug("findAccounts(), currentUser: {}", owner.getUsername());

    return bankAccountRepository.findByOwner(owner).stream()
                                .map(bankAccount -> AccountInfoResponse.of(bankAccount.getId(), bankAccount.getBalance()))
                                .collect(Collectors.toList());
  }

  @Override
  public AccountInfoResponse createAccount(CreateAccountRequest createAccountRequest) {
    log.debug("createAccount(), request: {}", createAccountRequest);

    BankAccount account = new BankAccount();

    BigDecimal initialBalance = Optional.ofNullable(createAccountRequest)
                                        .map(CreateAccountRequest::getInitialBalance)
                                        .orElse(BigDecimal.ZERO);

    account.setBalance(initialBalance);

    User owner = getCurrentUser();

    log.debug("createAccount(), currentUser: {}", owner.getUsername());

    account.setOwner(owner);

    BankAccount savedAccount = bankAccountRepository.save(account);

    return AccountInfoResponse.of(savedAccount.getId(), savedAccount.getBalance());
  }

  @Override
  public AccountInfoResponse getBalance(long accountId) {
    User owner = getCurrentUser();

    log.debug("getBalance(), accountId: {}, currentUser: {}", accountId, owner.getUsername());

    BankAccount account = bankAccountRepository.findByIdAndOwner(accountId, owner)
                                               .orElseThrow(() -> new BankAccountNotFoundException(accountId));

    return AccountInfoResponse.of(accountId, account.getBalance());
  }

  @Override
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public AccountInfoResponse setBalance(long accountId, BigDecimal balance) {
    User owner = getCurrentUser();

    log.debug("setBalance(), accountId: {}, balance: {}, currentUser: {}", accountId, balance, owner.getUsername());

    BankAccount account = bankAccountRepository.findByIdAndOwner(accountId, owner)
                                               .orElseThrow(() -> new BankAccountNotFoundException(accountId));

    account.setBalance(balance);

    BankAccount savedAccount = bankAccountRepository.save(account);

    return AccountInfoResponse.of(accountId, savedAccount.getBalance());
  }

  @Override
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public AccountInfoResponse transfer(TransferRequest transferRequest) {
    User owner = getCurrentUser();

    log.debug("transfer(), request: {}, currentUser: {}", transferRequest, owner.getUsername());

    BankAccount sourceAccount = bankAccountRepository.findByIdAndOwner(transferRequest.getFrom(), owner)
                                                     .orElseThrow(() -> new BankAccountNotFoundException(transferRequest.getFrom()));

    if (transferRequest.getFrom().equals(transferRequest.getTo())) {
      return AccountInfoResponse.of(transferRequest.getFrom(), sourceAccount.getBalance());
    }

    if (sourceAccount.getBalance().compareTo(transferRequest.getAmount()) < 0) {
      throw new InsufficientFundsException(sourceAccount.getId(), transferRequest.getAmount());
    }

    BankAccount destinationAccount = bankAccountRepository.findById(transferRequest.getTo())
                                                          .orElseThrow(() -> new BankAccountNotFoundException(transferRequest.getTo()));

    sourceAccount.setBalance(sourceAccount.getBalance().subtract(transferRequest.getAmount()));
    destinationAccount.setBalance(destinationAccount.getBalance().add(transferRequest.getAmount()));

    Transaction transaction = new Transaction();
    transaction.setFrom(sourceAccount);
    transaction.setTo(destinationAccount);
    transaction.setAmount(transferRequest.getAmount());

    bankAccountRepository.save(destinationAccount);

    BankAccount savedSourceAccount = bankAccountRepository.save(sourceAccount);

    transactionRepository.save(transaction);

    return AccountInfoResponse.of(savedSourceAccount.getId(), savedSourceAccount.getBalance());
  }

  private User getCurrentUser() {
    String username = authService.getAuthInfo().getPrincipal().toString();

    return userService.findByUsername(username);
  }

}
