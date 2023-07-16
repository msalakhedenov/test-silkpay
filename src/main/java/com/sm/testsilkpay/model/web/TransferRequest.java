package com.sm.testsilkpay.model.web;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TransferRequest {

  private Long       from;
  private Long       to;
  private BigDecimal amount;

}
