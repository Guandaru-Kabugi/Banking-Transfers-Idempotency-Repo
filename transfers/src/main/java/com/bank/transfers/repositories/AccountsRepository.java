package com.bank.transfers.repositories;

import com.bank.transfers.models.Accounts;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {

  boolean existsByAccountNumber(String accountNumber);

  Optional<Accounts> findByAccountNumber(String accountNumber);

}
