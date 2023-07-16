package com.sm.testsilkpay.repository;

import com.sm.testsilkpay.model.entity.BankAccount;
import com.sm.testsilkpay.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  @Query("SELECT transaction FROM Transaction transaction WHERE transaction.from = :account OR transaction.to = :account")
  List<Transaction> findByAccount(@Param("account") BankAccount bankAccount);

}
