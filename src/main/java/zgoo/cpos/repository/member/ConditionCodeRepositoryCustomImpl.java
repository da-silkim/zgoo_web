package zgoo.cpos.repository.member;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.member.ConditionCode;
import zgoo.cpos.domain.member.QConditionCode;
import zgoo.cpos.domain.member.QConditionVersionHist;
import zgoo.cpos.dto.member.ConditionDto.ConditionCodeBaseDto;
import zgoo.cpos.dto.member.ConditionDto.ConditionList;

@Slf4j
@RequiredArgsConstructor
public class ConditionCodeRepositoryCustomImpl implements ConditionCodeRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final MessageSource messageSource;
    QConditionCode conditionCode = QConditionCode.conditionCode1;
    QConditionVersionHist conditionVersion = QConditionVersionHist.conditionVersionHist;

    String getRequiredName() {
        return messageSource.getMessage("conditionmgmt.labels.requred", null,
                LocaleContextHolder.getLocale());
    }

    String getOptionalName() {
        return messageSource.getMessage("conditionmgmt.labels.optional", null,
                LocaleContextHolder.getLocale());
    }

    @Override
    public List<ConditionCodeBaseDto> findAllCustom() {
        List<ConditionCodeBaseDto> conList = queryFactory
                .select(Projections.fields(ConditionCodeBaseDto.class,
                        conditionCode.conditionCode.as("conditionCode"),
                        conditionCode.conditionName.as("conditionName"),
                        Expressions.stringTemplate("IF({0} = 'Y', {1}, {2})", conditionCode.section, getRequiredName(),
                                getOptionalName()).as("section"),
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

    @Override
    public List<ConditionList> findAllConditionWithVersion() {
        List<ConditionList> conList = queryFactory
                .select(Projections.fields(ConditionList.class,
                        conditionCode.conditionCode.as("conditionCode"),
                        conditionCode.conditionName.as("conditionName"),
                        Expressions.stringTemplate("IF({0} = 'Y', {1}, {2})", conditionCode.section, getRequiredName(),
                                getOptionalName()).as("section"),
                        conditionCode.regDt.as("regDt"),
                        conditionVersion.version.as("agreeVersion")))
                .from(conditionCode)
                .leftJoin(conditionVersion)
                .on(conditionCode.conditionCode.eq(conditionVersion.conditionCode.conditionCode),
                        conditionVersion.applyYn.eq("Y"))
                .orderBy(conditionCode.section.desc())
                .fetch();
        return conList;
    }
}
