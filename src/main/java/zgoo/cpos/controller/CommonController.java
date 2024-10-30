package zgoo.cpos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.code.CommonCdDto;
import zgoo.cpos.dto.code.GrpCodeDto;
import zgoo.cpos.service.CodeService;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CommonController {

    private final CodeService codeService;

    @PostMapping("/code_management/grpcode/new")
    public String createGrpCode(@Valid GrpCodeDto dto, BindingResult result) {
        log.info("==== create group code(list) ======");
        if (result.hasErrors()) {
            log.error("그룹코드 등록 에러: {}", result.getAllErrors());
            // TODO: web에 팝업띄우기
            return "pages/system/code_management";
        }

        log.info("groupcode_dto >> {}", dto.toString());

        codeService.saveGrpCode(dto);

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

        codeService.saveCommonCode(dto);

        return "redirect:/code_management/list";
    }

}
