package com.bank.transfers.utils;

public class AccountAlreadyExists extends RuntimeException {

  public AccountAlreadyExists(String message) {
    super(message);
  }
}
