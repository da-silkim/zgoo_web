package zgoo.cpos.repository.company;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zgoo.cpos.domain.company.CompanyRoaming;
import zgoo.cpos.domain.company.QCompanyRoaming;
import zgoo.cpos.dto.company.CompanyDto.CompanyRoamingtDto;

@RequiredArgsConstructor
public class CompanyRoamingRepositoryCustomImpl implements CompanyRoamingRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QCompanyRoaming r = QCompanyRoaming.companyRoaming;

    @Override
    public List<CompanyRoamingtDto> findAllByCompanyIdDto(Long companyId) {
        return queryFactory.select(Projections.fields(CompanyRoamingtDto.class,
                r.institutionCode.as("institutionCode"),
                r.institutionKey.as("institutionKey"),
                r.institutionEmail.as("institutionEmail")))
                .from(r)
                .where(r.company.id.eq(companyId))
                .fetch();
    }

    @Override
    public List<CompanyRoaming> findAllByCompanyId(Long companyId) {
        return queryFactory.selectFrom(r)
                .where(r.company.id.eq(companyId))
                .fetch();
    }

    @Override
    public void deleteAllByCompanyId(Long companyId) {
        queryFactory.delete(r).where(r.company.id.eq(companyId)).execute();
    }

}
