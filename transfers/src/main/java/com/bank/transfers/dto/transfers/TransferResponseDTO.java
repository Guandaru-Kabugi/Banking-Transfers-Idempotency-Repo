package com.bank.transfers.dto.transfers;

import com.bank.transfers.models.TransferStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class TransferResponseDTO {

  private UUID id;
  private String fromAccountNumber;
  private String toAccountNumber;
  private BigDecimal amount;
  private TransferStatus status;
  private Instant createdAt;
}
