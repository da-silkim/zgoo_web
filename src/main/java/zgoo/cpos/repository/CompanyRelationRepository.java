package zgoo.cpos.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import zgoo.cpos.domain.CompanyRelationInfo;

@Repository
public class CompanyRelationRepository {
    @PersistenceContext
    private EntityManager em;

    // 저장
    public void save(CompanyRelationInfo rinfo) {
        em.persist(rinfo);
    }

    // 조회 - 전체
    public List<CompanyRelationInfo> findAll() {
        return em.createQuery("select r from CompanyRelationInfo r", CompanyRelationInfo.class).getResultList();
    }
}
