package com.surveysaas.survey_platform.questions.domain;

import com.surveysaas.survey_platform.responses.domain.Answer;
import com.surveysaas.survey_platform.surveys.domain.Survey;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 500)
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    @Column(nullable = false)
    private Integer questionOrder;

    @Column(nullable = false)
    private Boolean required;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("optionOrder ASC")
    @Builder.Default
    private List<Option> options = new ArrayList<>();

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Answer> answers = new ArrayList<>();
}