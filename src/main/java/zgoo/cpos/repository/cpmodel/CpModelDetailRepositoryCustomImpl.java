package zgoo.cpos.repository.cpmodel;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.cp.CpModelDetail;
import zgoo.cpos.domain.cp.QCpModelDetail;

@Slf4j
@RequiredArgsConstructor
public class CpModelDetailRepositoryCustomImpl implements CpModelDetailRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QCpModelDetail modelDetail = QCpModelDetail.cpModelDetail;

    @Override
    public CpModelDetail findByModelId(Long modelId) {
        return queryFactory
            .selectFrom(modelDetail)
            .where(modelDetail.cpModel.id.eq(modelId))
            .fetchOne();
    }

    @Override
    public void deleteByModelId(Long modelId) {
        queryFactory
            .delete(modelDetail)
            .where(modelDetail.cpModel.id.eq(modelId))
            .execute();
    }
}
