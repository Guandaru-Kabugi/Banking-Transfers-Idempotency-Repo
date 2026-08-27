package com.bank.transfers.services.servicesimpl;


import com.bank.transfers.dto.accounts.AccountsRequestDTO;
import com.bank.transfers.dto.accounts.AccountsResponseDTO;
import com.bank.transfers.mappers.AccountsMappers;
import com.bank.transfers.models.AccountStatus;
import com.bank.transfers.models.Accounts;
import com.bank.transfers.repositories.AccountsRepository;
import com.bank.transfers.services.AccountsInterface;
import com.bank.transfers.utils.AccountAlreadyExists;
import com.bank.transfers.utils.AccountNotFoundException;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountsServiceImpl implements AccountsInterface {

  private final AccountsRepository accountsRepository;
  private static final SecureRandom random = new SecureRandom();

  @Override
  @Transactional
  public AccountsResponseDTO createNewAccount(AccountsRequestDTO requestDTO) {
    String accountNumber = generateBankAccountNumber();
    if (accountsRepository.existsByAccountNumber(accountNumber)) {
      throw new AccountAlreadyExists("Account already exists");
    }
    Accounts accounts = new Accounts();
    AccountsMappers.toEntity(requestDTO, accounts);
    accounts.setAccountNumber(accountNumber);
    accounts.setStatus(AccountStatus.ACTIVE);
    accounts.setBalance(new BigDecimal("0.00"));
    accountsRepository.save(accounts);
    return AccountsMappers.toResponse(accounts);
  }

  @Override
  public AccountsResponseDTO getAnAccount(String accountNumber) {
    try {
      Accounts account = accountsRepository.findByAccountNumber(accountNumber).orElseThrow(
          () -> new AccountNotFoundException(
              "Account with account number " + accountNumber + " not found."));
      return AccountsMappers.toResponse(account);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private String generateBankAccountNumber() {
    int firstDigit = 0;
    StringBuilder accountNumber = new StringBuilder();
    accountNumber.append(firstDigit);
    for (int i = 0; i < 9; i++) {
      accountNumber.append(random.nextInt(10));
    }
    return accountNumber.toString();
  }
}
