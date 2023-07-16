package com.sm.testsilkpay.repository;

import com.sm.testsilkpay.model.entity.BankAccount;
import com.sm.testsilkpay.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

  List<BankAccount> findByOwner(User owner);

  Optional<BankAccount> findByIdAndOwner(long accountId, User owner);

}
