package com.sumer.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "fees")
public class Fees {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Fee adminFee;

    @OneToOne(cascade = CascadeType.ALL)
    private Fee brokerFee;

    @OneToOne(cascade = CascadeType.ALL)
    private Fee policyFee;

    // getters and setters
}
