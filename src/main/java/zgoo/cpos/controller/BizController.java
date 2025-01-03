package zgoo.cpos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.biz.BizInfoDto.BizInfoRegDto;
import zgoo.cpos.service.BizService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/corp")
public class BizController {

    private final BizService bizService;

    // 법인 단건 조회
    @GetMapping("/get/{bizId}")
    public ResponseEntity<BizInfoRegDto> findBizOne(@PathVariable("bizId") Long bizId) {
        log.info("=== find biz info ===");

        try {
            BizInfoRegDto bizOne = this.bizService.findBizOne(bizId);

            if (bizOne == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(bizOne);
        } catch (Exception e) {
            log.error("[findBizInfoOne] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 법인 정보 등록
    @PostMapping("/new")
    public ResponseEntity<String> createBiz(@Valid @RequestBody BizInfoRegDto dto) {
        log.info("=== create biz info ===");

        try {
            this.bizService.saveBiz(dto);
            return ResponseEntity.ok("법인 정보가 정상적으로 등록되었습니다.");
        } catch (Exception e) {
            log.error("[createBiz] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("법인 정보 등록 중 오류 발생");
        }
    }

    // 법인 정보 수정
    @PatchMapping("/update/{bizId}")
    public ResponseEntity<String> updateBiz(@PathVariable("bizId") Long bizId, @RequestBody BizInfoRegDto dto) {
        log.info("=== update biz info ===");

        try {
            this.bizService.updateBizInfo(bizId, dto);
            log.info("=== biz info update complete ===");
            return ResponseEntity.ok("법인 정보가 정상적으로 수정되었습니다.");
        } catch (Exception e) {
            log.error("[updateBiz] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("법인 정보 수정 중 오류 발생");
        }
    }

    // 법인 정보 삭제
    @DeleteMapping("/delete/{bizId}")
    public ResponseEntity<String> deleteBiz(@PathVariable("bizId") Long bizId) {
        log.info("=== delete biz info ===");

        if (bizId == null) {
            log.error("bizId id is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body("법인ID가 없습니다.");
        }

        try {
            this.bizService.deleteBiz(bizId);
            return ResponseEntity.ok("법인 정보가 정상적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("[deleteBiz] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("법인 정보 삭제 중 오류 발생");
        }
    }
}
