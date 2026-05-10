package com.surveysaas.survey_platform.responses.domain;

import com.surveysaas.survey_platform.questions.domain.Option;
import com.surveysaas.survey_platform.questions.domain.Question;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id", nullable = false)
    private Response response;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // Para preguntas de selección (SINGLE_CHOICE, MULTIPLE_CHOICE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private Option selectedOption;

    // Para preguntas de texto libre o rating
    @Column(length = 2000)
    private String textValue;
}