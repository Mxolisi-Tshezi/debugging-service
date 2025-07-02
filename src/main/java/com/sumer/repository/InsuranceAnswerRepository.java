package com.sumer.repository;


import com.sumer.entity.InsuranceAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InsuranceAnswerRepository extends JpaRepository<InsuranceAnswer, Long> {
    List<InsuranceAnswer> findByUserId(Long userId);
    void deleteByUserIdAndQuestionId(Long userId, Long questionId);
}


