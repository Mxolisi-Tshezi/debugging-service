package com.sumer.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "co_isurer")
public class CoInsurer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private int percentage;
    private boolean principle;

}

