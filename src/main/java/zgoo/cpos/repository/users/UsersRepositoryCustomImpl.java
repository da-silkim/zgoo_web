package zgoo.cpos.repository.users;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.users.QUsers;
import zgoo.cpos.domain.users.Users;

@RequiredArgsConstructor
public class UsersRepositoryCustomImpl implements UsersRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QUsers users = QUsers.users;
    private final QCompany company = QCompany.company;

    @Override
    public List<Users> findAll() {
        return queryFactory
            .selectFrom(users)
            .leftJoin(users.company, company)
            .fetch();
    }

    @Override
    public Users findUserOne(String userID) {
        return queryFactory
            .selectFrom(users)
            .leftJoin(users.company, company)
            .where(users.userId.eq(userID))
            .fetchOne();
    }

    @Override
    public Long deleteUserOne(String userID) {
        return queryFactory
            .delete(users)
            .where(users.userId.eq(userID))
            .execute();
    }
}
