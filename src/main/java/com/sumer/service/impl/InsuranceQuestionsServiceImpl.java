package com.sumer.service.impl;

import com.sumer.dto.InsuranceQuestionDto;
import com.sumer.entity.InsuranceQuestion;
import com.sumer.repository.InsuranceQuestionRepository;
import com.sumer.service.interf.InsuranceQuestionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsuranceQuestionsServiceImpl implements InsuranceQuestionsService {

    private final InsuranceQuestionRepository questionRepository;

    @Override
    public InsuranceQuestionDto createQuestion(InsuranceQuestionDto questionDto) {
        InsuranceQuestion question = InsuranceQuestion.builder()
                .question(questionDto.getQuestion())
                .build();

        InsuranceQuestion saved = questionRepository.save(question);
        log.info("Created question with ID: {}", saved.getId());

        return mapToDto(saved);
    }

    @Override
    public List<InsuranceQuestionDto> getAllQuestions() {
        List<InsuranceQuestion> questions = questionRepository.findAll();
        return questions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<InsuranceQuestionDto> getQuestionById(Long id) {
        return questionRepository.findById(id)
                .map(this::mapToDto);
    }

    private InsuranceQuestionDto mapToDto(InsuranceQuestion question) {
        InsuranceQuestionDto dto = new InsuranceQuestionDto();
        dto.setId(question.getId());
        dto.setQuestion(question.getQuestion());
        return dto;
    }
}
