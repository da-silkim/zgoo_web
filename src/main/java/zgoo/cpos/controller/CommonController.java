package zgoo.cpos.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.CommonCdDto;
import zgoo.cpos.dto.GrpCodeDto;
import zgoo.cpos.service.CodeService;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CommonController {

    private final CodeService commonCdService;

    @GetMapping(value = "/dashboard")
    public String showDashboard(Model model) {
        log.info("Dashboard Home");
        // 필요한 data를 model에 추가 !!!

        return "pages/dashboard";
    }

    @GetMapping("/code_management/list")
    public String showCommonCodeList(Model model) {
        log.info("==== code management page(list) ======");

        // group code 등록폼 전달
        model.addAttribute("grpcodeDto", new GrpCodeDto());
        // commoncode 등록폼 전달
        model.addAttribute("commonCdDto", new CommonCdDto());

        // 그룹코드 조회
        List<GrpCodeDto> gcdlist = commonCdService.findGrpCodeAll();
        model.addAttribute("gcdlist", gcdlist);

        // 공통코드 조회
        List<CommonCdDto> ccdlist = commonCdService.findCommonCodeAll();
        model.addAttribute("ccdlist", ccdlist);

        return "pages/system/code_management";
    }

    @PostMapping("/code_management/grpcode/new")
    public String createGrpCode(@Valid GrpCodeDto dto, BindingResult result) {
        log.info("==== create group code(list) ======");
        if (result.hasErrors()) {
            log.error("그룹코드 등록 에러: {}", result.getAllErrors());
            // TODO: web에 팝업띄우기
            return "pages/system/code_management";
        }

        log.info("groupcode_dto >> {}", dto.toString());

        commonCdService.saveGrpCode(dto);

        return "redirect:/code_management/list";
    }

    @PostMapping("/code_management/commoncd/new")
    public String createCommonCode(@Valid CommonCdDto dto, BindingResult result) {
        log.info("==== create common code(list) ======");
        // valid error 발생시 등록폼 이동
        if (result.hasErrors()) {
            log.error("공통코드 등록 에러: {}", result.getAllErrors());
            // TODO: web에 팝업띄우기
            return "pages/system/code_management";
        }

        log.info("commoncd_dto >> {}", dto.toString());

        // 여기서 grpCode를 조회하고 saveCommonCode에 넘겼을때, gpr_code 테이블에 insert 수행 ,, 왜??
        // GrpCode grpCode = commonCdService.findGrpOne(dto.getId().getGrpCode());
        // log.info("grpcode_find : {}", grpCode);

        commonCdService.saveCommonCode(dto);

        return "redirect:/code_management/list";
    }

}
