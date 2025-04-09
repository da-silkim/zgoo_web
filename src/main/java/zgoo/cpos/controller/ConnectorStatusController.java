package zgoo.cpos.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.service.ConnectorStatusService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ConnectorStatusController {
    private final ConnectorStatusService connectorStatusService;

    @GetMapping("/api/connector-status/stream")
    public SseEmitter streamConnectorStatus() {
        log.info("SSE 스트림 연결 요청 수신");
        return connectorStatusService.createEmitter();
    }

    @GetMapping("/api/connector-status/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("activeConnections", connectorStatusService.getActiveConnectionCount());
        return ResponseEntity.ok(status);
    }
}
