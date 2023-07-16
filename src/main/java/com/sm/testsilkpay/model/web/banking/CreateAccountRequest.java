package com.sm.testsilkpay.model.web.banking;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CreateAccountRequest {

  private BigDecimal initialBalance;

}
