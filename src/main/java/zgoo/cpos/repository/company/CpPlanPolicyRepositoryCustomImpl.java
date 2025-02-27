package zgoo.cpos.repository.company;

import java.util.List;
import java.util.Optional;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zgoo.cpos.domain.company.CpPlanPolicy;
import zgoo.cpos.domain.company.QCpPlanPolicy;
import zgoo.cpos.dto.company.CompanyDto.CpPlanDto;

@RequiredArgsConstructor
public class CpPlanPolicyRepositoryCustomImpl implements CpPlanPolicyRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QCpPlanPolicy policy = QCpPlanPolicy.cpPlanPolicy;

    @Override
    public List<CpPlanDto> findAllByCompanyIdDto(Long companyId) {
        return queryFactory.select(Projections.fields(CpPlanDto.class,
                policy.name.as("planName")))
                .from(policy)
                .where(policy.company.id.eq(companyId))
                .fetch();
    }

    @Override
    public List<CpPlanPolicy> findAllByCompanyId(Long companyId) {
        return queryFactory.selectFrom(policy)
                .where(policy.company.id.eq(companyId))
                .fetch();
    }

    @Override
    public void deleteAllByCompanyId(Long companyId) {
        queryFactory.delete(policy).where(policy.company.id.eq(companyId)).execute();
    }

    @Override
    public List<CpPlanPolicy> findAll() {
        return queryFactory.selectFrom(policy).fetch();
    }

    @Override
    public CpPlanPolicy findByPlanName(String planName) {
        return queryFactory.selectFrom(policy).where(policy.name.eq(planName)).fetchOne();
    }

    @Override
    public CpPlanPolicy findByPlanNameAndCompanyId(String planName, Long companyId) {
        return queryFactory.selectFrom(policy)
                .where(policy.name.eq(planName).and(policy.company.id.eq(companyId))).fetchOne();
    }

    @Override
    public Optional<List<CpPlanDto>> findPlanListByCompanyId(Long companyId) {
        List<CpPlanDto> result = queryFactory.select(Projections.fields(CpPlanDto.class,
                policy.id.as("policyId"),
                policy.company.id.as("companyId"),
                policy.name.as("planName")))
                .from(policy)
                .where(policy.company.id.eq(companyId))
                .fetch();

        return Optional.ofNullable(result);
    }

}
