package com.sumer.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "quotes")
@Getter
@Setter
public class Qoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String postalCode;
    private String suburb;
    private String addressLine1;
    private String contactName;
    private double sumInsured;
    private boolean isDevicePrepaid;
    private String policyType;
    private boolean vatRegistered;

    // Initialize risks list
    @OneToMany(mappedBy = "quote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Risk> risks = new ArrayList<>();
}
