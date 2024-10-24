package zgoo.cpos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PageController {

    // 로그인
    // @RequestMapping("/login")
    // public String Login() {
    //     return "pages/login/login_page";
    // }

    // 대시보드
    // @RequestMapping("/main")
    // public String Dashboard() {
    //     return "pages/dashboard";
    // }
    
    // 지도
    @RequestMapping("/map")
    public String Map() {
        return "pages/map/map";
    }


    // ------------ 회원관리 ------------ //
    // 회원 리스트
    @RequestMapping("/member/member-list")
    public String MemberList() {
        return "pages/member/member_list";
    }

    // 회원 인증 내역
    @RequestMapping("/member/member-auth")
    public String MemberAuth() {
        return "pages/member/member_authentication";
    }


    // ------------ 충전소 관리 ------------ //
    // 충전소 리스트
    @RequestMapping("/charge/cs-list")
    public String CsList() {
        return "pages/charge/cs_list";
    }

    // ------------ 충전기 관리 ------------ //
    // 충전기 리스트
    @RequestMapping("/charge/cp-list")
    public String CpList() {
        return "pages/charge/cp_list";
    }


    // ------------ 충전현황 ------------ //
    // 실시간 충전 리스트
    @RequestMapping("/charge/cp-rtime-list")
    public String CpRTimeList() {
        return "pages/charge/cp_real_time_list";
    }


    // ------------ 시스템 ------------ //
    // 모델관리
    @RequestMapping("/system/model")
    public String ModelManagement() {
        return "pages/system/model_management";
    }

    // 공통코드 관리
    @RequestMapping("/system/comcode")
    public String CodeManagement() {
        return "pages/system/code_management";
    }

    // 사용자 관리
    @RequestMapping("/system/user")
    public String UserManagement() {
        return "pages/system/user_management";
    }

    // 공지사항 관리
    @RequestMapping("/system/notice")
    public String NoticeManagement() {
        return "pages/system/notice_management";
    }

    // 메뉴접근권한 관리
    @RequestMapping("/system/authority")
    public String AuthorityManagement() {
        return "pages/system/authority_management";
    }

    // 에러코드 관리
    @RequestMapping("/system/errcode")
    public String ErrcodeManagement() {
        return "pages/system/errcode_management";
    }

    // 요금제 관리
    @RequestMapping("/system/tariff")
    public String TariffManagement() {
        return "pages/system/tariff_management";
    }


    // ------------ 유지보수 ------------ //
    // 장애관리
    @RequestMapping("/maintenance/error")
    public String ErrorManagement() {
        return "pages/maintenance/error_management";
    }


    // ------------ 제어 ------------ //
    // 충전기 제어
    @RequestMapping("/control/cp-control")
    public String CpControl() {
        return "pages/control/cp_control";
    }


    // ------------ 고객센터 ------------ //
    // 1:1 문의
    @RequestMapping("/customer/voc")
    public String Voc() {
        return "pages/cutomer/voc";
    }

    // FAQ
    @RequestMapping("/customer/faq")
    public String Faq() {
        return "pages/cutomer/faq";
    }


    // ------------ 업체관리 ------------ //
    // 사업자 관리
    @RequestMapping("/biz/biz")
    public String BizManagement() {
        return "pages/biz/biz_management";
    }

    // 법인 관리
    @RequestMapping("/biz/corp")
    public String CorpManagement() {
        return "pages/biz/corporation_management";
    }
}
