package com.bank.transfers.mappers;

import com.bank.transfers.dto.transfers.TransferRequestDTO;
import com.bank.transfers.dto.transfers.TransferResponseDTO;
import com.bank.transfers.models.Transfer;

public class TransfersMappers {

  public static void toEntity(TransferRequestDTO transferRequestDTO, Transfer transfer) {
    transfer.setAmount(transferRequestDTO.getAmount());
    transfer.setFromAccountNumber(transferRequestDTO.getFromAccountNumber());
    transfer.setToAccountNumber(transferRequestDTO.getToAccountNumber());
  }
  public static TransferResponseDTO toDTO(Transfer transfer) {
    TransferResponseDTO transferResponseDTO = new TransferResponseDTO();
    transferResponseDTO.setId(transfer.getId());
    transferResponseDTO.setAmount(transfer.getAmount());
    transferResponseDTO.setFromAccountNumber(transfer.getFromAccountNumber());
    transferResponseDTO.setToAccountNumber(transfer.getToAccountNumber());
    transferResponseDTO.setStatus(transfer.getStatus());
    transferResponseDTO.setCreatedAt(transfer.getCreatedAt());
    return transferResponseDTO;
  }

}
