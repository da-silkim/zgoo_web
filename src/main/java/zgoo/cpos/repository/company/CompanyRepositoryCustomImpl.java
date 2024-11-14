package zgoo.cpos.repository.company;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.company.QCompanyContract;
import zgoo.cpos.domain.company.QCompanyRelationInfo;
import zgoo.cpos.dto.company.CompanyDto;
import zgoo.cpos.dto.company.CompanyDto.CompanyListDto;

@RequiredArgsConstructor
public class CompanyRepositoryCustomImpl implements CompanyRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QCompany company = QCompany.company;
    QCompanyRelationInfo relation = QCompanyRelationInfo.companyRelationInfo;
    QCompanyContract contract = QCompanyContract.companyContract;

    @Override
    public List<CompanyDto.CompanyListDto> findCompanyListAllCustom() {
        return queryFactory.select(Projections.fields(CompanyDto.CompanyListDto.class,
                company.id.as("companyId"),
                company.companyName.as("companyName"),
                company.companyLv.as("companyLv"),
                company.companyType.as("companyType"),
                relation.parentCompanyName.as("parentCompanyName"),
                contract.contractedAt.as("contractedAt"),
                contract.contractEnd.as("contractEnd"),
                contract.contractStatus.as("contractStatus")))
                .from(company)
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(contract).on(company.companyContract.eq(contract))
                .fetch();
    }

    @Override
    public List<CompanyListDto> findCompanyListById(Long id) {
        return queryFactory.select(Projections.fields(CompanyDto.CompanyListDto.class,
                company.id.as("companyId"),
                company.companyName.as("companyName"),
                company.companyLv.as("companyLv"),
                company.companyType.as("companyType"),
                relation.parentCompanyName.as("parentCompanyName"),
                contract.contractedAt.as("contractedAt"),
                contract.contractEnd.as("contractEnd"),
                contract.contractStatus.as("contractStatus")))
                .from(company)
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(contract).on(company.companyContract.eq(contract))
                .where(company.id.eq(id))
                .fetch();
    }

    @Override
    public List<CompanyListDto> findCompanyListByType(String type) {
        return queryFactory.select(Projections.fields(CompanyDto.CompanyListDto.class,
                company.id.as("companyId"),
                company.companyName.as("companyName"),
                company.companyLv.as("companyLv"),
                company.companyType.as("companyType"),
                relation.parentCompanyName.as("parentCompanyName"),
                contract.contractedAt.as("contractedAt"),
                contract.contractEnd.as("contractEnd"),
                contract.contractStatus.as("contractStatus")))
                .from(company)
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(contract).on(company.companyContract.eq(contract))
                .where(company.companyType.eq(type))
                .fetch();
    }

    @Override
    public List<CompanyListDto> findCompanyListByLv(String level) {
        return queryFactory.select(Projections.fields(CompanyDto.CompanyListDto.class,
                company.id.as("companyId"),
                company.companyName.as("companyName"),
                company.companyLv.as("companyLv"),
                company.companyType.as("companyType"),
                relation.parentCompanyName.as("parentCompanyName"),
                contract.contractedAt.as("contractedAt"),
                contract.contractEnd.as("contractEnd"),
                contract.contractStatus.as("contractStatus")))
                .from(company)
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(contract).on(company.companyContract.eq(contract))
                .where(company.companyLv.eq(level))
                .fetch();
    }

}
