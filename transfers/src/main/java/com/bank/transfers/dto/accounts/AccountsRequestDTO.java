package com.bank.transfers.dto.accounts;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccountsRequestDTO {

  @NotBlank(message = "Owner name is required")
  @Size(max = 100, message = "Owner name must not exceed 100 characters")
  private String ownerName;
}
