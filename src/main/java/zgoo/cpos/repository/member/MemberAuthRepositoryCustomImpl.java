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
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.member.QMember;
import zgoo.cpos.domain.member.QMemberAuth;
import zgoo.cpos.dto.member.MemberDto.MemberAuthDto;

@Slf4j
@RequiredArgsConstructor
public class MemberAuthRepositoryCustomImpl implements MemberAuthRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QCompany company = QCompany.company;
    QMember member = QMember.member;
    QMemberAuth memberAuth = QMemberAuth.memberAuth;

    @Override
    public Page<MemberAuthDto> findMemberAuthWithPagination(Pageable pageable) {
        List<MemberAuthDto> memberAuthList = queryFactory.select(Projections.fields(MemberAuthDto.class,
            memberAuth.idTag.as("idTag"),
            memberAuth.expireDate.as("expireDate"),
            memberAuth.useYn.as("useYn"),
            memberAuth.parentIdTag.as("parentIdTag"),
            memberAuth.totalChargingPower.as("totalChargingPower"),
            memberAuth.status.as("status"),
            memberAuth.totalChargingPrice.as("totalChargingPrice"),
            memberAuth.regDt.as("regDt"),
            member.name.as("name"),
            member.phoneNo.as("phoneNo"),
            company.companyName.as("companyName")))
            .from(memberAuth)
            .leftJoin(memberAuth.member, member)
            .leftJoin(member.company, company)
            .orderBy(memberAuth.regDt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long totalCount = queryFactory
            .select(memberAuth.count())
            .from(memberAuth)
            .fetchOne();

        return new PageImpl<>(memberAuthList, pageable, totalCount);
    }

    @Override
    public Page<MemberAuthDto> searchMemberAuthWithPagination(String idTag, String name, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (idTag != null && !idTag.isEmpty()) {
            builder.and(memberAuth.idTag.contains(idTag));
        }

        if (name != null && !name.isEmpty()) {
            builder.and(memberAuth.member.name.contains(name));
        }

        List<MemberAuthDto> memberAuthList = queryFactory.select(Projections.fields(MemberAuthDto.class,
            memberAuth.idTag.as("idTag"),
            memberAuth.expireDate.as("expireDate"),
            memberAuth.useYn.as("useYn"),
            memberAuth.parentIdTag.as("parentIdTag"),
            memberAuth.totalChargingPower.as("totalChargingPower"),
            memberAuth.status.as("status"),
            memberAuth.totalChargingPrice.as("totalChargingPrice"),
            memberAuth.regDt.as("regDt"),
            member.name.as("name"),
            member.phoneNo.as("phoneNo"),
            company.companyName.as("companyName")))
            .from(memberAuth)
            .leftJoin(memberAuth.member, member)
            .leftJoin(member.company, company)
            .where(builder)
            .orderBy(memberAuth.regDt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long totalCount = queryFactory
            .select(memberAuth.count())
            .from(memberAuth)
            .where(builder)
            .fetchOne();

        return new PageImpl<>(memberAuthList, pageable, totalCount);
    }
}
