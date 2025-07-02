package com.sumer.entity;

import com.sumer.entity.Insurance;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customer_detail_field")
@Data
public class CustomerDetailField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String value;

    @ManyToOne
    @JoinColumn(name = "insurance_id")
    private Insurance insurance;
}