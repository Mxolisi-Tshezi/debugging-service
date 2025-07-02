package com.sumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sumer.entity.BankDetails;
import com.sumer.entity.Fees;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import lombok.*;

;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PolicyFinanceDTO {
    private BankDetails bankDetails;
    private String defaultCoInsurers;
    private int paymentMethod;
    private String collectionDay;
    private double taxPercentage;
    private int coverTerm;
    private int paymentTerm;
    private int currencyType;
    private boolean financeEnabled;
    private String paymentStatus;

    @OneToOne(cascade = CascadeType.ALL)
    private Fees fees;

    private String commissionCalcMethod;
}
