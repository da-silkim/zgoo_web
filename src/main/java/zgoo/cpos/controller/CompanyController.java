package zgoo.cpos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.company.CompanyDto;
import zgoo.cpos.service.CompanyService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CompanyController {
    private final CompanyService companyService;

    /*
     * 사업자 등록(폼이동)
     */
    @GetMapping(value = "/company/new")
    public String createCompanyForm(Model model) {
        log.info("company_reg_form");

        // model.addAttribute("companyRegDto", new CompanyRegDto());

        // // 업체레벨에 따른 관계 선택을 위한 업체 리스트 가져오기
        // List<Company> companies = companyService.findAllCompany();

        // // TOP level
        // List<CompanyLvInfoDto> topCompanies = companies.stream()
        // .filter(company -> "TOP".equals(company.getCompanyLv()))
        // .map(company -> new CompanyLvInfoDto(company.getId(),
        // company.getCompanyName()))
        // .collect(Collectors.toList());

        // // companyLv가 "MID"인 CompanyLvInfoDto 리스트
        // List<CompanyLvInfoDto> midCompanies = companies.stream()
        // .filter(company -> "MID".equals(company.getCompanyLv()))
        // .map(company -> new CompanyLvInfoDto(company.getId(),
        // company.getCompanyName()))
        // .collect(Collectors.toList());

        // model.addAttribute("topList", topCompanies != null ? topCompanies :
        // Collections.emptyList());
        // model.addAttribute("midList", midCompanies != null ? midCompanies :
        // Collections.emptyList());

        return "company/createCompanyForm";
    }

    /*
     * 사업자 등록
     */
    @PostMapping(value = "/company/new")
    public String create(@RequestBody CompanyDto.CompanyRegDto dto, BindingResult result) {
        // valid error 발생시 등록폼 이동
        if (result.hasErrors()) {
            log.error("사업자 등록 에러: {}", result.getAllErrors());
            // TODO: web에 팝업띄우기
            return "company/createCompanyForm";
        }

        // companyService.saveCompany(dto);

        return "redirect:/";

    }
}
