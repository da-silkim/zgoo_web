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
import zgoo.cpos.domain.company.QCompanyRelationInfo;
import zgoo.cpos.domain.member.QMember;
import zgoo.cpos.domain.member.QMemberCar;
import zgoo.cpos.domain.member.QMemberCondition;
import zgoo.cpos.domain.member.QMemberCreditCard;
import zgoo.cpos.dto.member.MemberDto.MemberBaseDto;
import zgoo.cpos.dto.member.MemberDto.MemberCarDto;
import zgoo.cpos.dto.member.MemberDto.MemberConditionDto;
import zgoo.cpos.dto.member.MemberDto.MemberCreditCardDto;
import zgoo.cpos.dto.member.MemberDto.MemberDetailDto;
import zgoo.cpos.dto.member.MemberDto.MemberListDto;
import zgoo.cpos.dto.member.MemberDto.MemberRegDto;

@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

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
    public Page<MemberListDto> findMemberWithPagination(Pageable pageable) {

        List<MemberListDto> memberList = queryFactory.select(Projections.fields(MemberListDto.class,
                member.id.as("memberId"),
                member.memLoginId.as("memLoginId"),
                member.bizType.as("bizType"),
                member.name.as("name"),
                member.phoneNo.as("phoneNo"),
                member.idTag.as("idTag"),
                member.email.as("email"),
                member.joinedDt.as("joinedDt"),
                bizTypeName.name.as("bizTypeName")))
                .from(member)
                .leftJoin(bizTypeName).on(member.bizType.eq(bizTypeName.commonCode))
                .orderBy(member.joinedDt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(member.count())
                .from(member)
                .leftJoin(bizTypeName).on(member.bizType.eq(bizTypeName.commonCode))
                .fetchOne();

        return new PageImpl<>(memberList, pageable, totalCount);
    }

    @Override
    public Page<MemberListDto> searchMemberWithPagination(String idTag, String name,
            Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (idTag != null && !idTag.isEmpty()) {
            builder.and(member.idTag.contains(idTag));
        }

        if (name != null && !name.isEmpty()) {
            builder.and(member.name.contains(name));
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
                bizTypeName.name.as("bizTypeName")))
                .from(member)
                .leftJoin(bizTypeName).on(member.bizType.eq(bizTypeName.commonCode))
                .where(builder)
                .orderBy(member.joinedDt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(member.count())
                .from(member)
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
                bizTypeName.name.as("bizTypeName")))
                .from(member)
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
                biz.id.as("bizId"),
                bizTypeName.name.as("bizTypeName")))
                .from(member)
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
                bizTypeName.name.as("bizTypeName"),
                userStateName.name.as("userStateName")))
                .from(member)
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
                biz.id.as("bizId"),
                bizTypeName.name.as("bizTypeName"),
                userStateName.name.as("userStateName")))
                .from(member)
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
    public List<MemberListDto> findMemberList(String name, String phoneNo) {
        BooleanBuilder builder = new BooleanBuilder();

        if (name != null && !name.isEmpty()) {
            builder.and(member.name.contains(name));
        }

        if (phoneNo != null && !phoneNo.isEmpty()) {
            builder.and(member.phoneNo.contains(phoneNo));
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
                bizTypeName.name.as("bizTypeName")))
                .from(member)
                .leftJoin(bizTypeName).on(member.bizType.eq(bizTypeName.commonCode))
                .where(builder)
                .fetch();

        return memberList;
    }

    @Override
    public List<MemberListDto> findAllMemberListWithoutPagination(String idTag, String name) {
        BooleanBuilder builder = new BooleanBuilder();

        if (idTag != null && !idTag.isEmpty()) {
            builder.and(member.idTag.contains(idTag));
        }

        if (name != null && !name.isEmpty()) {
            builder.and(member.name.contains(name));
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
                bizTypeName.name.as("bizTypeName")))
                .from(member)
                .leftJoin(bizTypeName).on(member.bizType.eq(bizTypeName.commonCode))
                .where(builder)
                .orderBy(member.joinedDt.desc())
                .fetch();
    }

    @Override
    public List<MemberBaseDto> findAllMembersWithEmailAndMarketing() {

        // 이메일이 null 또는 빈문자열이 아닌 회원ID 추출
        List<Long> memberIds = queryFactory
                .select(member.id)
                .from(member)
                .where(member.email.isNotNull(), member.email.trim().ne(""))
                .fetch();

        return queryFactory.select(Projections.fields(MemberBaseDto.class,
                condition.member.id.as("memberId"),
                member.email.as("email"),
                member.name.as("name")))
                .from(condition)
                .leftJoin(member).on(member.eq(condition.member))
                .where(condition.agreeYn.eq("Y")
                        .and(condition.condition.conditionCode.eq("ES"))
                        .and(condition.member.id.in(memberIds)))
                .fetch();
    }
}
