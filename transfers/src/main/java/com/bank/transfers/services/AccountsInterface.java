package com.bank.transfers.services;

import com.bank.transfers.dto.accounts.AccountsRequestDTO;
import com.bank.transfers.dto.accounts.AccountsResponseDTO;

public interface AccountsInterface {
  AccountsResponseDTO createNewAccount(AccountsRequestDTO requestDTO);
  AccountsResponseDTO getAnAccount(String accountNumber);
}
