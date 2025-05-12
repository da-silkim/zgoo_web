package zgoo.cpos.repository.member;

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
import zgoo.cpos.domain.company.QCompanyRelationInfo;
import zgoo.cpos.domain.member.QMember;
import zgoo.cpos.domain.member.QVoc;
import zgoo.cpos.domain.users.QUsers;
import zgoo.cpos.dto.member.VocDto.VocListDto;
import zgoo.cpos.dto.member.VocDto.VocRegDto;
import zgoo.cpos.util.QueryUtils;

@Slf4j
@RequiredArgsConstructor
public class VocRepositoryCustomImpl implements VocRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QCompany company = QCompany.company;
    QCompanyRelationInfo relation = QCompanyRelationInfo.companyRelationInfo;
    QMember member = QMember.member;
    QUsers user = QUsers.users;
    QVoc voc = QVoc.voc;
    QCommonCode typeName = new QCommonCode("type");
    QCommonCode replyStatName = new QCommonCode("replyStat");
    QCommonCode channelName = new QCommonCode("channel");

    @Override
    public Page<VocListDto> findVocWithPagination(Pageable pageable, String levelPath, boolean isSuperAdmin) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(voc.delYn.eq("N"));

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }
        List<VocListDto> vocList = queryFactory.select(Projections.fields(VocListDto.class,
                voc.id.as("vocId"),
                voc.type.as("type"),
                voc.title.as("title"),
                voc.replyStat.as("replyStat"),
                voc.regDt.as("regDt"),
                voc.replyDt.as("replyDt"),
                member.name.as("name"),
                member.phoneNo.as("phoneNo"),
                typeName.name.as("typeName"),
                replyStatName.name.as("replyStatName")))
                .from(voc)
                .leftJoin(member).on(voc.member.eq(member))
                .leftJoin(member).on(voc.member.id.eq(member.id))
                .leftJoin(company).on(member.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(typeName).on(voc.type.eq(typeName.commonCode))
                .leftJoin(replyStatName).on(voc.replyStat.eq(replyStatName.commonCode))
                .where(builder)
                .orderBy(voc.regDt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(voc.count())
                .from(voc)
                .leftJoin(member).on(voc.member.eq(member))
                .leftJoin(member).on(voc.member.id.eq(member.id))
                .leftJoin(company).on(member.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(vocList, pageable, totalCount);
    }

    @Override
    public Page<VocListDto> searchVocWithPagination(String type, String replyStat, String name, Pageable pageable,
            String levelPath, boolean isSuperAdmin) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(voc.delYn.eq("N"));

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        if (type != null && !type.isEmpty()) {
            builder.and(voc.type.contains(type));
        }

        if (replyStat != null && !replyStat.isEmpty()) {
            builder.and(voc.replyStat.contains(replyStat));
        }

        if (name != null && !name.isEmpty()) {
            builder.and(member.name.contains(name));
        }

        List<VocListDto> vocList = queryFactory.select(Projections.fields(VocListDto.class,
                voc.id.as("vocId"),
                voc.type.as("type"),
                voc.title.as("title"),
                voc.replyStat.as("replyStat"),
                voc.regDt.as("regDt"),
                voc.replyDt.as("replyDt"),
                member.name.as("name"),
                member.phoneNo.as("phoneNo"),
                typeName.name.as("typeName"),
                replyStatName.name.as("replyStatName")))
                .from(voc)
                .leftJoin(member).on(voc.member.eq(member))
                .leftJoin(company).on(member.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(typeName).on(voc.type.eq(typeName.commonCode))
                .leftJoin(replyStatName).on(voc.replyStat.eq(replyStatName.commonCode))
                .where(builder)
                .orderBy(voc.regDt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(voc.count())
                .from(voc)
                .leftJoin(member).on(voc.member.eq(member))
                .leftJoin(company).on(member.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(vocList, pageable, totalCount);
    }

    @Override
    public VocRegDto findVocOne(Long vocId) {
        VocRegDto vocDto = queryFactory.select(Projections.fields(VocRegDto.class,
                voc.id.as("vocId"),
                voc.type.as("type"),
                voc.title.as("title"),
                voc.content.as("content"),
                voc.replyStat.as("replyStat"),
                voc.regDt.as("regDt"),
                voc.channel.as("channel"),
                voc.replyContent.as("replyContent"),
                member.name.as("name"),
                member.phoneNo.as("phoneNo"),
                typeName.name.as("typeName"),
                replyStatName.name.as("replyStatName"),
                channelName.name.as("channelName")))
                .from(voc)
                .leftJoin(member).on(voc.member.eq(member))
                .leftJoin(typeName).on(voc.type.eq(typeName.commonCode))
                .leftJoin(replyStatName).on(voc.replyStat.eq(replyStatName.commonCode))
                .leftJoin(channelName).on(voc.channel.eq(channelName.commonCode))
                .where(voc.id.eq(vocId))
                .fetchOne();

        return vocDto;
    }
}
