package com.sm.testsilkpay.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  @Check(constraints = "balance > 0")
  private BigDecimal balance;

  @JoinColumn(name = "user_id", nullable = false)
  @ManyToOne(cascade = CascadeType.ALL, optional = false)
  private User owner;

}
