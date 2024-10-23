package zgoo.cpos.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import zgoo.cpos.domain.CommonCode;
import zgoo.cpos.type.CommonCodeKey;

@Repository
public class CommonCodeRepository {
    @PersistenceContext
    private EntityManager em;

    // 저장
    public void save(CommonCode commonCode) {
        em.persist(commonCode);
    }

    // 조회
    public CommonCode findOne(String grpcode, String commoncd) {
        CommonCodeKey ckey = new CommonCodeKey(grpcode, commoncd);
        return em.find(CommonCode.class, ckey);
    }

    // 전체조회
    public List<CommonCode> findAll() {
        return em.createQuery("select c from CommonCode c", CommonCode.class).getResultList();
    }

    // 전체조회 - 조건(그룹코드)
    public List<CommonCode> findAllByGrpCode(String grpCode) {
        return em.createQuery("select c from CommonCode c where c.id.grpCode = :grpCode", CommonCode.class)
                .setParameter("grpCode", grpCode).getResultList();
    }
}
