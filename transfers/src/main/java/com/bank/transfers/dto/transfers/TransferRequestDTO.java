package com.bank.transfers.dto.transfers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class TransferRequestDTO {

  @NotBlank(message = "The sender account is required")
  @Size(min = 10, max = 10, message = "Input your 10 digit account number")
  private String fromAccountNumber;

  @NotBlank(message = "The receiver account is required")
  @Size(min = 10, max = 10, message = "Input your 10 digit account number")
  private String toAccountNumber;

  @NotNull(message = "Amount is required")
  @Positive(message = "Amount must be greater than zero")
  private BigDecimal amount;


}
