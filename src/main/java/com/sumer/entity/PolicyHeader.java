package com.sumer.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "policy_head")
public class PolicyHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int productId;
    private int subCampaignId;
    private int commissionVersion;
    private String oldPolicyNumber;
    private String actualPolicyNumber;
    private String comment;
    private String countryAlpha2Code;
    private String coverStartDate;
}

