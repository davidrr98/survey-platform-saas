package com.surveysaas.survey_platform.questions.domain;

import com.surveysaas.survey_platform.responses.domain.Answer;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 500)
    private String text;

    @Column(nullable = false)
    private Integer optionOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // En Option.java agrega:
    @OneToMany(mappedBy = "selectedOption", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Answer> answers = new ArrayList<>();
}