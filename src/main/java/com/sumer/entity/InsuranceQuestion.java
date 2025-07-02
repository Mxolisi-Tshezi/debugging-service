package com.sumer.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "insurance_questions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "question")
    private String question;
}
