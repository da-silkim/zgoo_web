package zgoo.cpos.repository.member;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.member.ConditionVersionHist;
import zgoo.cpos.domain.member.QConditionCode;
import zgoo.cpos.domain.member.QConditionVersionHist;
import zgoo.cpos.dto.member.ConditionDto.ConditionVersionHistBaseDto;

@Slf4j
@RequiredArgsConstructor
public class ConditionVersionHistRepositoryCustomImpl implements ConditionVersionHistRepositoryCustom {
        private final JPAQueryFactory queryFactory;
        QConditionVersionHist conditionVersionHist = QConditionVersionHist.conditionVersionHist;
        QConditionCode condition = QConditionCode.conditionCode1;

        @Override
        public List<ConditionVersionHistBaseDto> findAllByConditionCode(String conditionCode) {
                List<ConditionVersionHistBaseDto> conList = queryFactory
                        .select(Projections.fields(ConditionVersionHistBaseDto.class,
                                conditionVersionHist.id.as("conditionVersionHistId"),
                                conditionVersionHist.filePath.as("filePath"),
                                conditionVersionHist.originalName.as("originalName"),
                                conditionVersionHist.storedName.as("storedName"),
                                conditionVersionHist.version.as("version"),
                                conditionVersionHist.memo.as("memo"),
                                conditionVersionHist.applyYn.as("applyYn"),
                                conditionVersionHist.regDt.as("histRegDt"),
                                conditionVersionHist.applyDt.as("applyDt")))
                        .from(conditionVersionHist)
                        .where(conditionVersionHist.conditionCode.conditionCode.eq(conditionCode))
                        .orderBy(conditionVersionHist.version.desc())
                        .fetch();
                return conList;
        }

        @Override
        public void deleteByConditionCode(String conCode) {
                queryFactory
                        .delete(conditionVersionHist)
                        .where(conditionVersionHist.conditionCode.conditionCode.eq(conCode))
                        .execute();
        }

        @Override
        public ConditionVersionHistBaseDto findByIdCustom(Long id) {
                ConditionVersionHistBaseDto con = queryFactory
                        .select(Projections.fields(ConditionVersionHistBaseDto.class,
                                conditionVersionHist.id.as("conditionVersionHistId"),
                                conditionVersionHist.filePath.as("filePath"),
                                conditionVersionHist.originalName.as("originalName"),
                                conditionVersionHist.storedName.as("storedName"),
                                conditionVersionHist.version.as("version"),
                                conditionVersionHist.memo.as("memo"),
                                conditionVersionHist.applyYn.as("applyYn"),
                                conditionVersionHist.regDt.as("histRegDt"),
                                conditionVersionHist.applyDt.as("applyDt")))
                        .from(conditionVersionHist)
                        .where(conditionVersionHist.id.eq(id))
                        .fetchOne();
                return con;
        }

        @Override
        public ConditionVersionHist findRecentHistByConditionCode(String conditionCode, LocalDateTime applyDt) {
                return queryFactory
                        .selectFrom(conditionVersionHist)
                        .where(conditionVersionHist.conditionCode.conditionCode.eq(conditionCode)
                                .and(conditionVersionHist.applyDt.loe(applyDt)))
                        .orderBy(conditionVersionHist.applyDt.desc())
                        .fetchFirst();
        }

        @Override
        public ConditionVersionHist findApplyYesByConditionCode(String conditionCode) {
                return queryFactory
                        .selectFrom(conditionVersionHist)
                        .where(conditionVersionHist.conditionCode.conditionCode.eq(conditionCode)
                                .and(conditionVersionHist.applyYn.eq("Y")))
                        .fetchOne();
        }

        @Override
        public ConditionVersionHist findRevisionConditionByConditionCode(String conditionCode) {
                return queryFactory
                        .selectFrom(conditionVersionHist)
                        .where(conditionVersionHist.conditionCode.conditionCode.eq(conditionCode)
                                .and(conditionVersionHist.applyYn.eq("N"))
                                .and(conditionVersionHist.applyDt.gt(LocalDateTime.now()))
                                .and(conditionVersionHist.applyDt.eq(LocalDate.now().atStartOfDay().plusDays(30))))
                        .fetchOne();
        }
}
