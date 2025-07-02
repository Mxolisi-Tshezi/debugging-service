package com.sumer.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "fee")
public class Fee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int min;
    private int max;
    private int value;
    private String calculationMethod;
    private String coverPeriod;

}
