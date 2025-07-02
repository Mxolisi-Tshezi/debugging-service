package com.sumer.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "customer_header")
public class CustomerHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String initials;
    private String customerType;
    private String companyName;
    private String contactName;
    private int customerId;
    private int commissionRelId;
    private String language;
    private int postalAddressId;
    private int physicalAddressId;
    private int workAddressId;
    private boolean itcCheckAuthorised;
    private String countryAlpha2Code;
    private boolean vatRegistered;
    private String vatRegNo;
}

