package zgoo.cpos.repository.member;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.member.ConditionCode;
import zgoo.cpos.domain.member.QConditionCode;
import zgoo.cpos.dto.member.ConditionDto.ConditionCodeBaseDto;

@Slf4j
@RequiredArgsConstructor
public class ConditionCodeRepositoryCustomImpl implements ConditionCodeRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QConditionCode conditionCode = QConditionCode.conditionCode1;

    @Override
    public List<ConditionCodeBaseDto> findAllCustom() {
        List<ConditionCodeBaseDto> conList = queryFactory
                .select(Projections.fields(ConditionCodeBaseDto.class,
                    conditionCode.conditionCode.as("conditionCode"),
                    conditionCode.conditionName.as("conditionName"),
                    Expressions.stringTemplate("IF({0} = 'Y', '필수', '선택')", conditionCode.section).as("section"),
                    conditionCode.regDt.as("regDt")))
                .from(conditionCode)
                .orderBy(conditionCode.section.desc(), conditionCode.regDt.desc())
                .fetch();
        return conList;
    }

    @Override
    public ConditionCode findByConditionCode(String conCode) {
        return queryFactory
                .selectFrom(conditionCode)
                .where(conditionCode.conditionCode.eq(conCode))
                .fetchOne();
    }

    @Override
    public void deleteByConditionCode(String conCode) {
        queryFactory
                .delete(conditionCode)
                .where(conditionCode.conditionCode.eq(conCode))
                .execute();
    }
}
