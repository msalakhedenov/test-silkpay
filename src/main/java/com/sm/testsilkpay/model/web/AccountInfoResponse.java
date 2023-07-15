package com.sm.testsilkpay.model.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
@AllArgsConstructor(staticName = "of")
public class AccountInfoResponse {

  private long accountId;
  private BigDecimal balance;

}
