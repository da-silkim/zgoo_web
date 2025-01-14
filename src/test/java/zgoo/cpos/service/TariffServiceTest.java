package zgoo.cpos.service;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;

import zgoo.cpos.domain.company.CpPlanPolicy;
import zgoo.cpos.dto.company.CompanyDto.CpPlanDto;
import zgoo.cpos.dto.tariff.TariffDto.TariffPolicyDto;
import zgoo.cpos.repository.company.CompanyRepository;

@SpringBootTest
public class TariffServiceTest {

    @Autowired
    CompanyService companyService;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    TariffService tservice;

    @Test
    @DisplayName("요금제 저장 테스트")
    @Rollback(false)
    public void savePlanPolicyTest() throws Exception {
        // given
        Long companyId = 1L;

        CpPlanDto dto = CpPlanDto.builder()
                .planName("동아기본요금제")
                .build();

        // when
        tservice.savePlanPolicy(companyId, dto);

        // then

    }

    @Test
    @DisplayName("요금제 조회 테스트 - companyId")
    @Rollback(false)
    public void findPlanPolicyTest() throws Exception {
        // given
        Long companyId = 20L;

        // when
        List<CpPlanPolicy> resultList = tservice.searchPlanPolicyByCompanyId(companyId);

        // then
        if (resultList.isEmpty()) {
            System.out.println("No policy plan list");
        } else {
            for (CpPlanPolicy em : resultList) {
                System.out.println("== result >> ID: " + em.getId() + ", name: " + em.getName());
            }
        }

    }

    @Test
    @DisplayName("단가 조회 - 전체")
    @Rollback(false)
    public void findTariffPolicyTest() throws Exception {
        // given
        // when
        Page<TariffPolicyDto> resultList = tservice.searchTariffPolicyAll(1, 1);

        System.out.println(resultList.toString());
    }
}
