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
import zgoo.cpos.domain.biz.QBizInfo;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.company.QCompanyRelationInfo;
import zgoo.cpos.domain.member.QMember;
import zgoo.cpos.domain.member.QMemberCar;
import zgoo.cpos.domain.member.QMemberCondition;
import zgoo.cpos.domain.member.QMemberCreditCard;
import zgoo.cpos.dto.member.MemberDto.MemberCarDto;
import zgoo.cpos.dto.member.MemberDto.MemberConditionDto;
import zgoo.cpos.dto.member.MemberDto.MemberCreditCardDto;
import zgoo.cpos.dto.member.MemberDto.MemberDetailDto;
import zgoo.cpos.dto.member.MemberDto.MemberListDto;
import zgoo.cpos.dto.member.MemberDto.MemberRegDto;
import zgoo.cpos.util.QueryUtils;

@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QCompany company = QCompany.company;
    QMember member = QMember.member;
    QMemberCar car = QMemberCar.memberCar;
    QMemberCreditCard card = QMemberCreditCard.memberCreditCard;
    QMemberCondition condition = QMemberCondition.memberCondition;
    QBizInfo biz = QBizInfo.bizInfo;
    QCommonCode manufCdName = new QCommonCode("manufCd");
    QCommonCode bizTypeName = new QCommonCode("bizType");
    QCommonCode fnCodeName = new QCommonCode("fnCode");
    QCommonCode carTypeName = new QCommonCode("carType");
    QCommonCode conditionCodeName = new QCommonCode("conditionCode");
    QCommonCode userStateName = new QCommonCode("userState");
    QCompanyRelationInfo relation = QCompanyRelationInfo.companyRelationInfo;

    @Override
    public Page<MemberListDto> findMemberWithPagination(Pageable pageable, String levelPath, boolean isSuperAdmin) {
        BooleanBuilder builder = new BooleanBuilder();

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        List<MemberListDto> memberList = queryFactory.select(Projections.fields(MemberListDto.class,
                member.id.as("memberId"),
                member.memLoginId.as("memLoginId"),
                member.bizType.as("bizType"),
                member.name.as("name"),
                member.phoneNo.as("phoneNo"),
                member.idTag.as("idTag"),
                member.email.as("email"),
                member.joinedDt.as("joinedDt"),
                company.companyName.as("companyName"),
                bizTypeName.name.as("bizTypeName")))
                .from(member)
                .leftJoin(company).on(member.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(bizTypeName).on(member.bizType.eq(bizTypeName.commonCode))
                .where(builder)
                .orderBy(member.joinedDt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(member.count())
                .from(member)
                .leftJoin(company).on(member.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(bizTypeName).on(member.bizType.eq(bizTypeName.commonCode))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(memberList, pageable, totalCount);
    }

    @Override
    public Page<MemberListDto> searchMemberWithPagination(Long companyId, String idTag, String name,
            Pageable pageable, String levelPath, boolean isSuperAdmin) {
        BooleanBuilder builder = new BooleanBuilder();

        if (companyId != null) {
            builder.and(member.company.id.eq(companyId));
        }

        if (idTag != null && !idTag.isEmpty()) {
            builder.and(member.idTag.contains(idTag));
        }

        if (name != null && !name.isEmpty()) {
            builder.and(member.name.contains(name));
        }

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        List<MemberListDto> memberList = queryFactory.select(Projections.fields(MemberListDto.class,
                member.id.as("memberId"),
                member.memLoginId.as("memLoginId"),
                member.bizType.as("bizType"),
                member.name.as("name"),
                member.phoneNo.as("phoneNo"),
                member.idTag.as("idTag"),
                member.email.as("email"),
                member.joinedDt.as("joinedDt"),
                company.companyName.as("companyName"),
                bizTypeName.name.as("bizTypeName")))
                .from(member)
                .leftJoin(company).on(member.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(bizTypeName).on(member.bizType.eq(bizTypeName.commonCode))
                .where(builder)
                .orderBy(member.joinedDt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(member.count())
                .from(member)
                .leftJoin(company).on(member.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(bizTypeName).on(member.bizType.eq(bizTypeName.commonCode))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(memberList, pageable, totalCount);
    }

    @Override
    public MemberRegDto findMemberOne(Long memberId, List<MemberCreditCardDto> cardInfo, List<MemberCarDto> carInfo,
            List<MemberConditionDto> conditionInfo) {

        MemberRegDto memberDto = queryFactory.select(Projections.fields(MemberRegDto.class,
                member.id.as("memberId"),
                member.bizType.as("bizType"),
                member.name.as("name"),
                member.phoneNo.as("phoneNo"),
                member.memLoginId.as("memLoginId"),
                member.password.as("password"),
                member.idTag.as("idTag"),
                member.userState.as("userState"),
                member.email.as("email"),
                member.birth.as("birth"),
                member.zipCode.as("zipCode"),
                member.address.as("address"),
                member.addressDetail.as("addressDetail"),
                company.id.as("companyId"),
                company.companyName.as("companyName"),
                bizTypeName.name.as("bizTypeName")))
                .from(member)
                .leftJoin(company).on(member.company.eq(company))
                .leftJoin(bizTypeName).on(member.bizType.eq(bizTypeName.commonCode))
                .where(member.id.eq(memberId))
                .fetchOne();

        if (memberDto != null) {
            memberDto.setCard(cardInfo);
            memberDto.setCar(carInfo);
            memberDto.setCondition(conditionInfo);
        }

        return memberDto;
    }

    @Override
    public MemberRegDto findBizMemberOne(Long memberId, List<MemberCarDto> carInfo,
            List<MemberConditionDto> conditionInfo) {

        MemberRegDto memberDto = queryFactory.select(Projections.fields(MemberRegDto.class,
                member.id.as("memberId"),
                member.bizType.as("bizType"),
                member.name.as("name"),
                member.phoneNo.as("phoneNo"),
                member.memLoginId.as("memLoginId"),
                member.password.as("password"),
                member.idTag.as("idTag"),
                member.userState.as("userState"),
                member.email.as("email"),
                member.birth.as("birth"),
                member.zipCode.as("zipCode"),
                member.address.as("address"),
                member.addressDetail.as("addressDetail"),
                company.id.as("companyId"),
                company.companyName.as("companyName"),
                biz.id.as("bizId"),
                bizTypeName.name.as("bizTypeName")))
                .from(member)
                .leftJoin(company).on(member.company.eq(company))
                .leftJoin(biz).on(member.biz.eq(biz))
                .leftJoin(bizTypeName).on(member.bizType.eq(bizTypeName.commonCode))
                .where(member.id.eq(memberId))
                .fetchOne();

        if (memberDto != null) {
            memberDto.setCar(carInfo);
            memberDto.setCondition(conditionInfo);
        }

        return memberDto;
    }

    @Override
    public MemberDetailDto findMemberDetailOne(Long memberId, List<MemberCreditCardDto> cardInfo,
            List<MemberCarDto> carInfo,
            List<MemberConditionDto> conditionInfo) {
        MemberDetailDto memberDetailDto = queryFactory.select(Projections.fields(MemberDetailDto.class,
                member.id.as("memberId"),
                member.bizType.as("bizType"),
                member.name.as("name"),
                member.phoneNo.as("phoneNo"),
                member.memLoginId.as("memLoginId"),
                member.idTag.as("idTag"),
                member.userState.as("userState"),
                member.email.as("email"),
                member.birth.as("birth"),
                member.zipCode.as("zipCode"),
                member.address.as("address"),
                member.addressDetail.as("addressDetail"),
                company.id.as("companyId"),
                company.companyName.as("companyName"),
                bizTypeName.name.as("bizTypeName"),
                userStateName.name.as("userStateName")))
                .from(member)
                .leftJoin(company).on(member.company.eq(company))
                .leftJoin(bizTypeName).on(member.bizType.eq(bizTypeName.commonCode))
                .leftJoin(userStateName).on(member.userState.eq(userStateName.commonCode))
                .where(member.id.eq(memberId))
                .fetchOne();

        if (memberDetailDto != null) {
            memberDetailDto.setCard(cardInfo);
            memberDetailDto.setCar(carInfo);
            memberDetailDto.setCondition(conditionInfo);
        }

        return memberDetailDto;
    }

    @Override
    public MemberDetailDto findBizMemberDetailOne(Long memberId, List<MemberCarDto> carInfo,
            List<MemberConditionDto> conditionInfo) {
        MemberDetailDto memberDetailDto = queryFactory.select(Projections.fields(MemberDetailDto.class,
                member.id.as("memberId"),
                member.bizType.as("bizType"),
                member.name.as("name"),
                member.phoneNo.as("phoneNo"),
                member.memLoginId.as("memLoginId"),
                member.idTag.as("idTag"),
                member.userState.as("userState"),
                member.email.as("email"),
                member.birth.as("birth"),
                member.zipCode.as("zipCode"),
                member.address.as("address"),
                member.addressDetail.as("addressDetail"),
                company.id.as("companyId"),
                company.companyName.as("companyName"),
                biz.id.as("bizId"),
                bizTypeName.name.as("bizTypeName"),
                userStateName.name.as("userStateName")))
                .from(member)
                .leftJoin(company).on(member.company.eq(company))
                .leftJoin(biz).on(member.biz.eq(biz))
                .leftJoin(bizTypeName).on(member.bizType.eq(bizTypeName.commonCode))
                .leftJoin(userStateName).on(member.userState.eq(userStateName.commonCode))
                .where(member.id.eq(memberId))
                .fetchOne();

        if (memberDetailDto != null) {
            memberDetailDto.setCar(carInfo);
            memberDetailDto.setCondition(conditionInfo);
        }

        return memberDetailDto;
    }

    @Override
    public List<MemberListDto> findMemberList(String name, String phoneNo, String levelPath, boolean isSuperAdmin) {
        BooleanBuilder builder = new BooleanBuilder();

        if (name != null && !name.isEmpty()) {
            builder.and(member.name.contains(name));
        }

        if (phoneNo != null && !phoneNo.isEmpty()) {
            builder.and(member.phoneNo.contains(phoneNo));
        }

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        List<MemberListDto> memberList = queryFactory.select(Projections.fields(MemberListDto.class,
                member.id.as("memberId"),
                member.bizType.as("bizType"),
                member.name.as("name"),
                member.phoneNo.as("phoneNo"),
                member.memLoginId.as("memLoginId"),
                member.idTag.as("idTag"),
                member.userState.as("userState"),
                member.email.as("email"),
                member.birth.as("birth"),
                member.zipCode.as("zipCode"),
                member.address.as("address"),
                member.addressDetail.as("addressDetail"),
                company.id.as("companyId"),
                company.companyName.as("companyName"),
                bizTypeName.name.as("bizTypeName")))
                .from(member)
                .leftJoin(company).on(member.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(bizTypeName).on(member.bizType.eq(bizTypeName.commonCode))
                .where(builder)
                .fetch();

        return memberList;
    }

    @Override
    public List<MemberListDto> findAllMemberListWithoutPagination(Long companyId, String idTag, String name,
            String levelPath, boolean isSuperAdmin) {
        BooleanBuilder builder = new BooleanBuilder();

        if (companyId != null) {
            builder.and(member.company.id.eq(companyId));
        }

        if (idTag != null && !idTag.isEmpty()) {
            builder.and(member.idTag.contains(idTag));
        }

        if (name != null && !name.isEmpty()) {
            builder.and(member.name.contains(name));
        }

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        return queryFactory.select(Projections.fields(MemberListDto.class,
                member.id.as("memberId"),
                member.memLoginId.as("memLoginId"),
                member.bizType.as("bizType"),
                member.name.as("name"),
                member.phoneNo.as("phoneNo"),
                member.idTag.as("idTag"),
                member.email.as("email"),
                member.joinedDt.as("joinedDt"),
                company.companyName.as("companyName"),
                bizTypeName.name.as("bizTypeName")))
                .from(member)
                .leftJoin(company).on(member.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(bizTypeName).on(member.bizType.eq(bizTypeName.commonCode))
                .where(builder)
                .orderBy(member.joinedDt.desc())
                .fetch();
    }
}
