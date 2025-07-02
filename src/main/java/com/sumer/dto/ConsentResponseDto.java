package com.sumer.dto;
import com.sumer.entity.InsuranceConsent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsentResponseDto {
    private Long userId;
    private InsuranceConsent insuranceConsent;
    private LocalDateTime timestamp;
}