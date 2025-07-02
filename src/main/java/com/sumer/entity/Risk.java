package com.sumer.entity;

import com.sumer.entity.Customer;
import com.sumer.entity.DetailField;
import com.sumer.entity.RiskHeader;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "risk")
@Data
public class Risk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    @OneToOne(cascade = CascadeType.ALL)
    private RiskHeader header;

    private String description;
    private Double sumInsured;

    @OneToMany(cascade = CascadeType.ALL)
    private List<DetailField> detailFields;

    private String deviceMake;
    private String deviceModel;

    private boolean isDevicePrepaid; // fixed naming
    private String serviceProvider;
    private String addressLine1;
    private boolean decline;

    // Add the missing ManyToOne relationship with Customer
    @ManyToOne
    @JoinColumn(name = "customer_id") // Creates a foreign key in the risk table
    private Customer customer;  // This links the risk to a customer
    @ManyToOne
    @JoinColumn(name = "quote_id")  // This will create a foreign key to the quotes table
    private Quote quote;  // This links the Risk to a Quote
}
