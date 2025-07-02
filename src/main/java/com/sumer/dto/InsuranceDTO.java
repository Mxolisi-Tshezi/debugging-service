package com.sumer.dto;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsuranceDTO {
    private Long id;
    @Positive
    private double premium;
    @Positive
    private double commission;
    @Positive
    private double quotePrice;

    private List<PolicyDTO> policies;
    private PolicyHeaderDTO header;

    private List<AddressDTO> addresses;

    private List<CustomerDetailFieldDTO> detailFields;
    public InsuranceDTO(Long id) {
        this.id = id;
    }

    public void setRisks(ArrayList<Object> objects) {
    }

    public void setFinance(String s) {
    }
}
