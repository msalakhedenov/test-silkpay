package com.sm.testsilkpay.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Check(constraints = "amount > 0")
public class Transaction {

  @Id
  @GeneratedValue
  private Long id;

  @JoinColumn(nullable = false)
  @ManyToOne(optional = false)
  private BankAccount from;

  @JoinColumn(nullable = false)
  @ManyToOne(optional = false)
  private BankAccount to;

  @CreationTimestamp
  private LocalDateTime timestamp;

}
