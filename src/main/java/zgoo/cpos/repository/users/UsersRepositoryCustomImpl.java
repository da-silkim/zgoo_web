package zgoo.cpos.repository.users;

import java.util.List;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.users.QUsers;
import zgoo.cpos.domain.users.Users;

@Slf4j
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
    public List<Users> searchUsers(Long companyId, String companyType, String name) {
        log.info("Executing query to search users with companyId: {}, companyType: {}, name: {}", companyId, companyType, name);
        BooleanBuilder builder = new BooleanBuilder();

        if (companyId != null) {
            builder.and(users.company.id.eq(companyId));
        }

        if (companyType != null && !companyType.isEmpty()) {
            builder.and(company.companyType.eq(companyType));
        }

        if (name != null && !name.isEmpty()) {
            builder.and(users.name.contains(name));
        }

        return queryFactory
            .selectFrom(users)
            .leftJoin(users.company, company)
            .where(builder)
            .fetch();
    }

    @Override
    public Long deleteUserOne(String userID) {
        return queryFactory
            .delete(users)
            .where(users.userId.eq(userID))
            .execute();
    }
}
