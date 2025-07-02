package com.sumer.service.interf;

import com.sumer.dto.InsuranceQuestionDto;
import java.util.List;
import java.util.Optional;

public interface InsuranceQuestionsService {
    InsuranceQuestionDto createQuestion(InsuranceQuestionDto questionDto);
    List<InsuranceQuestionDto> getAllQuestions();
    Optional<InsuranceQuestionDto> getQuestionById(Long id);
}
