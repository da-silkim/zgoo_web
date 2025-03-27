package zgoo.cpos.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
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
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.code.CodeDto.CommCodeDto;
import zgoo.cpos.dto.code.CodeDto.GrpCodeDto;
import zgoo.cpos.service.CodeService;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/system/code")
public class CommonController {

    private final CodeService codeService;

    // @PostMapping("/system/code/grpcode/new")
    // public String createGrpCode(@Valid GrpCodeDto dto, BindingResult result) {
    // log.info("==== create group code(list) ======");
    // if (result.hasErrors()) {
    // log.error("그룹코드 등록 에러: {}", result.getAllErrors());
    // // TODO: web에 팝업띄우기
    // return "pages/system/code_management";
    // }

    // log.info("groupcode_dto >> {}", dto.toString());

    // codeService.saveGrpCode(dto);

    // return "redirect:/code_management/list";
    // }

    // @PostMapping("/code_management/commoncd/new")
    // public String createCommonCode(@Valid CommonCdDto dto, BindingResult result)
    // {
    // log.info("==== create common code(list) ======");
    // // valid error 발생시 등록폼 이동
    // if (result.hasErrors()) {
    // log.error("공통코드 등록 에러: {}", result.getAllErrors());
    // // TODO: web에 팝업띄우기
    // return "pages/system/code_management";
    // }

    // log.info("commoncd_dto >> {}", dto.toString());

    // // 여기서 grpCode를 조회하고 saveCommonCode에 넘겼을때, gpr_code 테이블에 insert 수행 ,, 왜??
    // // GrpCode grpCode = commonCdService.findGrpOne(dto.getId().getGrpCode());
    // // log.info("grpcode_find : {}", grpCode);

    // codeService.saveCommonCode(dto);

    // return "redirect:/code_management/list";
    // }

    // 그룹코드 - 등록
    @PostMapping("/grpcode/new")
    public ResponseEntity<Map<String, String>> createGrpCode(@Valid @RequestBody GrpCodeDto dto,
            BindingResult result, Principal principal) {
        log.info("==== create group code ======");
        
        Map<String, String> response = new HashMap<>();

        if (result.hasErrors()) {
            log.error("그룹코드 등록 에러: {}", result.getAllErrors());

            // 클라이언트에게 400 Bad Request 상태와 함께 에러 메시지 반환
            response.put("message", "유효성 검사 오류");
            return ResponseEntity.badRequest().body(response);
        }

        log.info("groupcode_dto >> {}", dto.toString());

        try {
            codeService.saveGrpCode(dto, principal.getName());
        } catch (Exception e) {
            log.error("[createGrpCode] error: {}", e.getMessage());
            response.put("message", "그룹코드 등록 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        // 성공적으로 저장된 경우 응답에 메시지와 리다이렉트 URL을 포함
        response.put("message", "그룹코드가 성공적으로 등록되었습니다.");
        // response.put("redirect", "/system/code/list"); // 리다이렉트 URL 포함
        return ResponseEntity.ok(response);
    }

    // 그룹코드 - 검색창으로 조회
    @GetMapping("/grpcode/search")
    public ResponseEntity<Map<String, Object>> searchGrpCode(
            @RequestParam(value = "grpcdName", required = false) String grpcdName) {
        log.info("==== search input group code(list) ======");

        Map<String, Object> response = new HashMap<>();

        try {
            List<GrpCodeDto> grpCodeList = grpcdName == null || grpcdName.isEmpty()
                    ? this.codeService.findGrpCodeAll()
                    : this.codeService.findGrpCodeByGrpcdName(grpcdName);
            response.put("searchGrpCode", grpCodeList);
        } catch (Exception e) {
            log.error("[searchGrpCode] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }

    // 그룹코드 - 연관된 공통코드 조회
    @GetMapping("/commoncd/search/{grpCode}")
    public ResponseEntity<List<CommCodeDto>> searchComCode(@PathVariable("grpCode") String grpCode) {
        log.info("==== find the associated common code(list) ======");

        try {
            List<CommCodeDto> commonCd = this.codeService.findCommonCdByGrpCd(grpCode);

            if (commonCd.isEmpty()) {
                log.info("조회된 공통코드가 없습니다.");
                return ResponseEntity.ok(Collections.emptyList());
            }

            return ResponseEntity.ok(commonCd);
        } catch (Exception e) {
            log.error("[searchComCode] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 그룹코드 - 그룹코드명 수정
    @PatchMapping("/grpcode/update")
    public ResponseEntity<String> updateGrpCode(@RequestBody GrpCodeDto grpCodeDto, Principal principal) {
        log.info("==== update group code(list) ======");

        try {
            this.codeService.updateGrpCode(grpCodeDto, principal.getName());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("[updateGrpCode] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("그룹코드 수정 중 오류가 발생했습니다.");
        }
    }

    // 그룹코드 - 삭제
    @DeleteMapping("/grpcode/delete/{grpcd}")
    public ResponseEntity<String> deleteGrpcd(@PathVariable("grpcd") String grpCode) {
        log.info("==== delete group code(list) ======");

        try {
            this.codeService.deleteGroupCode(grpCode);
            return ResponseEntity.ok("그룹코드가 정상적으로 삭제되었습니다.");
        } catch (EntityNotFoundException e) {
            log.error("[deleteGrpcd] EntityNotFoundException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 그룹코드를 찾을 수 없습니다.");
        } catch (Exception e) {
            log.error("[deleteGrpcd] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("그룹코드 삭제 중 오류가 발생했습니다.");
        }
    }

    // 공통코드 - 등록
    @PostMapping("/commoncd/new")
    public ResponseEntity<Map<String, String>> createCommonCode(@Valid @RequestBody CommCodeDto dto,
            BindingResult result, Principal principal) {
        log.info("==== create common code(list) ======");
        log.info("grpCode: {}, comCode: {}", dto.getGrpCode(), dto.getCommonCode());

        Map<String, String> response = new HashMap<>();

        if (dto == null || dto.getGrpCode() == null || dto.getCommonCode() == null) {
            log.error("DTO 또는 ID가 NULL입니다.");
            response.put("message", "DTO 또는 ID가 NULL입니다.");
            return ResponseEntity.badRequest().body(response);
        }

        if (result.hasErrors()) {
            log.error("공통코드 등록 에러: {}", result.getAllErrors());
            response.put("message", "유효성 검사 오류");
            return ResponseEntity.badRequest().body(response);
        }

        log.info("commoncd_dto >> {}", dto.toString());

        try {
            codeService.saveCommonCode(dto, principal.getName());
            response.put("message", "공통코드가 성공적으로 등록되었습니다.");
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            log.error("[createCommonCode] DataIntegrityViolationException반: {}", e.getMessage());
            response.put("message", "데이터 무결성 위반");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (NullPointerException e) {
            log.error("[createCommonCode] NullPointerException: {}", e.getMessage());
            response.put("message", "서버 오류");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            log.error("[createCommonCode] error: {}", e.getMessage());
            response.put("message", "공통코드 등록 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 공통코드 - 단건 조회
    @GetMapping("/commoncd/get/{grpcd}/{commoncd}")
    public ResponseEntity<CommCodeDto> findCommonOne(
            @PathVariable("grpcd") String grpcd, @PathVariable("commoncd") String commoncd) {
        log.info("==== search common code(list) ======");

        try {
            CommCodeDto commonCdDto = this.codeService.findCommCdGrpCd(grpcd, commoncd);
            if (commonCdDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.ok(commonCdDto);
        } catch (Exception e) {
            log.error("[findCommonOne] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 공통코드 - 수정
    @PatchMapping("/commoncd/update")
    public ResponseEntity<Map<String, String>> updateCommonCode(@RequestBody CommCodeDto commonCdDto,
            Principal principal) {
        log.info("==== update common code(list) ======");

        Map<String, String> response = new HashMap<>();
        
        try {
            CommCodeDto updatedCommonCode = this.codeService.updateCommonCodeInfo(commonCdDto, principal.getName());
            response.put("message", "공통코드가 성공적으로 수정되었습니다.");
            response.put("updatedCommonCode", updatedCommonCode.toString()); // 필요 시 수정된 코드 정보를 포함할 수 있습니다.
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            log.error("[updateCommonCode] EntityNotFoundException: {}", e.getMessage());
            response.put("message", "공통코드를 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            log.error("[updateCommonCode] error: {}", e.getMessage());
            response.put("message", "서버 오류");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 공통코드 - 삭제
    @DeleteMapping("/commoncd/delete/{grpcd}/{commoncd}")
    public ResponseEntity<String> deleteCommonCode(@PathVariable("grpcd") String grpCode,
            @PathVariable("commoncd") String commonCode) {
        log.info("==== delete common code(list) ======");

        try {
            this.codeService.deleteCommonCode(commonCode);
            return ResponseEntity.ok("공통코드가 정상적으로 삭제되었습니다.");
        } catch (EntityNotFoundException e) {
            log.error("[deleteCommonCode] EntityNotFoundException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 공통코드를 찾을 수 없습니다.");
        } catch (Exception e) {
            log.error("[deleteCommonCode] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공통코드 삭제 중 오류가 발생했습니다.");
        }
    }
}
