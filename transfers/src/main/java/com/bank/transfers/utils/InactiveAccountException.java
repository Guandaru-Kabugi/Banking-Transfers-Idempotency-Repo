package com.bank.transfers.utils;

public class InactiveAccountException extends RuntimeException {

  public InactiveAccountException(String message) {
    super(message);
  }
}
