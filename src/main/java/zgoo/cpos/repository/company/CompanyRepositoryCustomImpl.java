package zgoo.cpos.repository.company;

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
import zgoo.cpos.domain.company.Company;
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.company.QCompanyContract;
import zgoo.cpos.domain.company.QCompanyRelationInfo;
import zgoo.cpos.dto.company.CompanyDto;
import zgoo.cpos.dto.company.CompanyDto.BaseCompnayDto;
import zgoo.cpos.dto.company.CompanyDto.CompanyListDto;

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
        // 슈퍼 관리자가 아니고 levelPath가 있는 경우 조건 추가
        List<CompanyListDto> resultList;
        if (!isSuperAdmin) {
            resultList = queryFactory
                    .select(Projections.fields(CompanyDto.CompanyListDto.class,
                            company.id.as("companyId"),
                            company.companyName.as("companyName"),
                            company.companyLv.as("companyLv"),
                            companyLevelCode.name.as("companyLvName"),
                            company.companyType.as("companyType"),
                            companyTypeCode.name.as("companyTypeName"),
                            parentCompany.companyName.as("parentCompanyName"),
                            contract.contractedAt.as("contractedAt"),
                            contract.contractEnd.as("contractEnd"),
                            contract.contractStatus.as("contractStatus"),
                            contractStatusCode.name.as("contractStatName")))
                    .from(company)
                    .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                    .leftJoin(parentCompany).on(relation.parentCompany.eq(parentCompany))
                    .leftJoin(contract).on(company.companyContract.eq(contract))
                    .leftJoin(companyLevelCode).on(company.companyLv.eq(companyLevelCode.commonCode))
                    .leftJoin(companyTypeCode).on(company.companyType.eq(companyTypeCode.commonCode))
                    .leftJoin(contractStatusCode).on(contract.contractStatus.eq(contractStatusCode.commonCode))
                    .where(
                            relation.levelPath.eq(levelPath)
                                    .or(relation.levelPath.startsWith(levelPath + ".")))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(company.createdAt.desc())
                    .fetch();
        } else {
            // 슈퍼 관리자이거나 levelPath가 없는 경우 모든 회사 조회
            resultList = queryFactory
                    .select(Projections.fields(CompanyDto.CompanyListDto.class,
                            company.id.as("companyId"),
                            company.companyName.as("companyName"),
                            company.companyLv.as("companyLv"),
                            companyLevelCode.name.as("companyLvName"),
                            company.companyType.as("companyType"),
                            companyTypeCode.name.as("companyTypeName"),
                            parentCompany.companyName.as("parentCompanyName"),
                            contract.contractedAt.as("contractedAt"),
                            contract.contractEnd.as("contractEnd"),
                            contract.contractStatus.as("contractStatus"),
                            contractStatusCode.name.as("contractStatName")))
                    .from(company)
                    .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                    .leftJoin(parentCompany).on(relation.parentCompany.eq(parentCompany))
                    .leftJoin(contract).on(company.companyContract.eq(contract))
                    .leftJoin(companyLevelCode).on(company.companyLv.eq(companyLevelCode.commonCode))
                    .leftJoin(companyTypeCode).on(company.companyType.eq(companyTypeCode.commonCode))
                    .leftJoin(contractStatusCode).on(contract.contractStatus.eq(contractStatusCode.commonCode))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(company.createdAt.desc())
                    .fetch();
        }

        /* data수 카운트 */
        long total;
        if (!isSuperAdmin) {
            total = queryFactory
                    .select(company.count())
                    .from(company)
                    .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                    .where(
                            relation.levelPath.eq(levelPath)
                                    .or(relation.levelPath.startsWith(levelPath + ".")))
                    .fetchOne();
        } else {
            total = queryFactory
                    .select(company.count())
                    .from(company)
                    .fetchOne();
        }

        return new PageImpl<>(resultList, pageable, total);
    }

    @Override
    public Page<CompanyListDto> findCompanyListByIdPaging(Long id, Pageable pageable) {
        List<CompanyListDto> resultList = queryFactory
                .select(Projections.fields(CompanyDto.CompanyListDto.class,
                        company.id.as("companyId"),
                        company.companyName.as("companyName"),
                        company.companyLv.as("companyLv"),
                        companyLevelCode.name.as("companyLvName"),
                        company.companyType.as("companyType"),
                        companyTypeCode.name.as("companyTypeName"),
                        parentCompany.companyName.as("parentCompanyName"),
                        contract.contractedAt.as("contractedAt"),
                        contract.contractEnd.as("contractEnd"),
                        contract.contractStatus.as("contractStatus"),
                        contractStatusCode.name.as("contractStatName")))
                .from(company)
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(parentCompany).on(relation.parentCompany.eq(parentCompany))
                .leftJoin(contract).on(company.companyContract.eq(contract))
                .leftJoin(companyLevelCode).on(company.companyLv.eq(companyLevelCode.commonCode))
                .leftJoin(companyTypeCode).on(company.companyType.eq(companyTypeCode.commonCode))
                .leftJoin(contractStatusCode)
                .on(contract.contractStatus.eq(contractStatusCode.commonCode))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(company.id.eq(id))
                .orderBy(company.createdAt.desc())
                .fetch();

        /* data수 카운트 */
        long total = queryFactory
                .select(company.count())
                .from(company)
                .fetchOne();

        return new PageImpl<>(resultList, pageable, total);
    }

    @Override
    public Page<CompanyListDto> findCompanyListByTypePaging(String type, String levelPath, Pageable pageable,
            boolean isSuperAdmin) {

        List<CompanyListDto> resultList;
        if (!isSuperAdmin) {
            resultList = queryFactory
                    .select(Projections.fields(CompanyDto.CompanyListDto.class,
                            company.id.as("companyId"),
                            company.companyName.as("companyName"),
                            company.companyLv.as("companyLv"),
                            companyLevelCode.name.as("companyLvName"),
                            company.companyType.as("companyType"),
                            companyTypeCode.name.as("companyTypeName"),
                            parentCompany.companyName.as("parentCompanyName"),
                            contract.contractedAt.as("contractedAt"),
                            contract.contractEnd.as("contractEnd"),
                            contract.contractStatus.as("contractStatus"),
                            contractStatusCode.name.as("contractStatName")))
                    .from(company)
                    .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                    .leftJoin(parentCompany).on(relation.parentCompany.eq(parentCompany))
                    .leftJoin(contract).on(company.companyContract.eq(contract))
                    .leftJoin(companyLevelCode).on(company.companyLv.eq(companyLevelCode.commonCode))
                    .leftJoin(companyTypeCode).on(company.companyType.eq(companyTypeCode.commonCode))
                    .leftJoin(contractStatusCode)
                    .on(contract.contractStatus.eq(contractStatusCode.commonCode))
                    .where(company.companyType.eq(type)
                            .and(relation.levelPath.eq(levelPath).or(relation.levelPath.startsWith(levelPath + "."))))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(company.createdAt.desc())
                    .fetch();
        } else {
            resultList = queryFactory
                    .select(Projections.fields(CompanyDto.CompanyListDto.class,
                            company.id.as("companyId"),
                            company.companyName.as("companyName"),
                            company.companyLv.as("companyLv"),
                            companyLevelCode.name.as("companyLvName"),
                            company.companyType.as("companyType"),
                            companyTypeCode.name.as("companyTypeName"),
                            parentCompany.companyName.as("parentCompanyName"),
                            contract.contractedAt.as("contractedAt"),
                            contract.contractEnd.as("contractEnd"),
                            contract.contractStatus.as("contractStatus"),
                            contractStatusCode.name.as("contractStatName")))
                    .from(company)
                    .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                    .leftJoin(parentCompany).on(relation.parentCompany.eq(parentCompany))
                    .leftJoin(contract).on(company.companyContract.eq(contract))
                    .leftJoin(companyLevelCode).on(company.companyLv.eq(companyLevelCode.commonCode))
                    .leftJoin(companyTypeCode).on(company.companyType.eq(companyTypeCode.commonCode))
                    .leftJoin(contractStatusCode)
                    .on(contract.contractStatus.eq(contractStatusCode.commonCode))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .where(company.companyType.eq(type))
                    .orderBy(company.createdAt.desc())
                    .fetch();
        }

        /* data수 카운트 */
        long total;
        if (!isSuperAdmin) {
            total = queryFactory
                    .select(company.count())
                    .from(company)
                    .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                    .where(
                            company.companyType.eq(type)
                                    .and(
                                            relation.levelPath.eq(levelPath)
                                                    .or(relation.levelPath.startsWith(levelPath + "."))))
                    .fetchOne();
        } else {
            total = queryFactory
                    .select(company.count())
                    .from(company)
                    .fetchOne();
        }

        return new PageImpl<>(resultList, pageable, total);
    }

    @Override
    public Page<CompanyListDto> findCompanyListByLvPaging(String level, String levelPath, Pageable pageable,
            boolean isSuperAdmin) {
        List<CompanyListDto> resultList;
        if (!isSuperAdmin) {
            resultList = queryFactory
                    .select(Projections.fields(CompanyDto.CompanyListDto.class,
                            company.id.as("companyId"),
                            company.companyName.as("companyName"),
                            company.companyLv.as("companyLv"),
                            companyLevelCode.name.as("companyLvName"),
                            company.companyType.as("companyType"),
                            companyTypeCode.name.as("companyTypeName"),
                            parentCompany.companyName.as("parentCompanyName"),
                            contract.contractedAt.as("contractedAt"),
                            contract.contractEnd.as("contractEnd"),
                            contract.contractStatus.as("contractStatus"),
                            contractStatusCode.name.as("contractStatName")))
                    .from(company)
                    .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                    .leftJoin(parentCompany).on(relation.parentCompany.eq(parentCompany))
                    .leftJoin(contract).on(company.companyContract.eq(contract))
                    .leftJoin(companyLevelCode).on(company.companyLv.eq(companyLevelCode.commonCode))
                    .leftJoin(companyTypeCode).on(company.companyType.eq(companyTypeCode.commonCode))
                    .leftJoin(contractStatusCode)
                    .on(contract.contractStatus.eq(contractStatusCode.commonCode))
                    .where(
                            company.companyLv.eq(level)
                                    .and(
                                            relation.levelPath.eq(levelPath)
                                                    .or(relation.levelPath.startsWith(levelPath + "."))))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(company.createdAt.desc())
                    .fetch();
        } else {
            resultList = queryFactory
                    .select(Projections.fields(CompanyDto.CompanyListDto.class,
                            company.id.as("companyId"),
                            company.companyName.as("companyName"),
                            company.companyLv.as("companyLv"),
                            companyLevelCode.name.as("companyLvName"),
                            company.companyType.as("companyType"),
                            companyTypeCode.name.as("companyTypeName"),
                            parentCompany.companyName.as("parentCompanyName"),
                            contract.contractedAt.as("contractedAt"),
                            contract.contractEnd.as("contractEnd"),
                            contract.contractStatus.as("contractStatus"),
                            contractStatusCode.name.as("contractStatName")))
                    .from(company)
                    .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                    .leftJoin(parentCompany).on(relation.parentCompany.eq(parentCompany))
                    .leftJoin(contract).on(company.companyContract.eq(contract))
                    .leftJoin(companyLevelCode).on(company.companyLv.eq(companyLevelCode.commonCode))
                    .leftJoin(companyTypeCode).on(company.companyType.eq(companyTypeCode.commonCode))
                    .leftJoin(contractStatusCode).on(contract.contractStatus.eq(contractStatusCode.commonCode))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .where(company.companyLv.eq(level))
                    .orderBy(company.createdAt.desc())
                    .fetch();
        }
        /* data수 카운트 */
        long total;
        if (!isSuperAdmin) {
            total = queryFactory
                    .select(company.count())
                    .from(company)
                    .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                    .where(
                            company.companyLv.eq(level)
                                    .and(
                                            relation.levelPath.eq(levelPath)
                                                    .or(relation.levelPath.startsWith(levelPath + "."))))
                    .fetchOne();
        } else {
            total = queryFactory
                    .select(company.count())
                    .from(company)
                    .fetchOne();
        }
        return new PageImpl<>(resultList, pageable, total);
    }

    @Override
    public List<CompanyListDto> findCompanyListForSelectOptCl(String levelPath, boolean isSuperAdmin) {

        List<CompanyListDto> companyList;

        if (isSuperAdmin) {
            companyList = queryFactory
                    .select(Projections.fields(CompanyDto.CompanyListDto.class,
                            company.id.as("companyId"),
                            company.companyName.as("companyName"),
                            company.companyLv.as("companyLv"),
                            companyLevelCode.name.as("companyLvName"),
                            company.companyType.as("companyType"),
                            companyTypeCode.name.as("companyTypeName"),
                            parentCompany.companyName.as("parentCompanyName"),
                            contract.contractedAt.as("contractedAt"),
                            contract.contractEnd.as("contractEnd"),
                            contract.contractStatus.as("contractStatus"),
                            contractStatusCode.name.as("contractStatName")))
                    .from(company)
                    .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                    .leftJoin(parentCompany).on(relation.parentCompany.eq(parentCompany))
                    .leftJoin(contract).on(company.companyContract.eq(contract))
                    .leftJoin(companyLevelCode).on(company.companyLv.eq(companyLevelCode.commonCode))
                    .leftJoin(companyTypeCode).on(company.companyType.eq(companyTypeCode.commonCode))
                    .leftJoin(contractStatusCode)
                    .on(contract.contractStatus.eq(contractStatusCode.commonCode))
                    .orderBy(company.createdAt.desc())
                    .fetch();
        } else {
            companyList = queryFactory
                    .select(Projections.fields(CompanyDto.CompanyListDto.class,
                            company.id.as("companyId"),
                            company.companyName.as("companyName"),
                            company.companyLv.as("companyLv"),
                            companyLevelCode.name.as("companyLvName"),
                            company.companyType.as("companyType"),
                            companyTypeCode.name.as("companyTypeName"),
                            parentCompany.companyName.as("parentCompanyName"),
                            contract.contractedAt.as("contractedAt"),
                            contract.contractEnd.as("contractEnd"),
                            contract.contractStatus.as("contractStatus"),
                            contractStatusCode.name.as("contractStatName")))
                    .from(company)
                    .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                    .leftJoin(parentCompany).on(relation.parentCompany.eq(parentCompany))
                    .leftJoin(contract).on(company.companyContract.eq(contract))
                    .leftJoin(companyLevelCode).on(company.companyLv.eq(companyLevelCode.commonCode))
                    .leftJoin(companyTypeCode).on(company.companyType.eq(companyTypeCode.commonCode))
                    .leftJoin(contractStatusCode)
                    .where(
                            relation.levelPath.eq(levelPath)
                                    .or(relation.levelPath.startsWith(levelPath + ".")))
                    .on(contract.contractStatus.eq(contractStatusCode.commonCode))
                    .orderBy(company.createdAt.desc())
                    .fetch();
        }

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
        List<BaseCompnayDto> resultList;
        if (isSuperAdmin) {
            resultList = queryFactory.select(Projections.fields(BaseCompnayDto.class,
                    company.id.as("companyId"),
                    company.companyName.as("companyName"),
                    company.companyLv.as("companyLv"),
                    companyLevelCode.name.as("companyLvName"),
                    company.companyType.as("companyType"),
                    companyTypeCode.name.as("companyTypeName")))
                    .from(company)
                    .leftJoin(companyLevelCode).on(company.companyLv.eq(companyLevelCode.commonCode))
                    .leftJoin(companyTypeCode).on(company.companyType.eq(companyTypeCode.commonCode))
                    .orderBy(company.createdAt.asc())
                    .fetch();
        } else {
            resultList = queryFactory.select(Projections.fields(BaseCompnayDto.class,
                    company.id.as("companyId"),
                    company.companyName.as("companyName"),
                    company.companyLv.as("companyLv"),
                    companyLevelCode.name.as("companyLvName"),
                    company.companyType.as("companyType"),
                    companyTypeCode.name.as("companyTypeName")))
                    .from(company)
                    .leftJoin(companyLevelCode).on(company.companyLv.eq(companyLevelCode.commonCode))
                    .leftJoin(companyTypeCode).on(company.companyType.eq(companyTypeCode.commonCode))
                    .where(
                            relation.levelPath.eq(levelPath)
                                    .or(relation.levelPath.startsWith(levelPath + ".")))
                    .orderBy(company.createdAt.asc())
                    .fetch();
        }

        return resultList;
    }

}
