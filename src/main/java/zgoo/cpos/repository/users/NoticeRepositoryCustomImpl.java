package zgoo.cpos.repository.users;

import java.time.LocalDate;
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
import zgoo.cpos.domain.users.Notice;
import zgoo.cpos.domain.users.QNotice;
import zgoo.cpos.domain.users.QUsers;
import zgoo.cpos.domain.users.Users;
import zgoo.cpos.dto.users.NoticeDto.NoticeDetailDto;
import zgoo.cpos.dto.users.NoticeDto.NoticeListDto;

@Slf4j
@RequiredArgsConstructor
public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QNotice notice = QNotice.notice;
    QUsers users = QUsers.users;
    QCompany company = QCompany.company;
    QCommonCode typeCommonCode = new QCommonCode("type");
    LocalDate today = LocalDate.now();

    @Override
    public Page<NoticeListDto> findNoticeWithPagination(Pageable pageable) {
        List<NoticeListDto> noticeList = queryFactory.select(Projections.fields(NoticeListDto.class,
            notice.idx.as("idx"),
            notice.title.as("title"),
            notice.content.as("content"),
            notice.type.as("type"),
            notice.views.as("views"),
            notice.regDt.as("regDt"),
            users.userId.as("userId"),
            users.name.as("userName"),
            company.id.as("companyId"),
            company.companyName.as("companyName"),
            typeCommonCode.name.as("typeName")))
            .from(notice)
            .leftJoin(users).on(notice.user.userId.eq(users.userId))
            .leftJoin(company).on(users.company.id.eq(company.id))
            .leftJoin(typeCommonCode).on(notice.type.eq(typeCommonCode.commonCode))
            .where(
                notice.delYn.eq("N"),
                notice.startDate.loe(today),
                notice.endDate.goe(today)
            )
            .orderBy(notice.regDt.desc(), notice.idx.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long totalCount = queryFactory
            .select(notice.count())
            .from(notice)
            .where(notice.delYn.eq("N"),
                    notice.startDate.loe(today),
                    notice.endDate.goe(today))
            .fetchOne();

        return new PageImpl<>(noticeList, pageable, totalCount);
    }

    @Override
    public Page<NoticeListDto> searchNoticeListwithPagination(Long companyId, LocalDate startDate,
            LocalDate endDate, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(notice.delYn.eq("N"));
        builder.and(notice.startDate.loe(today));
        builder.and(notice.endDate.goe(today));

        if (companyId != null) {
            builder.and(company.id.eq(companyId));
        }

        if (startDate != null && endDate != null) {
            builder.and(notice.regDt.between(startDate.atStartOfDay(), endDate.atTime(23, 59, 59)));
        } else if (startDate != null) {
            builder.and(notice.regDt.goe(startDate.atStartOfDay()));
        } else if (endDate != null) {
            builder.and(notice.regDt.loe(endDate.atTime(23, 59, 59)));
        }

        List<NoticeListDto> noticeList = queryFactory.select(Projections.fields(NoticeListDto.class,
            notice.idx.as("idx"),
            notice.title.as("title"),
            notice.content.as("content"),
            notice.type.as("type"),
            notice.views.as("views"),
            notice.regDt.as("regDt"),
            users.userId.as("userId"),
            users.name.as("userName"),
            company.id.as("companyId"),
            company.companyName.as("companyName"),
            typeCommonCode.name.as("typeName")))
            .from(notice)
            .leftJoin(users).on(notice.user.userId.eq(users.userId))
            .leftJoin(company).on(users.company.id.eq(company.id))
            .leftJoin(typeCommonCode).on(notice.type.eq(typeCommonCode.commonCode))
            .where(builder)
            .orderBy(notice.regDt.desc(), notice.idx.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long totalCount = queryFactory
            .select(notice.count())
            .from(notice)
            .leftJoin(users).on(notice.user.userId.eq(users.userId))
            .leftJoin(company).on(users.company.id.eq(company.id))
            .where(builder)
            .fetchOne();

        return new PageImpl<>(noticeList, pageable, totalCount);
    }

    @Override
    public Notice findNoticeOne(Long idx) {
        if (idx == null) {
            log.error("Notice idx is null");
            throw new IllegalArgumentException("공지사항 ID는 null일 수 없습니다.");
        }

        return queryFactory
            .selectFrom(notice)
            .where(notice.idx.eq(idx))
            .fetchOne();
    }

    @Override
    public Users findUserOne(String userId) {
        return queryFactory
            .selectFrom(users)
            .where(users.userId.eq(userId))
            .fetchOne();
    }

    @Override
    public NoticeDetailDto findNoticeDetailOne(Long idx) {
        return queryFactory.select(Projections.fields(NoticeDetailDto.class,
                notice.idx.as("idx"),
                notice.title.as("title"),
                notice.content.as("content"),
                notice.type.as("type"),
                notice.views.as("views"),
                notice.regDt.as("regDt"),
                users.name.as("userName"),
                typeCommonCode.name.as("typeName")))
                .from(notice)
                .leftJoin(users).on(notice.user.userId.eq(users.userId))
                .leftJoin(typeCommonCode).on(notice.type.eq(typeCommonCode.commonCode))
                .where(notice.idx.eq(idx))
                .fetchOne();
    }

    @Override
    public NoticeDetailDto findPreviousNotice(Long idx, Long companyId, LocalDate startDate, LocalDate endDate) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(notice.delYn.eq("N"));
        builder.and(notice.startDate.loe(today));
        builder.and(notice.endDate.goe(today));

        if (companyId != null) {
            builder.and(company.id.eq(companyId));
        }

        if (startDate != null && endDate != null) {
            builder.and(notice.regDt.between(startDate.atStartOfDay(), endDate.atTime(23, 59, 59)));
        } else if (startDate != null) {
            builder.and(notice.regDt.goe(startDate.atStartOfDay()));
        } else if (endDate != null) {
            builder.and(notice.regDt.loe(endDate.atTime(23, 59, 59)));
        }

        builder.and(notice.idx.lt(idx));

        return queryFactory.select(Projections.fields(NoticeDetailDto.class,
                notice.idx.as("idx"),
                notice.title.as("title"),
                notice.content.as("content"),
                notice.type.as("type"),
                notice.views.as("views"),
                notice.regDt.as("regDt"),
                typeCommonCode.name.as("typeName")))
                .from(notice)
                .leftJoin(users).on(notice.user.userId.eq(users.userId))
                .leftJoin(company).on(users.company.id.eq(company.id))
                .leftJoin(typeCommonCode).on(notice.type.eq(typeCommonCode.commonCode))
                .where(builder)
                .orderBy(notice.idx.desc())
                .fetchFirst(); // 가장 큰 idx를 조회
    }

    @Override
    public NoticeDetailDto findNextNotice(Long idx, Long companyId, LocalDate startDate, LocalDate endDate) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(notice.delYn.eq("N"));
        builder.and(notice.startDate.loe(today));
        builder.and(notice.endDate.goe(today));

        if (companyId != null) {
            builder.and(company.id.eq(companyId));
        }

        if (startDate != null && endDate != null) {
            builder.and(notice.regDt.between(startDate.atStartOfDay(), endDate.atTime(23, 59, 59)));
        } else if (startDate != null) {
            builder.and(notice.regDt.goe(startDate.atStartOfDay()));
        } else if (endDate != null) {
            builder.and(notice.regDt.loe(endDate.atTime(23, 59, 59)));
        }

        builder.and(notice.idx.gt(idx));

        return queryFactory.select(Projections.fields(NoticeDetailDto.class,
                notice.idx.as("idx"),
                notice.title.as("title"),
                notice.content.as("content"),
                notice.type.as("type"),
                notice.views.as("views"),
                notice.regDt.as("regDt"),
                typeCommonCode.name.as("typeName")))
                .from(notice)
                .leftJoin(users).on(notice.user.userId.eq(users.userId))
                .leftJoin(company).on(users.company.id.eq(company.id))
                .leftJoin(typeCommonCode).on(notice.type.eq(typeCommonCode.commonCode))
                .where(builder)
                .orderBy(notice.idx.asc())
                .fetchFirst(); // 가장 작은 idx를 조회
    }

    @Override
    public Long deleteNoticeOne(Long idx) {
        return queryFactory
                .update(notice)
                .set(notice.delYn, "Y")
                .where(notice.idx.eq(idx)
                    .and(notice.delYn.eq("N")))
                .execute();
    }

    @Override
    public List<NoticeListDto> findLatestNoticeList() {
        List<NoticeListDto> noticeList = queryFactory.select(Projections.fields(NoticeListDto.class,
            notice.idx.as("idx"),
            notice.title.as("title"),
            notice.content.as("content"),
            notice.type.as("type"),
            notice.views.as("views"),
            notice.regDt.as("regDt"),
            users.userId.as("userId"),
            users.name.as("userName"),
            company.id.as("companyId"),
            company.companyName.as("companyName"),
            typeCommonCode.name.as("typeName")))
            .from(notice)
            .leftJoin(users).on(notice.user.userId.eq(users.userId))
            .leftJoin(company).on(users.company.id.eq(company.id))
            .leftJoin(typeCommonCode).on(notice.type.eq(typeCommonCode.commonCode))
            .where(
                notice.delYn.eq("N"),
                notice.startDate.loe(today),
                notice.endDate.goe(today)
            )
            .orderBy(notice.regDt.desc(), notice.idx.desc())
            .limit(5)
            .fetch();
        return noticeList;
    }
}
