package com.sumer.dto;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotePriceRequestDto {

    @JsonIgnore
    private Long userId;

    @NotNull(message = "Cart total is required")
    @Positive(message = "Cart total must be positive")
    private BigDecimal cartTotal;

    @NotNull(message = "Insurance type is required")
    private String insuranceType;

    @NotEmpty(message = "At least one answer is required")
    @Valid
    private List<QuestionAnswerDto> qualitativeAnswers;

    private String coveragePeriod; // 12_MONTHS, 24_MONTHS, etc.
    private String deviceCategory; // SMARTPHONE, LAPTOP, TABLET, etc.
}
