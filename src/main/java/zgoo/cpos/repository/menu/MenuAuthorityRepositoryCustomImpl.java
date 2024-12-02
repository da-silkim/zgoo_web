package zgoo.cpos.repository.menu;

import java.util.List;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.domain.company.QCompany;

@Slf4j
@RequiredArgsConstructor
public class MenuAuthorityRepositoryCustomImpl implements MenuAuthorityRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QCompany company = QCompany.company;
    QCommonCode commonCode = QCommonCode.commonCode1;
    
    @Override
    public List<Tuple> companyAuthorityList() {
        return queryFactory
            .select(company.id, company.companyName, commonCode.commonCode, commonCode.name)
            .from(company, commonCode)
            .where(commonCode.group.grpCode.eq("MENUACCLV")
                .and(commonCode.commonCode.ne("SU")))
            .orderBy(company.id.asc(), commonCode.commonCode.asc())
            .fetch();
    }
}
