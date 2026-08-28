package com.bank.transfers.services;

import com.bank.transfers.models.IdempotencyRecord;
import java.util.Optional;

public interface IdempotencyRecordInterface {
  Optional<IdempotencyRecord> findExisting (String idempotencyKey, String requestPath);
  IdempotencyRecord saveRecord(String idempotencyKey, String requestPath, String requestBodyHash, String responseBody, Integer responseStatus);
  String hashRequestBody(Object requestBody);
}
