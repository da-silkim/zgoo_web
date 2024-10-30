package zgoo.cpos.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.code.CommonCdDto;
import zgoo.cpos.dto.code.GrpCodeDto;
import zgoo.cpos.service.CodeService;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PageController {

    private final CodeService codeService;

    /*
     * 대시보드
     */
    @GetMapping(value = "/dashboard")
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
        return "pages/charge/charge_station_list";
    }

    /*
     * 충전기관리 > 충전기리스트
     */
    @GetMapping("/charger/list")
    public String showchargerlist(Model model) {
        log.info("=== Charger List Page ===");
        return "pages/charge/charger_list";
    }

    /*
     * 실시간충전리스트
     */
    @GetMapping("/charging/list")
    public String showcharginglist(Model model) {
        log.info("=== Charging List Page ===");
        return "pages/charge/charge_real_time_list";
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
        model.addAttribute("commonCdDto", new CommonCdDto());

        // 그룹코드 조회
        List<GrpCodeDto> gcdlist = codeService.findGrpCodeAll();
        model.addAttribute("gcdlist", gcdlist);

        // 공통코드 조회
        List<CommonCdDto> ccdlist = codeService.findCommonCodeAll();
        model.addAttribute("ccdlist", ccdlist);

        return "pages/system/code_management";
    }

    /*
     * 시스템 > 사용자 관리
     */

    @GetMapping("/system/user/list")
    public String showuserlist(Model model) {
        log.info("=== User List Page ===");
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

    /*
     * 시스템 > 요금제관리
     */

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
