package com.bank.transfers.utils;

public class SameAccountTransferException extends RuntimeException {

  public SameAccountTransferException(String message) {
    super(message);
  }
}
