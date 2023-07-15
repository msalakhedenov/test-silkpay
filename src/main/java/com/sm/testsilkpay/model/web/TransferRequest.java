package com.sm.testsilkpay.model.web;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TransferRequest {

  private Long from;
  private Long to;
  private Long amount;

}
