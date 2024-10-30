package zgoo.cpos.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.Company;
import zgoo.cpos.dto.company.CompanyListDto;
import zgoo.cpos.dto.company.CompanyLvInfoDto;
import zgoo.cpos.dto.company.CompanyRegDto;
import zgoo.cpos.service.CompanyService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CompanyController {
    private final CompanyService companyService;

    /*
     * 사업자 목록 조회
     */
    @GetMapping(value = "/company/list")
    public String companyList(Model model) {
        log.info("company_list");
        List<CompanyListDto> cdtolist = null;
        List<Company> companyList = companyService.findAllCompany();
        // List<CompanyRelationInfo> clist =
        // companyService.findAllCompanyWithRelation();

        if (companyList.size() > 0) {
            log.info("==========");
            log.info("company_name : {}", companyList.get(0).getCompanyName());
            log.info("parent_id : {}", companyList.get(0).getCompanyRelationInfo().getParentId());
            log.info("==========");

            cdtolist = companyList.stream()
                    .map(cinfo -> new CompanyListDto(cinfo.getId(),
                            cinfo.getCompanyName(),
                            cinfo.getCompanyType(),
                            cinfo.getCompanyLv(),
                            cinfo.getBizNum(),
                            cinfo.getBizType(),
                            cinfo.getConsignmentPayment(),
                            cinfo.getCompanyRelationInfo().getParentId(),
                            cinfo.getCreatedAt(),
                            cinfo.getUpdatedAt()))
                    .collect(Collectors.toList());
        } else {
            log.info("==== No comany list : {}", companyList);
        }

        model.addAttribute("clist", cdtolist);
        return "company/companyList";
    }

    /*
     * 사업자 등록(폼이동)
     */
    @GetMapping(value = "/company/new")
    public String createCompanyForm(Model model) {
        log.info("company_reg_form");

        model.addAttribute("companyRegDto", new CompanyRegDto());

        // 업체레벨에 따른 관계 선택을 위한 업체 리스트 가져오기
        List<Company> companies = companyService.findAllCompany();

        // TOP level
        List<CompanyLvInfoDto> topCompanies = companies.stream()
                .filter(company -> "TOP".equals(company.getCompanyLv()))
                .map(company -> new CompanyLvInfoDto(company.getId(), company.getCompanyName()))
                .collect(Collectors.toList());

        // companyLv가 "MID"인 CompanyLvInfoDto 리스트
        List<CompanyLvInfoDto> midCompanies = companies.stream()
                .filter(company -> "MID".equals(company.getCompanyLv()))
                .map(company -> new CompanyLvInfoDto(company.getId(), company.getCompanyName()))
                .collect(Collectors.toList());

        model.addAttribute("topList", topCompanies != null ? topCompanies : Collections.emptyList());
        model.addAttribute("midList", midCompanies != null ? midCompanies : Collections.emptyList());

        return "company/createCompanyForm";
    }

    /*
     * 사업자 등록
     */
    @PostMapping(value = "/company/new")
    public String create(@Valid CompanyRegDto dto, BindingResult result) {
        // valid error 발생시 등록폼 이동
        if (result.hasErrors()) {
            log.error("사업자 등록 에러: {}", result.getAllErrors());
            // TODO: web에 팝업띄우기
            return "company/createCompanyForm";
        }

        companyService.saveCompany(dto);

        return "redirect:/";

    }
}
