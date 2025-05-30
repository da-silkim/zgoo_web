package zgoo.cpos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@ActiveProfiles("dev") // dev 프로필 활성화
public class ChargingPaymentInfoServiceTest {

    @Autowired
    private ChargingPaymentInfoService chargingPaymentInfoService;

    // @Test
    // public void findChgPaymentInfoTest() {
    // log.info("findChgPaymentInfoTest");

    // // 페이지 번호를 0으로 시작하도록 수정 (또는 서비스에서 처리)
    // Page<ChgPaymentInfoDto> chargingPaymentInfo =
    // chargingPaymentInfoService.findChgPaymentInfo(
    // "", "", "", "", 1L, 0, 10, "admin");

    // log.info("Total elements: {}", chargingPaymentInfo.getTotalElements());
    // log.info("Total pages: {}", chargingPaymentInfo.getTotalPages());
    // log.info("Current page: {}", chargingPaymentInfo.getNumber());
    // log.info("Page size: {}", chargingPaymentInfo.getSize());

    // chargingPaymentInfo.getContent().forEach(dto -> {
    // log.info("Payment info: {}", dto);
    // });
    // }
}
