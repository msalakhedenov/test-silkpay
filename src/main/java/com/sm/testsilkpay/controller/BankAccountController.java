package com.sm.testsilkpay.controller;

import com.sm.testsilkpay.model.web.banking.AccountInfoResponse;
import com.sm.testsilkpay.model.web.banking.CreateAccountRequest;
import com.sm.testsilkpay.model.web.banking.TransferRequest;
import com.sm.testsilkpay.service.BankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/account")
@SecurityRequirement(name = "JWT")
@Tag(name = "Bank Account Management", description = "Create accounts, check balance and transfer funds")
public class BankAccountController {

  private final BankService bankService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
      summary = "Create a bank account",
      description = "Create a bank account optionally specifying its initial balance. Returns ID of newly created account and its balance",
      responses = {
          @ApiResponse(responseCode = "201", description = "Bank account successfully created")
      }
  )
  public AccountInfoResponse createAccount(@RequestBody(required = false) @Valid CreateAccountRequest createAccountRequest) {
    return bankService.createAccount(createAccountRequest);
  }

  @GetMapping("/{accountId}")
  @Operation(
      summary = "Check balance of the bank account",
      description = "Returns ID of the queried account and its balance",
      responses = {
          @ApiResponse(responseCode = "200", description = "Account with specified ID exists"),
          @ApiResponse(responseCode = "404", description = "Account with specified ID doesn't exist")
      }
  )
  public AccountInfoResponse getBalance(@PathVariable @Parameter(name = "Account ID") long accountId) {
    return bankService.getBalance(accountId);
  }

  @PostMapping("/transfer")
  @Operation(
      summary = "Transfer funds between accounts",
      description = "You can specify only one of YOUR accounts as the source of the transfer. Target account can be any existing account in the system",
      responses = {
          @ApiResponse(responseCode = "200", description = "Successful transfer"),
          @ApiResponse(responseCode = "404", description = "One of the specified accounts doesn't exist"),
          @ApiResponse(responseCode = "400", description = "Source account doesn't have enough balance to perform transfer")
      }
  )
  public AccountInfoResponse transfer(@RequestBody @Valid TransferRequest transferRequest) {
    return bankService.transfer(transferRequest);
  }

}
