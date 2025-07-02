package com.sumer.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "policy_finance")
public class PolicyFinance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
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

    // getters and setters
}

