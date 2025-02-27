package zgoo.cpos.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.biz.BizInfoDto.BizInfoListDto;
import zgoo.cpos.dto.biz.BizInfoDto.BizInfoRegDto;
import zgoo.cpos.dto.code.ChgErrorCodeDto;
import zgoo.cpos.dto.code.ChgErrorCodeDto.ChgErrorCodeListDto;
import zgoo.cpos.dto.code.CodeDto.CommCdBaseDto;
import zgoo.cpos.dto.code.CodeDto.CommCodeDto;
import zgoo.cpos.dto.code.CodeDto.GrpCodeDto;
import zgoo.cpos.dto.company.CompanyDto.BaseCompnayDto;
import zgoo.cpos.dto.company.CompanyDto.CompanyListDto;
import zgoo.cpos.dto.company.CompanyDto.CompanyRegDto;
import zgoo.cpos.dto.company.CompanyDto.CpPlanDto;
import zgoo.cpos.dto.cp.ChargerDto.ChargerListDto;
import zgoo.cpos.dto.cp.ChargerDto.ChargerRegDto;
import zgoo.cpos.dto.cp.ChargerDto.ConnectorStatusDto;
import zgoo.cpos.dto.cp.CpModelDto;
import zgoo.cpos.dto.cp.CpModelDto.CpModelListDto;
import zgoo.cpos.dto.cs.CsInfoDto;
import zgoo.cpos.dto.cs.CsInfoDto.CsInfoListDto;
import zgoo.cpos.dto.member.MemberDto;
import zgoo.cpos.dto.member.MemberDto.MemberListDto;
import zgoo.cpos.dto.member.VocDto;
import zgoo.cpos.dto.member.VocDto.VocListDto;
import zgoo.cpos.dto.menu.CompanyMenuAuthorityDto;
import zgoo.cpos.dto.menu.MenuAuthorityDto;
import zgoo.cpos.dto.menu.MenuDto;
import zgoo.cpos.dto.tariff.TariffDto.TariffPolicyDto;
import zgoo.cpos.dto.tariff.TariffDto.TariffRegDto;
import zgoo.cpos.dto.users.FaqDto;
import zgoo.cpos.dto.users.NoticeDto;
import zgoo.cpos.dto.users.UsersDto;
import zgoo.cpos.service.BizService;
import zgoo.cpos.service.ChargerService;
import zgoo.cpos.service.ChgErrorCodeService;
import zgoo.cpos.service.CodeService;
import zgoo.cpos.service.CompanyService;
import zgoo.cpos.service.CpModelService;
import zgoo.cpos.service.CsService;
import zgoo.cpos.service.FaqService;
import zgoo.cpos.service.MemberService;
import zgoo.cpos.service.MenuAuthorityService;
import zgoo.cpos.service.MenuService;
import zgoo.cpos.service.NoticeService;
import zgoo.cpos.service.TariffService;
import zgoo.cpos.service.UsersService;
import zgoo.cpos.service.VocService;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PageController {

    private final CodeService codeService;
    private final CompanyService companyService;
    private final UsersService usersService;
    private final MenuService menuService;
    private final MenuAuthorityService menuAuthorityService;
    private final NoticeService noticeService;
    private final FaqService faqService;
    private final CsService csService;
    private final CpModelService cpModelService;
    private final ChgErrorCodeService chgErrorCodeService;
    private final BizService bizService;
    private final TariffService tariffService;
    private final MemberService memberService;
    private final VocService vocService;
    private final ChargerService chargerService;

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
    public String showmemlist(
            @RequestParam(value = "companyIdSearch", required = false) Long companyId,
            @RequestParam(value = "idTagSearch", required = false) String idTag,
            @RequestParam(value = "nameSearch", required = false) String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        log.info("=== Member List Page ===");
        model.addAttribute("memRegDto", new MemberDto.MemberRegDto());

        try {
            // 회원리스트
            Page<MemberListDto> memberList = this.memberService.findMemberInfoWithPagination(companyId, idTag, name,
                    page, size);

            // 검색 조건 저장
            model.addAttribute("selectedCompanyId", companyId);
            model.addAttribute("selectedIdTag", idTag);
            model.addAttribute("selectedName", name);

            int totalPages = memberList.getTotalPages() == 0 ? 1 : memberList.getTotalPages(); // 전체 페이지 수

            model.addAttribute("memberList", memberList.getContent()); // 에러코드 list
            model.addAttribute("size", String.valueOf(size)); // 페이지당 보여지는 데이터 건 수
            model.addAttribute("currentPage", page); // 현재 페이지
            model.addAttribute("totalPages", totalPages); // 총 페이지 수
            model.addAttribute("totalCount", memberList.getTotalElements()); // 총 데이터

            List<CompanyListDto> companyList = this.companyService.findCompanyListAll(); // 사업자 list
            model.addAttribute("companyList", companyList);
            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);
            List<CommCdBaseDto> memStatList = codeService.commonCodeStringToNum("MEMSTATCD"); // 회원상태코드
            model.addAttribute("memStatList", memStatList);
            List<CommCdBaseDto> bizTypeList = codeService.commonCodeStringToNum("BIZTYPECD"); // 사업자구분코드
            model.addAttribute("bizTypeList", bizTypeList);
            List<CommCdBaseDto> creditCardList = codeService.commonCodeStringToNum("CREDITCARDCD"); // 카드사코드
            model.addAttribute("creditCardList", creditCardList);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("memberList", Collections.emptyList());
            model.addAttribute("size", Collections.emptyList());
            model.addAttribute("currentPage", Collections.emptyList());
            model.addAttribute("totalPages", Collections.emptyList());
            model.addAttribute("totalCount", Collections.emptyList());
            model.addAttribute("companyList", Collections.emptyList());
            model.addAttribute("showListCnt", Collections.emptyList());
            model.addAttribute("memStatList", Collections.emptyList());
            model.addAttribute("bizTypeList", Collections.emptyList());
            model.addAttribute("creditCardList", Collections.emptyList());
        }
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
    public String showstationlist(
            @RequestParam(value = "companyIdSearch", required = false) Long companyId,
            @RequestParam(value = "opSearch", required = false) String searchOp,
            @RequestParam(value = "contentSearch", required = false) String searchContent,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        log.info("=== Charging Station List Page ===");
        log.info("companyId: {}, searchOp: {}, searchContent: {}", companyId, searchOp, searchContent);
        model.addAttribute("csRegDto", new CsInfoDto.CsInfoRegDto());

        try {
            // 충전소 list
            Page<CsInfoListDto> csList;
            if (companyId == null && searchOp == null && searchContent == null) {
                csList = this.csService.findCsInfoAll(page, size);
            } else {
                csList = this.csService.searchCsInfoListWithPagination(companyId, searchOp, searchContent, page, size);
            }

            // 검색 조건 저장
            model.addAttribute("selectedCompanyId", companyId);
            model.addAttribute("selectedOpSearch", searchOp);
            model.addAttribute("selectedContentSearch", searchContent);

            int totalPages = csList.getTotalPages() == 0 ? 1 : csList.getTotalPages(); // 전체 페이지 수

            model.addAttribute("csList", csList.getContent()); // 충전소 list
            model.addAttribute("size", String.valueOf(size)); // 페이지당 보여지는 데이터 건 수
            model.addAttribute("currentPage", page); // 현재 페이지
            model.addAttribute("totalPages", totalPages); // 총 페이지 수
            model.addAttribute("totalCount", csList.getTotalElements()); // 총 데이터

            // 사업자 list(select option)
            List<CompanyListDto> companyList = this.companyService.findCompanyListAll();
            model.addAttribute("companyList", companyList);
            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);
            List<CommCdBaseDto> csFacility = codeService.commonCodeStringToNum("CSFACILITY"); // 충전소시설유형
            model.addAttribute("csFacility", csFacility);
            List<CommCdBaseDto> csFSub = codeService.commonCodeStringToNum("CSFSUB"); // 충전소시설구분
            model.addAttribute("csFSub", csFSub);
            List<CommCdBaseDto> opStepCd = codeService.commonCodeStringToNum("OPSTEPCD"); // 운영단계분류
            model.addAttribute("opStepCd", opStepCd);
            List<CommCdBaseDto> landType = codeService.commonCodeStringToNum("LANDTYPE"); // 부지구분
            model.addAttribute("landType", landType);
            List<CommCdBaseDto> faucetType = codeService.commonCodeStringToNum("FAUCETTYPE"); // 수전방식
            model.addAttribute("faucetType", faucetType);
            List<CommCdBaseDto> powerType = codeService.commonCodeStringToNum("POWERTYPE"); // 전압종류
            model.addAttribute("powerType", powerType);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("csList", Collections.emptyList());
            model.addAttribute("currentPage", Collections.emptyList());
            model.addAttribute("totalPages", Collections.emptyList());
            model.addAttribute("totalCount", Collections.emptyList());
            model.addAttribute("companyList", Collections.emptyList());
            model.addAttribute("showListCnt", Collections.emptyList());
            model.addAttribute("csFacility", Collections.emptyList());
            model.addAttribute("opStepCd", Collections.emptyList());
            model.addAttribute("landType", Collections.emptyList());
            model.addAttribute("faucetType", Collections.emptyList());
            model.addAttribute("powerType", Collections.emptyList());
        }

        return "pages/charge/cs_list";
    }

    /*
     * 충전기관리 > 충전기리스트
     */
    @GetMapping("/charger/list")
    public String showchargerlist(@RequestParam(value = "companyIdSearch", required = false) Long reqCompanyId,
            @RequestParam(value = "manfCodeSearch", required = false) String reqManfCd,
            @RequestParam(value = "opSearch", required = false) String reqOpSearch,
            @RequestParam(value = "contentSearch", required = false) String reqSearchContent,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        log.info("=== Charger List Page ===");
        log.info("== companyId: {}, page: {}, size: {}", reqCompanyId, page, size);

        // 충전기 등록폼 전달
        model.addAttribute("chargerRegDto", new ChargerRegDto());

        Page<ChargerListDto> chargerList;

        try {
            log.info("=== Charger DB search result >>>");

            // charger list 조회
            // 검색조건 check
            if (reqCompanyId == null && reqManfCd == null && reqOpSearch == null) {
                log.info("Search all charger list >>");
                chargerList = chargerService.searchCpListPageAll(page, size);

            } else {
                log.info("Search charger list by options:companyid:{},manfcode:{},op:{},content:{} >>",
                        reqCompanyId, reqManfCd, reqOpSearch, reqSearchContent);
                chargerList = chargerService.searchCpListPage(reqCompanyId, reqManfCd, reqOpSearch, reqSearchContent,
                        page, size);
            }

            // page 처리
            int totalPages = chargerList.getTotalPages() == 0 ? 1 : chargerList.getTotalPages();
            model.addAttribute("chargerList", chargerList.getContent());
            model.addAttribute("size", String.valueOf(size));
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalCount", chargerList.getTotalElements());
            log.info("===ChargerList_PageInfo >> totalPages:{}, totalCount:{}", totalPages,
                    chargerList.getTotalElements());

            // 충전기 커넥터 상태 리스트 조회
            List<ConnectorStatusDto> connStatList = chargerService.searchConStatListAll();
            Map<String, String> connStatusMap = connStatList.stream()
                    .collect(Collectors.groupingBy(ConnectorStatusDto::getChargerId,
                            Collectors.mapping(ConnectorStatusDto::getStatus, Collectors.joining(","))));
            model.addAttribute("connStatusMap", connStatusMap);
            log.info("=== connector_status map: {}", connStatusMap); // Map 전체 로깅

            // 검색 select options 조회
            // 1.사업자 리스트
            List<BaseCompnayDto> companyList = companyService.searchAllCompanyForSelectOpt();
            model.addAttribute("companyList", companyList);
            // 2.grid row count
            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT");
            model.addAttribute("showListCnt", showListCnt);
            // 3.제조사 리스트
            List<CommCdBaseDto> manfCd = codeService.commonCodeStringToNum("CGMANFCD");
            model.addAttribute("manfCdList", manfCd);
            // 4.공용구분
            List<CommCdBaseDto> commonTypeList = codeService.commonCodeStringToNum("CGCOMMONCD");
            model.addAttribute("commonTypeList", commonTypeList);
            // 5.충전기 미사용 사유
            List<CommCdBaseDto> reasonList = codeService.commonCodeStringToNum("NOTUSINGRSN");
            model.addAttribute("reasonList", reasonList);
            // 6.모뎀통신사
            List<CommCdBaseDto> modemCorpList = codeService.commonCodeStringToNum("MODEMCORP");
            model.addAttribute("modemCorpList", modemCorpList);
            // 7.모뎀계약상태
            List<CommCdBaseDto> modemContractOptionList = codeService.commonCodeStringToNum("MODEMCTCD");
            model.addAttribute("modemContractOptionList", modemContractOptionList);

        } catch (Exception e) {
            log.error("Error occurered while fetching charger list: {}", e.getMessage(), e);

            chargerList = Page.empty();
            model.addAttribute("companyList", Collections.emptyList());
            model.addAttribute("showListCnt", Collections.emptyList());
            model.addAttribute("manfCdList", Collections.emptyList());
            model.addAttribute("commonTypeList", Collections.emptyList());
            model.addAttribute("reasonList", Collections.emptyList());
            model.addAttribute("modemCorpList", Collections.emptyList());
            model.addAttribute("modemContractOptionList", Collections.emptyList());
        }

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
    public String showmodellist(
            @RequestParam(value = "companyIdSearch", required = false) Long companyId,
            @RequestParam(value = "manfCdSearch", required = false) String manfCode,
            @RequestParam(value = "chgSpeedCdSearch", required = false) String chgSpeedCode,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        log.info("=== Model List Page ===");
        model.addAttribute("cpModelDto", new CpModelDto.CpModelRegDto());

        try {
            // 충전기 모델 list
            Page<CpModelListDto> modelList;
            if (companyId == null && manfCode == null && chgSpeedCode == null) {
                modelList = this.cpModelService.findCpModelAll(page, size);
            } else {
                modelList = this.cpModelService.searchCpModelWithPagination(companyId, manfCode, chgSpeedCode, page,
                        size);
            }

            model.addAttribute("selectedCompanyId", companyId);
            model.addAttribute("selectedManfCd", manfCode);
            model.addAttribute("selectedChgSpeedCd", chgSpeedCode);

            int totalPages = modelList.getTotalPages() == 0 ? 1 : modelList.getTotalPages(); // 전체 페이지 수

            model.addAttribute("modelList", modelList.getContent()); // 충전기 모델 list
            model.addAttribute("size", String.valueOf(size)); // 페이지당 보여지는 데이터 건 수
            model.addAttribute("currentPage", page); // 현재 페이지
            model.addAttribute("totalPages", totalPages); // 총 페이지 수
            model.addAttribute("totalCount", modelList.getTotalElements()); // 총 데이터

            // 사업자 list(select option)
            List<CompanyListDto> companyList = this.companyService.findCompanyListAll();
            model.addAttribute("companyList", companyList);
            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);
            List<CommCdBaseDto> manfCd = codeService.commonCodeStringToNum("CGMANFCD"); // 충전기제조사
            model.addAttribute("manfCd", manfCd);
            List<CommCdBaseDto> chgTypeCd = codeService.commonCodeStringToNum("CHGINTTYPECD"); // 충전기설치유형
            model.addAttribute("chgTypeCd", chgTypeCd);
            List<CommCdBaseDto> chgSpeedCd = codeService.commonCodeStringToNum("CHGSPEEDCD"); // 충전기속도구분(충전기유형)
            model.addAttribute("chgSpeedCd", chgSpeedCd);
            List<CommCdBaseDto> connType = codeService.commonCodeStringToNum("CONNTYPE"); // 커넥터타입
            model.addAttribute("connType", connType);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("modelList", Collections.emptyList());
            model.addAttribute("currentPage", Collections.emptyList());
            model.addAttribute("totalPages", Collections.emptyList());
            model.addAttribute("totalCount", Collections.emptyList());
            model.addAttribute("companyList", Collections.emptyList());
            model.addAttribute("showListCnt", Collections.emptyList());
            model.addAttribute("manfCd", Collections.emptyList());
            model.addAttribute("chgTypeCd", Collections.emptyList());
            model.addAttribute("chgSpeedCd", Collections.emptyList());
            model.addAttribute("connType", Collections.emptyList());
        }
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
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
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
            List<CompanyListDto> companyList = this.companyService.findCompanyListAll();
            model.addAttribute("companyList", companyList);

            // 사용자 list
            Page<UsersDto.UsersListDto> userList;

            if (companyId == null && companyType == null && name == null) { // 검색 조건이 없으면 전체 조회
                userList = usersService.findUsersAll(page, size);
            } else { // 검색 조건이 1개 이상 존재할 경우
                userList = usersService.searchUsersListWithPagination(companyId, companyType, name, page, size);
            }

            // 검색 조건 저장
            model.addAttribute("selectedCompanyId", companyId);
            model.addAttribute("selectedCompanyType", companyType);
            model.addAttribute("selectedName", name);

            int totalPages = userList.getTotalPages() == 0 ? 1 : userList.getTotalPages(); // 전체 페이지 수

            model.addAttribute("ulist", userList.getContent()); // 사용자 list
            model.addAttribute("size", String.valueOf(size)); // 페이지당 보여지는 데이터 건 수
            model.addAttribute("currentPage", page); // 현재 페이지
            model.addAttribute("totalPages", totalPages); // 총 페이지 수
            model.addAttribute("totalCount", userList.getTotalElements()); // 총 데이터

            List<CommCdBaseDto> authList = codeService.findCommonCdNamesByGrpcd("MENUACCLV"); // 메뉴권한
            model.addAttribute("authList", authList);

            List<CommCdBaseDto> coKind = codeService.findCommonCdNamesByGrpcd("COKIND"); // 사업자 유형
            model.addAttribute("coKind", coKind);

            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
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
    public String shownoticelist(
            @RequestParam(value = "companyIdSearch", required = false) Long companyId,
            @RequestParam(value = "startDateSearch", required = false) String startDate,
            @RequestParam(value = "endDateSearch", required = false) String endDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        log.info("=== Notice List Page ===");
        log.info("companyId: {}, startDates: {}, endDate: {}", companyId, startDate, endDate);

        model.addAttribute("noticeDto", new NoticeDto.NoticeRegDto());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDateSearch = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate, formatter)
                : null;
        LocalDate endDateSearch = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate, formatter) : null;

        try {
            // 사업자 list
            List<CompanyListDto> companyList = this.companyService.findCompanyListAll();
            model.addAttribute("companyList", companyList);

            Page<NoticeDto.NoticeListDto> noticeList;

            if (companyId == null && startDate == null && endDate == null) {
                noticeList = this.noticeService.findNoticeAll(page, size);
            } else {
                noticeList = this.noticeService.searchNoticeListwithPagination(companyId, startDateSearch,
                        endDateSearch, page, size);
            }

            int totalPages = noticeList.getTotalPages() == 0 ? 1 : noticeList.getTotalPages();

            // 검색 조건 저장
            model.addAttribute("selectedCompanyId", companyId);
            model.addAttribute("selectedStartDate", startDate);
            model.addAttribute("selectedEndDate", endDate);

            // pagination
            model.addAttribute("noticeList", noticeList.getContent());
            model.addAttribute("size", String.valueOf(size));
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalCount", noticeList.getTotalElements());

            List<CommCdBaseDto> noticeTypeList = codeService.commonCodeStringToNum("NOTICETYPECD"); // 공지유형
            model.addAttribute("noticeTypeList", noticeTypeList);

            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);

        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("companyList", Collections.emptyList());
            model.addAttribute("noticeList", Collections.emptyList());
            model.addAttribute("size", Collections.emptyList());
            model.addAttribute("currentPage", Collections.emptyList());
            model.addAttribute("totalPages", Collections.emptyList());
            model.addAttribute("totalCount", Collections.emptyList());
            model.addAttribute("showListCnt", Collections.emptyList());
        }

        return "pages/system/notice_management";
    }

    /*
     * 시스템 > 메뉴 관리
     */
    @GetMapping("/system/menu/list")
    public String showmenulist(
            @RequestParam(value = "companyNameSearch", required = false) String companyName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        log.info("=== Menu List Page ===");

        model.addAttribute("menuRegDto", new MenuDto.MenuRegDto());

        log.info("companyName: {}", companyName);
        try {
            // 메뉴 list
            List<MenuDto.MenuListDto> menuList = menuService.findMenuList();
            model.addAttribute("menuList", menuList);

            // 사업자 list(select option)
            List<CompanyListDto> companyList = this.companyService.findCompanyListAll();
            model.addAttribute("companyList", companyList);

            // 사업장별 메뉴 접근 권한 list
            // List<CompanyMenuAuthorityDto.CompanyMenuRegDto> companyMenuList =
            // this.menuService.findCompanyDistinctList();
            // model.addAttribute("companyMenuList", companyMenuList);
            Page<CompanyMenuAuthorityDto.CompanyMenuRegDto> companyMenuList;

            if (companyName == null || companyName.isEmpty()) {
                log.info("[findCompanyMenuAll] companyName: {}", companyName);
                companyMenuList = this.menuService.findCompanyMenuAll(page, size);
            } else {
                log.info("[searchCompanyMenuWithPagination] companyName: {}", companyName);
                companyMenuList = this.menuService.searchCompanyMenuWithPagination(companyName, page, size);
            }

            int totalPages = companyMenuList.getTotalPages() == 0 ? 1 : companyMenuList.getTotalPages();

            // 검색 조건 저장
            model.addAttribute("selectedCompanyName", companyName);

            // pagination
            model.addAttribute("companyMenuList", companyMenuList.getContent());
            model.addAttribute("size", String.valueOf(size));
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalCount", companyMenuList.getTotalElements());

            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);

            // modal에서 보여주는 메뉴 list
            List<MenuDto.MenuAuthorityListDto> companyMenuModalList = this.menuService.findMenuListWithParentName();
            model.addAttribute("companyMenuModalList", companyMenuModalList);

        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("menuList", Collections.emptyList());
            model.addAttribute("companyList", Collections.emptyList());
            model.addAttribute("companyMenuList", Collections.emptyList());
            model.addAttribute("size", Collections.emptyList());
            model.addAttribute("currentPage", Collections.emptyList());
            model.addAttribute("totalPages", Collections.emptyList());
            model.addAttribute("totalCount", Collections.emptyList());
            model.addAttribute("showListCnt", Collections.emptyList());
            model.addAttribute("companyMenuModalList", Collections.emptyList());
        }

        return "pages/system/menu_management";
    }

    /*
     * 시스템 > 메뉴접근권한관리
     */
    @GetMapping("/system/authority/list")
    public String showautoritylist(Model model) {
        log.info("=== Authority List Page ===");

        // model.addAttribute("companyAuthorityList", new
        // MenuAuthorityDto.CompanyAuthorityListDto());

        try {
            // 사업자 권한 list
            List<MenuAuthorityDto.CompanyAuthorityListDto> companyAuthorityList = menuAuthorityService
                    .findCompanyAuthorityList();
            // log.info("사업자 권한 확인 >> {}", companyAuthorityList.toString());
            model.addAttribute("companyAuthorityList", companyAuthorityList);

        } catch (Exception e) {
            e.getStackTrace();
        }
        return "pages/system/authority_management";
    }

    /*
     * 시스템 > 에러코드관리
     */
    @GetMapping("/system/errcode/list")
    public String showerrcodelist(
            @RequestParam(value = "manfCdSearch", required = false) String manfCode,
            @RequestParam(value = "opSearch", required = false) String searchOp,
            @RequestParam(value = "contentSearch", required = false) String searchContent,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        log.info("=== Errcode List Page ===");
        log.info("manfCode: {}, searchOp: {}, searchContent: {}", manfCode, searchOp, searchContent);
        model.addAttribute("errCdDto", new ChgErrorCodeDto.ChgErrorCodeRegDto());

        try {
            // 에러코드 list
            Page<ChgErrorCodeListDto> errcdList = this.chgErrorCodeService.findErrorCodeWithPagination(manfCode,
                    searchOp, searchContent, page, size);

            // 검색 조건 저장
            model.addAttribute("selectedManfCd", manfCode);
            model.addAttribute("selectedOpSearch", searchOp);
            model.addAttribute("selectedContentSearch", searchContent);

            int totalPages = errcdList.getTotalPages() == 0 ? 1 : errcdList.getTotalPages(); // 전체 페이지 수

            model.addAttribute("errcdList", errcdList.getContent()); // 에러코드 list
            model.addAttribute("size", String.valueOf(size)); // 페이지당 보여지는 데이터 건 수
            model.addAttribute("currentPage", page); // 현재 페이지
            model.addAttribute("totalPages", totalPages); // 총 페이지 수
            model.addAttribute("totalCount", errcdList.getTotalElements()); // 총 데이터

            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);
            List<CommCdBaseDto> manfCd = codeService.commonCodeStringToNum("CGMANFCD"); // 충전기제조사
            model.addAttribute("manfCd", manfCd);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("errcdList", Collections.emptyList());
            model.addAttribute("currentPage", Collections.emptyList());
            model.addAttribute("totalPages", Collections.emptyList());
            model.addAttribute("totalCount", Collections.emptyList());
            model.addAttribute("showListCnt", Collections.emptyList());
            model.addAttribute("manfCd", Collections.emptyList());
        }
        return "pages/system/errcode_management";
    }

    /*
     * 시스템 > 요금제관리
     */
    @GetMapping("/system/tariff/list")
    public String showtarifflist(@RequestParam(value = "companyIdSearch", required = false) Long companyId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        log.info("=== Tariff List Page ===");
        log.info("== companyId: {}, page: {}, size: {}", companyId, page, size);

        // 요금제 등록폼 전달
        model.addAttribute("tariffRegDto", new TariffRegDto());

        Page<TariffPolicyDto> tariffpolicyList;

        try {

            log.info("=== Tariff DB search result >>>");

            // tariff policy list 조회
            // check null and call the approrpiate search method
            if (companyId != null) {
                log.info("Searching by companyId: {}", companyId);
                tariffpolicyList = tariffService.searchTariffPolicyByCompanyId(page, size, companyId);
            } else {
                log.info("Fetching all Tariff >> ");
                tariffpolicyList = tariffService.searchTariffPolicyAll(page, size);

            }

            // page 처리
            int totalPages = tariffpolicyList.getTotalPages() == 0 ? 1 : tariffpolicyList.getTotalPages(); // 전체 페이지 수
            model.addAttribute("tariffpolicyList", tariffpolicyList.getContent()); // 사용자 list
            model.addAttribute("size", String.valueOf(size)); // 페이지당 보여지는 데이터 건 수
            model.addAttribute("currentPage", page); // 현재 페이지
            model.addAttribute("totalPages", totalPages); // 총 페이지 수
            model.addAttribute("totalCount", tariffpolicyList.getTotalElements()); // 총 데이터
            log.info("==TariffList_PageInfo >> totalPages:{}, totalCount:{}", totalPages,
                    tariffpolicyList.getTotalElements());

            // select options 조회
            // 사업자 리스트
            List<BaseCompnayDto> companyList = companyService.searchAllCompanyForSelectOpt();
            log.info("== selectOption >> companyList : {}", companyList.toString());
            model.addAttribute("companyList", companyList);
            // 요금제 리스트(cpPlanPolicy)
            List<CpPlanDto> planList = tariffService.searchPlanPolicyAll();
            log.info("== selectOption >> planPolicy count : {}", planList.size());
            model.addAttribute("planList", planList);
            // grid row count
            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT");
            model.addAttribute("showListCnt", showListCnt);
            // 요금제 적용상태 코드 리스트
            List<CommCdBaseDto> tariffStatCodeList = codeService.commonCodeStringToNum("TARIFFSTATCD");
            model.addAttribute("tariffStatCodeList", tariffStatCodeList);

        } catch (Exception e) {
            log.error("Error occurred while fetching tariff list: {}", e.getMessage(), e);
            tariffpolicyList = Page.empty();

            model.addAttribute("companyList", Collections.emptyList());
            model.addAttribute("planList", Collections.emptyList());
            model.addAttribute("showListCnt", Collections.emptyList());
        }

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
    public String showvoclist(
            @RequestParam(value = "typeSearch", required = false) String type,
            @RequestParam(value = "replyStatSearch", required = false) String replyStat,
            @RequestParam(value = "nameSearch", required = false) String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        log.info("=== VOC List Page ===");
        model.addAttribute("vocAnswerDto", new VocDto.VocAnswerDto());

        try {
            Page<VocListDto> vocList = this.vocService.findVocInfoWithPagination(type, replyStat, name, page, size);

            // 검색 조건 저장
            model.addAttribute("selectedType", type);
            model.addAttribute("selectedReplyStat", replyStat);
            model.addAttribute("selectedName", name);

            int totalPages = vocList.getTotalPages() == 0 ? 1 : vocList.getTotalPages(); // 전체 페이지 수

            model.addAttribute("vocList", vocList.getContent()); // 1:1문의 list
            model.addAttribute("size", String.valueOf(size)); // 페이지당 보여지는 데이터 건 수
            model.addAttribute("currentPage", page); // 현재 페이지
            model.addAttribute("totalPages", totalPages); // 총 페이지 수
            model.addAttribute("totalCount", vocList.getTotalElements()); // 총 데이터

            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);
            List<CommCdBaseDto> vocTypeList = codeService.commonCodeStringToNum("VOCTYPE"); // 문의유형
            model.addAttribute("vocTypeList", vocTypeList);
            List<CommCdBaseDto> vocStatList = codeService.commonCodeStringToNum("VOCSTAT"); // 조치상태
            model.addAttribute("vocStatList", vocStatList);
            List<CommCdBaseDto> vocPathList = codeService.commonCodeStringToNum("VOCPATH"); // 문의경로
            model.addAttribute("vocPathList", vocPathList);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("memberList", Collections.emptyList());
            model.addAttribute("size", Collections.emptyList());
            model.addAttribute("currentPage", Collections.emptyList());
            model.addAttribute("totalPages", Collections.emptyList());
            model.addAttribute("totalCount", Collections.emptyList());
            model.addAttribute("showListCnt", Collections.emptyList());
            model.addAttribute("vocTypeList", Collections.emptyList());
            model.addAttribute("vocStatList", Collections.emptyList());
        }

        return "pages/customer/voc";
    }

    /*
     * 고객센터 > FAQ 관리
     */
    @GetMapping("/faq")
    public String showfaqlist(
            @RequestParam(value = "sectionSearch", required = false) String section,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        log.info("=== FAQ List Page ===");

        model.addAttribute("faqDto", new FaqDto.FaqRegDto());

        try {
            Page<FaqDto.FaqListDto> faqList;

            if (section == null) {
                faqList = this.faqService.findFaqAll(page, size);
            } else {
                faqList = this.faqService.searchFaqListWithPagination(section, page, size);
            }

            int totalPages = faqList.getTotalPages() == 0 ? 1 : faqList.getTotalPages();

            // 검색 조건 저장
            model.addAttribute("selectedSection", section);

            // pagination
            model.addAttribute("faqList", faqList.getContent());
            model.addAttribute("size", String.valueOf(size));
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalCount", faqList.getTotalElements());

            List<CommCdBaseDto> faqKindList = codeService.commonCodeStringToNum("FAQKIND"); // FAQ 구분코드
            model.addAttribute("faqKindList", faqKindList);

            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("selectedSection", Collections.emptyList());
            model.addAttribute("faqList", Collections.emptyList());
            model.addAttribute("size", Collections.emptyList());
            model.addAttribute("currentPage", Collections.emptyList());
            model.addAttribute("totalPages", Collections.emptyList());
            model.addAttribute("totalCount", Collections.emptyList());
            model.addAttribute("faqKindList", Collections.emptyList());
            model.addAttribute("showListCnt", Collections.emptyList());
        }
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
    public String showbizlist(
            @RequestParam(value = "companyIdSearch", required = false) Long companyId,
            @RequestParam(value = "companyTypeSearch", required = false) String companyType,
            @RequestParam(value = "companyLvSearch", required = false) String companyLv,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        log.info("=== Biz managment List Page ===");
        log.info("== companyId: {}, companyType:{}, companyLv: {}, page: {}, size: {}", companyId, companyType,
                companyLv, page, size);

        // 업체 등록폼 전달
        model.addAttribute("companyRegDto", new CompanyRegDto());

        Page<CompanyListDto> companyList;

        try {
            log.info("=== Compnay DB search result >>>");

            // check null and call the approrpiate search method
            if (companyId != null) {
                log.info("Searching by companyId: {}", companyId);
                companyList = companyService.searchCompanyById(companyId, page, size);
            } else if (companyType != null && !companyType.isEmpty()) {
                log.info("Searching by companyType: {}", companyType);
                companyList = companyService.searchCompanyByType(companyType, page, size);
            } else if (companyLv != null && !companyLv.isEmpty()) {
                log.info("Searching by CompanyLevel", companyLv);
                companyList = companyService.searchCompanyByLevel(companyLv, page, size);
            } else {
                log.info("Fetching all companies >> ");
                companyList = companyService.searchCompanyAll(page, size);
            }

            log.info("=== companyListDto : {}", companyList.toString());

            // page 처리
            int totalPages = companyList.getTotalPages() == 0 ? 1 : companyList.getTotalPages(); // 전체 페이지 수
            model.addAttribute("companyList", companyList.getContent()); // 사용자 list
            model.addAttribute("size", String.valueOf(size)); // 페이지당 보여지는 데이터 건 수
            model.addAttribute("currentPage", page); // 현재 페이지
            model.addAttribute("totalPages", totalPages); // 총 페이지 수
            model.addAttribute("totalCount", companyList.getTotalElements()); // 총 데이터

            // select options 조회
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

            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);

        } catch (Exception e) {

            log.error("Error occurred while fetching company list: {}", e.getMessage(), e);
            companyList = Page.empty();
        }

        return "pages/biz/biz_management";
    }

    /*
     * 업체관리 > 법인관리
     */
    @GetMapping("/corp/list")
    public String showcorplist(
            @RequestParam(value = "opSearch", required = false) String searchOp,
            @RequestParam(value = "contentSearch", required = false) String searchContent,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        log.info("=== Corp managment List Page ===");
        model.addAttribute("bizRegDto", new BizInfoRegDto());

        try {
            // 법인 list
            Page<BizInfoListDto> bizList = this.bizService.findBizInfoWithPagination(searchOp, searchContent,
                    page, size);

            // 검색 조건 저장
            model.addAttribute("selectedOpSearch", searchOp);
            model.addAttribute("selectedContentSearch", searchContent);

            int totalPages = bizList.getTotalPages() == 0 ? 1 : bizList.getTotalPages(); // 전체 페이지 수

            model.addAttribute("bizList", bizList.getContent()); // 법인 list
            model.addAttribute("size", String.valueOf(size)); // 페이지당 보여지는 데이터 건 수
            model.addAttribute("currentPage", page); // 현재 페이지
            model.addAttribute("totalPages", totalPages); // 총 페이지 수
            model.addAttribute("totalCount", bizList.getTotalElements()); // 총 데이터

            List<CompanyListDto> companyList = this.companyService.findCompanyListAll();
            model.addAttribute("companyList", companyList);
            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);
            List<CommCdBaseDto> creditCardList = codeService.commonCodeStringToNum("CREDITCARDCD"); // 카드사코드
            model.addAttribute("creditCardList", creditCardList);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("bizList", Collections.emptyList());
            model.addAttribute("currentPage", Collections.emptyList());
            model.addAttribute("totalPages", Collections.emptyList());
            model.addAttribute("totalCount", Collections.emptyList());
            model.addAttribute("companyList", Collections.emptyList());
            model.addAttribute("showListCnt", Collections.emptyList());
            model.addAttribute("creditCardList", Collections.emptyList());
        }
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
