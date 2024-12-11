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
import zgoo.cpos.domain.users.Faq;
import zgoo.cpos.domain.users.QFaq;
import zgoo.cpos.domain.users.QUsers;
import zgoo.cpos.dto.users.FaqDto.FaqDetailDto;
import zgoo.cpos.dto.users.FaqDto.FaqListDto;

@Slf4j
@RequiredArgsConstructor
public class FaqRepositoryCustomImpl implements FaqRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QFaq faq = QFaq.faq;
    QUsers users = QUsers.users;
    QCompany company = QCompany.company;
    QCommonCode sectionCommonCode = new QCommonCode("section");

    @Override
    public Page<FaqListDto> findFaqWithPagination(Pageable pageable) {
        List<FaqListDto> faqList = queryFactory.select(Projections.fields(FaqListDto.class,
            faq.id.as("id"),
            faq.title.as("title"),
            faq.content.as("content"),
            faq.user.userId.as("userId"),
            faq.section.as("section"),
            faq.delYn.as("delYn"),
            faq.regDt.as("regDt"),
            users.name.as("userName"),
            sectionCommonCode.name.as("sectionName")))
            .from(faq)
            .leftJoin(users).on(faq.user.userId.eq(users.userId))
            .leftJoin(company).on(users.company.id.eq(company.id))
            .leftJoin(sectionCommonCode).on(faq.section.eq(sectionCommonCode.commonCode))
            .where(faq.delYn.eq("N"))
            .orderBy(faq.regDt.desc(), faq.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long totalCount = queryFactory
            .selectFrom(faq)
            .where(faq.delYn.eq("N"))
            .fetch().size();

        return new PageImpl<>(faqList, pageable, totalCount);
    }

    @Override
    public Page<FaqListDto> searchFaqListWithPagination(String section, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(faq.delYn.eq("N"));

        if(section != null && !section.isEmpty()) {
            builder.and(faq.section.eq(section));
        }

        List<FaqListDto> faqList = queryFactory.select(Projections.fields(FaqListDto.class,
            faq.id.as("id"),
            faq.title.as("title"),
            faq.content.as("content"),
            faq.user.userId.as("userId"),
            faq.section.as("section"),
            faq.delYn.as("delYn"),
            faq.regDt.as("regDt"),
            users.name.as("userName"),
            sectionCommonCode.name.as("sectionName")))
            .from(faq)
            .leftJoin(users).on(faq.user.userId.eq(users.userId))
            .leftJoin(company).on(users.company.id.eq(company.id))
            .leftJoin(sectionCommonCode).on(faq.section.eq(sectionCommonCode.commonCode))
            .where(builder)
            .orderBy(faq.regDt.desc(), faq.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long totalCount = queryFactory
            .selectFrom(faq)
            .leftJoin(users).on(faq.user.userId.eq(users.userId))
            .leftJoin(company).on(users.company.id.eq(company.id))
            .where(builder)
            .fetch().size();

        return new PageImpl<>(faqList, pageable, totalCount);
    }

    @Override
    public Faq findFaqOne(Long id) {
        if (id == null) {
            log.error("Faq id is null");
            throw new IllegalArgumentException("FAQ ID는 null일 수 없습니다.");
        }

        return queryFactory
        .selectFrom(faq)
        .where(faq.id.eq(id))
        .fetchOne();
    }

    @Override
    public FaqDetailDto findFaqDetailOne(Long id) {
        return queryFactory.select(Projections.fields(FaqDetailDto.class,
                faq.id.as("id"),
                faq.title.as("title"),
                faq.content.as("content"),
                faq.user.userId.as("userId"),
                faq.section.as("section"),
                faq.regDt.as("regDt"),
                users.name.as("userName"),
                sectionCommonCode.name.as("sectionName")))
                .from(faq)
                .leftJoin(users).on(faq.user.userId.eq(users.userId))
                .leftJoin(company).on(users.company.id.eq(company.id))
                .leftJoin(sectionCommonCode).on(faq.section.eq(sectionCommonCode.commonCode))
                .where(faq.id.eq(id))
                .fetchOne();
    }

    @Override
    public FaqDetailDto findPreviousFaq(Long id, String section, Faq currentFaq) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(faq.delYn.eq("N"));

        if (section != null && !section.isEmpty()) {
            builder.and(faq.section.eq(section));
        }

        // builder.and(faq.id.lt(id));
        // regDt, id 조건 추가
        builder.and(
            faq.regDt.lt(currentFaq.getRegDt())
            .or(faq.regDt.eq(currentFaq.getRegDt()).and(faq.id.lt(id)))
        );

        return queryFactory.select(Projections.fields(FaqDetailDto.class,
                faq.id.as("id"),
                faq.title.as("title"),
                faq.content.as("content"),
                faq.user.userId.as("userId"),
                faq.section.as("section"),
                faq.regDt.as("regDt"),
                users.name.as("userName"),
                sectionCommonCode.name.as("sectionName")))
                .from(faq)
                .leftJoin(users).on(faq.user.userId.eq(users.userId))
                .leftJoin(company).on(users.company.id.eq(company.id))
                .leftJoin(sectionCommonCode).on(faq.section.eq(sectionCommonCode.commonCode))
                .where(builder)
                .orderBy(faq.regDt.desc(), faq.id.desc())
                .fetchFirst();
    }

    @Override
    public FaqDetailDto findNextFaq(Long id, String section, Faq currentFaq) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(faq.delYn.eq("N"));

        if (section != null && !section.isEmpty()) {
            builder.and(faq.section.eq(section));
        }

        // builder.and(faq.id.gt(id));
        // regDt, id 조건 추가
        builder.and(
            faq.regDt.gt(currentFaq.getRegDt())
            .or(faq.regDt.eq(currentFaq.getRegDt()).and(faq.id.gt(id)))
        );

        return queryFactory.select(Projections.fields(FaqDetailDto.class,
                faq.id.as("id"),
                faq.title.as("title"),
                faq.content.as("content"),
                faq.user.userId.as("userId"),
                faq.section.as("section"),
                faq.regDt.as("regDt"),
                users.name.as("userName"),
                sectionCommonCode.name.as("sectionName")))
                .from(faq)
                .leftJoin(users).on(faq.user.userId.eq(users.userId))
                .leftJoin(company).on(users.company.id.eq(company.id))
                .leftJoin(sectionCommonCode).on(faq.section.eq(sectionCommonCode.commonCode))
                .where(builder)
                .orderBy(faq.regDt.asc(), faq.id.asc())
                .fetchFirst();
    }

    @Override
    public Long deleteFaqOne(Long id) {
        return queryFactory
                .update(faq)
                .set(faq.delYn, "Y")
                .where(faq.id.eq(id)
                    .and(faq.delYn.eq("N")))
                .execute();
    }
}