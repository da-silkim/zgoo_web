package zgoo.cpos.repository.code;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.code.CommonCode;

public interface CommonCodeRepository extends JpaRepository<CommonCode, String>, CommonCodeRepositoryCustom {
    // @PersistenceContext
    // private EntityManager em;

    // // 저장
    // public void save(CommonCode commonCode) {
    // em.persist(commonCode);

    // }

    // // 조회
    // public CommonCode findOne(String grpcode, String commoncd) {
    // CommonCodeKey ckey = new CommonCodeKey(grpcode, commoncd);
    // return em.find(CommonCode.class, ckey);
    // }

    // // 전체조회
    // public List<CommonCode> findAll() {
    // return em.createQuery("select c from CommonCode c",
    // CommonCode.class).getResultList();
    // }

    // // 전체조회 - 조건(그룹코드)
    // public List<CommonCode> findAllByGrpCode(String grpCode) {
    // return em.createQuery("select c from CommonCode c where c.id.grpCode =
    // :grpCode", CommonCode.class)
    // .setParameter("grpCode", grpCode).getResultList();
    // }

    // // 그룹코드,그룹코드명,공통코드,공통코드명만 조회 - 조건(그룹코드) - JPQL
    // public List<GrpAndCommCdDto> findGrpAndCommCodeByGrpcode(String grpCode) {

    // return em.createQuery(
    // "select new zgoo.cpos.dto.GrpAndCommCdDto(g.grpCode, g.grpcdName,
    // c.id.commonCode, c.name)" +
    // " from GrpCode g" +
    // " inner join CommonCode c on g.grpCode = c.group.grpCode" +
    // " where g.grpCode = :grpCode",
    // GrpAndCommCdDto.class)
    // .setParameter("grpCode", grpCode)
    // .getResultList();

    // }

    // /*
    // * 삭제
    // *
    // */
    // public void deleteAllCommonCodeByGrpCode(String grpCode) {
    // em.createQuery("delete from CommonCode c where c.id.grpCode =
    // :grpCode").setParameter("grpCode", grpCode)
    // .executeUpdate();
    // }

    // public void deleteCommonCodeOne(String commonCode) {
    // em.createQuery("delete from CommonCode c where c.id.commonCode =
    // :commonCode")
    // .setParameter("commonCode", commonCode).executeUpdate();
    // }
}
