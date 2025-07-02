package com.sumer.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "risk_header")
public class RiskHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sasriaCategory;
    private int commissionRebate;
    private String matching;
    private int riskTypeId;
    private int addressId;

    @OneToMany(cascade = CascadeType.ALL)
    private List<CoInsurer> coInsurers;

    // getters and setters
}

