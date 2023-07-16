package com.sm.testsilkpay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sm.testsilkpay.model.entity.BankAccount;
import com.sm.testsilkpay.model.entity.Transaction;
import com.sm.testsilkpay.model.entity.User;
import com.sm.testsilkpay.model.web.banking.AccountInfoResponse;
import com.sm.testsilkpay.model.web.banking.CreateAccountRequest;
import com.sm.testsilkpay.model.web.banking.SetBalanceRequest;
import com.sm.testsilkpay.model.web.banking.TransferRequest;
import com.sm.testsilkpay.repository.BankAccountRepository;
import com.sm.testsilkpay.repository.TransactionRepository;
import com.sm.testsilkpay.repository.UserRepository;
import com.sm.testsilkpay.service.AuthService;
import com.sm.testsilkpay.util.security.JwtAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TestSilkPayApplicationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private AuthService authService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BankAccountRepository bankAccountRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  private User currentUser;

  @BeforeEach
  public void setup() {
    currentUser = new User(1L, "test", "test", List.of());

    userRepository.save(currentUser);

    when(authService.getAuthInfo()).thenReturn(new JwtAuthentication("test", true));
  }

  @Test
  @WithMockUser(username = "test")
  void createAccount() throws Exception {

    //
    //

    MvcResult mvcResult = mockMvc.perform(post("/api/accounts"))
                                 .andExpect(status().isCreated())
                                 .andReturn();

    //
    //

    String              response            = mvcResult.getResponse().getContentAsString();
    AccountInfoResponse accountInfoResponse = objectMapper.readValue(response, AccountInfoResponse.class);

    assertThat(accountInfoResponse).isNotNull();
    assertThat(accountInfoResponse.getAccountId()).isEqualTo(1L);
    assertThat(accountInfoResponse.getBalance()).isEqualTo(BigDecimal.ZERO);

    BankAccount account = bankAccountRepository.findByIdAndOwner(1L, currentUser).orElse(null);

    assertThat(account).isNotNull();
    assertThat(account.getId()).isEqualTo(1L);
    assertThat(account.getBalance()).isCloseTo(BigDecimal.ZERO, offset(BigDecimal.valueOf(0.00001)));
  }

  @Test
  @WithMockUser(username = "test")
  void createAccount__withInitialBalance() throws Exception {

    CreateAccountRequest createAccountRequest = new CreateAccountRequest();
    createAccountRequest.setInitialBalance(BigDecimal.TEN);

    String requestBody = objectMapper.writeValueAsString(createAccountRequest);

    //
    //

    MvcResult mvcResult = mockMvc.perform(
                                     post("/api/accounts")
                                         .content(requestBody)
                                         .contentType(MediaType.APPLICATION_JSON_VALUE)
                                 )
                                 .andExpect(status().isCreated())
                                 .andReturn();

    //
    //

    String              response            = mvcResult.getResponse().getContentAsString();
    AccountInfoResponse accountInfoResponse = objectMapper.readValue(response, AccountInfoResponse.class);

    assertThat(accountInfoResponse).isNotNull();
    assertThat(accountInfoResponse.getAccountId()).isEqualTo(1L);
    assertThat(accountInfoResponse.getBalance()).isEqualTo(BigDecimal.TEN);

    BankAccount account = bankAccountRepository.findByIdAndOwner(1L, currentUser).orElse(null);

    assertThat(account).isNotNull();
    assertThat(account.getId()).isEqualTo(1L);
    assertThat(account.getBalance()).isCloseTo(BigDecimal.TEN, offset(BigDecimal.valueOf(0.00001)));
  }

  @Test
  @WithMockUser(username = "test")
  public void getBalance() throws Exception {
    BankAccount bankAccount = new BankAccount(1L, BigDecimal.TEN, currentUser);

    bankAccountRepository.save(bankAccount);

    //
    //

    MvcResult mvcResult = mockMvc.perform(get("/api/accounts/1"))
                                 .andExpect(status().isOk())
                                 .andReturn();

    //
    //

    String              response            = mvcResult.getResponse().getContentAsString();
    AccountInfoResponse accountInfoResponse = objectMapper.readValue(response, AccountInfoResponse.class);

    assertThat(accountInfoResponse).isNotNull();
    assertThat(accountInfoResponse.getAccountId()).isEqualTo(1L);
    assertThat(accountInfoResponse.getBalance()).isCloseTo(BigDecimal.TEN, offset(BigDecimal.valueOf(0.0001)));
  }

  @Test
  @WithMockUser(username = "test")
  public void getBalance__accountDoesNotExist() throws Exception {

    //
    //

    mockMvc.perform(get("/api/accounts/1"))
           .andExpect(status().isNotFound());

    //
    //

  }

  @Test
  @WithMockUser(username = "test")
  public void setBalance() throws Exception {
    BankAccount bankAccount = new BankAccount(1L, BigDecimal.TEN, currentUser);

    bankAccountRepository.save(bankAccount);

    SetBalanceRequest setBalanceRequest = new SetBalanceRequest();
    setBalanceRequest.setBalance(BigDecimal.ZERO);

    String requestBody = objectMapper.writeValueAsString(setBalanceRequest);

    //
    //

    MvcResult mvcResult = mockMvc.perform(
                                     patch("/api/accounts/1")
                                         .content(requestBody)
                                         .contentType(MediaType.APPLICATION_JSON_VALUE)
                                 )
                                 .andExpect(status().isOk())
                                 .andReturn();

    //
    //

    String              response            = mvcResult.getResponse().getContentAsString();
    AccountInfoResponse accountInfoResponse = objectMapper.readValue(response, AccountInfoResponse.class);

    assertThat(accountInfoResponse).isNotNull();
    assertThat(accountInfoResponse.getAccountId()).isEqualTo(1L);
    assertThat(accountInfoResponse.getBalance()).isCloseTo(BigDecimal.ZERO, offset(BigDecimal.valueOf(0.0001)));

    BankAccount account = bankAccountRepository.findByIdAndOwner(1L, currentUser).orElse(null);

    assertThat(account).isNotNull();
    assertThat(account.getId()).isEqualTo(1L);
    assertThat(account.getBalance()).isCloseTo(BigDecimal.ZERO, offset(BigDecimal.valueOf(0.00001)));
  }

  @Test
  @WithMockUser(username = "test")
  public void setBalance__negativeBalance() throws Exception {
    BankAccount bankAccount = new BankAccount(1L, BigDecimal.TEN, currentUser);

    bankAccountRepository.save(bankAccount);

    SetBalanceRequest setBalanceRequest = new SetBalanceRequest();
    setBalanceRequest.setBalance(BigDecimal.ZERO.subtract(BigDecimal.TEN));

    String requestBody = objectMapper.writeValueAsString(setBalanceRequest);

    //
    //

    mockMvc.perform(
               patch("/api/accounts/1")
                   .content(requestBody)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
           )
           .andExpect(status().isBadRequest())
           .andReturn();

    //
    //

  }

  @Test
  @WithMockUser(username = "test")
  public void transfer() throws Exception {
    BankAccount srcBankAccount  = new BankAccount(1L, BigDecimal.TEN, currentUser);
    BankAccount destBankAccount = new BankAccount(2L, BigDecimal.ZERO, currentUser);

    bankAccountRepository.saveAll(List.of(srcBankAccount, destBankAccount));

    TransferRequest transferRequest = new TransferRequest();
    transferRequest.setFrom(srcBankAccount.getId());
    transferRequest.setTo(destBankAccount.getId());
    transferRequest.setAmount(BigDecimal.ONE);

    String requestBody = objectMapper.writeValueAsString(transferRequest);

    //
    //

    MvcResult mvcResult = mockMvc.perform(
                                     post("/api/accounts/transfer")
                                         .content(requestBody)
                                         .contentType(MediaType.APPLICATION_JSON_VALUE)
                                 )
                                 .andExpect(status().isOk())
                                 .andReturn();

    //
    //

    String response = mvcResult.getResponse().getContentAsString();

    AccountInfoResponse accountInfoResponse = objectMapper.readValue(response, AccountInfoResponse.class);

    assertThat(accountInfoResponse).isNotNull();
    assertThat(accountInfoResponse.getAccountId()).isEqualTo(1L);
    assertThat(accountInfoResponse.getBalance()).isCloseTo(BigDecimal.valueOf(9), offset(BigDecimal.valueOf(0.0001)));

    BankAccount savedSrcBankAccount = bankAccountRepository.findById(1L).orElse(null);

    assertThat(savedSrcBankAccount).isNotNull();
    assertThat(savedSrcBankAccount.getBalance()).isCloseTo(BigDecimal.valueOf(9), offset(BigDecimal.valueOf(0.0001)));

    BankAccount savedDestBankAccount = bankAccountRepository.findById(2L).orElse(null);

    assertThat(savedDestBankAccount).isNotNull();
    assertThat(savedDestBankAccount.getBalance()).isCloseTo(BigDecimal.ONE, offset(BigDecimal.valueOf(0.0001)));

    List<Transaction> transactions = transactionRepository.findByAccount(srcBankAccount);

    assertThat(transactions).hasSize(1);

    Transaction transaction = transactions.get(0);

    assertThat(transaction.getFrom().getId()).isEqualTo(1L);
    assertThat(transaction.getTo().getId()).isEqualTo(2L);
    assertThat(transaction.getAmount()).isCloseTo(BigDecimal.ONE, offset(BigDecimal.valueOf(0.0001)));
  }

  @Test
  @WithMockUser(username = "test")
  public void transfer__notEnoughBalance() throws Exception {
    BankAccount srcBankAccount  = new BankAccount(1L, BigDecimal.ONE, currentUser);
    BankAccount destBankAccount = new BankAccount(2L, BigDecimal.ZERO, currentUser);

    bankAccountRepository.saveAll(List.of(srcBankAccount, destBankAccount));

    TransferRequest transferRequest = new TransferRequest();
    transferRequest.setFrom(srcBankAccount.getId());
    transferRequest.setTo(destBankAccount.getId());
    transferRequest.setAmount(BigDecimal.TEN);

    String requestBody = objectMapper.writeValueAsString(transferRequest);

    //
    //

    mockMvc.perform(
               post("/api/accounts/transfer")
                   .content(requestBody)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
           )
           .andExpect(status().isBadRequest())
           .andReturn();

    //
    //

  }

  @Test
  @WithMockUser(username = "test")
  public void transfer__srcDoesNotExist() throws Exception {
    BankAccount destBankAccount = new BankAccount(2L, BigDecimal.ZERO, currentUser);

    bankAccountRepository.save(destBankAccount);

    TransferRequest transferRequest = new TransferRequest();
    transferRequest.setFrom(999L);
    transferRequest.setTo(destBankAccount.getId());
    transferRequest.setAmount(BigDecimal.TEN);

    String requestBody = objectMapper.writeValueAsString(transferRequest);

    //
    //

    mockMvc.perform(
               post("/api/accounts/transfer")
                   .content(requestBody)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
           )
           .andExpect(status().isNotFound())
           .andReturn();

    //
    //

  }

  @Test
  @WithMockUser(username = "test")
  public void transfer__destDoesNotExist() throws Exception {
    BankAccount srcBankAccount  = new BankAccount(1L, BigDecimal.TEN, currentUser);

    bankAccountRepository.save(srcBankAccount);

    TransferRequest transferRequest = new TransferRequest();
    transferRequest.setFrom(srcBankAccount.getId());
    transferRequest.setTo(999L);
    transferRequest.setAmount(BigDecimal.ONE);

    String requestBody = objectMapper.writeValueAsString(transferRequest);

    //
    //

    mockMvc.perform(
               post("/api/accounts/transfer")
                   .content(requestBody)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
           )
           .andExpect(status().isNotFound())
           .andReturn();

    //
    //

  }

}
