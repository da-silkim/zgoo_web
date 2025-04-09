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
                if (e.getMessage().contains("Broken pipe")) {
                    log.debug("클라이언트 연결 종료: {}", id);
                } else {
                    log.error("SSE 이벤트 전송 실패: {}", e.getMessage());
                }
            }
        });

        if (!deadEmitters.isEmpty()) {
            deadEmitters.forEach(id -> {
                emitters.remove(id);
                log.debug("Dead emitter 제거: {}", id);
            });
            log.info("비활성 SSE 연결 {} 개 제거, 현재 활성 연결: {}", deadEmitters.size(), emitters.size());
        }
    }

    public SseEmitter createEmitter() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        String emitterId = UUID.randomUUID().toString();

        log.info("새 SSE Emitter 생성: {}", emitterId);
        emitters.put(emitterId, emitter);

        emitter.onCompletion(() -> {
            log.debug("SSE 연결 완료: {}", emitterId);
            emitters.remove(emitterId);
        });

        emitter.onTimeout(() -> {
            log.debug("SSE 연결 타임아웃: {}", emitterId);
            emitters.remove(emitterId);
        });

        emitter.onError(e -> {
            if (e instanceof IOException && e.getMessage().contains("Broken pipe")) {
                log.debug("클라이언트 연결 종료: {}", emitterId);
            } else {
                log.error("SSE 연결 오류: {}", e.getMessage());
            }
            emitters.remove(emitterId);
        });

        // 초기 연결 확인 이벤트 전송
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("연결 성공"));
        } catch (IOException e) {
            log.error("초기 SSE 이벤트 전송 실패: {}", e.getMessage());
            emitters.remove(emitterId);
        }

        return emitter;
    }

    public int getActiveConnectionCount() {
        return emitters.size();
    }
}
