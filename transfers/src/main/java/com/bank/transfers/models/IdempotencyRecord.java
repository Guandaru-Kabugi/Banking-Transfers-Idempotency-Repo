package com.bank.transfers.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "idempotency_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdempotencyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String idempotencyKey;

    @Column(nullable = false)
    private String requestPath; // scope key to endpoint — same key on a different endpoint shouldn't collide

    @Column(nullable = false)
    private String requestBodyHash; // detect if same key is reused with a DIFFERENT payload (should be rejected as a conflict)

    @Column(columnDefinition = "TEXT")
    private String responseBody;

    @Column(nullable = false)
    private Integer responseStatus;

    private Instant createdAt;
    private Instant expiresAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        expiresAt = createdAt.plusSeconds(86400); // 24h
    }
}
