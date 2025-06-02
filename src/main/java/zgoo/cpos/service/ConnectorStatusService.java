package zgoo.cpos.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.cp.ChargerDto.ConnectorStatusDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectorStatusService {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @KafkaListener(topics = "${kafka.topic.connector-status}", containerFactory = "kafkaListenerContainerFactory")
    public void listenConnectorStatusUpdates(ConnectorStatusDto statusDto) {
        try {
            log.info("[Kafka] 커넥터 상태 업데이트 수신: {}", statusDto);
            sendToAllEmitters(statusDto);
        } catch (Exception e) {
            log.error("[Kafka] 커넥터 상태 업데이트 처리 중 오류: {}", e.getMessage(), e);
        }
    }

    public void sendToAllEmitters(ConnectorStatusDto statusDto) {
        List<String> deadEmitters = new ArrayList<>();

        emitters.forEach((id, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("connector-status-update")
                        .data(statusDto));
            } catch (IOException e) {
                deadEmitters.add(id);
                log.debug("Emitter 전송 실패: {}", id);
            }
        });

        deadEmitters.forEach(this::removeEmitter);
    }

    public SseEmitter createEmitter() {
        String emitterId = UUID.randomUUID().toString();
        SseEmitter emitter = new SseEmitter(180_000L); // 3분 타임아웃 설정

        // 기존 emitter가 있다면 완료 처리
        emitters.values().forEach(existingEmitter -> {
            try {
                existingEmitter.complete();
            } catch (Exception e) {
                log.warn("기존 emitter 정리 중 오류", e);
            }
        });
        emitters.clear();

        emitter.onCompletion(() -> {
            log.debug("SSE 연결 완료: {}", emitterId);
            removeEmitter(emitterId);
        });

        emitter.onTimeout(() -> {
            log.debug("SSE 연결 타임아웃: {}", emitterId);
            removeEmitter(emitterId);
        });

        emitter.onError(e -> {
            log.debug("SSE 연결 오류: {}", emitterId);
            removeEmitter(emitterId);
        });

        emitters.put(emitterId, emitter);

        // 연결 즉시 초기 데이터 전송
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("연결 성공"));
        } catch (IOException e) {
            log.error("초기 이벤트 전송 실패", e);
            removeEmitter(emitterId);
        }

        return emitter;
    }

    private void removeEmitter(String emitterId) {
        SseEmitter emitter = emitters.remove(emitterId);
        if (emitter != null) {
            try {
                emitter.complete();
            } catch (Exception e) {
                log.warn("Emitter 완료 처리 중 오류", e);
            }
        }
    }

    public int getActiveConnectionCount() {
        return emitters.size();
    }
}
