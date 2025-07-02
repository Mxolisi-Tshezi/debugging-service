package com.sumer.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "policy")
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private PolicyHeader header;

    @OneToOne(cascade = CascadeType.ALL)
    private PolicyFinance finance;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Risk> risks;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<DetailField> detailFields;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insurance_id")
    private Insurance insurance;

}
