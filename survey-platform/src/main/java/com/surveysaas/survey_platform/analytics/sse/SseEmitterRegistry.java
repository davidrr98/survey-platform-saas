package com.surveysaas.survey_platform.analytics.sse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SseEmitterRegistry {

    // ConcurrentHashMap + CopyOnWriteArrayList para thread safety
    private final Map<UUID, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter register(UUID surveyId) {
        SseEmitter emitter = new SseEmitter(300_000L); // 5 min timeout

        emitters.computeIfAbsent(surveyId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> remove(surveyId, emitter));
        emitter.onTimeout(() -> remove(surveyId, emitter));
        emitter.onError(e -> remove(surveyId, emitter));

        return emitter;
    }

    public void broadcast(UUID surveyId, Object data) {
        List<SseEmitter> surveyEmitters = emitters.getOrDefault(surveyId, List.of());
        surveyEmitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("survey-update")
                        .data(data));
            } catch (IOException e) {
                remove(surveyId, emitter);
            }
        });
    }

    private void remove(UUID surveyId, SseEmitter emitter) {
        List<SseEmitter> surveyEmitters = emitters.get(surveyId);
        if (surveyEmitters != null) {
            surveyEmitters.remove(emitter);
        }
    }
}