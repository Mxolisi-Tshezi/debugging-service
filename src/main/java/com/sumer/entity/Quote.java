package com.sumer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "quotes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String postalCode;
    private String suburb;
    private String addressLine1;
    private String contactName;
    private Double sumInsured;
    private Boolean isDevicePrepaid;
    private String policyType;
    private Boolean vatRegistered;
    private String serviceProvider;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "quote")
    private List<Risk> risks;

}

