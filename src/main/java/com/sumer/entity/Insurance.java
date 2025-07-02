package com.sumer.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "insurance")
public class Insurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "premium")
    private Double premium;

    @Column(name = "commission")
    private Double commission;
    @Column(name = "quote_price")
    private Double quotePrice;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "insurance")
    private Set<Policy> policies;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private CustomerHeader header;
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.DETACH, orphanRemoval = false, mappedBy = "insurance")
    private List<Address> addresses;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY, mappedBy = "insurance")
    private Set<CustomerDetailField> detailFields;

}
