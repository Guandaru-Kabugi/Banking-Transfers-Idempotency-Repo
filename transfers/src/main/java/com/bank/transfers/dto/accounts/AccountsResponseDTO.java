package com.bank.transfers.dto.accounts;


import com.bank.transfers.models.AccountStatus;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Data;

@Data
public class AccountsResponseDTO {
  private Long id;
  private String accountNumber;
  private String accountOwner;
  private BigDecimal balance;
  private AccountStatus status;
  private Instant createdAt;
}
