package com.bank.transfers.controllers;


import com.bank.transfers.dto.accounts.AccountsRequestDTO;
import com.bank.transfers.dto.accounts.AccountsResponseDTO;
import com.bank.transfers.services.AccountsInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountsController {

  private final AccountsInterface accountsInterface;

  @PostMapping("/create")
  public ResponseEntity<?> createAnAccount (@Valid @RequestBody AccountsRequestDTO requestDTO){
    AccountsResponseDTO responseDTO = accountsInterface.createNewAccount(requestDTO);
    return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
  }

  @GetMapping("/fetch")
  public ResponseEntity<?> fetchAnAccount (@RequestParam("accountNumber") String accountNumber){
    AccountsResponseDTO responseDTO = accountsInterface.getAnAccount(accountNumber);
    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
  }

}
