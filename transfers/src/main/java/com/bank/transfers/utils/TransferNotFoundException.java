package com.bank.transfers.utils;

public class TransferNotFoundException extends RuntimeException {

  public TransferNotFoundException(String message) {
    super(message);
  }
}
