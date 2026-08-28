package com.bank.transfers.services.servicesimpl;

import com.bank.transfers.models.IdempotencyRecord;
import com.bank.transfers.repositories.IdempotencyRecordRepository;
import com.bank.transfers.services.IdempotencyRecordInterface;
import com.bank.transfers.utils.IdempotencyConflictException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdempotencyRecordServiceImpl implements IdempotencyRecordInterface {

  private final IdempotencyRecordRepository idempotencyRecordRepository;
  private final ObjectMapper objectMapper;

  @Override
  public Optional<IdempotencyRecord> findExisting(String idempotencyKey, String requestPath) {
    Optional<IdempotencyRecord> existing = idempotencyRecordRepository.findByIdempotencyKey(idempotencyKey);
    existing.ifPresent((record) -> {
      if(!record.getRequestPath().equals(requestPath)){
        throw new IdempotencyConflictException("The key is being used on a different endpoint");
      }
    });
    return existing;
  }

  @Override
  public IdempotencyRecord saveRecord(String idempotencyKey, String requestPath,
      String requestBodyHash, String responseBody, Integer responseStatus) {
    IdempotencyRecord idempotencyRecord = new IdempotencyRecord();
    idempotencyRecord.setIdempotencyKey(idempotencyKey);
    idempotencyRecord.setRequestPath(requestPath);
    idempotencyRecord.setRequestBodyHash(requestBodyHash);
    idempotencyRecord.setResponseBody(responseBody);
    idempotencyRecord.setResponseStatus(responseStatus);
    idempotencyRecordRepository.save(idempotencyRecord);
    return idempotencyRecord;
  }

  @Override
  public String hashRequestBody(Object requestBody) {
    try{
      String json = objectMapper.writeValueAsString(requestBody);
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(json.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(hash);
    } catch (Exception e) {
      throw new RuntimeException("Failed to hash the request body", e);
    }
  }
}
