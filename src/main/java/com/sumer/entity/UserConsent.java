package com.sumer.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_consents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserConsent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;


    private InsuranceConsentStatus insuranceConsentStatus;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(length = 500)
    private String remarks;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
