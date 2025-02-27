package zgoo.cpos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.cp.CpModel;
import zgoo.cpos.dto.cp.CpModelDto.CpModelDetailDto;
import zgoo.cpos.dto.cp.CpModelDto.CpModelListDto;
import zgoo.cpos.dto.cp.CpModelDto.CpModelRegDto;
import zgoo.cpos.service.CpModelService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/system/model")
public class CpModelController {

    private final CpModelService cpModelService;

    // 충전기 모델 단거조회 by 사업자ID
    @GetMapping("/get/modal/list/{companyId}")
    public ResponseEntity<List<CpModelListDto>> findcpmodelList(@PathVariable("companyId") Long companyId) {
        log.info("=== find cp model List info by companyId ===");

        try {
            List<CpModelListDto> modelList = this.cpModelService.findCpModelListByCompany(companyId);

            if (modelList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(modelList);
        } catch (Exception e) {
            log.error("[CpModelController - findcpmodelList] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 충전기 모델 단건 조회(모달)
    @GetMapping("/get/modal/one/{modelCode}")
    public ResponseEntity<CpModelListDto> findCpModelModalOne(@PathVariable("modelCode") String modelCode) {
        log.info("=== find cp model info(Modal) ===");

        try {
            CpModelListDto findOne = this.cpModelService.findCpModelModalOne(modelCode);

            if (findOne == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(findOne);
        } catch (Exception e) {
            log.error("[findCpModelOne_Modal] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 충전기 모델 단건 조회
    @GetMapping("/get/{modelId}")
    public ResponseEntity<CpModelRegDto> findCpModelOne(@PathVariable("modelId") Long modelId) {
        log.info("=== find cp model info ===");

        try {
            CpModelRegDto cpModeFindOne = this.cpModelService.findCpModelOne(modelId);

            if (cpModeFindOne == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(cpModeFindOne);
        } catch (Exception e) {
            log.error("[findCpModelOne] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 충전기 모델 상세조회
    @GetMapping("/detail/{modelId}")
    public String detailCpModel(Model model, @PathVariable("modelId") Long modelId,
            @RequestParam(value = "companyIdSearch", required = false) Long companyId,
            @RequestParam(value = "manfCdSearch", required = false) String manfCode,
            @RequestParam(value = "chgSpeedCdSearch", required = false) String chgSpeedCode,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("=== detail cp model info ===");

        try {
            CpModelDetailDto cpModel = this.cpModelService.findCpModelDetailOne(modelId);
            model.addAttribute("cpModel", cpModel);

            // pagination 관련 파라미터 추가
            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);
            model.addAttribute("selectedCompanyId", companyId);
            model.addAttribute("selectedManfCd", manfCode);
            model.addAttribute("selectedChgSpeedCd", chgSpeedCode);
        } catch (Exception e) {
            log.error("[detailCpModel] error: {}", e.getMessage());
        }

        return "pages/system/model_management_detail";
    }

    // 충전기 모델 등록
    @PostMapping("/new")
    public ResponseEntity<String> createCpModel(@Valid @RequestBody CpModelRegDto dto,
            @ModelAttribute("loginUserId") String loginUserId) {
        log.info("=== create cp model info ===");

        try {
            if (loginUserId != null && !loginUserId.isEmpty()) {
                dto.setUserId(loginUserId);
            } else {
                // dto.setUserId("daadmin"); // 비로그인, 테스트 사용자ID
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("충전기 모델 정보를 등록할 사용자ID가 없습니다.");
            }
            this.cpModelService.saveCpModelInfo(dto);
            return ResponseEntity.ok("충전기 모델 정보가 정상적으로 등록되었습니다.");
        } catch (Exception e) {
            log.error("[createCpModel] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("충전기 모델 등록 중 오류 발생");
        }
    }

    // 충전기 모델 수정
    @PatchMapping("/update/{modelId}")
    public ResponseEntity<String> updateCpModel(@PathVariable("modelId") Long modelId, @RequestBody CpModelRegDto dto) {
        log.info("=== update cp model info ===");

        try {
            CpModel model = this.cpModelService.updateCpModelInfo(dto, modelId);
            this.cpModelService.updateCpModelDetailInfo(dto, model);
            this.cpModelService.updateCpConnectorInfo(dto, model);

            log.info("=== cp model info update complete ===");
            return ResponseEntity.ok("충전기 모델 정보가 정상적으로 수정되었습니다.");
        } catch (Exception e) {
            log.error("[updateCpModel] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("충전기 모델 수정 중 오류 발생");
        }
    }

    // 충전기 모델 삭제
    @DeleteMapping("/delete/{modelId}")
    public ResponseEntity<String> deleteCpModel(@PathVariable("modelId") Long modelId) {
        log.info("=== delete cp model info ===");

        if (modelId == null) {
            log.error("modelId id is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("충전소기 모델ID가 없습니다.");
        }

        try {
            this.cpModelService.deleteCpModel(modelId);
            return ResponseEntity.ok("충전소기 모델이 정상적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("[deleteCpModel] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("충전기 모델 삭제 중 오류 발생");
        }
    }
}
