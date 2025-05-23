package zgoo.cpos.controller;

import java.security.Principal;
import java.util.HashMap;
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
import zgoo.cpos.domain.company.Company;
import zgoo.cpos.dto.company.CompanyDto;
import zgoo.cpos.dto.company.CompanyDto.CompanyRegDto;
import zgoo.cpos.service.ComService;
import zgoo.cpos.service.CompanyService;
import zgoo.cpos.util.MenuConstants;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/biz_management")
public class CompanyController {
    private final CompanyService companyService;
    private final ComService comService;

    @GetMapping("/biz/search/{companyId}")
    public ResponseEntity<CompanyRegDto> searchForUpdate(@PathVariable("companyId") String companyId) {
        log.info("=== search By Id >> companyId:{}", companyId);
        try {
            CompanyRegDto findOne = companyService.searchCompanyById(companyId);

            if (findOne == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } else {
                log.info("=== search(for update) Result >> :{}", findOne.toString());
            }

            return ResponseEntity.ok(findOne);
        } catch (Exception e) {
            log.error("[Controller - searchForUpdate] : {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /*
     * 사업자 등록
     */
    @PostMapping(value = "/biz/new")
    public ResponseEntity<Map<String, String>> createCompany(@Valid @RequestBody CompanyDto.CompanyRegDto dto,
            BindingResult result, Principal principal) {
        log.info("==== create Request Company(dto):{}", dto.toString());
        Map<String, String> response = new HashMap<>();

        // valid error 발생시 등록폼 이동
        if (result.hasErrors()) {
            log.error("사업자 등록 에러: {}", result.getAllErrors());
            // TODO: web에 팝업띄우기

            // 클라이언트에게 400 Bad Request 상태와 함께 에러 메시지 반환
            response.put("message", "createCompany >> 유효성 검사 오류");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            ResponseEntity<Map<String, String>> permissionCheck = this.comService.checkUserPermissionsMsg(principal,
                    MenuConstants.BIZ);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            // 저장
            Company saved = companyService.saveCompany(dto);

            log.info("=== company saved:{}", saved.toString());

        } catch (Exception e) {
            log.error("[createCompany] 알 수 없는 오류 발생: {}", e.getMessage());
            response.put("message", "서버 오류");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.put("message", "성공적으로 등록되었습니다.");
        return ResponseEntity.ok(response);

    }

    /*
     * 사업자 정보 수정
     */
    @PatchMapping("/biz/update")
    public ResponseEntity<String> updateCompany(@RequestBody CompanyRegDto dto, Principal principal) {
        log.info("==== update Request Company(dto):{}", dto.toString());

        try {
            ResponseEntity<String> permissionCheck = this.comService.checkUserPermissions(principal,
                    MenuConstants.BIZ);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            companyService.updateCompanyAll(dto);
            log.info("=== update complete!!");
            return ResponseEntity.ok("업체정보 수정 성공");
        } catch (Exception e) {
            log.error("Company update failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업체정보 수정 오류");
        }
    }

    /*
     * 사업자 삭제
     */
    @DeleteMapping("/biz/delete/{companyId}")
    public ResponseEntity<String> deleteCompany(@PathVariable("companyId") String companyId, Principal principal) {
        log.info("==== delete group code(list) ======");

        try {
            ResponseEntity<String> permissionCheck = this.comService.checkUserPermissions(principal,
                    MenuConstants.BIZ);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            companyService.deleteCompany(Long.parseLong(companyId));
            return ResponseEntity.ok("Delete Success!");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("companyID:" + companyId + " is not exist.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while delete Company Info..");
        }
    }

    // /**
    // * 회사 계층 구조 조회
    // */
    // @GetMapping("/biz/hierarchy/{companyId}")
    // public ResponseEntity<?> getCompanyHierarchy(@PathVariable("companyId") Long
    // companyId, Principal principal) {
    // log.info("=== get company hierarchy >> companyId:{}", companyId);

    // try {
    // ResponseEntity<String> permissionCheck =
    // this.comService.checkUserPermissions(principal,
    // MenuConstants.BIZ);
    // if (permissionCheck != null) {
    // return permissionCheck;
    // }

    // Map<String, Object> hierarchy =
    // companyService.getCompanyHierarchy(companyId);
    // return ResponseEntity.ok(hierarchy);
    // } catch (EntityNotFoundException e) {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회사를 찾을 수 없습니다: " +
    // e.getMessage());
    // } catch (Exception e) {
    // log.error("[Controller - getCompanyHierarchy] : {}", e.getMessage(), e);
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가
    // 발생했습니다");
    // }
    // }
}
