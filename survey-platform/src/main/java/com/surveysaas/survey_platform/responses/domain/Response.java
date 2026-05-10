package com.surveysaas.survey_platform.responses.domain;

import com.surveysaas.survey_platform.surveys.domain.Survey;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "responses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Token anónimo para trazabilidad sin exponer identidad
    @Column(nullable = false, unique = true)
    private UUID respondentToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant submittedAt;

    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Answer> answers = new ArrayList<>();
}