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
@Schema(description = "An object containing initial balance of the account to be created")
public class CreateAccountRequest {

  @Schema(description = "Initial balance")
  @DecimalMin(value = "0.0", message = "Balance must be >= 0")
  private BigDecimal initialBalance;

}
