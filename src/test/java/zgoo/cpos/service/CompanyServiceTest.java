package zgoo.cpos.service;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zgoo.cpos.dto.company.CompanyDto;
import zgoo.cpos.dto.company.CompanyDto.CompanyListDto;
import zgoo.cpos.repository.company.CompanyRepository;

@SpringBootTest
@Transactional
public class CompanyServiceTest {

    @Autowired
    CompanyService companyService;
    @Autowired
    CompanyRepository companyRepository;

    @Test
    @DisplayName("조회 - 업체관리 업체리스트 차트 리스트 가져오기")
    public void findCompanyListAllTest() throws Exception {
        // given

        // when
        List<CompanyDto.CompanyListDto> flist = companyService.searchCompanyList();

        for (CompanyListDto companyListDto : flist) {
            System.out.println("=== result : " + companyListDto.toString());
            System.out.println("company_name:" + companyListDto.getCompanyName());
        }

        // then
    }

    @Test
    @Rollback(false)
    @DisplayName("저장 >> 사업자/관계/로밍/담당자/PG/계약정보 저장")
    public void saveCompanyTest() throws Exception {
        // given
        CompanyDto.CompanyRegDto dto = CompanyDto.CompanyRegDto.builder()
                .companyType("OP")
                .companyLv("TOP")
                .companyName("동아일렉콤")
                .bizNum("1234")
                .ceoName("홍길동")
                .headPhone(null)
                .bizKind("1")
                .bizType("PB")
                .parentId(1L)
                .institutionCode("ME")
                .institutionKey("test")
                .institutionEmail("test@mail.com")
                .staffName("담당자1")
                .consignmentPayment("C")
                .sspMallId("testmallid")
                .mid("testmid")
                .merchantKey("testmerchantKey")
                .contractStatus("1")
                .contractedAt(LocalDateTime.now())
                .contractStart(LocalDateTime.now())
                .contractEnd(LocalDateTime.now())
                .asCompany("1")
                .asNum("1234444")
                .build();

        // when
        companyService.saveCompany(dto);

        // then
        List<CompanyListDto> flist = companyService.searchCompanyList();
        for (CompanyListDto data : flist) {
            System.out.println("=== result : " + data.toString());
        }
    }

}
