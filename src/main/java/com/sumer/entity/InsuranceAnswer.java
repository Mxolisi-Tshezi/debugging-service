package com.sumer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "insurance_answers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "answer")
    private String answer;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
    }
}
