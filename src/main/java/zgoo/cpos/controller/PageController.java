package zgoo.cpos.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.code.CodeDto.CommCdBaseDto;
import zgoo.cpos.dto.code.CodeDto.CommCodeDto;
import zgoo.cpos.dto.code.CodeDto.GrpCodeDto;
import zgoo.cpos.dto.company.CompanyDto.CompanyListDto;
import zgoo.cpos.dto.company.CompanyDto.CompanyRegDto;
import zgoo.cpos.dto.users.UsersDto;
import zgoo.cpos.service.CodeService;
import zgoo.cpos.service.CompanyService;
import zgoo.cpos.service.UsersService;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PageController {

    private final CodeService codeService;
    private final CompanyService companyService;
    private final UsersService usersService;

    /*
     * 대시보드
     */
    @GetMapping("/dashboard")
    public String showdashboard(Model model) {
        log.info("Dashboard Home");
        // 필요한 data를 model에 추가 !!!

        return "pages/dashboard";
    }

    /*
     * 지도
     */
    @GetMapping("/map")
    public String showmap(Model model) {
        log.info("=== Map Page ===");
        return "pages/map/map";
    }

    /*
     * 회원관리 > 회원리스트
     */
    @GetMapping("/member/list")
    public String showmemlist(Model model) {
        log.info("=== Member List Page ===");
        return "pages/member/member_list";
    }

    /*
     * 회원관리 > 회원인증내역
     */
    @GetMapping("/member/authentication/list")
    public String showauthlist(Model model) {
        log.info("=== Member Authorization List Page ===");
        return "pages/member/member_authentication";
    }

    /*
     * 충전소관리 > 충전소리스트
     */
    @GetMapping("/station/list")
    public String showstationlist(Model model) {
        log.info("=== Charging Station List Page ===");
        return "pages/charge/cs_list";
    }

    /*
     * 충전기관리 > 충전기리스트
     */
    @GetMapping("/charger/list")
    public String showchargerlist(Model model) {
        log.info("=== Charger List Page ===");
        return "pages/charge/cp_list";
    }

    /*
     * 실시간충전리스트
     */
    @GetMapping("/charging/list")
    public String showcharginglist(Model model) {
        log.info("=== Charging List Page ===");
        return "pages/charge/cp_real_time_list";
    }

    /*
     * 시스템 > 모델관리
     */
    @GetMapping("/system/model/list")
    public String showmodellist(Model model) {
        log.info("=== Model List Page ===");
        return "pages/system/model_management";
    }

    /*
     * 시스템 > 공통코드관리
     */
    @GetMapping("/system/code/list")
    public String showcodelist(Model model) {
        log.info("=== Code Management List Page ===");

        // group code 등록폼 전달
        model.addAttribute("grpcodeDto", new GrpCodeDto());
        // commoncode 등록폼 전달
        model.addAttribute("commonCdDto", new CommCodeDto());

        try {
            // 그룹코드 조회
            List<GrpCodeDto> gcdlist = codeService.findGrpCodeAll();
            model.addAttribute("gcdlist", gcdlist);

            log.info("== grpcode list found : ", gcdlist.size());

            // 공통코드 조회
            List<CommCodeDto> ccdlist = codeService.findCommonCodeAll();
            model.addAttribute("ccdlist", ccdlist);

            log.info("== commCode list found : ", ccdlist.size());

        } catch (Exception e) {

            log.error("Error occurred while fetching code list: {}", e.getMessage(), e);
            model.addAttribute("gcdlist", Collections.emptyList());
            model.addAttribute("ccdlist", Collections.emptyList());
        }

        return "pages/system/code_management";
    }

    /*
     * 시스템 > 사용자 관리
     */

    @GetMapping("/system/user/list")
    public String showuserlist(
        @RequestParam(value = "companyIdSearch", required = false) Long companyId,
        @RequestParam(value = "companyTypeSearch", required = false) String companyType,
        @RequestParam(value = "nameSearch", required = false) String name,
        @RequestParam(value = "page", defaultValue="0") int page,
        @RequestParam(value = "size", defaultValue="10") int size,
        Model model) {

        log.info("=== User List Page ===");
        log.info("companyId: {}, companyType: {}, name: {}", companyId, companyType, name);
        model.addAttribute("usersDto", new UsersDto.UsersRegDto());
       
        /*
         * required = false일 때 요청 파라미터 값이 없으면 null를 저장해야 하는데
         * companyType, name 값이 공백으로 처리되는 문제발생(companyId는 정상적으로 처리)
         * null 값이 아닌 공백이 들어왔을 경우 null 처리
         */
        if (companyType != null && companyType.isEmpty()) {
            companyType = null;
        }

        if (name != null && name.trim().isEmpty()) {
            name = null;
        }

        try {
            log.info("=== user DB search result >>>");

            // 사업자 list
            List<CompanyListDto> companyList = companyService.searchCompanyAll();
            model.addAttribute("companyList", companyList);
            
            // 사용자 list
            Page<UsersDto.UsersListDto> userList;

            if(companyId == null && companyType == null && name == null) {  // 검색 조건이 없으면 전체 조회
                userList = usersService.findUsersAll(page, size);
            } else {    // 검색 조건이 1개 이상 존재할 경우
                userList = usersService.searchUsersListWithPagination(companyId, companyType, name, page, size);
            }

            // 검색 조건 저장
            model.addAttribute("selectedCompanyId", companyId);
            model.addAttribute("selectedCompanyType", companyType);
            model.addAttribute("selectedName", name);

            int totalPages = userList.getTotalPages() == 0 ? 1 : userList.getTotalPages();  // 전체 페이지 수

            model.addAttribute("ulist", userList.getContent());               // 사용자 list
            model.addAttribute("size", String.valueOf(size));                 // 페이지당 보여지는 데이터 건 수
            model.addAttribute("currentPage", page);                          // 현재 페이지
            model.addAttribute("totalPages", totalPages);                     // 총 페이지 수
            model.addAttribute("totalCount", userList.getTotalElements());    // 총 데이터

            List<CommCdBaseDto> authList = codeService.findCommonCdNamesByGrpcd("MENUACCLV");   // 메뉴권한
            model.addAttribute("authList", authList);
            // log.info("== authList : {}", authList.toString());
            
            List<CommCdBaseDto> coKind = codeService.findCommonCdNamesByGrpcd("COKIND");        // 사업자 유형
            model.addAttribute("coKind", coKind);

            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 메뉴권한
            model.addAttribute("showListCnt", showListCnt);

        } catch (Exception e) {
            log.error("Error occurred while fetching user list: {}", e.getMessage(), e);
            model.addAttribute("companyList", Collections.emptyList());
            model.addAttribute("ulist", Collections.emptyList());
            model.addAttribute("currentPage", Collections.emptyList());
            model.addAttribute("totalPages", Collections.emptyList());
            model.addAttribute("totalCount", Collections.emptyList());
            model.addAttribute("authList", Collections.emptyList());
            model.addAttribute("coKind", Collections.emptyList());
            model.addAttribute("showListCnt", Collections.emptyList());
        }

        return "pages/system/user_management";
    }

    /*
     * 시스템 > 공지사항관리
     */
    @GetMapping("/system/notice/list")
    public String shownoticelist(Model model) {
        log.info("=== Notice List Page ===");
        return "pages/system/notice_management";
    }

    /*
     * 시스템 > 메뉴접근권한관리
     */
    @GetMapping("/system/authority/list")
    public String showautoritylist(Model model) {
        log.info("=== Authority List Page ===");
        return "pages/system/authority_management";
    }

    /*
     * 시스템 > 에러코드관리
     */
    @GetMapping("/system/errcode/list")
    public String showerrcodelist(Model model) {
        log.info("=== Errcode List Page ===");
        return "pages/system/errcode_management";
    }

    /*
     * 시스템 > 요금제관리
     */
    @GetMapping("/system/tariff/list")
    public String showtarifflist(Model model) {
        log.info("=== Tariff List Page ===");
        return "pages/system/tariff_management";
    }

    /*
     * 유지보수 > 장애관리
     */
    @GetMapping("/maintenance/errlist")
    public String showerrlist(Model model) {
        log.info("=== Maintenance Error List Page ===");
        return "pages/maintenance/error_management";
    }

    /*
     * 제어 > 충전기제어
     */
    @GetMapping("/control/charger/list")
    public String showcontrollist(Model model) {
        log.info("=== Charger Control List Page ===");
        return "pages/control/cp_control";
    }

    /*
     * 고객센터 > 1:1 문의
     */
    @GetMapping("/voc")
    public String showvoclist(Model model) {
        log.info("=== VOC List Page ===");
        return "pages/customer/voc";
    }

    /*
     * 고객센터 > FAQ 관리
     */
    @GetMapping("/faq")
    public String showfaqlist(Model model) {
        log.info("=== FAQ List Page ===");
        return "pages/customer/faq";
    }

    /*
     * 이력 > 충전이력
     */

    /*
     * 이력 > 결제이력
     */

    /*
     * 이력 > 통신이력
     */

    /*
     * 이력 > 에러이력
     */

    /*
     * 정산 > 매출정산
     */

    /*
     * 정산 > 매입관리
     */

    /*
     * 통계 > 매출통계
     */

    /*
     * 통계 > 이용률통계
     */

    /*
     * 통계 > 충전량통계
     */

    /*
     * 통계 > 장애율통계
     */

    /*
     * 업체관리 > 사업자관리
     */
    @GetMapping("/biz/list")
    public String showbizlist(Model model) {
        log.info("=== Biz managment List Page ===");

        // 업체 등록폼 전달
        model.addAttribute("companyRegDto", new CompanyRegDto());
        // 업체 로밍정보 등록폼 전달

        try {
            log.info("=== DB search result >>>");
            // 사업자 list
            List<CompanyListDto> flist = companyService.searchCompanyAll();
            log.info("=== companyListDto : {}", flist.toString());
            model.addAttribute("companyList", flist);

            List<CommCdBaseDto> lvList = codeService.findCommonCdNamesByGrpcd("COLV"); // 사업자레벨
            log.info("== lvList : {}", lvList.toString());
            model.addAttribute("clvlist", lvList);

            List<CommCdBaseDto> coKindList = codeService.findCommonCdNamesByGrpcd("COKIND"); // 사업자유형(위탁,충전)
            log.info("=== coKindList : {}", coKindList.toString());
            model.addAttribute("ckindlist", coKindList);

            List<CommCdBaseDto> biztypeList = codeService.findCommonCdNamesByGrpcd("BIZTYPECD"); // 사업자구분(법인/개인)
            log.info("=== biztypelist : {}", biztypeList.toString());
            model.addAttribute("biztypelist", biztypeList);

            List<CommCdBaseDto> bizkindList = codeService.findCommonCdNamesByGrpcd("BIZKIND"); // 업종(제조업)
            log.info("=== bizkindlist : {}", bizkindList.toString());
            model.addAttribute("bizkindlist", bizkindList);

            List<CommCdBaseDto> consignmentList = codeService.findCommonCdNamesByGrpcd("CONSIGNMENTCD"); // 결제위탁여부
            log.info("=== consignmentList : {}", consignmentList.toString());
            model.addAttribute("consignmentList", consignmentList);

            // 계약상태
            List<CommCdBaseDto> contractStatList = codeService.findCommonCdNamesByGrpcd("CONSTAT");
            log.info("=== contractStatList : {}", contractStatList.toString());
            model.addAttribute("contractStatList", contractStatList);

            // 유지보수업체
            List<CommCdBaseDto> mcompanyList = codeService.findCommonCdNamesByGrpcd("MCOMPANY");
            log.info("=== mcompanyList : {}", mcompanyList.toString());
            model.addAttribute("mcompanyList", mcompanyList);

        } catch (Exception e) {

            log.error("Error occurred while fetching company list: {}", e.getMessage(), e);
        }

        return "pages/biz/biz_management";
    }

    /*
     * 업체관리 > 법인관리
     */
    @GetMapping("/corp/list")
    public String showcorplist(Model model) {
        log.info("=== Corp managment List Page ===");
        return "pages/biz/corporation_management";
    }

    /*
     * 펌웨어 > 펌웨어 버전관리
     */

    /*
     * 펌웨어 > 펌웨어 업데이트
     */

    /*
     * 예약
     */
}
