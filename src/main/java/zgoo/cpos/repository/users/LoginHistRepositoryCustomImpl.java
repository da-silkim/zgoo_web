package zgoo.cpos.repository.users;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.users.LoginHist;
import zgoo.cpos.domain.users.QLoginHist;

@Slf4j
@RequiredArgsConstructor
public class LoginHistRepositoryCustomImpl implements LoginHistRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QLoginHist loginHist = QLoginHist.loginHist;

    @Override
    public LoginHist findRecentLoginHist(String userId) {
        return queryFactory
            .selectFrom(loginHist)
            .where(loginHist.userId.eq(userId))
            .orderBy(loginHist.loginDate.desc(), loginHist.id.desc())
            .limit(1)
            .fetchOne();
    }   
}
