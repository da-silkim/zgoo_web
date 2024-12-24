package zgoo.cpos.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.cs.CsInfo;
import zgoo.cpos.dto.cs.CsInfoDto.CsInfoDetailDto;
import zgoo.cpos.dto.cs.CsInfoDto.CsInfoRegDto;
import zgoo.cpos.service.CsService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/station/list")
public class CsController {

    private final CsService csService;

    // 충전소 이름 중복 검사
    @GetMapping("/checkStationName")
    public ResponseEntity<Boolean> checkStationName(@RequestParam String stationName) {
        log.info("=== duplicate check stationName ===");

        try {
            boolean response = this.csService.isStationNameDuplicate(stationName);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[checkStationName] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 충전소 단건 조회
    @GetMapping("/get/{stationId}")
    public ResponseEntity<CsInfoRegDto> findCsInfoOne(@PathVariable("stationId") String stationId) {
        log.info("=== find cs info ===");

        try {
            CsInfoRegDto csInfoFindOne = this.csService.findCsInfoOne(stationId);

            if (csInfoFindOne == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(csInfoFindOne);
        } catch (Exception e) {
            log.error("[findCsInfoOne] error: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 충전소 상세 조회
    @GetMapping("/detail/{stationId}")
    public String detailCsInfo(Model model, @PathVariable("stationId") String stationId,
            @RequestParam(value = "companyIdSearch", required = false) Long companyId,
            @RequestParam(value = "opSearch", required = false) String searchOp,
            @RequestParam(value = "contentSearch", required = false) String searchContent,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("=== detail cs info ===");

        try {
            CsInfoDetailDto csInfo = this.csService.findCsInfoDetailOne(stationId);
            model.addAttribute("csInfo", csInfo);

            // 이전글, 다음글 조회
            CsInfoDetailDto previousCsInfo = this.csService.findPreviousCsInfo(stationId, companyId, searchOp, searchContent);
            CsInfoDetailDto nextCsInfo = this.csService.findNextCsInfo(stationId, companyId, searchOp, searchContent);
            model.addAttribute("previousCsInfo", previousCsInfo);
            model.addAttribute("nextCsInfo", nextCsInfo);

            // pagination 관련 파라미터 추가
            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);
            model.addAttribute("selectedCompanyId", companyId);
            model.addAttribute("selectedOpSearch", searchOp);
            model.addAttribute("selectedContentSearch", searchContent);
        } catch (Exception e) {
            log.error("[detailCsInfo] error: {}", e.getMessage());
        }

        return "pages/charge/cs_list_detail";
    }

    // 충전소 등록
    @PostMapping("/new")
    public ResponseEntity<Map<String, String>> createCsInfo(@Valid @RequestBody CsInfoRegDto dto) {
        log.info("=== create cs info ===");

        Map<String, String> response = new HashMap<>();

        try {
            String result = this.csService.saveCsInfo(dto);
            if (result == null) {
                response.put("message", "충전소 정보 등록에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            response.put("stationId", result);
            response.put("message", "충전소 정보가 정상적으로 등록되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[createCsInfo] error: {}", e.getMessage());
            response.put("message", "오류 발생으로 충전소 정보 등록에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 충전소 수정
    @PatchMapping("/update")
    public ResponseEntity<String> updateCsInfo(@RequestBody CsInfoRegDto dto) {
        log.info("=== update cs info ===");

        try {
            CsInfo updatedCsInfo = this.csService.updateCsInfo(dto);
            this.csService.updateCsKepcoContractInfo(updatedCsInfo, dto);
            this.csService.updateCsLandInfo(updatedCsInfo, dto);

            log.info("=== cs info update complete ===");
            return ResponseEntity.ok("충전소가 정상적으로 수정되었습니다.");
        } catch (Exception e) {
            log.error("[updateCsInfo] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 충전소 삭제
    @DeleteMapping("/delete/{stationId}")
    public ResponseEntity<String> deleteCsInfo(@PathVariable("stationId") String stationId) {
        log.info("=== delete cs info ===");

        try {
            if (stationId == null) {
                log.error("cs info id is null");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body("충전소ID가 없습니다.");
            }
            this.csService.deleteCsInfo(stationId);
            return ResponseEntity.ok("충전소가 정상적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("[deleteCsInfo] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("충전소 삭제 중 오류 발생");
        }
    }
}
