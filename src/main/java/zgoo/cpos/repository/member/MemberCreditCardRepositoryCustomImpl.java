package zgoo.cpos.repository.member;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.domain.member.MemberCreditCard;
import zgoo.cpos.domain.member.QMemberCreditCard;
import zgoo.cpos.dto.member.MemberDto.MemberCreditCardDto;

@Slf4j
@RequiredArgsConstructor
public class MemberCreditCardRepositoryCustomImpl implements MemberCreditCardRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QMemberCreditCard card = QMemberCreditCard.memberCreditCard;
    QCommonCode fnCodeName = new QCommonCode("fnCode");

    @Override
    public List<MemberCreditCardDto> findAllByMemberIdDto(Long memberId) {
        return queryFactory.select(Projections.fields(MemberCreditCardDto.class,
            card.tid.as("tid"),
            card.cardNum.as("cardNum"),
            card.fnCode.as("fnCode"),
            card.representativeCard.as("representativeCard"),
            Expressions.stringTemplate("IF({0} = 'Y', '대표카드', '')", card.representativeCard).as("representativeCardCheck"),
            Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '미등록', '등록완료')", card.tid).as("tidCheck"),
            card.regDt.as("cardRegDt"),
            fnCodeName.name.as("fnCodeName")))
            .from(card)
            .leftJoin(fnCodeName).on(card.fnCode.eq(fnCodeName.commonCode))
            .where(card.member.id.eq(memberId))
            .fetch();
    }

    @Override
    public List<MemberCreditCard> findAllByMemberId(Long memberId) {
        return queryFactory
            .selectFrom(card)
            .where(card.member.id.eq(memberId))
            .fetch();
    }

    @Override
    public void deleteAllByMemberId(Long memberId) {
        queryFactory
            .delete(card)
            .where(card.member.id.eq(memberId))
            .execute();
    }
}
