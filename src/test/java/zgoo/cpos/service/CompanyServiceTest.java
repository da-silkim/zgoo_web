package zgoo.cpos.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import zgoo.cpos.repository.CompanyRepository;

@SpringBootTest
@Transactional
public class CompanyServiceTest {

    @Autowired
    CompanyService companyService;
    @Autowired
    CompanyRepository companyRepository;

    @Test
    public void 업체등록테스트() throws Exception {
        // // given
        // Company company = new Company();
        // company.setCompanyName("test");
        // company.setCompanyType("OP");
        // company.setCompanyLv("ROOT");
        // company.setBizNum("12341234");
        // company.setBizType("법인");
        // company.setBizKind("제조업");
        // company.setCeoName("홍길동");
        // company.setHeadPhone("02-1111-2222");
        // company.setZipcode("123-123");
        // company.setAddress("서울특별시 금천구 가산디지털2로 135");
        // company.setAddressDetail("가산어반워크 725,6호");
        // company.setStaffName("담당자1");
        // company.setStaffEmail("test@mail.com");
        // company.setStaffTel(null);
        // company.setStaffPhone(null);
        // company.setConsignmentPayment("자체");
        // company.setCreatedAt(LocalDateTime.now());
        // company.setUpdatedAt(null);

        // // when
        // Long saveId = companyService.saveCompany(company);

        // // then
        // assertEquals(company, companyRepository.findOne(saveId));
    }

    @Test
    public void 업체명_중복등록_예외() throws Exception {
        // // given
        // Company company = new Company();
        // company.setCompanyName("test");
        // company.setCompanyType("OP");
        // company.setCompanyLv("ROOT");
        // company.setBizNum("12341234");
        // company.setBizType("법인");
        // company.setBizKind("제조업");
        // company.setCeoName("홍길동");
        // company.setHeadPhone("02-1111-2222");
        // company.setZipcode("123-123");
        // company.setAddress("서울특별시 금천구 가산디지털2로 135");
        // company.setAddressDetail("가산어반워크 725,6호");
        // company.setStaffName("담당자1");
        // company.setStaffEmail("test@mail.com");
        // company.setStaffTel(null);
        // company.setStaffPhone(null);
        // company.setConsignmentPayment("자체");
        // company.setCreatedAt(LocalDateTime.now());
        // company.setUpdatedAt(null);

        // Company company2 = new Company();
        // company2.setCompanyName("test");
        // company2.setCompanyType("OP");
        // company2.setCompanyLv("ROOT");
        // company2.setBizNum("12341234");
        // company2.setBizType("법인");
        // company2.setBizKind("제조업");
        // company2.setCeoName("홍길동");
        // company2.setHeadPhone("02-1111-2222");
        // company2.setZipcode("123-123");
        // company2.setAddress("서울특별시 금천구 가산디지털2로 135");
        // company2.setAddressDetail("가산어반워크 725,6호");
        // company2.setStaffName("담당자1");
        // company2.setStaffEmail("test@mail.com");
        // company2.setStaffTel(null);
        // company2.setStaffPhone(null);
        // company2.setConsignmentPayment("자체");
        // company2.setCreatedAt(LocalDateTime.now());
        // company2.setUpdatedAt(null);

        // // when
        // assertThrows(DataIntegrityViolationException.class, () -> {
        // companyService.saveCompany(company);
        // companyService.saveCompany(company2);
        // });

        // // then

    }

    @Test
    public void 전체조회() throws Exception {
        // // given
        // Company company = Company.builder()
        // .companyName(null)
        // .build();
        // company.setCompanyName("test");
        // company.setCompanyType("OP");
        // company.setCompanyLv("ROOT");
        // company.setBizNum("12341234");
        // company.setBizType("법인");
        // company.setConsignmentPayment("자체");
        // company.setCreatedAt(LocalDateTime.now());
        // company.setUpdatedAt(null);

        // Company company1 = new Company();
        // company1.setCompanyName("test1");
        // company1.setCompanyType("OP");
        // company1.setCompanyLv("ROOT");
        // company1.setBizNum("12341234");
        // company1.setBizType("법인");
        // company1.setConsignmentPayment("자체");
        // company1.setCreatedAt(LocalDateTime.now());
        // company1.setUpdatedAt(null);

        // companyService.saveCompany(company);
        // companyService.saveCompany(company1);

        // // when
        // List<Company> list = companyService.findAllCompany();

        // for (Company data : list) {
        // System.out.println("company_list:" + data.toString());
        // }

        // // then
        // assertEquals(2, list.size());
    }

    @Test
    public void 조건조회() throws Exception {

        // System.out.println("Test STart!!@");
        // // given
        // CompanyRegDto dto = new CompanyRegDto();
        // dto.setCompanyName("test");
        // dto.setCompanyType("OP");
        // dto.setCompanyLv("TOP");
        // dto.setBizNum("12341234");
        // dto.setBizType("법인");
        // dto.setConsignmentPayment("C");
        // dto.setParentId("해당없음");
        // companyService.saveCompany(dto);

        // // when
        // List<Company> list_top = companyService.findCompanyByLv("TOP");
        // List<Company> list_mid = companyService.findCompanyByLv("MID");

        // for (Company data : list_top) {
        // System.out.println("company_TOP_list:" + data.toString());
        // }

        // for (Company data : list_mid) {
        // System.out.println("company_MID_list:" + data.toString());
        // }

        // // then
    }
}
