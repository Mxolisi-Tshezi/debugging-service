package com.sumer.repository;


import com.sumer.entity.InsuranceQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InsuranceQuestionRepository extends JpaRepository<InsuranceQuestion, Long> {
    long countByIdIn(List<Long> ids);
}
