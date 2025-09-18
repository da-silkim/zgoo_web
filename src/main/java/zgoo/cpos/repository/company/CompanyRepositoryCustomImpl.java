package zgoo.cpos.repository.company;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.domain.company.Company;
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.company.QCompanyContract;
import zgoo.cpos.domain.company.QCompanyRelationInfo;
import zgoo.cpos.dto.company.CompanyDto;
import zgoo.cpos.dto.company.CompanyDto.BaseCompnayDto;
import zgoo.cpos.dto.company.CompanyDto.CompanyListDto;
import zgoo.cpos.util.LocaleUtil;
import zgoo.cpos.util.QueryUtils;

@RequiredArgsConstructor
@Slf4j
public class CompanyRepositoryCustomImpl implements CompanyRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QCompany company = QCompany.company;
    QCompanyRelationInfo relation = QCompanyRelationInfo.companyRelationInfo;
    QCompany parentCompany = new QCompany("parentCompany");
    QCompanyContract contract = QCompanyContract.companyContract;
    QCommonCode companyLevelCode = new QCommonCode("companyLv");
    QCommonCode companyTypeCode = new QCommonCode("companyType");
    QCommonCode contractStatusCode = new QCommonCode("contractStatus");

    @Override
    public Page<CompanyListDto> findCompanyListPaging(Pageable pageable, String levelPath, boolean isSuperAdmin) {
        BooleanBuilder builder = new BooleanBuilder();

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        List<CompanyListDto> resultList = queryFactory
                .select(Projections.fields(CompanyDto.CompanyListDto.class,
                        company.id.as("companyId"),
                        company.companyName.as("companyName"),
                        company.companyLv.as("companyLv"),
                        LocaleUtil.isEnglish() ? companyLevelCode.nameEn.as("companyLvName")
                                : companyLevelCode.name.as("companyLvName"),
                        company.companyType.as("companyType"),
                        LocaleUtil.isEnglish() ? companyTypeCode.nameEn.as("companyTypeName")
                                : companyTypeCode.name.as("companyTypeName"),
                        parentCompany.companyName.as("parentCompanyName"),
                        contract.contractedAt.as("contractedAt"),
                        contract.contractEnd.as("contractEnd"),
                        contract.contractStatus.as("contractStatus"),
                        LocaleUtil.isEnglish() ? contractStatusCode.nameEn.as("contractStatName")
                                : contractStatusCode.name.as("contractStatName")))
                .from(company)
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(parentCompany).on(relation.parentCompany.eq(parentCompany))
                .leftJoin(contract).on(company.companyContract.eq(contract))
                .leftJoin(companyLevelCode).on(company.companyLv.eq(companyLevelCode.commonCode))
                .leftJoin(companyTypeCode).on(company.companyType.eq(companyTypeCode.commonCode))
                .leftJoin(contractStatusCode).on(contract.contractStatus.eq(contractStatusCode.commonCode))
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(company.createdAt.desc())
                .fetch();

        /* data수 카운트 */
        long total = queryFactory
                .select(company.count())
                .from(company)
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(resultList, pageable, total);
    }

    @Override
    public List<CompanyListDto> findCompanyListForSelectOptCl(String levelPath, boolean isSuperAdmin) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        List<CompanyListDto> companyList = queryFactory
                .select(Projections.fields(CompanyDto.CompanyListDto.class,
                        company.id.as("companyId"),
                        company.companyName.as("companyName"),
                        company.companyLv.as("companyLv"),
                        LocaleUtil.isEnglish() ? companyLevelCode.nameEn.as("companyLvName")
                                : companyLevelCode.name.as("companyLvName"),
                        company.companyType.as("companyType"),
                        LocaleUtil.isEnglish() ? companyTypeCode.nameEn.as("companyTypeName")
                                : companyTypeCode.name.as("companyTypeName"),
                        parentCompany.companyName.as("parentCompanyName"),
                        contract.contractedAt.as("contractedAt"),
                        contract.contractEnd.as("contractEnd"),
                        contract.contractStatus.as("contractStatus"),
                        LocaleUtil.isEnglish() ? contractStatusCode.nameEn.as("contractStatName")
                                : contractStatusCode.name.as("contractStatName")))
                .from(company)
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(parentCompany).on(relation.parentCompany.eq(parentCompany))
                .leftJoin(contract).on(company.companyContract.eq(contract))
                .leftJoin(companyLevelCode).on(company.companyLv.eq(companyLevelCode.commonCode))
                .leftJoin(companyTypeCode).on(company.companyType.eq(companyTypeCode.commonCode))
                .leftJoin(contractStatusCode)
                .where(builder)
                .on(contract.contractStatus.eq(contractStatusCode.commonCode))
                .orderBy(company.createdAt.desc())
                .fetch();

        return companyList;
    }

    @Override
    public Company findCompanyOne(Long id) {
        return queryFactory
                .selectFrom(company)
                .where(company.id.eq(id))
                .fetchOne();
    }

    @Override
    public Optional<Company> findByCompanyName(String companyName) {
        Company result = queryFactory
                .selectFrom(company)
                .where(company.companyName.eq(companyName))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<Company> findByLevelPathStartingWith(String levelPathPrefix) {
        log.info("=== findByLevelPathStartingWith : {}", levelPathPrefix);
        return queryFactory
                .selectFrom(company)
                .join(relation).on(company.companyRelationInfo.eq(relation))
                .where(relation.levelPath.startsWith(levelPathPrefix))
                .orderBy(relation.levelPath.asc())
                .fetch();
    }

    @Override
    public String findLevelPathByCompanyId(Long companyId) {
        return queryFactory
                .select(relation.levelPath)
                .from(company)
                .join(relation).on(company.companyRelationInfo.eq(relation))
                .where(company.id.eq(companyId))
                .fetchOne();
    }

    @Override
    public String findCompanyLvById(Long companyId) {
        return queryFactory
                .select(company.companyLv)
                .from(company)
                .where(company.id.eq(companyId))
                .fetchOne();
    }

    @Override
    public List<BaseCompnayDto> findCompanyListForSelectOptBc(String levelPath, boolean isSuperAdmin) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        List<BaseCompnayDto> resultList = queryFactory.select(Projections.fields(BaseCompnayDto.class,
                company.id.as("companyId"),
                company.companyName.as("companyName"),
                company.companyLv.as("companyLv"),
                LocaleUtil.isEnglish() ? companyLevelCode.nameEn.as("companyLvName")
                        : companyLevelCode.name.as("companyLvName"),
                company.companyType.as("companyType"),
                LocaleUtil.isEnglish() ? companyTypeCode.nameEn.as("companyTypeName")
                        : companyTypeCode.name.as("companyTypeName")))
                .from(company)
                .leftJoin(companyLevelCode).on(company.companyLv.eq(companyLevelCode.commonCode))
                .leftJoin(companyTypeCode).on(company.companyType.eq(companyTypeCode.commonCode))
                .where(builder)
                .orderBy(company.createdAt.asc())
                .fetch();

        return resultList;
    }

    @Override
    public Page<CompanyListDto> findCompanyListByConditionPaging(Long companyId, String companyType, String companyLv,
            Pageable pageable, String levelPath, boolean isSuperAdmin) {

        BooleanBuilder builder = new BooleanBuilder();
        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        if (companyId != null) {
            builder.and(company.id.eq(companyId));
        }

        if (companyType != null && !companyType.isEmpty()) {
            builder.and(company.companyType.eq(companyType));
        }

        if (companyLv != null && !companyLv.isEmpty()) {
            builder.and(company.companyLv.eq(companyLv));
        }

        List<CompanyListDto> resultList = queryFactory
                .select(Projections.fields(CompanyDto.CompanyListDto.class,
                        company.id.as("companyId"),
                        company.companyName.as("companyName"),
                        company.companyLv.as("companyLv"),
                        LocaleUtil.isEnglish() ? companyLevelCode.nameEn.as("companyLvName")
                                : companyLevelCode.name.as("companyLvName"),
                        company.companyType.as("companyType"),
                        LocaleUtil.isEnglish() ? companyTypeCode.nameEn.as("companyTypeName")
                                : companyTypeCode.name.as("companyTypeName"),
                        parentCompany.companyName.as("parentCompanyName"),
                        contract.contractedAt.as("contractedAt"),
                        contract.contractEnd.as("contractEnd"),
                        contract.contractStatus.as("contractStatus"),
                        LocaleUtil.isEnglish() ? contractStatusCode.nameEn.as("contractStatName")
                                : contractStatusCode.name.as("contractStatName")))
                .from(company)
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(parentCompany).on(relation.parentCompany.eq(parentCompany))
                .leftJoin(contract).on(company.companyContract.eq(contract))
                .leftJoin(companyLevelCode).on(company.companyLv.eq(companyLevelCode.commonCode))
                .leftJoin(companyTypeCode).on(company.companyType.eq(companyTypeCode.commonCode))
                .leftJoin(contractStatusCode).on(contract.contractStatus.eq(contractStatusCode.commonCode))
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(company.createdAt.desc())
                .fetch();

        /* data수 카운트 */
        long total = queryFactory
                .select(company.count())
                .from(company)
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(resultList, pageable, total);

    }

}
