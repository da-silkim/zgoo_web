package zgoo.cpos.repository.member;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.domain.member.MemberCondition;
import zgoo.cpos.domain.member.QMemberCondition;
import zgoo.cpos.dto.member.MemberDto.MemberConditionDto;

@Slf4j
@RequiredArgsConstructor
public class MemberConditionRepositoryCustomImpl implements MemberConditionRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QMemberCondition condition = QMemberCondition.memberCondition;
    QCommonCode conditionCodeName = new QCommonCode("conditionCode");

    @Override
    public List<MemberConditionDto> findAllByMemberIdDto(Long memberId) {
        return queryFactory.select(Projections.fields(MemberConditionDto.class,
            condition.conditionCode.as("conditionCode"),
            condition.agreeYn.as("agreeYn"),
            condition.section.as("section"),
            Expressions.stringTemplate("IF({0} = 'Y', '동의', '미동의')", condition.agreeYn).as("agreeYnCheck"),
            conditionCodeName.name.as("conditionCodeName")))
            .from(condition)
            .leftJoin(conditionCodeName).on(condition.conditionCode.eq(conditionCodeName.commonCode))
            .where(condition.member.id.eq(memberId))
            .fetch();
    }

    @Override
    public List<MemberCondition> findAllByMemberId(Long memberId) {
        return queryFactory
            .selectFrom(condition)
            .where(condition.member.id.eq(memberId))
            .fetch();
    }

    @Override
    public void deleteAllByMemberId(Long memberId) {
        queryFactory
            .delete(condition)
            .where(condition.member.id.eq(memberId))
            .execute();
    }
}
