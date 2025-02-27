package zgoo.cpos.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.company.CompanyDto.CpPlanDto;
import zgoo.cpos.dto.tariff.TariffDto.TariffInfoDto;
import zgoo.cpos.dto.tariff.TariffDto.TariffRegDto;
import zgoo.cpos.service.TariffService;
import zgoo.cpos.util.DateUtils;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/system/tariff")
public class TariffController {

    private final TariffService tariffService;

    // 요금정보 조회
    @GetMapping("/tariffinfo/get/{tariffId}")
    public ResponseEntity<List<TariffInfoDto>> findTariffInfo(@PathVariable("tariffId") Long tariffId) {
        log.info("=== find TariffInfo >> TariffId:{} ===", tariffId);

        try {
            List<TariffInfoDto> tinfoList = tariffService.searchTariffInfoListByTariffId(tariffId);
            if (!tinfoList.isEmpty()) {
                log.info("search TariffInfo complete >> tariffInfo: {}", tinfoList);
                return ResponseEntity.ok(tinfoList);
            } else {
                log.info("No TariffInfo >> tariffId:{}", tariffId);
                return ResponseEntity.ok(Collections.emptyList());
            }
        } catch (Exception e) {
            log.error("TariffController >> findTariffInfo error: {}", e, e.getMessage());
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    // 요금제 list 조회
    @GetMapping("/get/planInfo/{companyId}")
    public ResponseEntity<List<CpPlanDto>> findplanListWithCompanyId(@PathVariable("companyId") Long companyId) {
        log.info("==== find PlanInfo List with CompanyId:{}", companyId);
        try {
            List<CpPlanDto> planList = tariffService.searchPlanListWithCompanyId(companyId);

            if (planList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(planList);
        } catch (Exception e) {
            log.error("[TariffController - findplanListWithCompanyId] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 요금제 등록
    @PostMapping("/new")
    public ResponseEntity<Map<String, String>> createPlan(@Valid @RequestBody TariffRegDto requestDto,
            BindingResult result) {
        log.info("==== create Request Charger Plan(dto):{}", requestDto.toString());
        Map<String, String> response = new HashMap<>();

        requestDto = DateUtils.preprocessApplyDate(requestDto);

        // valid error 발생시 등록 폼 이동
        if (result.hasErrors()) {
            log.error("Charger Plan save form error: {}", result.getAllErrors());
            // TODO: web에 팝업띄워서 알리기

            // 클라이언트에게 400 Bad Request 상태와 함께 에러 메시지 반환
            response.put("message", "createPlan >> Validation check error");
            return ResponseEntity.badRequest().body(response);
        }

        try {

            // 요금제 저장(cpPlanPolicy)
            CpPlanDto planDto = CpPlanDto.builder()
                    .companyId(requestDto.getCompanyId())
                    .planName(requestDto.getPolicyName())
                    .build();

            tariffService.savePlanPolicy(requestDto.getCompanyId(), planDto);

            // Tariff policy 저장
            Long tariffId = tariffService.saveTariffPolicy(requestDto);

            // Tariff Info 저장
            tariffService.saveTariffInfo(requestDto, tariffId);

        } catch (Exception e) {
            log.error("TariffController >> createPlan Error:{}", e, e.getMessage());
            response.put("message", "서버 오류");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.put("message", "성공적으로 등록되었습니다.");
        return ResponseEntity.ok(response);
    }

    // 요금제 수정
    @PatchMapping("/update")
    public ResponseEntity<String> updatePlan(@RequestBody TariffRegDto requestDto) {
        log.info("==== update Request Charger Plan(dto):{}", requestDto.toString());

        try {
            tariffService.updateTariffPolicy(requestDto);

            return ResponseEntity.ok("요금제 수정 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("요금제 수정 오류");
        }

    }

    // 요금제 삭제
    @DeleteMapping("/delete/{tariffId}")
    public ResponseEntity<String> deletePlan(@PathVariable("tariffId") Long tariffId) {
        log.info("==== delete selected tariffId : {}", tariffId);

        try {
            tariffService.deletePlan(tariffId);
            return ResponseEntity.ok("Delete Success!");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("tariffId:" + tariffId + " is not exist.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while delete Company Info..");
        }
    }
}
