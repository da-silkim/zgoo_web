package zgoo.cpos.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.cp.ChargerDto.ChargerRegDto;
import zgoo.cpos.service.ChargerService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/charger")
public class CpController {
    private final ChargerService chargerService;

    /*
     * 충전기 등록
     */
    @PostMapping("/new")
    public ResponseEntity<Map<String, String>> createCpInfo(@Valid @RequestBody ChargerRegDto reqdto,
            BindingResult bindingResult) {
        log.info("=== create Charger info >> {}", reqdto.toString());

        Map<String, String> response = new HashMap<>();

        // valid error발생시 결과 리턴
        if (bindingResult.hasErrors()) {
            log.error("충전기등록 에러: {}", bindingResult.getAllErrors());

            // 클라이언트에게 400 Bad Request 상태와 함께 에러 메시지 반환
            bindingResult.getFieldErrors().forEach(error -> {
                response.put(error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(response);
        }

        try {
            String result = chargerService.createCpInfo(reqdto);
            if (result == null) {
                response.put("message", "충전기 등록에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            response.put("chargerId", result);
            response.put("message", "충전기 정보가 정상적으로 등록되었습니다.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("[createCpInfo] error: {}", e.getMessage());
            response.put("message", "오류 발생으로 충전기 등록에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /*
     * 충전기ID get
     */
    @GetMapping("/get/create/cpid")
    public ResponseEntity<Map<String, String>> createCpId(@RequestParam String stationId) {
        log.info("=== Get new CPID ===");

        Map<String, String> respnose = new HashMap<>();

        try {
            String createdCpId = chargerService.createCpId(stationId);
            if (createdCpId.equals("")) {
                respnose.put("message", "충전기ID 생성에 실패하였습니다.");
                return ResponseEntity.badRequest().body(respnose);
            }
            respnose.put("cpid", createdCpId);
            return ResponseEntity.ok(respnose);

        } catch (Exception e) {
            log.error("[createCpid] error: {}", e.getMessage());
            respnose.put("message", "충전기ID 생성중 오류가 발생하였습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respnose);
        }
    }

    // 모뎀 시리얼번호 중복검사
    @GetMapping("/modem/serialnum/dupcheck")
    public ResponseEntity<Boolean> checkModemSerialNum(@RequestParam String serialNum) {
        log.info("=== duplicate check modem serial number ===");

        try {
            boolean response = chargerService.isModemSerialDuplicate(serialNum);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[checkModemSerialNumber] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 모뎀 번호 중복검사
    @GetMapping("/modem/modemNum/dupcheck")
    public ResponseEntity<Boolean> checkModemNum(@RequestParam String modemNum) {
        log.info("=== duplicate check modemNumber ===");

        try {
            boolean response = chargerService.isModemNumDuplicate(modemNum);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[checkModemNumber] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*
     * 충전기 정보 상세 조회
     */
    @GetMapping("/detail/{chargerId}")
    public String detailCpInfo(Model model, @PathVariable("chargerId") String chargerId,
            @RequestParam(value = "companyIdSearch", required = false) Long reqCompanyId,
            @RequestParam(value = "manfCodeSearch", required = false) String reqManfCd,
            @RequestParam(value = "opSearch", required = false) String reqOpSearch,
            @RequestParam(value = "contentSearch", required = false) String reqSearchContent,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return "pages/charge/cp_list_detail";

    }
}
