package zgoo.cpos.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import zgoo.cpos.domain.GrpCode;

@Repository
public class GrpCodeRepository {

    @PersistenceContext
    private EntityManager em;

    // 저장
    public void save(GrpCode grpCode) {
        em.persist(grpCode);
    }

    // 조회
    public GrpCode findOne(String grpcode) {
        return em.find(GrpCode.class, grpcode);
    }

    // 전체조회
    public List<GrpCode> findAll() {
        return em.createQuery("select g from GrpCode", GrpCode.class).getResultList();
    }
}
