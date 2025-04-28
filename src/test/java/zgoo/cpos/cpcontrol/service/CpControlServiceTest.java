package zgoo.cpos.cpcontrol.service;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@ActiveProfiles("dev") // dev 프로필 활성화
public class CpControlServiceTest {

    @Autowired
    private CpControlService cpControlService;

    @Test
    void testGetTidData() {
        String tid = "dongatestm01162504230937413517";
        String approvalDate = "20250423";

        Map<String, Object> result = cpControlService.getTidData(tid, approvalDate);

        log.info("결과: {}", result);
    }
}
