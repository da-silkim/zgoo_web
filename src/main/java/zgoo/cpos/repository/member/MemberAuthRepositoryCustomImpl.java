package zgoo.cpos.repository.member;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.company.QCompanyRelationInfo;
import zgoo.cpos.domain.history.QChargingHist;
import zgoo.cpos.domain.member.QMember;
import zgoo.cpos.domain.member.QMemberAuth;
import zgoo.cpos.dto.member.MemberDto.MemberAuthDto;

@Slf4j
@RequiredArgsConstructor
public class MemberAuthRepositoryCustomImpl implements MemberAuthRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QMember member = QMember.member;
    QMemberAuth memberAuth = QMemberAuth.memberAuth;
    QChargingHist hist = QChargingHist.chargingHist;
    QCompanyRelationInfo relation = QCompanyRelationInfo.companyRelationInfo;

    @Override
    public Page<MemberAuthDto> findMemberAuthWithPagination(Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        List<MemberAuthDto> memberAuthList = queryFactory.select(Projections.fields(MemberAuthDto.class,
                memberAuth.idTag.as("idTag"),
                memberAuth.expireDate.as("expireDate"),
                memberAuth.useYn.as("useYn"),
                memberAuth.parentIdTag.as("parentIdTag"),
                ExpressionUtils.as(
                        JPAExpressions
                                .select(hist.chargeAmount.sum()
                                        .coalesce(BigDecimal.ZERO))
                                .from(hist)
                                .where(hist.idTag.eq(memberAuth.idTag)),
                        "totalChargingPower"),
                memberAuth.status.as("status"),
                memberAuth.totalChargingPrice.as("totalChargingPrice"),
                memberAuth.regDt.as("regDt"),
                member.name.as("name"),
                member.phoneNo.as("phoneNo")))
                // company.companyName.as("companyName")))
                .from(memberAuth)
                .leftJoin(memberAuth.member, member)
                .where(builder)
                .orderBy(memberAuth.regDt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(memberAuth.count())
                .from(memberAuth)
                .leftJoin(memberAuth.member, member)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(memberAuthList, pageable, totalCount);
    }

    @Override
    public Page<MemberAuthDto> searchMemberAuthWithPagination(String idtag, String name, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (idtag != null && !idtag.isEmpty()) {
            builder.and(memberAuth.idTag.contains(idtag));
        }

        if (name != null && !name.isEmpty()) {
            builder.and(memberAuth.member.name.contains(name));
        }

        List<MemberAuthDto> memberAuthList = queryFactory.select(Projections.fields(MemberAuthDto.class,
                memberAuth.idTag.as("idTag"),
                memberAuth.expireDate.as("expireDate"),
                memberAuth.useYn.as("useYn"),
                memberAuth.parentIdTag.as("parentIdTag"),
                ExpressionUtils.as(
                        JPAExpressions
                                .select(hist.chargeAmount.sum()
                                        .coalesce(BigDecimal.ZERO))
                                .from(hist)
                                .where(hist.idTag.eq(memberAuth.idTag)),
                        "totalChargingPower"),
                memberAuth.status.as("status"),
                memberAuth.totalChargingPrice.as("totalChargingPrice"),
                memberAuth.regDt.as("regDt"),
                member.name.as("name"),
                member.phoneNo.as("phoneNo")))
                .from(memberAuth)
                .leftJoin(memberAuth.member, member)
                .where(builder)
                .orderBy(memberAuth.regDt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(memberAuth.count())
                .from(memberAuth)
                .leftJoin(memberAuth.member, member)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(memberAuthList, pageable, totalCount);
    }

    @Override
    public MemberAuthDto findMemberAuthOne(String idtag) {
        log.info("[MemberAuthRepositoryCustomImpl] findMemberAuthOne idtag: {}", idtag);

        MemberAuthDto memberAuthDto = queryFactory.select(Projections.fields(MemberAuthDto.class,
                memberAuth.idTag.as("idTag"),
                memberAuth.expireDate.as("expireDate"),
                memberAuth.useYn.as("useYn"),
                memberAuth.parentIdTag.as("parentIdTag"),
                ExpressionUtils.as(
                        JPAExpressions
                                .select(hist.chargeAmount.sum()
                                        .coalesce(BigDecimal.ZERO))
                                .from(hist)
                                .where(hist.idTag.eq(memberAuth.idTag)),
                        "totalChargingPower"),
                memberAuth.status.as("status"),
                memberAuth.totalChargingPrice.as("totalChargingPrice"),
                memberAuth.regDt.as("regDt"),
                member.name.as("name"),
                member.phoneNo.as("phoneNo")))
                .from(memberAuth)
                .leftJoin(memberAuth.member, member)
                .where(memberAuth.idTag.eq(idtag))
                .fetchOne();
        return memberAuthDto;
    }

    @Override
    public List<MemberAuthDto> findAllMemberTagWithoutPagination(String idTag, String name) {
        BooleanBuilder builder = new BooleanBuilder();

        if (idTag != null && !idTag.isEmpty()) {
            builder.and(memberAuth.idTag.contains(idTag));
        }

        if (name != null && !name.isEmpty()) {
            builder.and(memberAuth.member.name.contains(name));
        }

        return queryFactory.select(Projections.fields(MemberAuthDto.class,
                memberAuth.idTag.as("idTag"),
                memberAuth.expireDate.as("expireDate"),
                memberAuth.useYn.as("useYn"),
                memberAuth.parentIdTag.as("parentIdTag"),
                ExpressionUtils.as(
                        JPAExpressions
                                .select(hist.chargeAmount.sum()
                                        .coalesce(BigDecimal.ZERO))
                                .from(hist)
                                .where(hist.idTag.eq(memberAuth.idTag)),
                        "totalChargingPower"),
                memberAuth.status.as("status"),
                memberAuth.totalChargingPrice.as("totalChargingPrice"),
                memberAuth.regDt.as("regDt"),
                member.name.as("name"),
                member.phoneNo.as("phoneNo")))
                .from(memberAuth)
                .leftJoin(memberAuth.member, member)
                .where(builder)
                .orderBy(memberAuth.regDt.desc())
                .fetch();
    }
}
