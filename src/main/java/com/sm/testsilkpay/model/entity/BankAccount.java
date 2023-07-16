package com.sm.testsilkpay.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Check(constraints = "balance > 0")
public class BankAccount {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private BigDecimal balance;

  @JoinColumn(name = "user_id", nullable = false)
  @ManyToOne(cascade = CascadeType.ALL, optional = false)
  private User owner;

  @OneToMany(cascade = CascadeType.ALL)
  private List<Transaction> transactions;

}
