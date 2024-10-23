package zgoo.cpos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CommonController {

    @GetMapping(value = "/dashboard")
    public String showDashboard(Model model) {
        log.info("Dashboard Home");
        // 필요한 data를 model에 추가 !!!

        return "pages/dashboard";
    }

    @GetMapping("/commoncode")
    public String showCommonCodeList() {
        log.info("[PAGE] : CommonCode LIst Page");

        return "pages/system/code_management";
    }

}
