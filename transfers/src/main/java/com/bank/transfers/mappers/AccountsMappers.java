package com.bank.transfers.mappers;


import com.bank.transfers.dto.accounts.AccountsRequestDTO;
import com.bank.transfers.dto.accounts.AccountsResponseDTO;
import com.bank.transfers.models.Accounts;

public class AccountsMappers {
  public static Accounts toEntity(AccountsRequestDTO requestDTO, Accounts accounts){
    accounts.setOwnerName(requestDTO.getOwnerName());
    return accounts;
  }

  public static AccountsResponseDTO toResponse(Accounts accounts){
    AccountsResponseDTO accountsResponseDTO = new AccountsResponseDTO();
    accountsResponseDTO.setAccountNumber(accounts.getAccountNumber());
    accountsResponseDTO.setId(accounts.getId());
    accountsResponseDTO.setStatus(accounts.getStatus());
    accountsResponseDTO.setBalance(accounts.getBalance());
    accountsResponseDTO.setAccountOwner(accounts.getOwnerName());
    accountsResponseDTO.setCreatedAt(accounts.getCreatedAt());
    accountsResponseDTO.setVersion(accounts.getVersion());
    return accountsResponseDTO;
  }
}
