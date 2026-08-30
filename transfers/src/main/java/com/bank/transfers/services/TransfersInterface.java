package com.bank.transfers.services;

import com.bank.transfers.dto.transfers.TransferRequestDTO;
import com.bank.transfers.dto.transfers.TransferResponseDTO;
import java.util.UUID;

public interface TransfersInterface {
  TransferResponseDTO executeTransfer(String idempotencyKey, TransferRequestDTO transferRequestDTO);
  TransferResponseDTO getTransferById(UUID transferId);
}
