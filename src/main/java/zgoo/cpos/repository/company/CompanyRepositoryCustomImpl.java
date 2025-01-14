package zgoo.cpos.repository.company;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.code.QCommonCode;
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
    QCompanyContract contract = QCompanyContract.companyContract;
    QCommonCode companyLevelCode = new QCommonCode("companyLv");
    QCommonCode companyTypeCode = new QCommonCode("companyType");
    QCommonCode contractStatusCode = new QCommonCode("contractStatus");

    @Override
    public Page<CompanyListDto> findCompanyListAllCustom(Pageable pageable) {
        List<CompanyListDto> resultList = queryFactory
                .select(Projections.fields(CompanyDto.CompanyListDto.class,
                        company.id.as("companyId"),
                        company.companyName.as("companyName"),
                        company.companyLv.as("companyLv"),
                        companyLevelCode.name.as("companyLvName"),
                        company.companyType.as("companyType"),
                        companyTypeCode.name.as("companyTypeName"),
                        relation.parentCompanyName.as("parentCompanyName"),
                        contract.contractedAt.as("contractedAt"),
                        contract.contractEnd.as("contractEnd"),
                        contract.contractStatus.as("contractStatus"),
                        contractStatusCode.name.as("contractStatName")))
                .from(company)
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(contract).on(company.companyContract.eq(contract))
                .leftJoin(companyLevelCode).on(company.companyLv.eq(companyLevelCode.commonCode))
                .leftJoin(companyTypeCode).on(company.companyType.eq(companyTypeCode.commonCode))
                .leftJoin(contractStatusCode)
                .on(contract.contractStatus.eq(contractStatusCode.commonCode))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
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
    public Page<CompanyListDto> findCompanyListById(Long id, Pageable pageable) {
        List<CompanyListDto> resultList = queryFactory
                .select(Projections.fields(CompanyDto.CompanyListDto.class,
                        company.id.as("companyId"),
                        company.companyName.as("companyName"),
                        company.companyLv.as("companyLv"),
                        companyLevelCode.name.as("companyLvName"),
                        company.companyType.as("companyType"),
                        companyTypeCode.name.as("companyTypeName"),
                        relation.parentCompanyName.as("parentCompanyName"),
                        contract.contractedAt.as("contractedAt"),
                        contract.contractEnd.as("contractEnd"),
                        contract.contractStatus.as("contractStatus"),
                        contractStatusCode.name.as("contractStatName")))
                .from(company)
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
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
    public Page<CompanyListDto> findCompanyListByType(String type, Pageable pageable) {
        List<CompanyListDto> resultList = queryFactory
                .select(Projections.fields(CompanyDto.CompanyListDto.class,
                        company.id.as("companyId"),
                        company.companyName.as("companyName"),
                        company.companyLv.as("companyLv"),
                        companyLevelCode.name.as("companyLvName"),
                        company.companyType.as("companyType"),
                        companyTypeCode.name.as("companyTypeName"),
                        relation.parentCompanyName.as("parentCompanyName"),
                        contract.contractedAt.as("contractedAt"),
                        contract.contractEnd.as("contractEnd"),
                        contract.contractStatus.as("contractStatus"),
                        contractStatusCode.name.as("contractStatName")))
                .from(company)
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
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

        /* data수 카운트 */
        long total = queryFactory
                .select(company.count())
                .from(company)
                .fetchOne();

        return new PageImpl<>(resultList, pageable, total);
    }

    @Override
    public Page<CompanyListDto> findCompanyListByLv(String level, Pageable pageable) {
        List<CompanyListDto> resultList = queryFactory
                .select(Projections.fields(CompanyDto.CompanyListDto.class,
                        company.id.as("companyId"),
                        company.companyName.as("companyName"),
                        company.companyLv.as("companyLv"),
                        companyLevelCode.name.as("companyLvName"),
                        company.companyType.as("companyType"),
                        companyTypeCode.name.as("companyTypeName"),
                        relation.parentCompanyName.as("parentCompanyName"),
                        contract.contractedAt.as("contractedAt"),
                        contract.contractEnd.as("contractEnd"),
                        contract.contractStatus.as("contractStatus"),
                        contractStatusCode.name.as("contractStatName")))
                .from(company)
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(contract).on(company.companyContract.eq(contract))
                .leftJoin(companyLevelCode).on(company.companyLv.eq(companyLevelCode.commonCode))
                .leftJoin(companyTypeCode).on(company.companyType.eq(companyTypeCode.commonCode))
                .leftJoin(contractStatusCode)
                .on(contract.contractStatus.eq(contractStatusCode.commonCode))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(company.companyLv.eq(level))
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
    public List<BaseCompnayDto> findAllCompanyForSelectOpt() {
        return queryFactory.select(Projections.fields(BaseCompnayDto.class,
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
    }

}
