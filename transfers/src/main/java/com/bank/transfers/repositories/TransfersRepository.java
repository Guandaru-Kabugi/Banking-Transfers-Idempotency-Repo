package com.bank.transfers.repositories;

import com.bank.transfers.models.Transfer;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransfersRepository extends JpaRepository<Transfer, UUID> {

}
