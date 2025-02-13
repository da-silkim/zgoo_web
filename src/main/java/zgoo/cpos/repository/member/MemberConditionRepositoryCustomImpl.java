package zgoo.cpos.repository.member;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.member.MemberCondition;
import zgoo.cpos.domain.member.QConditionCode;
import zgoo.cpos.domain.member.QMemberCondition;
import zgoo.cpos.dto.member.MemberDto.MemberConditionDto;

@Slf4j
@RequiredArgsConstructor
public class MemberConditionRepositoryCustomImpl implements MemberConditionRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QMemberCondition memberCondition = QMemberCondition.memberCondition;
    QConditionCode conCode = QConditionCode.conditionCode1;

    @Override
    public List<MemberConditionDto> findAllByMemberIdDto(Long memberId) {
        return queryFactory.select(Projections.fields(MemberConditionDto.class,
            memberCondition.condition.conditionCode.as("conditionCode"),
            memberCondition.agreeYn.as("agreeYn"),
            memberCondition.agreeDt.as("agreeDt"),
            Expressions.stringTemplate("IF({0} = 'Y', '동의', '미동의')", memberCondition.agreeYn).as("agreeYnCheck"),
            conCode.conditionName.as("conditionCodeName"),
            Expressions.stringTemplate("IF({0} = 'Y', '필수', '선택')", conCode.section).as("section")))
            .from(memberCondition)
            .leftJoin(conCode).on(conCode.conditionCode.eq(memberCondition.condition.conditionCode))
            .where(memberCondition.member.id.eq(memberId))
            .fetch();
    }

    @Override
    public List<MemberCondition> findAllByMemberId(Long memberId) {
        return queryFactory
            .selectFrom(memberCondition)
            .where(memberCondition.member.id.eq(memberId))
            .fetch();
    }

    @Override
    public void deleteAllByMemberId(Long memberId) {
        queryFactory
            .delete(memberCondition)
            .where(memberCondition.member.id.eq(memberId))
            .execute();
    }
}
