package com.sm.testsilkpay.model.web.banking;

import io.swagger.v3.oas.annotations.media.Schema;
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
  private Long from;

  @Schema(description = "ID of the target account")
  private Long to;

  @Schema(description = "Transfer amount")
  @Min(value = 0, message = "Transfer amount must be positive")
  private BigDecimal amount;

}
