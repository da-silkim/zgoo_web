package zgoo.cpos.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.biz.BizInfoDto.BizInfoListDto;
import zgoo.cpos.dto.biz.BizInfoDto.BizInfoRegDto;
import zgoo.cpos.dto.calc.PurchaseDto.PurchaseListDto;
import zgoo.cpos.dto.code.ChgErrorCodeDto;
import zgoo.cpos.dto.code.ChgErrorCodeDto.ChgErrorCodeListDto;
import zgoo.cpos.dto.code.CodeDto.CommCdBaseDto;
import zgoo.cpos.dto.code.CodeDto.CommCodeDto;
import zgoo.cpos.dto.code.CodeDto.GrpCodeDto;
import zgoo.cpos.dto.company.CompanyDto.BaseCompnayDto;
import zgoo.cpos.dto.company.CompanyDto.CompanyListDto;
import zgoo.cpos.dto.company.CompanyDto.CompanyRegDto;
import zgoo.cpos.dto.company.CompanyDto.CpPlanDto;
import zgoo.cpos.dto.cp.ChargerDto.ChargerCountBySidoDto;
import zgoo.cpos.dto.cp.ChargerDto.ChargerListDto;
import zgoo.cpos.dto.cp.ChargerDto.ChargerRegDto;
import zgoo.cpos.dto.cp.ChargerDto.ConnectorStatusCountDto;
import zgoo.cpos.dto.cp.ChargerDto.ConnectorStatusDto;
import zgoo.cpos.dto.cp.ChargerDto.FacilityCountDto;
import zgoo.cpos.dto.cp.CpMaintainDto.CpMaintainListDto;
import zgoo.cpos.dto.cp.CpModelDto;
import zgoo.cpos.dto.cp.CpModelDto.CpModelListDto;
import zgoo.cpos.dto.cp.CurrentChargingListDto;
import zgoo.cpos.dto.cs.CsInfoDto;
import zgoo.cpos.dto.cs.CsInfoDto.CsInfoListDto;
import zgoo.cpos.dto.cs.CsInfoDto.StationOpStatusDto;
import zgoo.cpos.dto.fw.CpFwversionDto;
import zgoo.cpos.dto.history.ChargingHistDto;
import zgoo.cpos.dto.history.ChgCommlogDto;
import zgoo.cpos.dto.history.ErrorHistDto;
import zgoo.cpos.dto.history.PaymentHistDto;
import zgoo.cpos.dto.member.ConditionDto.ConditionCodeBaseDto;
import zgoo.cpos.dto.member.ConditionDto.ConditionList;
import zgoo.cpos.dto.member.MemberAuthHistDto;
import zgoo.cpos.dto.member.MemberDto;
import zgoo.cpos.dto.member.MemberDto.MemberAuthDto;
import zgoo.cpos.dto.member.MemberDto.MemberListDto;
import zgoo.cpos.dto.member.VocDto;
import zgoo.cpos.dto.member.VocDto.VocListDto;
import zgoo.cpos.dto.menu.CompanyMenuAuthorityDto;
import zgoo.cpos.dto.menu.MenuAuthorityDto.MenuAuthorityBaseDto;
import zgoo.cpos.dto.menu.MenuDto;
import zgoo.cpos.dto.payment.ChgPaymentInfoDto;
import zgoo.cpos.dto.payment.ChgPaymentSummaryDto;
import zgoo.cpos.dto.statistics.ErrorDto.ErrorBarDto;
import zgoo.cpos.dto.statistics.ErrorDto.ErrorLineChartDto;
import zgoo.cpos.dto.statistics.PurchaseSalesDto.PurchaseSalesBarDto;
import zgoo.cpos.dto.statistics.PurchaseSalesDto.PurchaseSalesLineChartDto;
import zgoo.cpos.dto.statistics.PurchaseSalesDto.SalesDashboardDto;
import zgoo.cpos.dto.statistics.TotalkwDto.TotalkwBarDto;
import zgoo.cpos.dto.statistics.TotalkwDto.TotalkwDashboardDto;
import zgoo.cpos.dto.statistics.TotalkwDto.TotalkwLineChartDto;
import zgoo.cpos.dto.statistics.UsageDto.UsageBarDto;
import zgoo.cpos.dto.statistics.UsageDto.UsageLineChartDto;
import zgoo.cpos.dto.statistics.YearOptionDto;
import zgoo.cpos.dto.tariff.TariffDto.TariffPolicyDto;
import zgoo.cpos.dto.tariff.TariffDto.TariffRegDto;
import zgoo.cpos.dto.users.FaqDto;
import zgoo.cpos.dto.users.NoticeDto;
import zgoo.cpos.dto.users.NoticeDto.NoticeListDto;
import zgoo.cpos.dto.users.UsersDto;
import zgoo.cpos.service.BizService;
import zgoo.cpos.service.ChargerService;
import zgoo.cpos.service.ChargingHistService;
import zgoo.cpos.service.ChargingPaymentInfoService;
import zgoo.cpos.service.ChgCommlogService;
import zgoo.cpos.service.ChgErrorCodeService;
import zgoo.cpos.service.CodeService;
import zgoo.cpos.service.ComService;
import zgoo.cpos.service.CompanyService;
import zgoo.cpos.service.ConditionService;
import zgoo.cpos.service.CpCurrentTxService;
import zgoo.cpos.service.CpMaintainService;
import zgoo.cpos.service.CpModelService;
import zgoo.cpos.service.CsService;
import zgoo.cpos.service.ErrorHistService;
import zgoo.cpos.service.FaqService;
import zgoo.cpos.service.FwService;
import zgoo.cpos.service.MemberService;
import zgoo.cpos.service.MenuAuthorityService;
import zgoo.cpos.service.MenuService;
import zgoo.cpos.service.NoticeService;
import zgoo.cpos.service.PaymentHistService;
import zgoo.cpos.service.PurchaseService;
import zgoo.cpos.service.StatisticsService;
import zgoo.cpos.service.TariffService;
import zgoo.cpos.service.UsersService;
import zgoo.cpos.service.VocService;
import zgoo.cpos.type.ocpp.ConfigurationKey;
import zgoo.cpos.type.ocpp.MessageTrigger;
import zgoo.cpos.type.ocpp.ResetType;
import zgoo.cpos.util.MenuConstants;

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
    private final ConditionService conditionService;
    private final CpMaintainService cpMaintainService;
    private final CpCurrentTxService cpCurrentTxService;
    private final ChargingHistService chargingHistService;
    private final ChgCommlogService chgCommlogService;
    private final StatisticsService statisticsService;
    private final ComService comService;
    private final ChargingPaymentInfoService chargingPaymentInfoService;
    private final PurchaseService purchaseService;
    private final ErrorHistService errorHistService;
    private final FwService fwService;
    private final PaymentHistService paymentHistService;

    /*
     * 대시보드
     */
    @GetMapping("/dashboard")
    public String showdashboard(Model model, Principal principal) {
        log.info("Dashboard Home");

        try {
            long cpCount = this.chargerService.countCharger(principal.getName());
            model.addAttribute("cpCount", cpCount);

            ConnectorStatusCountDto connStatus = this.chargerService.getConnectorStatusCount(principal.getName());
            model.addAttribute("connStatus", connStatus);

            StationOpStatusDto opStatus = this.csService.getStationOpStatusCount(principal.getName());
            model.addAttribute("opStatus", opStatus);

            SalesDashboardDto saleStatus = this.chargingPaymentInfoService.findPaymentByPeriod(principal.getName());
            model.addAttribute("saleStatus", saleStatus);

            TotalkwDashboardDto chgStatus = this.chargingHistService.findChargingHistByPeriod(principal.getName());
            model.addAttribute("chgStatus", chgStatus);

            List<ChargerCountBySidoDto> chargerCountList = this.chargerService
                    .countChargerBySidoAndType(principal.getName());
            model.addAttribute("chargerCountList", chargerCountList);

            List<FacilityCountDto> facilityList = this.chargerService.countFacilityBySidoAndType(null, null,
                    principal.getName());
            model.addAttribute("facilityList", facilityList);

            List<ErrorHistDto> errorHistList = this.errorHistService.findLatestErrorHist(principal.getName());
            model.addAttribute("errorHistList", errorHistList);

            List<NoticeListDto> noticeList = this.noticeService.findLatestNoticeList(principal.getName());
            model.addAttribute("noticeList", noticeList);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("cpCount", null);
            model.addAttribute("connStatus", null);
            model.addAttribute("opStatus", null);
            model.addAttribute("chgStatus", null);
            model.addAttribute("chargerCountList", Collections.emptyList());
            model.addAttribute("facilityList", Collections.emptyList());
            model.addAttribute("noticeList", Collections.emptyList());
        }

        return "pages/dashboard";
    }

    /*
     * 지도
     */
    @GetMapping("/map")
    public String showmap(Model model) {
        log.info("=== Map Page ===");

        try {

        } catch (Exception e) {
            e.getStackTrace();
        }

        return "pages/map/map";
    }

    /*
     * 회원관리 > 회원리스트
     */
    @GetMapping("/member/list")
    public String showmemlist(
            @RequestParam(value = "idTagSearch", required = false) String idTag,
            @RequestParam(value = "nameSearch", required = false) String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model, Principal principal) {
        log.info("=== Member List Page ===");
        model.addAttribute("memRegDto", new MemberDto.MemberRegDto());

        try {
            // 회원리스트
            Page<MemberListDto> memberList = this.memberService.findMemberInfoWithPagination(idTag, name,
                    page, size, principal.getName());

            // 검색 조건 저장
            model.addAttribute("selectedIdTag", idTag);
            model.addAttribute("selectedName", name);

            int totalPages = memberList.getTotalPages() == 0 ? 1 : memberList.getTotalPages(); // 전체 페이지 수

            model.addAttribute("memberList", memberList.getContent()); // 회원 list
            model.addAttribute("size", String.valueOf(size)); // 페이지당 보여지는 데이터 건 수
            model.addAttribute("currentPage", page); // 현재 페이지
            model.addAttribute("totalPages", totalPages); // 총 페이지 수
            model.addAttribute("totalCount", memberList.getTotalElements()); // 총 데이터

            List<ConditionList> conList = this.conditionService.findAllConditionWithVersion();
            model.addAttribute("conList", conList);
            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);
            List<CommCdBaseDto> memStatList = codeService.findCommonCdNamesByGrpcd("MEMSTATCD"); // 회원상태코드
            model.addAttribute("memStatList", memStatList);
            List<CommCdBaseDto> bizTypeList = codeService.findCommonCdNamesByGrpcd("BIZTYPECD"); // 사업자구분코드
            model.addAttribute("bizTypeList", bizTypeList);
            List<CommCdBaseDto> creditCardList = codeService.findCommonCdNamesByGrpcd("CREDITCARDCD"); // 카드사코드
            model.addAttribute("creditCardList", creditCardList);
            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.MEMBER_LIST);
            model.addAttribute("menuAuthority", menuAuthority);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("memberList", Collections.emptyList());
            model.addAttribute("size", Collections.emptyList());
            model.addAttribute("currentPage", Collections.emptyList());
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
    public String showauthlist(
            @RequestParam(value = "opSearch", required = false) String searchOp,
            @RequestParam(value = "contentSearch", required = false) String contentSearch,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size, Model model, Principal principal) {
        log.info("=== Member Authorization List Page ===");
        log.info("searchOp: {}, contentSearch: {}, fromDate: {}, toDate: {}", searchOp, contentSearch, fromDate,
                toDate);
        log.info("page: {}, size: {}", page, size);

        // 검색조건 저장
        model.addAttribute("selectedOpSearch", searchOp);
        model.addAttribute("selectedContentSearch", contentSearch);
        model.addAttribute("selectedTimeFrom", fromDate);
        model.addAttribute("selectedTimeTo", toDate);

        Page<MemberAuthHistDto> authList;
        try {
            if ((searchOp == null || searchOp.isEmpty()) && (contentSearch == null || contentSearch.isEmpty())
                    && (fromDate == null || fromDate.isEmpty()) && (toDate == null || toDate.isEmpty())) {
                log.info("Search all member auth hist list >>");
                authList = this.memberService.findAllMemberAuthHistWithPagination(page, size);
            } else {
                log.info("Search member auth hist list by options:op:{},content:{},fromDate:{},toDate:{} >>",
                        searchOp, contentSearch, fromDate, toDate);
                authList = this.memberService.findMemberAuthHistWithPagination(searchOp, contentSearch, fromDate,
                        toDate, page, size);
            }

            // Page처리
            int totalPages = authList.getTotalPages() == 0 ? 1 : authList.getTotalPages(); // 전체 페이지
                                                                                           // 수
            int startNumber = page * size;

            model.addAttribute("authList", authList.getContent());
            model.addAttribute("size", String.valueOf(size));
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalCount", authList.getTotalElements());
            model.addAttribute("startNumber", startNumber);
            log.info("=== MemberAuthHistList_PageInfo >> totalPages:{}, totalCount:{}", totalPages,
                    authList.getTotalElements());

            // 검색옵션 model value 추가
            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT");
            model.addAttribute("showListCnt", showListCnt);
            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.MEMBER_AUTH);
            model.addAttribute("menuAuthority", menuAuthority);

        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("showListCnt", Collections.emptyList());
            model.addAttribute("authList", Collections.emptyList());
            model.addAttribute("size", Collections.emptyList());
            model.addAttribute("currentPage", Collections.emptyList());
            model.addAttribute("totalPages", Collections.emptyList());
            model.addAttribute("totalCount", Collections.emptyList());
        }
        return "pages/member/member_authentication";
    }

    /*
     * 회원관리 > 회원카드관리
     */
    @GetMapping("/member/tag/list")
    public String showtaglist(
            @RequestParam(value = "idTagSearch", required = false) String idTag,
            @RequestParam(value = "nameSearch", required = false) String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model, Principal principal) {
        log.info("=== Member Tag List Page ===");

        try {
            // 회원카드리스트
            Page<MemberAuthDto> memberAuthList = this.memberService.findMemberAuthInfoWithPagination(idTag, name, page,
                    size, principal.getName());

            // 검색 조건 저장
            model.addAttribute("selectedIdTag", idTag);
            model.addAttribute("selectedName", name);

            int totalPages = memberAuthList.getTotalPages() == 0 ? 1 : memberAuthList.getTotalPages(); // 전체 페이지 수

            model.addAttribute("memberAuthList", memberAuthList.getContent()); // 회원카드 list
            model.addAttribute("size", String.valueOf(size)); // 페이지당 보여지는 데이터 건 수
            model.addAttribute("currentPage", page); // 현재 페이지
            model.addAttribute("totalPages", totalPages); // 총 페이지 수
            model.addAttribute("totalCount", memberAuthList.getTotalElements()); // 총 데이터

            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);
            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.MEMBER_TAG);
            model.addAttribute("menuAuthority", menuAuthority);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("showListCnt", Collections.emptyList());
        }

        return "pages/member/member_tag";
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
            Model model, Principal principal) {
        log.info("=== Charging Station List Page ===");
        log.info("companyId: {}, searchOp: {}, searchContent: {}", companyId, searchOp, searchContent);
        model.addAttribute("csRegDto", new CsInfoDto.CsInfoRegDto());

        try {
            // 충전소 list
            Page<CsInfoListDto> csList = this.csService.findCsInfoWithPagination(companyId, searchOp, searchContent,
                    page, size, principal.getName());

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
            List<CompanyListDto> companyList = this.companyService.findCompanyListAll(principal.getName());
            model.addAttribute("companyList", companyList);
            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);
            List<CommCdBaseDto> csFacility = codeService.findCommonCdNamesByGrpcd("CSFACILITY"); // 충전소시설유형
            model.addAttribute("csFacility", csFacility);
            List<CommCdBaseDto> csFSub = codeService.findCommonCdNamesByGrpcd("CSFSUB"); // 충전소시설구분
            model.addAttribute("csFSub", csFSub);
            List<CommCdBaseDto> opStepCd = codeService.findCommonCdNamesByGrpcd("OPSTEPCD"); // 운영단계분류
            model.addAttribute("opStepCd", opStepCd);
            List<CommCdBaseDto> landType = codeService.findCommonCdNamesByGrpcd("LANDTYPE"); // 부지구분
            model.addAttribute("landType", landType);
            List<CommCdBaseDto> faucetType = codeService.findCommonCdNamesByGrpcd("FAUCETTYPE"); // 수전방식
            model.addAttribute("faucetType", faucetType);
            List<CommCdBaseDto> powerType = codeService.findCommonCdNamesByGrpcd("POWERTYPE"); // 전압종류
            model.addAttribute("powerType", powerType);
            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.STATION_LIST);
            model.addAttribute("menuAuthority", menuAuthority);
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
            Model model, Principal principal) {

        log.info("=== Charger List Page ===");
        log.info("== companyId: {}, manfCd: {}, opSearch: {}, contentSearch: {}",
                reqCompanyId, reqManfCd, reqOpSearch, reqSearchContent);
        log.info("== page: {}, size: {}", page, size);

        // 검색 조건을 모델에 추가 (중요: 이 부분이 누락되어 있었음)
        model.addAttribute("selectedCompanyId", reqCompanyId);
        model.addAttribute("selectedManfCd", reqManfCd);
        model.addAttribute("selectedOpSearch", reqOpSearch);
        model.addAttribute("selectedContentSearch", reqSearchContent);
        // model.addAttribute("selectedSize", size);

        // 충전기 등록폼 전달
        model.addAttribute("chargerRegDto", new ChargerRegDto());

        Page<ChargerListDto> chargerList;

        try {
            log.info("=== Charger DB search result >>>");

            // charger list 조회
            // 검색조건 check
            if (reqCompanyId == null && (reqManfCd == null || reqManfCd.isEmpty())
                    && (reqOpSearch == null || reqOpSearch.isEmpty())
                    && (reqSearchContent == null || reqSearchContent.isEmpty())) {
                log.info("Search all charger list >>");
                chargerList = chargerService.searchCpListPageAll(page, size, principal.getName());
            } else {
                log.info("Search charger list by options:companyid:{},manfcode:{},op:{},content:{} >>",
                        reqCompanyId, reqManfCd, reqOpSearch, reqSearchContent);
                chargerList = chargerService.searchCpListPage(reqCompanyId, reqManfCd, reqOpSearch, reqSearchContent,
                        page, size, principal.getName());
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

            // 충전기 커넥터 상태 리스트 조회 - 현재 페이지의 충전기 ID만 추출하여 조회
            try {
                // 현재 페이지의 충전기 ID 목록 추출
                List<String> currentPageChargerIds = chargerList.getContent().stream()
                        .map(ChargerListDto::getChargerId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                // 현재 페이지의 충전기 ID에 대한 커넥터 상태만 조회
                List<ConnectorStatusDto> connStatList = chargerService
                        .searchConStatListByChargerIds(currentPageChargerIds);

                // 안전하게 Map 생성 - 예외가 발생해도 빈 Map 반환
                Map<String, List<Map<String, String>>> connStatusMap = new HashMap<>();

                if (connStatList != null && !connStatList.isEmpty()) {
                    // chargerId별로 커넥터 상태 정보를 그룹화
                    connStatusMap = connStatList.stream()
                            .filter(dto -> dto != null && dto.getChargerId() != null)
                            .collect(Collectors.groupingBy(
                                    ConnectorStatusDto::getChargerId,
                                    Collectors.mapping(
                                            dto -> {
                                                Map<String, String> connInfo = new HashMap<>();
                                                connInfo.put("status",
                                                        dto.getStatus() != null ? dto.getStatus().toString()
                                                                : "Unknown");
                                                connInfo.put("connectionYn",
                                                        dto.getConnectionYn() != null ? dto.getConnectionYn().toString()
                                                                : "N");
                                                connInfo.put("connectorId",
                                                        dto.getConnectorId() != null ? dto.getConnectorId().toString()
                                                                : "0");
                                                return connInfo;
                                            },
                                            Collectors.toList())));
                }

                model.addAttribute("connStatusMap", connStatusMap);
                log.info("=== connector_status map size: {}", connStatusMap.size());
                // connStatusMap의 내용을 로깅
                connStatusMap.forEach((chargerId, statusList) -> {
                    log.info("=== Charger ID: {}, Connector Count: {}", chargerId, statusList.size());
                    statusList.forEach(connInfo -> log.info("===== Connector ID: {}, Status: {}, ConnectionYn: {}",
                            connInfo.get("connectorId"), connInfo.get("status"), connInfo.get("connectionYn")));
                });
            } catch (Exception e) {
                log.error("Error while fetching connector status: {}", e.getMessage());
                // 오류 발생 시 빈 Map 추가
                model.addAttribute("connStatusMap", new HashMap<String, List<Map<String, String>>>());
            }

            // 검색 select options 조회
            // 1.사업자 리스트
            List<BaseCompnayDto> companyList = companyService.searchAllCompanyForSelectOpt(principal.getName());
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

            // 8. 메뉴권한조회
            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.CHARGER_LIST);
            model.addAttribute("menuAuthority", menuAuthority);

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
    public String showcharginglist(@RequestParam(value = "companyIdSearch", required = false) Long reqCompanyId,
            @RequestParam(value = "chgStartTimeFrom", required = false) String reqChgStartTimeFrom,
            @RequestParam(value = "chgStartTimeTo", required = false) String reqChgStartTimeTo,
            @RequestParam(value = "opSearch", required = false) String reqOpSearch,
            @RequestParam(value = "contentSearch", required = false) String reqSearchContent,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model, Principal principal) {
        log.info("=== Current Charging List Page ===");
        log.info("== companyId: {}, chgStartTimeFrom: {}, chgStartTimeTo: {}, opSearch: {}, contentSearch: {}",
                reqCompanyId, reqChgStartTimeFrom, reqChgStartTimeTo, reqOpSearch, reqSearchContent);
        log.info("== page: {}, size: {}", page, size);

        // 검색 조건을 모델에 추가 (중요: 이 부분이 누락되어 있었음)
        model.addAttribute("selectedCompanyId", reqCompanyId);
        model.addAttribute("selectedTimeFrom", reqChgStartTimeFrom);
        model.addAttribute("selectedTimeTo", reqChgStartTimeTo);
        model.addAttribute("selectedOpSearch", reqOpSearch);
        model.addAttribute("selectedContentSearch", reqSearchContent);
        // model.addAttribute("selectedSize", size);

        Page<CurrentChargingListDto> chargingList;

        try {
            log.info("=== Current Charging List DB search result >>>");

            // current charging list 조회
            // 검색조건 check
            if (reqCompanyId == null && reqChgStartTimeFrom == null && reqChgStartTimeTo == null
                    && reqOpSearch == null) {
                log.info("Search all current charging list >>");
                chargingList = cpCurrentTxService.findCurrentChargingListAll(page, size, principal.getName());
            } else {
                log.info(
                        "Search current charging list by options:companyid:{},chgStartTimeFrom:{},chgStartTimeTo:{},op:{},content:{} >>",
                        reqCompanyId, reqChgStartTimeFrom, reqChgStartTimeTo, reqOpSearch, reqSearchContent);
                chargingList = cpCurrentTxService.findCurrentChargingList(reqCompanyId, reqChgStartTimeFrom,
                        reqChgStartTimeTo, reqOpSearch, reqSearchContent, page, size, principal.getName());
            }

            // page 처리
            int totalPages = chargingList.getTotalPages() == 0 ? 1 : chargingList.getTotalPages();
            int startNumber = page * size;
            model.addAttribute("chargingList", chargingList.getContent());
            model.addAttribute("size", String.valueOf(size));
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalCount", chargingList.getTotalElements());
            model.addAttribute("startNumber", startNumber);
            log.info("===CurrentChargingList_PageInfo >> totalPages:{}, totalCount:{}", totalPages,
                    chargingList.getTotalElements());

            // 검색 select options 조회
            // 1.사업자 리스트
            List<BaseCompnayDto> companyList = companyService.searchAllCompanyForSelectOpt(principal.getName());
            model.addAttribute("companyList", companyList);
            // 2.grid row count
            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT");
            model.addAttribute("showListCnt", showListCnt);
            // // 3. 메뉴권한조회
            // MenuAuthorityBaseDto menuAuthority =
            // this.menuAuthorityService.searchUserAuthority(principal.getName(),
            // MenuConstants.CHARGING_LIST);
            // model.addAttribute("menuAuthority", menuAuthority);

        } catch (Exception e) {
            log.error("Error occurered while fetching current charging list: {}", e.getMessage(), e);

            chargingList = Page.empty();
            model.addAttribute("companyList", Collections.emptyList());
            model.addAttribute("showListCnt", Collections.emptyList());
        }

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
            Model model, Principal principal) {
        log.info("=== Model List Page ===");
        model.addAttribute("cpModelDto", new CpModelDto.CpModelRegDto());

        try {
            // 충전기 모델 list
            Page<CpModelListDto> modelList = this.cpModelService.findCpModelInfoWithPagination(companyId, manfCode,
                    chgSpeedCode, page, size, principal.getName());

            model.addAttribute("selectedCompanyId", companyId);
            model.addAttribute("selectedManfCd", manfCode);
            model.addAttribute("selectedChgSpeedCd", chgSpeedCode);

            int totalPages = modelList.getTotalPages() == 0 ? 1 : modelList.getTotalPages(); // 전체 페이지 수

            model.addAttribute("modelList", modelList.getContent()); // 충전기 모델 list
            model.addAttribute("size", String.valueOf(size)); // 페이지당 보여지는 데이터 건 수
            model.addAttribute("currentPage", page); // 현재 페이지
            model.addAttribute("totalPages", totalPages); // 총 페이지 수
            model.addAttribute("totalCount", modelList.getTotalElements()); // 총 데이터

            List<CompanyListDto> companyList = this.companyService.findCompanyListAll(principal.getName()); // 사업자 list
            model.addAttribute("companyList", companyList);
            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);
            List<CommCdBaseDto> manfCd = codeService.findCommonCdNamesByGrpcd("CGMANFCD"); // 충전기제조사
            model.addAttribute("manfCd", manfCd);
            List<CommCdBaseDto> chgTypeCd = codeService.findCommonCdNamesByGrpcd("CHGINTTYPECD"); // 충전기설치유형
            model.addAttribute("chgTypeCd", chgTypeCd);
            List<CommCdBaseDto> chgSpeedCd = codeService.findCommonCdNamesByGrpcd("CHGSPEEDCD"); // 충전기속도구분(충전기유형)
            model.addAttribute("chgSpeedCd", chgSpeedCd);
            List<CommCdBaseDto> connType = codeService.findCommonCdNamesByGrpcd("CONNTYPE"); // 커넥터타입
            model.addAttribute("connType", connType);
            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.MODEL);
            model.addAttribute("menuAuthority", menuAuthority);
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
    public String showcodelist(Model model, Principal principal) {
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

            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.CODE);
            model.addAttribute("menuAuthority", menuAuthority);
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
            Model model, Principal principal) {

        log.info("=== User List Page ===");
        log.info("companyId: {}, companyType: {}, name: {}", companyId, companyType, name);
        model.addAttribute("usersDto", new UsersDto.UsersRegDto());

        try {
            log.info("=== user DB search result >>>");

            // 사업자 list
            List<CompanyListDto> companyList = this.companyService.findCompanyListAll(principal.getName());
            model.addAttribute("companyList", companyList);

            // 사용자 list
            Page<UsersDto.UsersListDto> userList = this.usersService.findUsersWithPagination(companyId, companyType,
                    name, page, size, principal.getName());

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

            List<CommCdBaseDto> authList = this.usersService.searchMenuAccessList(principal.getName()); // 메뉴권한
            model.addAttribute("authList", authList);

            List<CommCdBaseDto> coKind = codeService.findCommonCdNamesByGrpcd("COKIND"); // 사업자 유형
            model.addAttribute("coKind", coKind);

            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);

            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.USER);
            model.addAttribute("menuAuthority", menuAuthority);
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
            Model model, Principal principal) {
        log.info("=== Notice List Page ===");
        log.info("companyId: {}, startDates: {}, endDate: {}", companyId, startDate, endDate);

        model.addAttribute("noticeDto", new NoticeDto.NoticeRegDto());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDateSearch = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate, formatter)
                : null;
        LocalDate endDateSearch = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate, formatter) : null;

        try {
            // 사업자 list
            List<CompanyListDto> companyList = this.companyService.findCompanyListAll(principal.getName());
            model.addAttribute("companyList", companyList);

            Page<NoticeDto.NoticeListDto> noticeList = this.noticeService.findNoticeWithPagintaion(companyId,
                    startDateSearch, endDateSearch, page, size, principal.getName());

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

            List<CommCdBaseDto> noticeTypeList = codeService.findCommonCdNamesByGrpcd("NOTICETYPECD"); // 공지유형
            model.addAttribute("noticeTypeList", noticeTypeList);

            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);

            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.NOTICE);
            model.addAttribute("menuAuthority", menuAuthority);
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
            Model model, Principal principal) {
        log.info("=== Menu List Page ===");

        model.addAttribute("menuRegDto", new MenuDto.MenuRegDto());

        log.info("companyName: {}", companyName);
        try {
            // 메뉴 list
            List<MenuDto.MenuListDto> menuList = menuService.findMenuList();
            model.addAttribute("menuList", menuList);

            // 사업자 list(select option)
            List<CompanyListDto> companyList = this.companyService.findCompanyListAll(principal.getName());
            model.addAttribute("companyList", companyList);

            // 사업장별 메뉴 접근 권한 list
            // List<CompanyMenuAuthorityDto.CompanyMenuRegDto> companyMenuList =
            // this.menuService.findCompanyDistinctList();
            // model.addAttribute("companyMenuList", companyMenuList);
            Page<CompanyMenuAuthorityDto.CompanyMenuRegDto> companyMenuList;

            if (companyName == null || companyName.isEmpty()) {
                log.info("[findCompanyMenuAll] companyName: {}", companyName);
                companyMenuList = this.menuService.findCompanyMenuAll(page, size, principal.getName());
            } else {
                log.info("[searchCompanyMenuWithPagination] companyName: {}", companyName);
                companyMenuList = this.menuService.searchCompanyMenuWithPagination(companyName, page, size,
                        principal.getName());
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

            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.MENU);
            model.addAttribute("menuAuthority", menuAuthority);
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
    public String showautoritylist(Model model, Principal principal) {
        log.info("=== Authority List Page ===");

        try {
            // 사업자 권한 list
            // List<MenuAuthorityDto.CompanyAuthorityListDto> companyAuthorityList =
            // menuAuthorityService
            // .findCompanyAuthorityList();
            // log.info("사업자 권한 확인 >> {}", companyAuthorityList.toString());
            // model.addAttribute("companyAuthorityList", companyAuthorityList);

            List<CompanyListDto> companyList = this.companyService.findCompanyListAll(principal.getName()); // 사업자 list
            model.addAttribute("companyList", companyList);

            List<CommCdBaseDto> authorityList = codeService.commonCodeMenuAuthority("MENUACCLV"); // 메뉴권한
            model.addAttribute("authorityList", authorityList);

            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.MENU_AUTH);
            // System.out.println("권한: " + menuAuthority.getAuthority());
            // System.out.println("등록권한: " + menuAuthority.getModYn());
            // System.out.println("principal getName : " + principal.getName());
            model.addAttribute("menuAuthority", menuAuthority);

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
            Model model, Principal principal) {
        log.info("=== Errcode List Page ===");
        log.info("manfCode: {}, searchOp: {}, searchContent: {}", manfCode, searchOp, searchContent);
        model.addAttribute("errCdDto", new ChgErrorCodeDto.ChgErrorCodeRegDto());

        try {
            // 에러코드 list
            Page<ChgErrorCodeListDto> errcdList = this.chgErrorCodeService.findErrorCodeInfoWithPagination(manfCode,
                    searchOp, searchContent, page, size, principal.getName());

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
            List<CommCdBaseDto> manfCd = codeService.findCommonCdNamesByGrpcd("CGMANFCD"); // 충전기제조사
            model.addAttribute("manfCd", manfCd);
            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.ERRCODE);
            model.addAttribute("menuAuthority", menuAuthority);
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
            Model model, Principal principal) {

        log.info("=== Tariff List Page ===");
        log.info("== companyId: {}, page: {}, size: {}", companyId, page, size);

        // 검색 조건을 모델에 추가
        model.addAttribute("selectedCompanyId", companyId);
        // model.addAttribute("selectedSize", size);

        // 요금제 등록폼 전달
        model.addAttribute("tariffRegDto", new TariffRegDto());

        Page<TariffPolicyDto> tariffpolicyList;

        try {

            // tariff policy list 조회
            // check null and call the approrpiate search method
            if (companyId != null) {
                log.info("Searching by companyId: {}", companyId);
                tariffpolicyList = tariffService.searchTariffPolicyByCompanyId(page, size, companyId,
                        principal.getName());
            } else {
                log.info("Fetching all Tariff >> ");
                tariffpolicyList = tariffService.searchTariffPolicyAll(page, size, principal.getName());

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
            List<BaseCompnayDto> companyList = companyService.searchAllCompanyForSelectOpt(principal.getName());
            log.info("== selectOption >> companyList : {}", companyList.toString());
            model.addAttribute("companyList", companyList);
            // 요금제 리스트(cpPlanPolicy)
            List<CpPlanDto> planList = tariffService.searchPlanPolicyAll(principal.getName());
            log.info("== selectOption >> planPolicy count : {}", planList.size());
            model.addAttribute("planList", planList);
            // grid row count
            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT");
            model.addAttribute("showListCnt", showListCnt);
            // 요금제 적용상태 코드 리스트
            List<CommCdBaseDto> tariffStatCodeList = codeService.commonCodeStringToNum("TARIFFSTATCD");
            model.addAttribute("tariffStatCodeList", tariffStatCodeList);
            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.TARIFF);
            model.addAttribute("menuAuthority", menuAuthority);
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
     * 시스템 > 약관관리
     */
    @GetMapping("/system/condition")
    public String showcondition(Model model, Principal principal) {
        log.info("=== Condition List Page ===");

        try {
            List<ConditionCodeBaseDto> conList = this.conditionService.findConditionCodeAll();
            model.addAttribute("conList", conList);
            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.CONDITION);
            model.addAttribute("menuAuthority", menuAuthority);
        } catch (Exception e) {
            e.getStackTrace();
        }
        return "pages/system/condition_management";
    }

    /*
     * 유지보수 > 장애관리
     */
    @GetMapping("/maintenance/errlist")
    public String showerrlist(
            @RequestParam(value = "companyIdSearch", required = false) Long companyId,
            @RequestParam(value = "opSearch", required = false) String searchOp,
            @RequestParam(value = "contentSearch", required = false) String searchContent,
            @RequestParam(value = "processStatusSearch", required = false) String processStatus,
            @RequestParam(value = "startDateSearch", required = false) LocalDate startDate,
            @RequestParam(value = "endDateSearch", required = false) LocalDate endDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model, Principal principal) {
        log.info("=== Maintenance Error List Page ===");

        try {
            Page<CpMaintainListDto> cpList = this.cpMaintainService.findCpMaintainInfoWithPagination(companyId,
                    searchOp, searchContent,
                    processStatus, startDate, endDate, page, size, principal.getName());

            // 검색 조건 저장
            model.addAttribute("selectedCompanyId", companyId);
            model.addAttribute("selectedOpSearch", searchOp);
            model.addAttribute("selectedContentSearch", searchContent);
            model.addAttribute("selectedProcessStatus", processStatus);
            model.addAttribute("selectedStartDate", startDate);
            model.addAttribute("selectedEndDate", endDate);

            int totalPages = cpList.getTotalPages() == 0 ? 1 : cpList.getTotalPages(); // 전체 페이지 수

            model.addAttribute("cpList", cpList.getContent()); // 장애관리 list
            model.addAttribute("size", String.valueOf(size)); // 페이지당 보여지는 데이터 건 수
            model.addAttribute("currentPage", page); // 현재 페이지
            model.addAttribute("totalPages", totalPages); // 총 페이지 수
            model.addAttribute("totalCount", cpList.getTotalElements()); // 총 데이터

            List<CompanyListDto> companyList = this.companyService.findCompanyListAll(principal.getName());
            model.addAttribute("companyList", companyList);

            List<CommCdBaseDto> frList = codeService.findCommonCdNamesByGrpcd("FRCODE"); // 장애접수유형코드
            model.addAttribute("frList", frList);

            List<CommCdBaseDto> fstatList = codeService.findCommonCdNamesByGrpcd("FSTATCODE"); // 장애처리상태코드
            model.addAttribute("fstatList", fstatList);

            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);

            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.MAINTEN_ERR);
            model.addAttribute("menuAuthority", menuAuthority);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("size", Collections.emptyList());
            model.addAttribute("cpList", Collections.emptyList());
            model.addAttribute("currentPage", Collections.emptyList());
            model.addAttribute("totalPages", Collections.emptyList());
            model.addAttribute("totalCount", Collections.emptyList());
            model.addAttribute("companyList", Collections.emptyList());
            model.addAttribute("frList", Collections.emptyList());
            model.addAttribute("fstatList", Collections.emptyList());
            model.addAttribute("showListCnt", Collections.emptyList());
            model.addAttribute("menuAuthority", Collections.emptyList());
        }

        return "pages/maintenance/error_management";
    }

    /*
     * 제어 > 충전기제어
     */
    @GetMapping("/control/charger/list")
    public String showcontrollist(
            @RequestParam(value = "companyIdSearch", required = false) Long reqCompanyId,
            @RequestParam(value = "opSearch", required = false) String reqOpSearch,
            @RequestParam(value = "contentSearch", required = false) String reqContentSearch,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model, Principal principal) {
        log.info("=== Charger Control List Page ===");
        log.info("== companyId: {}, opSearch: {}, contentSearch: {}", reqCompanyId, reqOpSearch, reqContentSearch);
        log.info("== page: {}, size: {}", page, size);

        try {

            // 검색조건 값 저장
            model.addAttribute("selectedCompanyId", reqCompanyId);
            model.addAttribute("selectedOpSearch", reqOpSearch);
            model.addAttribute("selectedContentSearch", reqContentSearch);

            Page<ChargerListDto> chargerList;
            if (reqCompanyId == null && reqOpSearch == null && reqContentSearch == null) {
                log.info("Search all charger list >>");
                chargerList = chargerService.searchCpListPageAll(page, size, principal.getName());
            } else {
                log.info("Search charger list by options:companyid:{},op:{},content:{} >>",
                        reqCompanyId, reqOpSearch, reqContentSearch);
                chargerList = chargerService.searchCpListPage(reqCompanyId, null, reqOpSearch, reqContentSearch,
                        page, size, principal.getName());
            }

            // Page 처리
            int totalpages = chargerList.getTotalPages() == 0 ? 1 : chargerList.getTotalPages();
            int startNumber = page * size;
            model.addAttribute("chargerList", chargerList.getContent());
            model.addAttribute("size", String.valueOf(size));
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalpages);
            model.addAttribute("totalCount", chargerList.getTotalElements());
            model.addAttribute("startNumber", startNumber);
            log.info("== ChargerControl_PageInfo >> totalPages:{}, totalCount:{}", totalpages,
                    chargerList.getTotalElements());

            // select option 조회
            List<CompanyListDto> companyList = this.companyService.findCompanyListAll(principal.getName());
            model.addAttribute("companyList", companyList);
            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT");
            model.addAttribute("showListCnt", showListCnt);
            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.CONTROL_CHARGER);
            model.addAttribute("menuAuthority", menuAuthority);

            // Reset type
            model.addAttribute("resetTypes", ResetType.values());
            // MessageTrigger
            model.addAttribute("triggerMessages", MessageTrigger.values());
            // ConfigurationKey
            model.addAttribute("configurationKeys", ConfigurationKey.values());

        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("companyList", Collections.emptyList());
            model.addAttribute("showListCnt", Collections.emptyList());
            model.addAttribute("menuAuthority", Collections.emptyList());
            model.addAttribute("chargerList", Collections.emptyList());
            model.addAttribute("size", Collections.emptyList());
            model.addAttribute("currentPage", Collections.emptyList());
            model.addAttribute("totalPages", Collections.emptyList());
            model.addAttribute("totalCount", Collections.emptyList());
        }
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
            Model model, Principal principal) {
        log.info("=== VOC List Page ===");
        model.addAttribute("vocRegDto", new VocDto.VocRegDto());

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
            List<CommCdBaseDto> vocTypeList = codeService.findCommonCdNamesByGrpcd("VOCTYPE"); // 문의유형
            model.addAttribute("vocTypeList", vocTypeList);
            List<CommCdBaseDto> vocStatList = codeService.findCommonCdNamesByGrpcd("VOCSTAT"); // 조치상태
            model.addAttribute("vocStatList", vocStatList);
            List<CommCdBaseDto> vocPathList = codeService.findCommonCdNamesByGrpcd("VOCPATH"); // 문의경로
            model.addAttribute("vocPathList", vocPathList);
            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.VOC);
            model.addAttribute("menuAuthority", menuAuthority);
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
            model.addAttribute("vocPathList", Collections.emptyList());
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
            Model model, Principal principal) {
        log.info("=== FAQ List Page ===");

        model.addAttribute("faqDto", new FaqDto.FaqRegDto());

        try {
            Page<FaqDto.FaqListDto> faqList = this.faqService.findFaqWithPagination(section, page, size,
                    principal.getName());

            int totalPages = faqList.getTotalPages() == 0 ? 1 : faqList.getTotalPages();

            // 검색 조건 저장
            model.addAttribute("selectedSection", section);

            // pagination
            model.addAttribute("faqList", faqList.getContent());
            model.addAttribute("size", String.valueOf(size));
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalCount", faqList.getTotalElements());

            List<CommCdBaseDto> faqKindList = codeService.findCommonCdNamesByGrpcd("FAQKIND"); // FAQ 구분코드
            model.addAttribute("faqKindList", faqKindList);

            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);
            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.FAQ);
            model.addAttribute("menuAuthority", menuAuthority);
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
    @GetMapping("/history/charging")
    public String showcharginghistory(
            @RequestParam(value = "companyIdSearch", required = false) Long companyId,
            @RequestParam(value = "opSearch", required = false) String searchOp,
            @RequestParam(value = "contentSearch", required = false) String searchContent,
            @RequestParam(value = "chgStartTimeFrom", required = false) String searchFrom,
            @RequestParam(value = "chgStartTimeTo", required = false) String searchTo,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model, Principal principal) {
        log.info("=== Charging History Page ===");
        log.info("== companyId: {}, searchFrom: {}, searchTo: {}, opSearch: {}, contentSearch: {}",
                companyId, searchFrom, searchTo, searchOp, searchContent);
        log.info("== page: {}, size: {}", page, size);

        // 검색 조건 저장
        model.addAttribute("selectedCompanyId", companyId);
        model.addAttribute("selectedOpSearch", searchOp);
        model.addAttribute("selectedContentSearch", searchContent);
        model.addAttribute("selectedTimeFrom", searchFrom);
        model.addAttribute("selectedTimeTo", searchTo);

        Page<ChargingHistDto> chargingHistList;
        try {
            if (companyId == null && searchOp == null && searchContent == null && searchFrom == null
                    && searchTo == null) {
                log.info("=== >> Start find all charging history");
                chargingHistList = this.chargingHistService.findAllChargingHist(page, size, principal.getName());
            } else {
                log.info("=== >> Start find charging history with search condition");
                chargingHistList = this.chargingHistService.findChargingHist(companyId, searchFrom, searchTo,
                        searchOp, searchContent, page, size, principal.getName());
            }

            // page 처리
            int totalPages = chargingHistList.getTotalPages() == 0 ? 1 : chargingHistList.getTotalPages();
            int startNumber = page * size;
            model.addAttribute("chargingHistList", chargingHistList.getContent());
            model.addAttribute("size", String.valueOf(size));
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalCount", chargingHistList.getTotalElements());
            model.addAttribute("startNumber", startNumber);
            log.info("===ChargingHistory_PageInfo >> totalPages:{}, totalCount:{}", totalPages,
                    chargingHistList.getTotalElements());
            // 데이터 내용 확인을 위한 로그 추가
            if (!chargingHistList.getContent().isEmpty()) {
                log.info("===ChargingHistory_FirstData >> {}", chargingHistList.getContent().get(0));
            }

            // select option - 검색조건
            List<CompanyListDto> companyList = this.companyService.findCompanyListAll(principal.getName());
            model.addAttribute("companyList", companyList);

            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);

            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.HIST_CHARGING);
            model.addAttribute("menuAuthority", menuAuthority);
        } catch (Exception e) {
            e.getStackTrace();
            log.error("Error occurred while fetching charging history: {}", e.getMessage(), e);
            model.addAttribute("chargingHistList", Collections.emptyList());
            model.addAttribute("companyList", Collections.emptyList());
            model.addAttribute("showListCnt", Collections.emptyList());
            model.addAttribute("menuAuthority", Collections.emptyList());
        }

        return "pages/history/charging_history";
    }

    /*
     * 이력 > 결제이력
     */
    @GetMapping("/history/payment")
    public String showpaymenthistory(
            @RequestParam(value = "companyIdSearch", required = false) Long companyId,
            @RequestParam(value = "opSearch", required = false) String searchOp,
            @RequestParam(value = "contentSearch", required = false) String searchContent,
            @RequestParam(value = "stateCode", required = false) String stateCode,
            @RequestParam(value = "transactionStart", required = false) String transactionStart,
            @RequestParam(value = "transactionEnd", required = false) String transactionEnd,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model, Principal principal) {
        log.info("=== Payment History Page ===");

        try {

            model.addAttribute("selectedCompanyId", companyId);
            model.addAttribute("selectedOpSearch", searchOp);
            model.addAttribute("selectedContentSearch", searchContent);
            model.addAttribute("selectedStateCode", stateCode);
            model.addAttribute("selectedTransactionStart", transactionStart);
            model.addAttribute("selectedTransactionEnd", transactionEnd);

            Page<PaymentHistDto> paymentHistList = this.paymentHistService.findPaymentHist(companyId, searchOp,
                    searchContent, stateCode,
                    transactionStart, transactionEnd, page, size, principal.getName());

            int totalPages = paymentHistList.getTotalPages() == 0 ? 1 : paymentHistList.getTotalPages();
            int startNumber = page * size;
            model.addAttribute("paymentHistList", paymentHistList.getContent());
            model.addAttribute("size", String.valueOf(size));
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalCount", paymentHistList.getTotalElements());
            model.addAttribute("startNumber", startNumber);
            log.info("===PaymentHist_PageInfo >> totalPages:{}, totalCount:{}", totalPages,
                    paymentHistList.getTotalElements());

            List<CompanyListDto> companyList = this.companyService.findCompanyListAll(principal.getName());
            model.addAttribute("companyList", companyList);

            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);

            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.HIST_PAYMENT);
            model.addAttribute("menuAuthority", menuAuthority);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("companyList", Collections.emptyList());
            model.addAttribute("showListCnt", Collections.emptyList());
            model.addAttribute("menuAuthority", Collections.emptyList());
        }

        return "pages/history/payment_history";
    }

    /*
     * 이력 > 통신이력
     */
    @GetMapping("/history/comm")
    public String showcommhistory(
            @RequestParam(value = "opSearch", required = false) String searchOp,
            @RequestParam(value = "contentSearch", required = false) String searchContent,
            @RequestParam(value = "recvFromSearch", required = false) String recvFrom,
            @RequestParam(value = "recvToSearch", required = false) String recvTo,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model, Principal principal) {
        log.info("=== Communication History Page ===");

        model.addAttribute("selectedOpSearch", searchOp);
        model.addAttribute("selectedContentSearch", searchContent);
        model.addAttribute("selectedRecvFrom", recvFrom);
        model.addAttribute("selectedRecvTo", recvTo);

        try {

            Page<ChgCommlogDto> chgCommlogList;
            if (searchOp == null && searchContent == null && recvFrom == null && recvTo == null) {
                log.info("=== >> Start find all communication history");
                chgCommlogList = this.chgCommlogService.findAllChgCommlog(page, size, principal.getName());
            } else {
                log.info("=== >> Start find communication history with search condition");
                chgCommlogList = this.chgCommlogService.findChgCommlog(searchOp, searchContent, recvFrom, recvTo,
                        page, size, principal.getName());
            }

            // page 처리
            int totalPages = chgCommlogList.getTotalPages() == 0 ? 1 : chgCommlogList.getTotalPages();
            int startNumber = page * size;
            model.addAttribute("chgCommlogList", chgCommlogList.getContent());
            model.addAttribute("size", String.valueOf(size));
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalCount", chgCommlogList.getTotalElements());
            model.addAttribute("startNumber", startNumber);
            log.info("===ChgCommlog_PageInfo >> totalPages:{}, totalCount:{}", totalPages,
                    chgCommlogList.getTotalElements());

            // 검색옵션
            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);

            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.HIST_COMM);
            model.addAttribute("menuAuthority", menuAuthority);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("showListCnt", Collections.emptyList());
            model.addAttribute("menuAuthority", Collections.emptyList());
        }

        return "pages/history/comm_history";
    }

    /*
     * 이력 > 에러이력
     */
    @GetMapping("/history/error")
    public String showerrorhistory(
            @RequestParam(value = "companyIdSearch", required = false) Long reqCompanyId,
            @RequestParam(value = "manfCodeSearch", required = false) String reqManfCd,
            @RequestParam(value = "opSearch", required = false) String reqOpSearch,
            @RequestParam(value = "contentSearch", required = false) String reqContentSearch,
            @RequestParam(value = "chgStartTimeFrom", required = false) String reqStartDate,
            @RequestParam(value = "chgStartTimeTo", required = false) String reqEndDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model, Principal principal) {
        log.info("=== Error History Page ===");
        log.info(
                "== reqCompanyId: {}, reqManfCd: {}, reqOpSearch: {}, reqContentSearch: {}, reqStartDate: {}, reqEndDate: {}",
                reqCompanyId, reqManfCd, reqOpSearch, reqContentSearch, reqStartDate, reqEndDate);
        log.info("== page: {}, size: {}", page, size);

        try {

            // 검색조건 옵션 저장
            model.addAttribute("selectedCompanyId", reqCompanyId);
            model.addAttribute("selectedManfCd", reqManfCd);
            model.addAttribute("selectedOpSearch", reqOpSearch);
            model.addAttribute("selectedContentSearch", reqContentSearch);
            model.addAttribute("selectedTimeFrom", reqStartDate);
            model.addAttribute("selectedTimeTo", reqEndDate);

            // 에러 list 조회
            Page<ErrorHistDto> errorHistList;
            if (reqCompanyId == null && reqOpSearch == null && reqContentSearch == null && reqStartDate == null
                    && reqEndDate == null) {
                log.info("=== >> Start find all error history");
                errorHistList = this.errorHistService.findAllErrorHist(page, size, principal.getName());
            } else {
                log.info("=== >> Start find error history with search condition");
                errorHistList = this.errorHistService.findErrorHist(reqCompanyId, reqManfCd, reqStartDate, reqEndDate,
                        reqOpSearch,
                        reqContentSearch, page, size, principal.getName());
            }

            // Page처리
            int totalpages = errorHistList.getTotalPages() == 0 ? 1 : errorHistList.getTotalPages();
            int startNumber = page * size;
            model.addAttribute("errorHistList", errorHistList.getContent());
            model.addAttribute("size", String.valueOf(size));
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalpages);
            model.addAttribute("totalCount", errorHistList.getTotalElements());
            model.addAttribute("startNumber", startNumber);
            log.info("===ErrorHist_PageInfo >> totalPages:{}, totalCount:{}", totalpages,
                    errorHistList.getTotalElements());

            // 검색옵션 조회
            List<CompanyListDto> companyList = this.companyService.findCompanyListAll(principal.getName());
            model.addAttribute("companyList", companyList);

            // 제조사 리스트
            List<CommCdBaseDto> manfCd = codeService.commonCodeStringToNum("CGMANFCD");
            model.addAttribute("manfCdList", manfCd);

            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);

            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.HIST_ERR);
            model.addAttribute("menuAuthority", menuAuthority);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("showListCnt", Collections.emptyList());
            model.addAttribute("menuAuthority", Collections.emptyList());
        }

        return "pages/history/error_history";
    }

    /*
     * 정산 > 충전결제정보
     */
    @GetMapping("/calc/chgpayment")
    public String showchgpayment(
            @RequestParam(value = "companyIdSearch", required = false) Long companyId,
            @RequestParam(value = "opSearch", required = false) String searchOp,
            @RequestParam(value = "contentSearch", required = false) String searchContent,
            @RequestParam(value = "startMonthSearch", required = false) String searchFrom,
            @RequestParam(value = "endMonthSearch", required = false) String searchTo,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model, Principal principal) {
        log.info("=== Charging Payment Information Page ===");

        try {
            // 검색 조건이 모두 null인 경우 현재 년월을 기본값으로 설정
            if (searchOp == null && searchContent == null && searchFrom == null && searchTo == null
                    && companyId == null) {
                LocalDate now = LocalDate.now();
                String currentYearMonth = now.format(DateTimeFormatter.ofPattern("yyyyMM"));
                searchFrom = currentYearMonth;
                searchTo = currentYearMonth;
                log.info("=== >> 검색 조건이 없어 현재 년월({})로 설정합니다.", currentYearMonth);
            }

            // 모델에 검색 조건 추가
            model.addAttribute("selectedOpSearch", searchOp);
            model.addAttribute("selectedContentSearch", searchContent);
            model.addAttribute("selectedStartMonth", searchFrom);
            model.addAttribute("selectedEndMonth", searchTo);
            model.addAttribute("selectedCompanyId", companyId);

            log.info("=== >> 충전 결제 정보 조회: opSearch={}, contentSearch={}, startMonth={}, endMonth={}, companyId={}",
                    searchOp, searchContent, searchFrom, searchTo, companyId);

            // 충전 결제 정보 조회 (페이징)
            Page<ChgPaymentInfoDto> chgPaymentList = this.chargingPaymentInfoService.findChgPaymentInfo(
                    searchFrom, searchTo, searchOp, searchContent, companyId, page, size, principal.getName());

            // 전체 데이터에 대한 합계 계산 (집계 쿼리 사용)
            ChgPaymentSummaryDto summary = this.chargingPaymentInfoService.calculatePaymentSummary(
                    searchFrom, searchTo, searchOp, searchContent, companyId, principal.getName());

            // 모델에 합계 정보 추가
            model.addAttribute("summary", summary);

            // 페이지 처리
            int totalPages = chgPaymentList.getTotalPages() == 0 ? 1 : chgPaymentList.getTotalPages();
            model.addAttribute("chgPaymentList", chgPaymentList.getContent());
            model.addAttribute("size", String.valueOf(size));
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalCount", chgPaymentList.getTotalElements());
            log.info("=== >> 충전 결제 정보 조회 결과: 총 페이지={}, 총 데이터={}", totalPages, chgPaymentList.getTotalElements());

            // 검색옵션
            List<CompanyListDto> companyList = this.companyService.findCompanyListAll(principal.getName());
            model.addAttribute("companyList", companyList);

            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);

            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.CALC_CHGPAYMENT);
            model.addAttribute("menuAuthority", menuAuthority);
        } catch (Exception e) {
            log.error("충전 결제 정보 조회 중 오류 발생", e);
            e.printStackTrace();
            model.addAttribute("showListCnt", Collections.emptyList());
            model.addAttribute("menuAuthority", Collections.emptyList());
            model.addAttribute("companyList", Collections.emptyList());
        }

        return "pages/calc/chgpayment";
    }

    /*
     * 정산 > 매입관리
     */
    @GetMapping("/calc/purchase")
    public String showpurchase(
            @RequestParam(value = "opSearch", required = false) String searchOp,
            @RequestParam(value = "contentSearch", required = false) String searchContent,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model, Principal principal) {
        log.info("=== Purchase Management Page ===");

        try {
            model.addAttribute("selectedOpSearch", searchOp);
            model.addAttribute("selectedContentSearch", searchContent);
            model.addAttribute("selectedStartDate", startDate);
            model.addAttribute("selectedEndDate", endDate);

            Page<PurchaseListDto> purList = this.purchaseService.findPurchaseInfoWithPagination(searchOp, searchContent,
                    startDate, endDate, page, size);

            int totalPages = purList.getTotalPages() == 0 ? 1 : purList.getTotalPages();

            model.addAttribute("purList", purList.getContent());
            model.addAttribute("size", String.valueOf(size));
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalCount", purList.getTotalElements());

            List<CommCdBaseDto> accList = codeService.findCommonCdNamesByGrpcd("ACCOUNTCD"); // 계정과목
            model.addAttribute("accList", accList);

            List<CommCdBaseDto> purchaseList = codeService.findCommonCdNamesByGrpcd("PURCHASEMTH"); // 매입거래지불수단
            model.addAttribute("purchaseList", purchaseList);

            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);

            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.CALC_PURCHASE);
            model.addAttribute("menuAuthority", menuAuthority);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("purList", Collections.emptyList());
            model.addAttribute("accList", Collections.emptyList());
            model.addAttribute("purchaseList", Collections.emptyList());
            model.addAttribute("showListCnt", Collections.emptyList());
            model.addAttribute("menuAuthority", Collections.emptyList());
        }

        return "pages/calc/purchase";
    }

    /*
     * 통계 > 매입/매출통계
     */
    @GetMapping("/statistics/purchaseandsales")
    public String showpurchaseandsales(
            @RequestParam(value = "companyIdSearch", required = false) Long companyId,
            @RequestParam(value = "opSearch", required = false) String searchOp,
            @RequestParam(value = "contentSearch", required = false) String searchContent,
            @RequestParam(value = "yearSearch", required = false) Integer year,
            Model model, Principal principal) {
        log.info("=== Purchase and Sales Statistics Page ===");
        log.info("companyId: {}, opSearch: {}, contentSearch: {}, yearSearch: {}", companyId, searchOp, searchContent,
                year);

        try {
            model.addAttribute("selectedCompanyId", companyId);
            model.addAttribute("selectedOpSearch", searchOp);
            model.addAttribute("selectedContentSearch", searchContent);
            model.addAttribute("selectedYear", year);

            PurchaseSalesBarDto pursales = this.statisticsService.searchYearPurchaseSales(companyId, searchOp,
                    searchContent, year, principal.getName());
            model.addAttribute("pursales", pursales);
            log.info("pursales >> {}", pursales.toString());

            PurchaseSalesLineChartDto lineChart = this.statisticsService.searchMonthlyPurchaseSales(companyId, searchOp,
                    searchContent, year, principal.getName());
            model.addAttribute("lineChart", lineChart);
            log.info("lineChart >> {}", lineChart.toString());

            List<CompanyListDto> companyList = this.companyService.findCompanyListAll(principal.getName());
            model.addAttribute("companyList", companyList);
            List<YearOptionDto> yearOption = this.comService.generateYearOptions();
            model.addAttribute("yearOption", yearOption);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("companyList", Collections.emptyList());
            model.addAttribute("yearOption", Collections.emptyList());
        }

        return "pages/statistics/purchaseandsales_statistics";
    }

    /*
     * 통계 > 이용률통계
     */
    @GetMapping("/statistics/usage")
    public String showusage(
            @RequestParam(value = "companyIdSearch", required = false) Long companyId,
            @RequestParam(value = "opSearch", required = false) String searchOp,
            @RequestParam(value = "contentSearch", required = false) String searchContent,
            @RequestParam(value = "yearSearch", required = false) Integer year,
            Model model, Principal principal) {
        log.info("=== Usage Statistics Page ===");
        log.info("companyId: {}, opSearch: {}, contentSearch: {}, yearSearch: {}", companyId, searchOp, searchContent,
                year);

        try {
            model.addAttribute("selectedCompanyId", companyId);
            model.addAttribute("selectedOpSearch", searchOp);
            model.addAttribute("selectedContentSearch", searchContent);
            model.addAttribute("selectedYear", year);

            UsageBarDto usage = this.statisticsService.searchYearUsage(companyId, searchOp, searchContent, year,
                    principal.getName());
            model.addAttribute("usage", usage);
            log.info("usage >> {}", usage.toString());

            UsageLineChartDto lineChart = this.statisticsService.searchMonthlyUsage(companyId, searchOp, searchContent,
                    year, principal.getName());
            model.addAttribute("lineChart", lineChart);
            log.info("lineChart >> {}", lineChart.toString());

            List<CompanyListDto> companyList = this.companyService.findCompanyListAll(principal.getName());
            model.addAttribute("companyList", companyList);

            List<YearOptionDto> yearOption = this.comService.generateYearOptions();
            model.addAttribute("yearOption", yearOption);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("companyList", Collections.emptyList());
            model.addAttribute("yearOption", Collections.emptyList());
        }

        return "pages/statistics/usage_statistics";
    }

    /*
     * 통계 > 충전량통계
     */
    @GetMapping("/statistics/totalkw")
    public String showtotalkw(
            @RequestParam(value = "companyIdSearch", required = false) Long companyId,
            @RequestParam(value = "opSearch", required = false) String searchOp,
            @RequestParam(value = "contentSearch", required = false) String searchContent,
            @RequestParam(value = "yearSearch", required = false) Integer year,
            Model model, Principal principal) {
        log.info("=== Charge Statistics Page ===");
        log.info("companyId: {}, opSearch: {}, contentSearch: {}, yearSearch: {}", companyId, searchOp, searchContent,
                year);

        try {
            model.addAttribute("selectedCompanyId", companyId);
            model.addAttribute("selectedOpSearch", searchOp);
            model.addAttribute("selectedContentSearch", searchContent);
            model.addAttribute("selectedYear", year);

            TotalkwBarDto totalkw = this.statisticsService.searchYearChargeAmount(companyId, searchOp, searchContent,
                    year, principal.getName());
            model.addAttribute("totalkw", totalkw);
            log.info("totalkw >> {}", totalkw.toString());

            TotalkwLineChartDto lineChart = this.statisticsService.searchMonthlyChargeAmount(companyId, searchOp,
                    searchContent, year, principal.getName());
            model.addAttribute("lineChart", lineChart);
            log.info("lineChart >> {}", lineChart.toString());

            List<CompanyListDto> companyList = this.companyService.findCompanyListAll(principal.getName());
            model.addAttribute("companyList", companyList);

            List<YearOptionDto> yearOption = this.comService.generateYearOptions();
            model.addAttribute("yearOption", yearOption);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("companyList", Collections.emptyList());
            model.addAttribute("yearOption", Collections.emptyList());
        }

        return "pages/statistics/totalkw_statistics";
    }

    /*
     * 통계 > 장애율통계
     */
    @GetMapping("/statistics/error")
    public String showerror(
            @RequestParam(value = "companyIdSearch", required = false) Long companyId,
            @RequestParam(value = "opSearch", required = false) String searchOp,
            @RequestParam(value = "contentSearch", required = false) String searchContent,
            @RequestParam(value = "yearSearch", required = false) Integer year,
            Model model, Principal principal) {
        log.info("=== Error Statistics Page ===");
        log.info("companyId: {}, opSearch: {}, contentSearch: {}, yearSearch: {}", companyId, searchOp, searchContent,
                year);

        try {
            model.addAttribute("selectedCompanyId", companyId);
            model.addAttribute("selectedOpSearch", searchOp);
            model.addAttribute("selectedContentSearch", searchContent);
            model.addAttribute("selectedYear", year);

            ErrorBarDto errHist = this.statisticsService.searchYearError(companyId, searchOp, searchContent, year,
                    principal.getName());
            model.addAttribute("errHist", errHist);
            log.info("errHist >> {}", errHist.toString());

            ErrorLineChartDto lineChart = this.statisticsService.searchMonthlyError(companyId, searchOp, searchContent,
                    year, principal.getName());
            model.addAttribute("lineChart", lineChart);
            log.info("lineChart >> {}", lineChart.toString());

            List<CompanyListDto> companyList = this.companyService.findCompanyListAll(principal.getName());
            model.addAttribute("companyList", companyList);

            List<YearOptionDto> yearOption = this.comService.generateYearOptions();
            model.addAttribute("yearOption", yearOption);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("companyList", Collections.emptyList());
            model.addAttribute("yearOption", Collections.emptyList());
        }

        return "pages/statistics/error_statistics";
    }

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
            Model model, Principal principal) {

        log.info("=== Biz managment List Page ===");
        log.info("== companyId: {}, companyType:{}, companyLv: {}, page: {}, size: {}", companyId, companyType,
                companyLv, page, size);

        // 검색 조건유지
        model.addAttribute("selectedCompanyId", companyId);
        model.addAttribute("selectedCompanyKind", companyType);
        model.addAttribute("selectedCompanyLv", companyLv);
        model.addAttribute("selectedSize", size);

        // 업체 등록폼 전달
        model.addAttribute("companyRegDto", new CompanyRegDto());

        Page<CompanyListDto> companyList;

        String longinId = principal.getName();

        try {
            log.info("=== Compnay DB search result >>>");

            // check null and call the approrpiate search method
            if (companyId == null && (companyType == null || companyType.isEmpty())
                    && (companyLv == null || companyLv.isEmpty())) {
                log.info("Fetching all companies >> ");
                companyList = companyService.searchCompanyAll(longinId, page, size);
            } else {
                log.info("Fetching company with condition >> companyId: {}, companyType: {}, companyLv: {}", companyId,
                        companyType, companyLv);
                companyList = companyService.searchCompanyList(longinId, companyId, companyType, companyLv, page, size);
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

            // 전체 사업자 리스트
            List<BaseCompnayDto> allCompanyList = companyService.searchAllCompanyForSelectOpt(longinId);
            model.addAttribute("allCompanyList", allCompanyList);

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
            List<CommCdBaseDto> contractStatList = codeService.findCommonCdNamesByGrpcd("CONTSTAT");
            log.info("=== contractStatList : {}", contractStatList.toString());
            model.addAttribute("contractStatList", contractStatList);

            // 유지보수업체
            List<CommCdBaseDto> mcompanyList = codeService.findCommonCdNamesByGrpcd("MCOMPANY");
            log.info("=== mcompanyList : {}", mcompanyList.toString());
            model.addAttribute("mcompanyList", mcompanyList);

            // 사업자코드
            List<CommCdBaseDto> companyCodeList = codeService.findCommonCdNamesByGrpcd("COMPANYCD");
            model.addAttribute("companyCodeList", companyCodeList);

            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);

            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.BIZ);
            model.addAttribute("menuAuthority", menuAuthority);
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
            Model model, Principal principal) {
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

            // List<CompanyListDto> companyList = this.companyService.findCompanyListAll();
            // model.addAttribute("companyList", companyList);
            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);
            // List<CommCdBaseDto> creditCardList =
            // codeService.commonCodeStringToNum("CREDITCARDCD"); // 카드사코드
            // model.addAttribute("creditCardList", creditCardList);
            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.CORP);
            model.addAttribute("menuAuthority", menuAuthority);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("bizList", Collections.emptyList());
            model.addAttribute("currentPage", Collections.emptyList());
            model.addAttribute("totalPages", Collections.emptyList());
            model.addAttribute("totalCount", Collections.emptyList());
            // model.addAttribute("companyList", Collections.emptyList());
            model.addAttribute("showListCnt", Collections.emptyList());
            // model.addAttribute("creditCardList", Collections.emptyList());
        }
        return "pages/biz/corporation_management";
    }

    /*
     * 펌웨어 > 펌웨어 버전관리
     */
    @GetMapping("/fw/version")
    public String showfwversion(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model, Principal principal) {
        log.info("=== Firmware Version Page ===");
        log.info("== page: {}, size: {}", page, size);

        try {

            // 등록된 펌웨어 버전 정보 조회
            Page<CpFwversionDto> fwVersionList = this.fwService.findFwVersionList(page, size, principal.getName());

            int totalPages = fwVersionList.getTotalPages() == 0 ? 1 : fwVersionList.getTotalPages();
            model.addAttribute("fwList", fwVersionList.getContent());
            model.addAttribute("size", String.valueOf(size));
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalCount", fwVersionList.getTotalElements());

            // select options
            // 사업자 리스트
            List<CompanyListDto> companyList = this.companyService.findCompanyListAll(principal.getName());
            model.addAttribute("companyList", companyList);

            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);

            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.FW_VERSION);
            model.addAttribute("menuAuthority", menuAuthority);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("companyList", Collections.emptyList());
            model.addAttribute("showListCnt", Collections.emptyList());
            model.addAttribute("menuAuthority", Collections.emptyList());
        }

        return "pages/firmware/fw_version";
    }

    /*
     * 펌웨어 > 펌웨어 업데이트
     */
    @GetMapping("/fw/update")
    public String showfwupdate(
            @RequestParam(value = "companyIdSearch", required = false) Long companyId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model, Principal principal) {
        log.info("=== Firmware Update Page ===");

        try {
            // model.addAttribute("selectedCompanyId", companyId);

            Pageable pageable = PageRequest.of(page, size);
            Page<ChargerListDto> cplist = Page.empty(pageable);

            int totalpages = cplist.getTotalPages() == 0 ? 1 : cplist.getTotalPages();
            model.addAttribute("cpList", cplist.getContent());
            model.addAttribute("size", String.valueOf(size));
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalpages);
            model.addAttribute("totalCount", cplist.getTotalElements());

            List<CompanyListDto> companyList = this.companyService.findCompanyListAll(principal.getName());
            model.addAttribute("companyList", companyList);

            List<CommCdBaseDto> showListCnt = codeService.commonCodeStringToNum("SHOWLISTCNT"); // 그리드 row 수
            model.addAttribute("showListCnt", showListCnt);

            MenuAuthorityBaseDto menuAuthority = this.menuAuthorityService.searchUserAuthority(principal.getName(),
                    MenuConstants.FW_UPDATE);
            model.addAttribute("menuAuthority", menuAuthority);
        } catch (Exception e) {
            e.getStackTrace();
            model.addAttribute("cpList", Collections.emptyList());
            model.addAttribute("size", String.valueOf(size));
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", 1);
            model.addAttribute("totalCount", 0L);

            model.addAttribute("companyList", Collections.emptyList());
            model.addAttribute("showListCnt", Collections.emptyList());
            model.addAttribute("menuAuthority", Collections.emptyList());
        }

        return "pages/firmware/fw_update";
    }

    /*
     * 예약
     */

    /*
     * 개인정보처리방침
     */
    @GetMapping("/policy/privacy")
    public String showprivacy() {
        log.info("=== Privacy Page ===");

        return "fragments/privacy";
    }
}
