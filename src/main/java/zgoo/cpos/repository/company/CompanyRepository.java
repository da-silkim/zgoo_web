package zgoo.cpos.repository.company;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.company.Company;

public interface CompanyRepository extends JpaRepository<Company, Long>, CompanyRepositoryCustom {

    // @PersistenceContext
    // private EntityManager em;

    // // 저장
    // public void save(Company company) {
    // em.persist(company);
    // }

    // // 조회-단일
    // public Company findOne(Long id) {
    // return em.find(Company.class, id);
    // }

    // // 조회-전체
    // public List<Company> findAll() {
    // return em.createQuery("select c from Company c",
    // Company.class).getResultList();
    // }

    // // 조회 - 상위사업자
    // public List<CompanyLvInfoDto> findCompanyByLevel(String companyLv) {
    // return em.createQuery(
    // "select new zgoo.cpos.dto.CompanyLvInfoDto(c.companyId, c.companyName) " +
    // "from Company c where c.companyLv = :companyLv",
    // CompanyLvInfoDto.class)
    // .setParameter("companyLv", companyLv)
    // .getResultList();

    // }

}
