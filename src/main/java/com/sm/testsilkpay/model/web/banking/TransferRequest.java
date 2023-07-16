package com.sm.testsilkpay.model.web.banking;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Schema(description = "An object containing information about a transfer to be performed")
public class TransferRequest {

  @Schema(description = "ID of the source account")
  @Min(value = 1, message = "Account ID must be specified")
  private Long from;

  @Schema(description = "ID of the target account")
  @Min(value = 1, message = "Account ID must be specified")
  private Long to;

  @Schema(description = "Transfer amount")
  @DecimalMin(value = "0.0", inclusive = false, message = "Transfer amount must be positive non-zero value")
  private BigDecimal amount;

}
