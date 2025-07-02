package com.sumer.entity;

import com.sumer.entity.Risk;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "customer")
@Data
public class Customer {
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

    private String serviceProvider = "Vodacom"; // Default service provider

    // One customer has many risks
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)  // 'customer' refers to the field in Risk
    private List<Risk> risks;
}
