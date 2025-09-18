package zgoo.cpos.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.code.CodeDto.CommCdBaseDto;
import zgoo.cpos.dto.company.CompanyDto.CompanyListDto;
import zgoo.cpos.dto.cp.ChargerDto.ChargerListDto;
import zgoo.cpos.dto.cp.CpModelDto.CpModelListDto;
import zgoo.cpos.dto.fw.CpFwversionDto;
import zgoo.cpos.dto.fw.FwVersionRegDto;
import zgoo.cpos.dto.menu.MenuAuthorityDto.MenuAuthorityBaseDto;
import zgoo.cpos.service.ChargerService;
import zgoo.cpos.service.CodeService;
import zgoo.cpos.service.ComService;
import zgoo.cpos.service.CompanyService;
import zgoo.cpos.service.CpModelService;
import zgoo.cpos.service.FwService;
import zgoo.cpos.service.MenuAuthorityService;
import zgoo.cpos.util.MenuConstants;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/fw")
public class FirmwareController {
    private final FwService fwService;
    private final ComService comService;
    private final CpModelService cpModelService;
    private final CompanyService companyService;
    private final CodeService codeService;
    private final MenuAuthorityService menuAuthorityService;
    private final ChargerService chargerService;
    private final MessageSource messageSource;

    /*
     * 펌웨어 버전 등록
     */
    @PostMapping("/version/new")
    public ResponseEntity<Map<String, String>> createFwVersion(@Valid @RequestBody FwVersionRegDto reqdto,
            BindingResult bindingResult, Principal principal) {
        log.info("=== create Firmware Version info >> {}", reqdto.toString());

        Map<String, String> response = new HashMap<>();

        // valid error발생시 결과 리턴
        if (bindingResult.hasErrors()) {
            log.error("fw version add error: {}", bindingResult.getAllErrors());

            // 클라이언트에게 400 Bad Request 상태와 함께 에러 메시지 반환
            bindingResult.getFieldErrors().forEach(error -> {
                response.put(error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(response);
        }

        try {

            // html코드에서 악의적으로 '등록'버튼 권한체크 영억을 지우고 등록시도시 추가 권한체크 진행
            ResponseEntity<Map<String, String>> permissionCheck = this.comService.checkUserPermissionsMsg(principal,
                    MenuConstants.FW_VERSION);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            String result = fwService.registFwVersion(reqdto, principal.getName());
            if (result == null) {
                response.put("message", messageSource.getMessage("fw.api.messages.fwversionaddfailed", null,
                        LocaleContextHolder.getLocale()));
                return ResponseEntity.badRequest().body(response);
            }

            response.put("message", messageSource.getMessage("fw.api.messages.fwversionaddsuccess", null,
                    LocaleContextHolder.getLocale()));
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("[createFwVersion] error: {}", e.getMessage());
            response.put("message", messageSource.getMessage("fw.api.messages.fwversionaddfailed", null,
                    LocaleContextHolder.getLocale()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 펌웨어 버전 삭제
     */
    @DeleteMapping("/version/delete/{selectedFwId}")
    public ResponseEntity<String> deleteFwVersion(@PathVariable("selectedFwId") String fwId,
            Principal principal) {
        log.info("=== delete Firmware Version ID >> {}", fwId);

        if (fwId == null) {
            log.error("fwId is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fwId is null");
        }

        try {
            ResponseEntity<String> permissionCheck = this.comService.checkUserPermissions(principal,
                    MenuConstants.CORP);
            if (permissionCheck != null) {
                return permissionCheck;
            }

            fwService.deleteFwVersion(fwId);
            return ResponseEntity.ok(messageSource.getMessage("fw.api.messages.fwversiondeletesuccess", null,
                    LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            log.error("[deleteFwVersion] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("fw.api.messages.fwversiondeletefailed", null,
                            LocaleContextHolder.getLocale()));
        }
    }

    /**
     * 펌웨어업데이트 - 업체선택에 따른 모델리스트 조회
     */
    @RequestMapping("/model/list")
    public ResponseEntity<List<CpModelListDto>> getModelList(@RequestParam String companyId) {
        log.info("=== getModelList by companyId : {}", companyId);

        if (companyId == null || companyId.isEmpty()) {
            log.error("companyId is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        }

        try {
            List<CpModelListDto> modelList = cpModelService.findCpModelListByCompany(Long.parseLong(companyId));
            return ResponseEntity.ok(modelList);
        } catch (Exception e) {
            log.error("[getModelList] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    /**
     * 펌웨어업데이트 - 모델선택에 따른 버전리스트 조회
     */
    @RequestMapping("/version/list")
    public ResponseEntity<List<CpFwversionDto>> getVersionList(@RequestParam String companyId,
            @RequestParam String modelCode) {
        log.info("=== getVersionList by companyId : {}, modelCode : {}", companyId, modelCode);

        if (companyId == null || companyId.isEmpty() || modelCode == null || modelCode.isEmpty()) {
            log.error("companyId or modelCode is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        }

        try {
            List<CpFwversionDto> versionList = fwService.findFwVersionListByCompanyAndModel(Long.parseLong(companyId),
                    modelCode);
            return ResponseEntity.ok(versionList);
        } catch (Exception e) {
            log.error("[getVersionList] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    /**
     * 펌웨어업데이트 - 버전선택에 따른 url 조회
     */
    @RequestMapping("/update/url")
    public ResponseEntity<String> getUrl(@RequestParam String companyId, @RequestParam String modelCode,
            @RequestParam String version) {
        log.info("=== getUrl by companyId : {}, modelCode : {}, version : {}", companyId, modelCode, version);

        if (companyId == null || companyId.isEmpty() || modelCode == null || modelCode.isEmpty()
                || version == null || version.isEmpty()) {
            log.error("companyId or modelCode or version is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }

        try {
            String url = fwService.findFwUrlByCompanyAndModelAndVersion(Long.parseLong(companyId), modelCode, version);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            log.error("[getUrl] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }
    }

    @GetMapping("/update/get/cplist")
    public String showfwupdate(
            @RequestParam(value = "companyIdSearch", required = false) Long companyId,
            @RequestParam(value = "stationIdSearch", required = false) String stationId,
            @RequestParam(value = "companyIdFw", required = false) Long companyIdFw,
            @RequestParam(value = "modelSearch", required = false) String modelSearch,
            @RequestParam(value = "versionSearch", required = false) String versionSearch,
            @RequestParam(value = "urlSearch", required = false) String urlSearch,
            @RequestParam(value = "retries", required = false) String retries,
            @RequestParam(value = "retryInterval", required = false) String retryInterval,
            @RequestParam(value = "retrieveDate", required = false) String retrieveDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model, Principal principal) {
        log.info(
                "=== get fw update charger list >> companyId : {}, stationId : {}, companyIdFw : {}, modelSearch : {}, versionSearch : {}, urlSearch : {}, retries : {}, retryInterval : {}, retrieveDate : {}, page : {}, size : {}",
                companyId, stationId, companyIdFw, modelSearch, versionSearch, urlSearch, retries, retryInterval,
                retrieveDate, page, size);

        try {
            // 검색조건 저장
            model.addAttribute("selectedCompanyId", companyId);
            model.addAttribute("selectedStationId", stationId);

            // 펌웨어 선택 값들 저장
            model.addAttribute("selectedCompanyIdFw", companyIdFw);
            model.addAttribute("selectedModelSearch", modelSearch);
            model.addAttribute("selectedVersionSearch", versionSearch);
            model.addAttribute("selectedUrlSearch", urlSearch);
            model.addAttribute("selectedRetries", retries);
            model.addAttribute("selectedRetryInterval", retryInterval);
            model.addAttribute("selectedRetrieveDate", retrieveDate);

            // 충전기 list 조회
            Page<ChargerListDto> cplist = chargerService.findChargerListForFwUpdate(companyId, stationId, page, size);

            int totalpages = cplist.getTotalPages() == 0 ? 1 : cplist.getTotalPages();
            model.addAttribute("cpList", cplist.getContent());
            model.addAttribute("size", String.valueOf(size));
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalpages);
            model.addAttribute("totalCount", cplist.getTotalElements());

            // select options
            List<CompanyListDto> companyList = this.companyService.findCompanyListAll(principal.getName());
            model.addAttribute("companyList", companyList);

            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);

            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.FW_UPDATE);
            model.addAttribute("menuAuthority", menuAuthority);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("companyList", Collections.emptyList());
            model.addAttribute("showListCnt", Collections.emptyList());
            model.addAttribute("menuAuthority", Collections.emptyList());
        }

        return "pages/firmware/fw_update";
    }

}
