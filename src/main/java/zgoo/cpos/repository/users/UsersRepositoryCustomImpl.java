package zgoo.cpos.repository.users;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.users.QUsers;
import zgoo.cpos.domain.users.Users;
import zgoo.cpos.dto.users.UsersDto;

@Slf4j
@RequiredArgsConstructor
public class UsersRepositoryCustomImpl implements UsersRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QUsers users = QUsers.users;
    QCompany company = QCompany.company;
    QCommonCode companyTypeCommonCode =  new QCommonCode("companyType");
    QCommonCode userAuthorityCommonCode = new QCommonCode("authority");

    @Override
    public List<Users> findAll() {
        return queryFactory
            .selectFrom(users)
            .leftJoin(users.company, company)
            .fetch();
    }

    @Override
    public Page<Users> findUsersWithPagination(Pageable pageable) {
        List<Users> usersList =  queryFactory
                                .selectFrom(users)
                                .leftJoin(users.company, company).fetchJoin()
                                .orderBy(users.regDt.desc())
                                .offset(pageable.getOffset())
                                .limit(pageable.getPageSize())
                                .fetch();

        long totalCount = queryFactory
                                .selectFrom(users)
                                .fetchCount();
                            
        return new PageImpl<>(usersList, pageable, totalCount);
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
    public Users finsUserOneNotJoinedComapny(String userID) {
        return queryFactory
            .selectFrom(users)
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
    public Page<Users> searchUsersWithPagination(Long companyId, String companyType, String name, Pageable pageable) {
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

        List<Users> usersList =  queryFactory
                                .selectFrom(users)
                                .leftJoin(users.company, company)
                                .where(builder)
                                .offset(pageable.getOffset())
                                .limit(pageable.getPageSize())
                                .fetch();

        long totalCount = queryFactory
                                .selectFrom(users)
                                .leftJoin(users.company, company)
                                .where(builder)
                                .fetchCount();
                           
        return new PageImpl<>(usersList, pageable, totalCount);
    }

    @Override
    public Long deleteUserOne(String userID) {
        return queryFactory
            .delete(users)
            .where(users.userId.eq(userID))
            .execute();
    }

    @Override
    public Page<UsersDto.UsersListDto> findUsersWithPaginationToDto(Pageable pageable) {
         List<UsersDto.UsersListDto> usersList = queryFactory.select(Projections.fields(UsersDto.UsersListDto.class,
                users.userId.as("userId"),
                users.name.as("name"),
                users.password.as("password"),
                users.phone.as("phone"),
                users.email.as("email"),
                users.authority.as("authority"),
                users.regDt.as("regDt"), 
                company.id.as("companyId"),
                company.companyName.as("companyName"),
                company.companyType.as("companyType"),
                userAuthorityCommonCode.name.as("authorityName"),
                companyTypeCommonCode.name.as("companyTypeName")))
                .from(users)
                .leftJoin(users.company, company)
                .leftJoin(userAuthorityCommonCode).on(users.authority.eq(userAuthorityCommonCode.commonCode))
                .leftJoin(companyTypeCommonCode).on(company.companyType.eq(companyTypeCommonCode.commonCode))
                .orderBy(users.regDt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .selectFrom(users)
                .fetchCount();

        usersList.forEach(user -> {
            log.info("Authority Name: {}", user.getAuthorityName());
            log.info("Company Type Name: {}", user.getCompanyTypeName());
        });
                
            
        return new PageImpl<>(usersList, pageable, totalCount);
    }

    @Override
    public Page<UsersDto.UsersListDto> searchUsersWithPaginationToDto(Long companyId, String companyType, String name, Pageable pageable) {
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

        List<UsersDto.UsersListDto> usersList = queryFactory.select(Projections.fields(UsersDto.UsersListDto.class,
                users.userId.as("userId"),
                users.name.as("name"),
                users.password.as("password"),
                users.phone.as("phone"),
                users.email.as("email"),
                users.authority.as("authority"),
                users.regDt.as("regDt"), 
                company.id.as("companyId"),
                company.companyName.as("companyName"),
                company.companyType.as("companyType"),
                userAuthorityCommonCode.name.as("authorityName"),
                companyTypeCommonCode.name.as("companyTypeName")))
                .from(users)
                .leftJoin(users.company, company)
                .leftJoin(userAuthorityCommonCode).on(users.authority.eq(userAuthorityCommonCode.commonCode))
                .leftJoin(companyTypeCommonCode).on(company.companyType.eq(companyTypeCommonCode.commonCode))
                .orderBy(users.regDt.desc())
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .selectFrom(users)
                .leftJoin(users.company, company)
                .where(builder)
                .fetchCount();
           
        return new PageImpl<>(usersList, pageable, totalCount);
    }
}
