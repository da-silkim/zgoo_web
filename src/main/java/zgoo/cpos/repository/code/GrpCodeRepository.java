package zgoo.cpos.repository.code;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.code.GrpCode;

// @Repository
public interface GrpCodeRepository extends JpaRepository<GrpCode, String>, GrpCodeRepositoryCustom {

    // @PersistenceContext
    // private EntityManager em;

    // // 저장
    // public void save(GrpCode grpCode) {
    // em.persist(grpCode);
    // }

    // // 조회
    // public GrpCode findOne(String grpcode) {
    // return em.find(GrpCode.class, grpcode);
    // }

    // // 조회(조건) - 그룹코드명
    // public GrpCode findByGrpName(String name) {
    // return em.createQuery("select g from GrpCode g where g.grpcdName = :name",
    // GrpCode.class)
    // .setParameter("name", name)
    // .getSingleResult();
    // }

    // // 전체조회
    // public List<GrpCode> findAll() {
    // return em.createQuery("select g from GrpCode g",
    // GrpCode.class).getResultList();
    // }

    // // 삭제
    // public void deleteGrpCode(String grpCode) {
    // em.createQuery("delete from GrpCode g where g.grpCode =
    // :grpCode").setParameter("grpCode", grpCode)
    // .executeUpdate();
    // }
}
