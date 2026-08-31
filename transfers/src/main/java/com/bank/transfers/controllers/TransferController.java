package com.bank.transfers.controllers;

import com.bank.transfers.dto.transfers.TransferRequestDTO;
import com.bank.transfers.dto.transfers.TransferResponseDTO;
import com.bank.transfers.services.TransfersInterface;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TransferController {

  private final TransfersInterface transfersInterface;

  @PostMapping("/transfer")
  public ResponseEntity<TransferResponseDTO> transfer(
      @Valid @RequestBody TransferRequestDTO transferRequestDTO,
      @RequestHeader("Idempotency-Key") String idempotencyKey) {
    return ResponseEntity.ok(
        transfersInterface.executeTransfer(idempotencyKey, transferRequestDTO));
  }
  @GetMapping("/{id}")
  public ResponseEntity<TransferResponseDTO> getTransfer(
      @PathVariable UUID id
  ){
    return ResponseEntity.ok(transfersInterface.getTransferById(id));
  }


}
