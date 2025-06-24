package zgoo.cpos.repository.payment;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.charger.QCpInfo;
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.company.QCompanyRelationInfo;
import zgoo.cpos.domain.cs.QCsInfo;
import zgoo.cpos.domain.history.QChargingHist;
import zgoo.cpos.domain.payment.QPgTrxRecon;
import zgoo.cpos.dto.history.PaymentHistDto;
import zgoo.cpos.util.QueryUtils;

@RequiredArgsConstructor
@Slf4j
public class PgTrxReconRepositoryCustomImpl implements PgTrxReconRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QPgTrxRecon recon = QPgTrxRecon.pgTrxRecon;
    private final QCompany company = QCompany.company;
    private final QCompanyRelationInfo relation = QCompanyRelationInfo.companyRelationInfo;
    private final QCsInfo csInfo = QCsInfo.csInfo;
    private final QCpInfo cpInfo = QCpInfo.cpInfo;
    private final QChargingHist chargingHist = QChargingHist.chargingHist;

    @Override
    public Page<PaymentHistDto> findPaymentHist(Pageable pageable, String levelPath, boolean isSuperAdmin,
            Long companyId, String searchOp, String searchContent, String stateCode, String transactionStart,
            String transactionEnd) {

        log.info(
                "=== >> findPaymentHist >> companyId: {}, searchOp: {}, searchContent: {}, stateCode: {}, transactionStart: {}, transactionEnd: {}",
                companyId, searchOp, searchContent, stateCode, transactionStart, transactionEnd);

        BooleanBuilder builder = new BooleanBuilder();

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        if (companyId != null) {
            builder.and(csInfo.company.id.eq(companyId));
        }

        if (transactionStart != null && !transactionStart.isEmpty()) {
            // 날짜 형식 확인 및 변환
            String fromDateStr;
            if (transactionStart.length() <= 10) { // yyyy-MM-dd 형식인 경우
                fromDateStr = transactionStart.replace("-", ""); // YYYYMMDD 형식으로 변환
            } else { // 이미 yyyyMMdd 형식인 경우
                fromDateStr = transactionStart;
            }
            builder.and(recon.appDt.goe(fromDateStr));
        }

        if (transactionEnd != null && !transactionEnd.isEmpty()) {
            // 날짜 형식 확인 및 변환
            String toDateStr;
            if (transactionEnd.length() <= 10) { // yyyy-MM-dd 형식인 경우
                toDateStr = transactionEnd.replace("-", ""); // YYYYMMDD 형식으로 변환
            } else { // 이미 yyyyMMdd 형식인 경우
                toDateStr = transactionEnd;
            }
            builder.and(recon.appDt.loe(toDateStr));
        }

        if (stateCode != null && !stateCode.isEmpty()) {
            builder.and(recon.stateCd.eq(stateCode));
        }

        if (searchOp != null && !searchOp.isEmpty()) {
            switch (searchOp.toLowerCase()) {
                case "tid":
                    builder.and(recon.tid.containsIgnoreCase(searchContent)
                            .or(recon.otid.containsIgnoreCase(searchContent)));
                    break;
                case "approvalnum":
                    builder.and(recon.appNo.containsIgnoreCase(searchContent));
                    break;
                case "chargerid":
                    builder.and(recon.goodsNm.containsIgnoreCase(searchContent));
                    break;
                default:
                    break;
            }
        }

        // 쿼리 실행
        List<PaymentHistDto> results = queryFactory
                .select(Projections.fields(PaymentHistDto.class,
                        company.companyName.as("companyName"),
                        recon.goodsNm.as("chargerId"),
                        Expressions.dateTimeTemplate(Timestamp.class,
                                "CASE WHEN {0} IS NOT NULL AND {1} IS NOT NULL THEN STR_TO_DATE(CONCAT({0},{1}),'%Y%m%d %H%i%s') ELSE NULL END",
                                recon.appDt, recon.appTm).as("approvalTime"),
                        Expressions.dateTimeTemplate(Timestamp.class,
                                "CASE WHEN {0} IS NOT NULL AND {1} IS NOT NULL THEN STR_TO_DATE(CONCAT({0},{1}),'%Y%m%d %H%i%s') ELSE NULL END",
                                recon.ccDt, recon.ccTm).as("cancelTime"),
                        recon.mid.as("mid"),
                        recon.tid.as("tid"),
                        recon.otid.as("otid"),
                        recon.goodsAmt.as("goddsAmt"),
                        recon.stateCd.as("stateCode"),
                        recon.appNo.as("approvalNum"),
                        recon.fnCd.as("cardCompany"),
                        recon.paymentNo.as("cardNum"),
                        recon.createdAt.as("regTime")))
                .from(recon)
                .leftJoin(cpInfo).on(recon.goodsNm.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.id.eq(company.id))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .orderBy(recon.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수 조회
        long total = queryFactory
                .select(recon.count())
                .from(recon)
                .leftJoin(cpInfo).on(recon.goodsNm.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.id.eq(company.id))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }

}
