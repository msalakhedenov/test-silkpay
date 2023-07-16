package com.sm.testsilkpay.service.impl;

import com.sm.testsilkpay.exception.InsufficientFundsException;
import com.sm.testsilkpay.model.entity.BankAccount;
import com.sm.testsilkpay.model.entity.User;
import com.sm.testsilkpay.model.web.banking.AccountInfoResponse;
import com.sm.testsilkpay.model.web.banking.CreateAccountRequest;
import com.sm.testsilkpay.model.web.banking.TransferRequest;
import com.sm.testsilkpay.repository.BankAccountRepository;
import com.sm.testsilkpay.repository.TransactionRepository;
import com.sm.testsilkpay.service.AuthService;
import com.sm.testsilkpay.service.UserService;
import com.sm.testsilkpay.util.security.JwtAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankServiceImplTest {

  @Mock
  private UserService           userService;
  @Mock
  private AuthService           authService;
  @Mock
  private BankAccountRepository bankAccountRepository;
  @Mock
  private TransactionRepository transactionRepository;

  @InjectMocks
  private BankServiceImpl bankService;

  private User currentUser;

  @BeforeEach
  public void setup() {
    currentUser = new User(1L, "test", "test", List.of());

    when(authService.getAuthInfo()).thenReturn(new JwtAuthentication(currentUser.getUsername(), true));
    when(userService.findByUsername(eq(currentUser.getUsername()))).thenReturn(currentUser);
  }

  /* arrange //// act //// assert */
  @Test
  void findAccounts() {
    BankAccount bankAccount = new BankAccount(1L, BigDecimal.ONE, currentUser);

    List<BankAccount> accounts = List.of(bankAccount);

    when(bankAccountRepository.findByOwner(same(currentUser))).thenReturn(accounts);

    //
    //

    List<AccountInfoResponse> foundAccounts = bankService.findAccounts();

    //
    //

    assertThat(foundAccounts).hasSize(1);

    AccountInfoResponse foundAccountInfo = foundAccounts.get(0);

    assertThat(foundAccountInfo.getAccountId()).isEqualTo(1L);
    assertThat(foundAccountInfo.getBalance()).isEqualTo(BigDecimal.ONE);
  }

  /* arrange //// act //// assert */
  @Test
  void createAccount() {
    CreateAccountRequest createAccountRequest = new CreateAccountRequest();
    createAccountRequest.setInitialBalance(BigDecimal.ONE);

    when(bankAccountRepository.save(any())).thenReturn(new BankAccount(1L, createAccountRequest.getInitialBalance(), currentUser));

    //
    //

    AccountInfoResponse account = bankService.createAccount(createAccountRequest);

    //
    //

    assertThat(account).isNotNull();
    assertThat(account.getAccountId()).isEqualTo(1L);
    assertThat(account.getBalance()).isEqualTo(BigDecimal.ONE);
  }

  /* arrange //// act //// assert */
  @Test
  void getBalance() {
    BankAccount bankAccount = new BankAccount(1L, BigDecimal.ONE, currentUser);

    when(bankAccountRepository.findByIdAndOwner(eq(1L), same(currentUser))).thenReturn(Optional.of(bankAccount));

    //
    //

    AccountInfoResponse accountInfoResponse = bankService.getBalance(1L);

    //
    //

    assertThat(accountInfoResponse).isNotNull();
    assertThat(accountInfoResponse.getAccountId()).isEqualTo(1L);
    assertThat(accountInfoResponse.getBalance()).isEqualTo(BigDecimal.ONE);
  }

  /* arrange //// act //// assert */
  @Test
  void setBalance() {
    BankAccount bankAccount = new BankAccount(1L, BigDecimal.ONE, currentUser);

    when(bankAccountRepository.findByIdAndOwner(eq(1L), same(currentUser))).thenReturn(Optional.of(bankAccount));
    when(bankAccountRepository.save(same(bankAccount))).thenReturn(bankAccount);

    //
    //

    AccountInfoResponse accountInfoResponse = bankService.setBalance(1L, BigDecimal.TEN);

    //
    //

    assertThat(accountInfoResponse).isNotNull();
    assertThat(accountInfoResponse.getAccountId()).isEqualTo(1L);
    assertThat(accountInfoResponse.getBalance()).isEqualTo(BigDecimal.TEN);
  }

  /* arrange //// act //// assert */
  @Test
  void transfer() {
    BankAccount sourceBankAccount      = new BankAccount(1L, BigDecimal.TEN, currentUser);
    BankAccount destinationBankAccount = new BankAccount(2L, BigDecimal.ZERO, currentUser);

    when(bankAccountRepository.findByIdAndOwner(eq(1L), same(currentUser))).thenReturn(Optional.of(sourceBankAccount));
    when(bankAccountRepository.findById(eq(2L))).thenReturn(Optional.of(destinationBankAccount));
    when(bankAccountRepository.save(same(sourceBankAccount))).thenReturn(new BankAccount(1L, BigDecimal.valueOf(9), currentUser));
    when(bankAccountRepository.save(same(destinationBankAccount))).thenReturn(new BankAccount(1L, BigDecimal.ONE, currentUser));

    TransferRequest transferRequest = new TransferRequest();
    transferRequest.setFrom(sourceBankAccount.getId());
    transferRequest.setTo(destinationBankAccount.getId());
    transferRequest.setAmount(BigDecimal.ONE);

    //
    //

    AccountInfoResponse accountInfoResponse = bankService.transfer(transferRequest);

    //
    //

    assertThat(accountInfoResponse).isNotNull();
    assertThat(accountInfoResponse.getAccountId()).isEqualTo(1L);
    assertThat(accountInfoResponse.getBalance()).isEqualTo(BigDecimal.valueOf(9));
  }

  /* arrange //// act //// assert */
  @Test
  void transfer__notEnoughBalance() {
    BankAccount sourceBankAccount      = new BankAccount(1L, BigDecimal.ZERO, currentUser);
    BankAccount destinationBankAccount = new BankAccount(2L, BigDecimal.ZERO, currentUser);

    when(bankAccountRepository.findByIdAndOwner(eq(1L), same(currentUser))).thenReturn(Optional.of(sourceBankAccount));

    TransferRequest transferRequest = new TransferRequest();
    transferRequest.setFrom(sourceBankAccount.getId());
    transferRequest.setTo(destinationBankAccount.getId());
    transferRequest.setAmount(BigDecimal.TEN);

    //
    //

    Exception exception = catchException(() -> bankService.transfer(transferRequest));

    //
    //

    assertThat(exception).isExactlyInstanceOf(InsufficientFundsException.class);
  }

}