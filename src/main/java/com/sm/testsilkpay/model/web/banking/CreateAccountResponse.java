package com.sm.testsilkpay.model.web.banking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(staticName = "of")
public class CreateAccountResponse {

  private long accountId;

}