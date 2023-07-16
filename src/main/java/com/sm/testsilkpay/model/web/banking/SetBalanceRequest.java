package com.sm.testsilkpay.model.web.banking;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Schema(description = "An object containing new balance value for an account")
public class SetBalanceRequest {

  @Schema(description = "New balance of the account")
  @DecimalMin(value = "0.0", message = "Balance must be >= 0")
  private BigDecimal balance;

}
