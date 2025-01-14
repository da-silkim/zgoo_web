package zgoo.cpos.repository.tariff;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.company.QCpPlanPolicy;
import zgoo.cpos.domain.tariff.QTariffInfo;
import zgoo.cpos.domain.tariff.QTariffPolicy;
import zgoo.cpos.domain.tariff.TariffPolicy;
import zgoo.cpos.dto.tariff.TariffDto.TariffInfoDto;
import zgoo.cpos.dto.tariff.TariffDto.TariffPolicyDto;

@RequiredArgsConstructor
@Slf4j
public class TariffPolicyRepositoryCustomImpl implements TariffPolicyRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QCompany company = QCompany.company;
    QTariffPolicy tpolicy = QTariffPolicy.tariffPolicy;
    QCpPlanPolicy ppolicy = QCpPlanPolicy.cpPlanPolicy;
    QTariffInfo tinfo = QTariffInfo.tariffInfo;
    QCommonCode applyCodeName = new QCommonCode("applyCode");

    @Override
    public Page<TariffPolicyDto> findAllTariffPolicyPaging(Pageable page) {
        List<TariffPolicyDto> resultList = queryFactory
                .select(Projections.fields(
                        TariffPolicyDto.class,
                        tpolicy.id.as("tariffId"),
                        tpolicy.policy.id.as("policyId"),
                        ppolicy.name.as("policyName"),
                        company.id.as("companyId"),
                        company.companyName.as("companyName"),
                        tpolicy.apply_date.as("applyDate"),
                        tpolicy.apply_code.as("applyCode"),
                        applyCodeName.name.as("applyCodeName")))
                .from(tpolicy)
                .leftJoin(ppolicy).on(tpolicy.policy.eq(ppolicy))
                .leftJoin(company).on(ppolicy.company.eq(company))
                .leftJoin(applyCodeName).on(tpolicy.apply_code.eq(applyCodeName.commonCode))
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .orderBy(tpolicy.regDt.desc())
                .fetch();

        /* data수 카운트 */
        long total = queryFactory
                .select(tpolicy.count())
                .from(tpolicy)
                .fetchOne();

        return new PageImpl<>(resultList, page, total);
    }

    @Override
    public Page<TariffPolicyDto> findTariffPolicyByCompanyIdPaging(Pageable pageable, Long companyId) {
        List<TariffPolicyDto> resultList = queryFactory
                .select(Projections.fields(TariffPolicyDto.class,
                        tpolicy.id.as("tariffId"),
                        tpolicy.policy.id.as("policyId"),
                        ppolicy.name.as("policyName"),
                        company.id.as("companyId"),
                        company.companyName.as("companyName"),
                        tpolicy.apply_date.as("applyDate"),
                        tpolicy.apply_code.as("applyCode"),
                        applyCodeName.name.as("applyCodeName")))
                .from(tpolicy)
                .leftJoin(ppolicy).on(tpolicy.policy.eq(ppolicy))
                .leftJoin(company).on(ppolicy.company.eq(company))
                .leftJoin(applyCodeName).on(tpolicy.apply_code.eq(applyCodeName.commonCode))
                .where(company.id.eq(companyId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(tpolicy.regDt.desc())
                .fetch();

        /* data수 카운트 */
        long total = queryFactory
                .select(tpolicy.count())
                .from(tpolicy)
                .fetchOne();

        return new PageImpl<>(resultList, pageable, total);
    }

    @Override
    public TariffPolicy findTariffPolicyByPolicyId(Long policyId) {
        return queryFactory.selectFrom(tpolicy).where(tpolicy.policy.id.eq(policyId)).fetchOne();
    }

    @Override
    public Long findTariffIdByPlanName(String planName) {
        return queryFactory
                .select(tpolicy.id)
                .from(tpolicy)
                .leftJoin(ppolicy).on(tpolicy.policy.id.eq(ppolicy.id))
                .where(ppolicy.name.eq(planName))
                .fetchOne();
    }

    @Override
    public Optional<TariffPolicy> findTariffPolicyByPlanName(String planName) {

        TariffPolicy result = queryFactory.selectFrom(tpolicy)
                .leftJoin(ppolicy).on(tpolicy.policy.id.eq(ppolicy.id))
                .where(ppolicy.name.eq(planName))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<TariffPolicy> findTariffListByPolicyId(Long policyId) {
        return queryFactory.selectFrom(tpolicy).where(tpolicy.policy.id.eq(policyId)).fetch();
    }

    @Override
    public TariffPolicy findByApplyCodeAndPolicyId(String applyCode, Long policyId) {
        return queryFactory.selectFrom(tpolicy)
                .where(tpolicy.apply_code.eq(applyCode).and(tpolicy.policy.id.eq(policyId))).fetchOne();
    }

    @Override
    public Optional<List<TariffInfoDto>> findTariffInfoListByTariffId(Long tariffId) {
        List<TariffInfoDto> resultList = queryFactory
                .select(Projections.fields(TariffInfoDto.class,
                        tinfo.id.as("tariffinfoId"),
                        tinfo.hour.as("hour"),
                        tinfo.memSlowUnitCost.as("memSlowUnitCost"),
                        tinfo.nomemSlowUnitCost.as("nomemSlowUnitCost"),
                        tinfo.memFastUnitCost.as("memFastUnitCost"),
                        tinfo.nomemFastUnitCost.as("nomemFastUnitCost")))
                .from(tinfo)
                .where(tinfo.tariffPolicy.id.eq(tariffId))
                .fetch();

        return Optional.ofNullable(resultList);
    }

}
