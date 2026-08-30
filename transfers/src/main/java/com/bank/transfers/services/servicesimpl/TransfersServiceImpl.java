package com.bank.transfers.services.servicesimpl;

import com.bank.transfers.dto.transfers.TransferRequestDTO;
import com.bank.transfers.dto.transfers.TransferResponseDTO;
import com.bank.transfers.mappers.TransfersMappers;
import com.bank.transfers.models.AccountStatus;
import com.bank.transfers.models.Accounts;
import com.bank.transfers.models.IdempotencyRecord;
import com.bank.transfers.models.Transfer;
import com.bank.transfers.models.TransferStatus;
import com.bank.transfers.repositories.AccountsRepository;
import com.bank.transfers.repositories.TransfersRepository;
import com.bank.transfers.services.IdempotencyRecordInterface;
import com.bank.transfers.services.TransfersInterface;
import com.bank.transfers.utils.AccountNotFoundException;
import com.bank.transfers.utils.IdempotencyConflictException;
import com.bank.transfers.utils.InactiveAccountException;
import com.bank.transfers.utils.InsufficientFundsException;
import com.bank.transfers.utils.SameAccountTransferException;
import com.bank.transfers.utils.TransferNotFoundException;
import com.bank.transfers.utils.constants.Constants;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransfersServiceImpl implements TransfersInterface {
  private final IdempotencyRecordInterface  idempotencyRecordInterface;
  private final AccountsRepository accountsRepository;
  private final TransfersRepository transfersRepository;
  private final ObjectMapper objectMapper;

  @Override
  @Transactional
  public TransferResponseDTO executeTransfer(String idempotencyKey,
      TransferRequestDTO transferRequestDTO) {

    //1. Idempotency check first
    String bodyHash = idempotencyRecordInterface.hashRequestBody(transferRequestDTO);
    Optional<IdempotencyRecord> existing = idempotencyRecordInterface.findExisting(idempotencyKey,
        Constants.TRANSFER_PATH);
    if (existing.isPresent()) {
      IdempotencyRecord idempotencyRecord = existing.get();
      if(!idempotencyRecord.getRequestBodyHash().equals(bodyHash)) {
        throw new IdempotencyConflictException("This idempotency key was already used with a different path");
      }
      //2. Same key return the same payload
      return deserializeResponse(idempotencyRecord.getResponseBody());
    }
    //3. Validate to ensure you cannot transfer to same account
    if (transferRequestDTO.getFromAccountNumber().equals(transferRequestDTO.getToAccountNumber())) {
      throw new SameAccountTransferException("Cannot transfer to same account");
    }
    //4. fetch both accounts to see if they exist
    Accounts fromAccount = accountsRepository.findByAccountNumber(
        transferRequestDTO.getFromAccountNumber()).orElseThrow(() -> new AccountNotFoundException("Sender account not found: " +  transferRequestDTO.getFromAccountNumber()));
    Accounts toAccount = accountsRepository.findByAccountNumber(transferRequestDTO.getToAccountNumber()).orElseThrow(() -> new AccountNotFoundException("Receiver account not found: " +  transferRequestDTO.getToAccountNumber()));
    //5. Status Checks
    if (fromAccount.getStatus() != AccountStatus.ACTIVE){
      throw new InactiveAccountException("Sender account is not active");
    }
    if  (toAccount.getStatus() != AccountStatus.ACTIVE){
      throw new InactiveAccountException("Receiver account is not active");
    }
    //6. Balance Check
    if(fromAccount.getBalance().compareTo(transferRequestDTO.getAmount()) < 0){
      throw new InsufficientFundsException("Insufficient funds");
    }
    //7. create the transfer record, status starts as pending
    Transfer transfer = new Transfer();
    TransfersMappers.toEntity(transferRequestDTO, transfer);
    transfersRepository.save(transfer);
    try{
       fromAccount.setBalance(fromAccount.getBalance().subtract(transferRequestDTO.getAmount()));
       toAccount.setBalance(toAccount.getBalance().add(transferRequestDTO.getAmount()));
       accountsRepository.save(toAccount);
       accountsRepository.save(fromAccount);

       transfer.setStatus(TransferStatus.COMPLETED);
       transfer.setCompletedAt(java.time.Instant.now());
       transfersRepository.save(transfer);
    }catch(Exception e){
      transfer.setStatus(TransferStatus.FAILED);
      transfer.setFailureReason(e.getMessage());
      transfersRepository.save(transfer);
      throw e;
    }
    TransferResponseDTO transferResponseDTO = TransfersMappers.toDTO(transfer);
    // 8. Cache the response against the idempotency key for future retries
    idempotencyRecordInterface.saveRecord(idempotencyKey, Constants.TRANSFER_PATH, bodyHash, serializeResponse(transferResponseDTO), 200);



    return transferResponseDTO;
  }

  @Override
  public TransferResponseDTO getTransferById(UUID transferId) {
    Transfer transfer = transfersRepository.findById(transferId).orElseThrow(() -> new TransferNotFoundException("The transfer is not found"));
    return TransfersMappers.toDTO(transfer);
  }
  private String serializeResponse(TransferResponseDTO transferResponseDTO) {
    try {
      return objectMapper.writeValueAsString(transferResponseDTO);
    }catch(Exception e) {
      throw new RuntimeException("Failed to serialize response",e);
    }
  }
  private TransferResponseDTO deserializeResponse(String json) {
    try {
      return objectMapper.readValue(json, TransferResponseDTO.class);
    }catch (Exception e) {
      throw new RuntimeException("Failed to deserialize response",e);
    }
  }
}
