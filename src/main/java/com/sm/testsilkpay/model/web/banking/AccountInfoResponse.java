package com.sm.testsilkpay.model.web.banking;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
@AllArgsConstructor(staticName = "of")
@Schema(description = "An object containing information about an account")
public class AccountInfoResponse {

  @Schema(description = "ID of the account")
  private long accountId;

  @Schema(description = "Current balance of the account")
  private BigDecimal balance;

}
