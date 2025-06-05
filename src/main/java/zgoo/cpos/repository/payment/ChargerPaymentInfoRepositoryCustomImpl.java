package zgoo.cpos.repository.payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.charger.QCpInfo;
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.company.QCompanyRelationInfo;
import zgoo.cpos.domain.cp.QCpModel;
import zgoo.cpos.domain.cs.QCsInfo;
import zgoo.cpos.domain.history.QChargingHist;
import zgoo.cpos.domain.payment.PgTrxRecon;
import zgoo.cpos.domain.payment.QChargerPaymentInfo;
import zgoo.cpos.domain.payment.QPgSettlmntDetail;
import zgoo.cpos.domain.payment.QPgSettlmntTotal;
import zgoo.cpos.domain.payment.QPgTrxRecon;
import zgoo.cpos.dto.payment.ChgPaymentInfoDto;
import zgoo.cpos.dto.payment.ChgPaymentSummaryDto;
import zgoo.cpos.dto.statistics.PurchaseSalesDto.PurchaseSalesLineChartBaseDto;
import zgoo.cpos.dto.statistics.PurchaseSalesDto.SalesDashboardDto;
import zgoo.cpos.util.QueryUtils;

@RequiredArgsConstructor
@Slf4j
public class ChargerPaymentInfoRepositoryCustomImpl implements ChargerPaymentInfoRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChgPaymentInfoDto> findChgPaymentInfo(String startMonthSearch, String endMonthSearch, String searchOp,
            String searchContent, Long companyId, Pageable pageable, String levelPath, boolean isSuperAdmin) {

        QChargerPaymentInfo chgPaymentInfo = QChargerPaymentInfo.chargerPaymentInfo;
        QCompany company = QCompany.company;
        QCompanyRelationInfo relation = QCompanyRelationInfo.companyRelationInfo;
        QCpInfo cpInfo = QCpInfo.cpInfo;
        QCsInfo csInfo = QCsInfo.csInfo;
        QChargingHist chargingHist = QChargingHist.chargingHist;
        QPgTrxRecon pgTrxRecon = QPgTrxRecon.pgTrxRecon;

        log.info(
                "=== >> find charger payment info with search condition: searchOp: {}, searchContent: {}, startMonthSearch: {}, endMonthSearch: {}, companyId: {}",
                searchOp, searchContent, startMonthSearch, endMonthSearch, companyId);

        // 파라미터 순서 확인 및 수정
        // 컨트롤러에서 전달된 순서: opSearch, contentSearch, startMonth, endMonth, companyId
        // 현재 메서드 파라미터: startMonthSearch, endMonthSearch, searchOp, searchContent,
        // companyId
        // 실제 사용해야 할 값으로 재할당
        String actualSearchOp = searchOp;
        String actualSearchContent = searchContent;
        String actualStartMonth = startMonthSearch;
        String actualEndMonth = endMonthSearch;

        // 값이 잘못 전달된 경우 (로그에서 확인된 패턴)
        if ((searchOp != null && (searchOp.matches("\\d{4}-\\d{2}") || searchOp.matches("\\d{6}"))) ||
                (searchContent != null
                        && (searchContent.matches("\\d{4}-\\d{2}") || searchContent.matches("\\d{6}")))) {
            // 날짜 형식이 searchOp나 searchContent에 있으면 파라미터가 잘못 전달된 것
            log.info("Detected parameter order mismatch, rearranging parameters");
            actualStartMonth = searchOp;
            actualEndMonth = searchContent;
            actualSearchOp = startMonthSearch;
            actualSearchContent = endMonthSearch;
        }

        BooleanBuilder builder = new BooleanBuilder();

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        if (actualStartMonth != null && !actualStartMonth.isEmpty()) {
            try {
                // 년월 형식 변환 (YYYYMM -> YYYY-MM 또는 그대로 사용)
                String formattedStartMonth = actualStartMonth;
                if (actualStartMonth.matches("\\d{6}")) {
                    formattedStartMonth = actualStartMonth.substring(0, 4) + "-" + actualStartMonth.substring(4, 6);
                    log.info("Reformatted start month from {} to {}", actualStartMonth, formattedStartMonth);
                }

                // 년월 형식(YYYY-MM)을 해당 월의 첫 날로 변환
                LocalDateTime fromDateTime = YearMonth.parse(formattedStartMonth)
                        .atDay(1)
                        .atStartOfDay();

                builder.and(chgPaymentInfo.timestamp.goe(fromDateTime));
                log.info("Applied start date filter: {}", fromDateTime);
            } catch (Exception e) {
                log.warn("Invalid startMonthSearch format: {}", actualStartMonth, e);
            }
        }

        if (actualEndMonth != null && !actualEndMonth.isEmpty()) {
            try {
                // 년월 형식 변환 (YYYYMM -> YYYY-MM 또는 그대로 사용)
                String formattedEndMonth = actualEndMonth;
                if (actualEndMonth.matches("\\d{6}")) {
                    formattedEndMonth = actualEndMonth.substring(0, 4) + "-" + actualEndMonth.substring(4, 6);
                    log.info("Reformatted end month from {} to {}", actualEndMonth, formattedEndMonth);
                }

                // 년월 형식(YYYY-MM)을 해당 월의 마지막 날로 변환
                LocalDateTime toDateTime;

                // 현재 날짜 확인
                LocalDate currentDate = LocalDate.now();
                YearMonth endYearMonth = YearMonth.parse(formattedEndMonth);

                // 조회 종료 월이 현재 월과 같은 경우, 전일까지만 조회
                if (endYearMonth.getYear() == currentDate.getYear() &&
                        endYearMonth.getMonthValue() == currentDate.getMonthValue()) {

                    // // 전일 날짜의 끝으로 설정 (어제 23:59:59) - real
                    // toDateTime = LocalDate.now().minusDays(1).atTime(23, 59, 59);

                    // 오늘 날짜의 끝으로 설정 (오늘 23:59:59) - test
                    toDateTime = LocalDate.now().atTime(23, 59, 59);

                    log.info("Current month detected. Setting end date to yesterday: {}", toDateTime);
                } else {
                    // 다른 월은 해당 월의 마지막 날로 설정
                    toDateTime = endYearMonth.atEndOfMonth()
                            .atTime(23, 59, 59);
                    log.info("Setting end date to end of month: {}", toDateTime);
                }

                builder.and(chgPaymentInfo.timestamp.loe(toDateTime));
            } catch (Exception e) {
                log.warn("Invalid endMonthSearch format: {}", actualEndMonth, e);
            }
        }

        if (companyId != null) {
            builder.and(company.id.eq(companyId));
        }

        if (actualSearchOp != null && !actualSearchOp.isEmpty() && actualSearchContent != null
                && !actualSearchContent.isEmpty()) {
            log.info("Applying search condition: {} = {}", actualSearchOp, actualSearchContent);

            switch (actualSearchOp.toLowerCase()) {
                case "stationname":
                    builder.and(csInfo.stationName.containsIgnoreCase(actualSearchContent));
                    break;
                case "chargerid":
                    builder.and(chgPaymentInfo.chargerId.containsIgnoreCase(actualSearchContent));
                    break;
                case "cardnumber":
                    builder.and(chgPaymentInfo.preCardNum.containsIgnoreCase(actualSearchContent));
                    break;
                case "approvalnumber":
                    builder.and(chgPaymentInfo.preApprovalNo.containsIgnoreCase(actualSearchContent));
                    break;
                default:
                    log.warn("Unknown search operation: {}, ignoring search condition", actualSearchOp);
                    break;
            }
        } else {
            log.info("No search condition applied");
        }

        // 1. 관제 결제 데이터 조회 (기본 정보)
        List<ChgPaymentInfoDto> results = null;

        // 페이징 여부에 따라 쿼리 실행 방식 분기
        if (pageable.isUnpaged()) {
            // 페이징 없이 전체 데이터 조회
            results = queryFactory
                    .select(Projections.fields(ChgPaymentInfoDto.class,
                            // 관제 결제 데이터 - CONCAT 수정
                            Expressions.stringTemplate(
                                    "DATE_FORMAT(CONCAT('20', {0}, {1}), '%Y-%m-%d %H:%i:%s')",
                                    chgPaymentInfo.preTransactionDate,
                                    chgPaymentInfo.preTransactionTime).as("paymentTime"),
                            company.companyName.as("companyName"),
                            csInfo.stationName.as("stationName"),
                            chgPaymentInfo.chargerId,
                            chargingHist.chargeAmount.as("chgAmount"),
                            chgPaymentInfo.preAmount.as("chgPrice"),
                            chgPaymentInfo.cancelAmount.as("cancelCost"),
                            chgPaymentInfo.resultPrice.as("realCost"),
                            chgPaymentInfo.preApprovalNo.as("pgAppNum"),
                            chgPaymentInfo.preTid.as("tid")))
                    .from(chgPaymentInfo)
                    .leftJoin(cpInfo).on(chgPaymentInfo.chargerId.eq(cpInfo.id))
                    .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                    .leftJoin(company).on(csInfo.company.eq(company))
                    .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                    .leftJoin(chargingHist).on(chgPaymentInfo.transactionId.eq(chargingHist.transactionId))
                    .where(builder)
                    .orderBy(chgPaymentInfo.timestamp.desc())
                    .fetch();
        } else {
            // 페이징 적용하여 데이터 조회
            results = queryFactory
                    .select(Projections.fields(ChgPaymentInfoDto.class,
                            // 관제 결제 데이터 - CONCAT 수정
                            Expressions.stringTemplate(
                                    "DATE_FORMAT(CONCAT('20', {0}, {1}), '%Y-%m-%d %H:%i:%s')",
                                    chgPaymentInfo.preTransactionDate,
                                    chgPaymentInfo.preTransactionTime).as("paymentTime"),
                            company.companyName.as("companyName"),
                            csInfo.stationName.as("stationName"),
                            chgPaymentInfo.chargerId,
                            chargingHist.chargeAmount.as("chgAmount"),
                            chgPaymentInfo.preAmount.as("chgPrice"),
                            chgPaymentInfo.cancelAmount.as("cancelCost"),
                            chgPaymentInfo.resultPrice.as("realCost"),
                            chgPaymentInfo.preApprovalNo.as("pgAppNum"),
                            chgPaymentInfo.preTid.as("tid")))
                    .from(chgPaymentInfo)
                    .leftJoin(cpInfo).on(chgPaymentInfo.chargerId.eq(cpInfo.id))
                    .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                    .leftJoin(company).on(csInfo.company.eq(company))
                    .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                    .leftJoin(chargingHist).on(chgPaymentInfo.transactionId.eq(chargingHist.transactionId))
                    .where(builder)
                    .orderBy(chgPaymentInfo.timestamp.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }

        // 2. 결과가 없으면 빈 페이지 반환
        if (results.isEmpty()) {
            log.info("No results found for the given search criteria");
            return new PageImpl<>(results, pageable, 0);
        }

        // 3. 관제 결제 데이터의 TID 목록 추출
        List<String> tids = results.stream()
                .map(ChgPaymentInfoDto::getTid)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        log.info("Found {} TIDs for PG transaction lookup", tids.size());

        // 4. TID가 없으면 PG 거래 데이터 조회 생략
        if (tids.isEmpty()) {
            // 총 개수 조회 - count() 사용
            Long total = queryFactory
                    .select(chgPaymentInfo.count())
                    .from(chgPaymentInfo)
                    .leftJoin(cpInfo).on(chgPaymentInfo.chargerId.eq(cpInfo.id))
                    .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                    .leftJoin(company).on(csInfo.company.eq(company))
                    .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                    .where(builder)
                    .fetchOne();

            log.info("Total count: {}", total);

            return new PageImpl<>(results, pageable, total != null ? total : 0);
        }

        // 5. PG 거래 데이터 조회 (TID 기준) - 대체 쿼리 추가
        Map<String, List<PgTrxRecon>> pgTrxReconMap = new HashMap<>();
        for (String tid : tids) {
            if (tid == null || tid.isEmpty()) {
                log.warn("Skipping null or empty TID");
                continue;
            }

            log.info("Querying PG transactions for TID: {}", tid);

            List<PgTrxRecon> pgTrxRecons = queryFactory
                    .selectFrom(pgTrxRecon)
                    .where(pgTrxRecon.tid.eq(tid))
                    .fetch();

            // 해당 TID의 거래 데이터가 있으면 맵에 추가
            if (!pgTrxRecons.isEmpty()) {
                pgTrxReconMap.put(tid, pgTrxRecons);
                log.info("Found {} PG transactions for TID: {}", pgTrxRecons.size(), tid);

                // 로그 추가: 각 PG 트랜잭션 레코드 정보 출력
                pgTrxRecons.forEach(trx -> {
                    log.info("PG Transaction: TID={}, State={}, AppNo={}, GoodsAmt={}, CardNo={}",
                            trx.getTid(), trx.getStateCd(), trx.getAppNo(), trx.getGoodsAmt(), trx.getPaymentNo());
                });

                // 승인 거래 찾기 (상태 코드가 0인 거래)
                List<PgTrxRecon> approvedTrxs = pgTrxRecons.stream()
                        .filter(trx -> "0".equals(trx.getStateCd()))
                        .collect(Collectors.toList());

                if (!approvedTrxs.isEmpty()) {
                    PgTrxRecon approvedTrx = approvedTrxs.get(0);

                    // 승인 거래의 TID와 승인번호를 기준으로 취소 거래 조회
                    List<PgTrxRecon> cancelTrxs = queryFactory
                            .selectFrom(pgTrxRecon)
                            .where(
                                    pgTrxRecon.otid.eq(approvedTrx.getTid())
                                            .or(pgTrxRecon.appNo.eq(approvedTrx.getAppNo())
                                                    .and(pgTrxRecon.tid.ne(approvedTrx.getTid()))))
                            .fetch();

                    if (!cancelTrxs.isEmpty()) {
                        log.info("Found {} cancel transactions for TID: {} or AppNo: {}",
                                cancelTrxs.size(), approvedTrx.getTid(), approvedTrx.getAppNo());

                        // 취소 거래 정보 로깅
                        cancelTrxs.forEach(cancelTrx -> {
                            log.info("Cancel Transaction: TID={}, OTID={}, State={}, AppNo={}, GoodsAmt={}",
                                    cancelTrx.getTid(), cancelTrx.getOtid(), cancelTrx.getStateCd(),
                                    cancelTrx.getAppNo(), cancelTrx.getGoodsAmt());
                        });

                        // 취소 거래 목록을 원래 거래 목록에 추가
                        pgTrxRecons.addAll(cancelTrxs);
                        pgTrxReconMap.put(tid, pgTrxRecons);
                    }
                }
            }
        }

        // 5. PG 거래 데이터 조회 (TID 기준) - 대체 쿼리 추가
        if (pgTrxReconMap.isEmpty() && !tids.isEmpty()) {
            log.warn("No PG transactions found with direct TID match. Trying alternative query...");

            // TID 패턴 매칭 시도 (TID가 부분 문자열로 포함된 경우)
            for (String tid : tids) {
                if (tid == null || tid.isEmpty())
                    continue;

                List<PgTrxRecon> pgTrxRecons = queryFactory
                        .selectFrom(pgTrxRecon)
                        .where(pgTrxRecon.tid.like("%" + tid + "%")
                                .or(pgTrxRecon.otid.like("%" + tid + "%")))
                        .fetch();

                if (!pgTrxRecons.isEmpty()) {
                    log.info("Found {} PG transactions with pattern matching for TID: {}", pgTrxRecons.size(), tid);
                    pgTrxReconMap.put(tid, pgTrxRecons);

                    // 로그 추가
                    pgTrxRecons.forEach(trx -> {
                        log.info("Pattern matched PG Transaction: TID={}, OTID={}, State={}, AppNo={}",
                                trx.getTid(), trx.getOtid(), trx.getStateCd(), trx.getAppNo());
                    });
                }
            }
        }

        log.info("Found PG transactions for {} TIDs", pgTrxReconMap.size());

        // 6. PG 거래 데이터를 DTO에 매핑
        for (ChgPaymentInfoDto dto : results) {
            String tid = dto.getTid();
            String pgAppNum = dto.getPgAppNum();

            if (tid == null || tid.isEmpty()) {
                log.info("Skipping DTO with null or empty TID");
                continue;
            }

            log.info("Processing DTO with TID: {}, PgAppNum: {}", tid, pgAppNum);

            // TID로 매칭된 트랜잭션 확인
            List<PgTrxRecon> trxRecons = pgTrxReconMap.get(tid);

            // TID로 매칭된 트랜잭션이 없으면 승인번호로 직접 찾기
            if ((trxRecons == null || trxRecons.isEmpty()) && pgAppNum != null && !pgAppNum.isEmpty()) {
                log.info("No transactions found by TID, trying by approval number: {}", pgAppNum);

                // 승인번호로 직접 쿼리
                trxRecons = queryFactory
                        .selectFrom(pgTrxRecon)
                        .where(pgTrxRecon.appNo.eq(pgAppNum))
                        .fetch();

                if (!trxRecons.isEmpty()) {
                    log.info("Found {} transactions by approval number: {}", trxRecons.size(), pgAppNum);
                    pgTrxReconMap.put(tid, trxRecons); // 캐시에 추가
                }
            }

            if (trxRecons == null || trxRecons.isEmpty()) {
                log.warn("No PG transactions found for TID: {} or AppNum: {}", tid, pgAppNum);
                continue;
            }

            // 상태 코드별로 트랜잭션 분류 - 수정된 부분
            Map<String, List<PgTrxRecon>> trxListByState = new HashMap<>();
            for (PgTrxRecon trx : trxRecons) {
                String stateCd = trx.getStateCd();
                if (!trxListByState.containsKey(stateCd)) {
                    trxListByState.put(stateCd, new ArrayList<>());
                }
                trxListByState.get(stateCd).add(trx);
                log.info("Classified transaction: TID={}, State={}, GoodsAmt={}",
                        trx.getTid(), stateCd, trx.getGoodsAmt());
            }

            // 상태 코드 0 (승인) 처리
            List<PgTrxRecon> approvedTrxList = trxListByState.getOrDefault("0", Collections.emptyList());
            if (!approvedTrxList.isEmpty()) {
                // 승인 거래는 첫 번째 것만 사용
                PgTrxRecon approvedTrx = approvedTrxList.get(0);
                log.info("Processing approved transaction (state=0): TID={}, AppNo={}, GoodsAmt={}",
                        approvedTrx.getTid(), approvedTrx.getAppNo(), approvedTrx.getGoodsAmt());

                // 승인 일시 설정
                if (approvedTrx.getAppDt() != null && approvedTrx.getAppTm() != null) {
                    dto.setPgAppTime(formatDateTime(approvedTrx.getAppDt(), approvedTrx.getAppTm()));
                }

                // 결제 수단 타입 설정
                dto.setPgPayType(getPaymentTypeName(approvedTrx.getPartSvcCd(), approvedTrx.getFnCd()));

                // 카드 번호 설정
                dto.setCardNumber(approvedTrx.getPaymentNo());

                // 원거래 TID 설정
                dto.setOtid(approvedTrx.getOtid());

                // 상태 코드 설정
                dto.setStateCd(approvedTrx.getStateCd());

                // 승인 금액 설정
                if (approvedTrx.getGoodsAmt() != null) {
                    dto.setPgAppAmount(approvedTrx.getGoodsAmt());

                    // 취소 금액 합산 (상태코드 1, 2 모두 포함)
                    BigDecimal totalCancelAmount = BigDecimal.ZERO;

                    // 상태코드 1 (전체 취소) 처리
                    List<PgTrxRecon> fullCancelTrxList = trxListByState.getOrDefault("1", Collections.emptyList());
                    for (PgTrxRecon cancelTrx : fullCancelTrxList) {
                        if (cancelTrx.getGoodsAmt() != null) {
                            totalCancelAmount = totalCancelAmount.add(cancelTrx.getGoodsAmt());
                            log.info("Adding full cancel amount: {}, TID: {}",
                                    cancelTrx.getGoodsAmt(), cancelTrx.getTid());
                        }
                    }

                    // 상태코드 2 (부분 취소) 처리
                    List<PgTrxRecon> partialCancelTrxList = trxListByState.getOrDefault("2", Collections.emptyList());
                    for (PgTrxRecon cancelTrx : partialCancelTrxList) {
                        if (cancelTrx.getGoodsAmt() != null) {
                            totalCancelAmount = totalCancelAmount.add(cancelTrx.getGoodsAmt());
                            log.info("Adding partial cancel amount: {}, TID: {}",
                                    cancelTrx.getGoodsAmt(), cancelTrx.getTid());
                        }
                    }

                    // 취소 금액 설정
                    dto.setPgCancelAmount(totalCancelAmount);

                    // 실제 결제 금액 = 승인 금액 - 취소 금액
                    BigDecimal appAmount = approvedTrx.getGoodsAmt();
                    BigDecimal paymentAmount = appAmount.subtract(totalCancelAmount);
                    dto.setPgPaymentAmount(paymentAmount);

                    log.info("Final calculation: AppAmount={}, TotalCancelAmount={}, PaymentAmount={}",
                            appAmount, totalCancelAmount, paymentAmount);
                }

                continue; // 승인 거래 처리 완료
            }

            // 상태 코드 1 (전체 취소) 처리
            List<PgTrxRecon> fullCancelTrxList = trxListByState.getOrDefault("1", Collections.emptyList());
            if (!fullCancelTrxList.isEmpty()) {
                log.info("Processing full cancel transaction (state=1): TID={}, AppNo={}, GoodsAmt={}",
                        fullCancelTrxList.get(0).getTid(), fullCancelTrxList.get(0).getAppNo(),
                        fullCancelTrxList.get(0).getGoodsAmt());

                // 승인 일시 설정
                if (fullCancelTrxList.get(0).getAppDt() != null && fullCancelTrxList.get(0).getAppTm() != null) {
                    dto.setPgAppTime(
                            formatDateTime(fullCancelTrxList.get(0).getAppDt(), fullCancelTrxList.get(0).getAppTm()));
                }

                // 결제 수단 타입 설정
                dto.setPgPayType(getPaymentTypeName(fullCancelTrxList.get(0).getPartSvcCd(),
                        fullCancelTrxList.get(0).getFnCd()));

                // 카드 번호 설정
                dto.setCardNumber(fullCancelTrxList.get(0).getPaymentNo());

                // 원거래 TID 설정
                dto.setOtid(fullCancelTrxList.get(0).getOtid());

                // 상태 코드 설정
                dto.setStateCd(fullCancelTrxList.get(0).getStateCd());

                // 전체 취소의 경우 승인 금액과 취소 금액이 동일
                if (fullCancelTrxList.get(0).getGoodsAmt() != null) {
                    BigDecimal amount = fullCancelTrxList.get(0).getGoodsAmt();
                    dto.setPgAppAmount(amount);
                    dto.setPgCancelAmount(amount);
                    dto.setPgPaymentAmount(BigDecimal.ZERO); // 실제 결제 금액은 0
                }

                continue; // 전체 취소 거래 처리 완료
            }

            // 상태 코드 2 (부분 취소) 처리 - 원거래(상태코드 0)가 없는 경우
            List<PgTrxRecon> partialCancelTrxList = trxListByState.getOrDefault("2", Collections.emptyList());
            if (!partialCancelTrxList.isEmpty()) {
                log.info("Processing orphaned partial cancel transaction (state=2): TID={}, OTID={}",
                        partialCancelTrxList.get(0).getTid(), partialCancelTrxList.get(0).getOtid());

                // 원거래 TID로 원거래 정보 조회
                String otid = partialCancelTrxList.get(0).getOtid();
                if (otid != null && !otid.isEmpty()) {
                    PgTrxRecon originalTrx = queryFactory
                            .selectFrom(pgTrxRecon)
                            .where(pgTrxRecon.tid.eq(otid).and(pgTrxRecon.stateCd.eq("0")))
                            .fetchFirst();

                    if (originalTrx != null) {
                        log.info("Found original transaction: TID={}, GoodsAmt={}",
                                originalTrx.getTid(), originalTrx.getGoodsAmt());

                        // 승인 일시 설정
                        if (originalTrx.getAppDt() != null && originalTrx.getAppTm() != null) {
                            dto.setPgAppTime(formatDateTime(originalTrx.getAppDt(), originalTrx.getAppTm()));
                        }

                        // 결제 수단 타입 설정
                        dto.setPgPayType(getPaymentTypeName(originalTrx.getPartSvcCd(), originalTrx.getFnCd()));

                        // 카드 번호 설정
                        dto.setCardNumber(originalTrx.getPaymentNo());

                        // 원거래 TID 설정
                        dto.setOtid(originalTrx.getOtid());

                        // 상태 코드 설정 - 부분 취소 상태로 설정
                        dto.setStateCd(partialCancelTrxList.get(0).getStateCd());

                        // 금액 정보 설정
                        if (originalTrx.getGoodsAmt() != null && partialCancelTrxList.get(0).getGoodsAmt() != null) {
                            dto.setPgAppAmount(originalTrx.getGoodsAmt());
                            dto.setPgCancelAmount(partialCancelTrxList.get(0).getGoodsAmt());

                            // 실제 결제 금액 = 승인 금액 - 취소 금액
                            BigDecimal appAmount = originalTrx.getGoodsAmt();
                            BigDecimal cancelAmount = partialCancelTrxList.get(0).getGoodsAmt();
                            BigDecimal paymentAmount = appAmount.subtract(cancelAmount);
                            dto.setPgPaymentAmount(paymentAmount);
                        }
                    } else {
                        log.warn("Original transaction not found for OTID: {}", otid);

                        // 원거래를 찾지 못한 경우 부분 취소 정보만 설정
                        if (partialCancelTrxList.get(0).getAppDt() != null
                                && partialCancelTrxList.get(0).getAppTm() != null) {
                            dto.setPgAppTime(formatDateTime(partialCancelTrxList.get(0).getAppDt(),
                                    partialCancelTrxList.get(0).getAppTm()));
                        }

                        dto.setPgPayType(
                                getPaymentTypeName(partialCancelTrxList.get(0).getPartSvcCd(),
                                        partialCancelTrxList.get(0).getFnCd()));
                        dto.setCardNumber(partialCancelTrxList.get(0).getPaymentNo());
                        dto.setOtid(partialCancelTrxList.get(0).getOtid());
                        dto.setStateCd(partialCancelTrxList.get(0).getStateCd());

                        if (partialCancelTrxList.get(0).getGoodsAmt() != null) {
                            dto.setPgCancelAmount(partialCancelTrxList.get(0).getGoodsAmt());
                            // 승인 금액과 결제 금액은 알 수 없음
                        }
                    }
                }
            }
        }

        // 7. 문자열로 받은 금액 데이터를 BigDecimal로 변환
        results.forEach(this::convertStringToBigDecimal);

        // 8. 총 개수 조회 - count() 사용
        Long total = queryFactory
                .select(chgPaymentInfo.count())
                .from(chgPaymentInfo)
                .leftJoin(cpInfo).on(chgPaymentInfo.chargerId.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .fetchOne();

        log.info("Total count: {}", total);

        return new PageImpl<>(results, pageable, total != null ? total : 0);
    }

    /**
     * 문자열로 받은 금액 데이터를 BigDecimal로 변환
     */
    private void convertStringToBigDecimal(ChgPaymentInfoDto dto) {
        // cancelCost 변환
        Object cancelCostObj = dto.getCancelCost();
        if (cancelCostObj instanceof String) {
            String cancelCostStr = (String) cancelCostObj;
            try {
                if (cancelCostStr != null && !cancelCostStr.isEmpty()) {
                    dto.setCancelCost(Integer.parseInt(cancelCostStr));
                } else {
                    dto.setCancelCost(0);
                }
            } catch (NumberFormatException e) {
                log.warn("Invalid cancel_cost format: {}", cancelCostStr);
                dto.setCancelCost(0);
            }
        } else if (cancelCostObj == null) {
            dto.setCancelCost(0);
        }

        // realCost 변환
        Object realCostObj = dto.getRealCost();
        if (realCostObj instanceof String) {
            String realCostStr = (String) realCostObj;
            try {
                if (realCostStr != null && !realCostStr.isEmpty()) {
                    dto.setRealCost(Integer.parseInt(realCostStr));
                } else {
                    dto.setRealCost(0);
                }
            } catch (NumberFormatException e) {
                log.warn("Invalid real_cost format: {}", realCostStr);
                dto.setRealCost(0);
            }
        } else if (realCostObj == null) {
            dto.setRealCost(0);
        }

        // chgPrice 변환
        Object chgPriceObj = dto.getChgPrice();
        if (chgPriceObj instanceof String) {
            String chgPriceStr = (String) chgPriceObj;
            try {
                if (chgPriceStr != null && !chgPriceStr.isEmpty()) {
                    dto.setChgPrice(Integer.parseInt(chgPriceStr));
                } else {
                    dto.setChgPrice(0);
                }
            } catch (NumberFormatException e) {
                log.warn("Invalid chg_price format: {}", chgPriceStr);
                dto.setChgPrice(0);
            }
        } else if (chgPriceObj == null) {
            dto.setChgPrice(0);
        }
    }

    // 날짜 및 시간 포맷팅 (YYYYMMDD, HHMMSS -> YYYY-MM-DD HH:MM:SS)
    private String formatDateTime(String date, String time) {
        if (date == null || time == null) {
            log.warn("Date or time is null: date={}, time={}", date, time);
            return null;
        }

        try {
            // 시간이 6자리보다 짧으면 앞에 0 채우기
            String formattedTime = time;
            if (time.length() < 6) {
                formattedTime = String.format("%06d", Integer.parseInt(time));
            }

            return date.substring(0, 4) + "-" +
                    date.substring(4, 6) + "-" +
                    date.substring(6, 8) + " " +
                    formattedTime.substring(0, 2) + ":" +
                    formattedTime.substring(2, 4) + ":" +
                    formattedTime.substring(4, 6);
        } catch (Exception e) {
            log.error("Error formatting date/time: date={}, time={}, error={}", date, time, e.getMessage());
            return null;
        }
    }

    // 지불수단 이름 반환
    private String getPaymentTypeName(String partSvcCd, String fnCd) {
        if (partSvcCd == null && fnCd == null) {
            log.warn("Both partSvcCd and fnCd are null");
            return "알 수 없음";
        }

        if ("0000".equals(partSvcCd)) {
            return "신용카드";
        } else if ("E005".equals(partSvcCd)) {
            return "NFC결제";
        } else {
            // 카드사 코드에 따른 이름 매핑
            Map<String, String> fnCdMap = Map.of(
                    "01", "비씨카드",
                    "02", "국민카드",
                    "03", "하나카드",
                    "04", "삼성카드",
                    "05", "신한카드",
                    "06", "현대카드",
                    "07", "롯데카드");

            return fnCdMap.getOrDefault(fnCd, "기타");
        }
    }

    @Override
    public ChgPaymentSummaryDto calculatePaymentSummary(String startMonthSearch, String endMonthSearch, String searchOp,
            String searchContent, Long companyId, String levelPath, boolean isSuperAdmin) {
        QChargerPaymentInfo chgPaymentInfo = QChargerPaymentInfo.chargerPaymentInfo;
        QCompany company = QCompany.company;
        QCompanyRelationInfo relation = QCompanyRelationInfo.companyRelationInfo;
        QCpInfo cpInfo = QCpInfo.cpInfo;
        QCsInfo csInfo = QCsInfo.csInfo;
        QChargingHist chargingHist = QChargingHist.chargingHist;
        QPgTrxRecon pgTrxRecon = QPgTrxRecon.pgTrxRecon;

        log.info(
                "=== >> Calculating payment summary with search condition: searchOp: {}, searchContent: {}, startMonthSearch: {}, endMonthSearch: {}, companyId: {}",
                searchOp, searchContent, startMonthSearch, endMonthSearch, companyId);

        // 파라미터 순서 확인 및 수정 (기존 findChgPaymentInfo 메서드와 동일한 로직)
        String actualSearchOp = searchOp;
        String actualSearchContent = searchContent;
        String actualStartMonth = startMonthSearch;
        String actualEndMonth = endMonthSearch;

        if ((searchOp != null && (searchOp.matches("\\d{4}-\\d{2}") || searchOp.matches("\\d{6}"))) ||
                (searchContent != null
                        && (searchContent.matches("\\d{4}-\\d{2}") || searchContent.matches("\\d{6}")))) {
            log.info("Detected parameter order mismatch, rearranging parameters");
            actualStartMonth = searchOp;
            actualEndMonth = searchContent;
            actualSearchOp = startMonthSearch;
            actualSearchContent = endMonthSearch;
        }

        // 검색 조건 빌더 생성 (기존 findChgPaymentInfo 메서드와 동일한 로직)
        BooleanBuilder builder = new BooleanBuilder();

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(relation.levelPath.containsIgnoreCase(levelPath));
        }

        // 시작 날짜 조건
        if (actualStartMonth != null && !actualStartMonth.isEmpty()) {
            try {
                String formattedStartMonth = actualStartMonth;
                if (actualStartMonth.matches("\\d{6}")) {
                    formattedStartMonth = actualStartMonth.substring(0, 4) + "-" + actualStartMonth.substring(4, 6);
                }

                LocalDateTime fromDateTime = YearMonth.parse(formattedStartMonth)
                        .atDay(1)
                        .atStartOfDay();

                builder.and(chgPaymentInfo.timestamp.goe(fromDateTime));
            } catch (Exception e) {
                log.warn("Invalid startMonthSearch format: {}", actualStartMonth, e);
            }
        }

        // 종료 날짜 조건
        if (actualEndMonth != null && !actualEndMonth.isEmpty()) {
            try {
                String formattedEndMonth = actualEndMonth;
                if (actualEndMonth.matches("\\d{6}")) {
                    formattedEndMonth = actualEndMonth.substring(0, 4) + "-" + actualEndMonth.substring(4, 6);
                }

                LocalDateTime toDateTime;
                LocalDate currentDate = LocalDate.now();
                YearMonth endYearMonth = YearMonth.parse(formattedEndMonth);

                if (endYearMonth.getYear() == currentDate.getYear() &&
                        endYearMonth.getMonthValue() == currentDate.getMonthValue()) {
                    // // 현재 날짜의 전일 23:59:59로 설정
                    // toDateTime = LocalDate.now().minusDays(1).atTime(23, 59, 59);

                    // 오늘 날짜의 끝으로 설정 (오늘 23:59:59) - test
                    toDateTime = LocalDate.now().atTime(23, 59, 59);
                } else {
                    toDateTime = endYearMonth.atEndOfMonth().atTime(23, 59, 59);
                }

                builder.and(chgPaymentInfo.timestamp.loe(toDateTime));
            } catch (Exception e) {
                log.warn("Invalid endMonthSearch format: {}", actualEndMonth, e);
            }
        }

        // 회사 ID 조건
        if (companyId != null) {
            builder.and(company.id.eq(companyId));
        }

        // 검색 조건
        if (actualSearchOp != null && !actualSearchOp.isEmpty() && actualSearchContent != null
                && !actualSearchContent.isEmpty()) {
            switch (actualSearchOp.toLowerCase()) {
                case "stationname":
                    builder.and(csInfo.stationName.containsIgnoreCase(actualSearchContent));
                    break;
                case "chargerid":
                    builder.and(chgPaymentInfo.chargerId.containsIgnoreCase(actualSearchContent));
                    break;
                case "cardnumber":
                    builder.and(chgPaymentInfo.preCardNum.containsIgnoreCase(actualSearchContent));
                    break;
                case "approvalnumber":
                    builder.and(chgPaymentInfo.preApprovalNo.containsIgnoreCase(actualSearchContent));
                    break;
                default:
                    break;
            }
        }

        // 1. 관제 결제 데이터 합계 조회
        Integer totalChgPrice = queryFactory
                .select(chgPaymentInfo.preAmount.sum())
                .from(chgPaymentInfo)
                .leftJoin(cpInfo).on(chgPaymentInfo.chargerId.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .fetchOne();

        // 2. 관제 취소금액 합계 조회 (문자열 필드이므로 개별 조회 후 합산)
        Integer totalCancelCost = queryFactory
                .select(chgPaymentInfo.cancelAmount.sum())
                .from(chgPaymentInfo)
                .leftJoin(cpInfo).on(chgPaymentInfo.chargerId.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .fetchOne();

        // 3. 관제 결제금액 합계 조회 (문자열 필드이므로 개별 조회 후 합산)
        Integer totalRealCost = queryFactory
                .select(chgPaymentInfo.resultPrice.sum())
                .from(chgPaymentInfo)
                .leftJoin(cpInfo).on(chgPaymentInfo.chargerId.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .fetchOne();

        // 4. PG 거래 데이터 조회
        // 4.1 관제 결제 데이터의 TID 목록 추출
        List<String> tids = queryFactory
                .select(chgPaymentInfo.preTid)
                .from(chgPaymentInfo)
                .leftJoin(cpInfo).on(chgPaymentInfo.chargerId.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .fetch();

        // 4.2 PG 승인금액 합계 (상태코드 0인 거래)
        BigDecimal totalPgAppAmount = BigDecimal.ZERO;
        if (!tids.isEmpty()) {
            BigDecimal sum = queryFactory
                    .select(pgTrxRecon.goodsAmt.sum())
                    .from(pgTrxRecon)
                    .where(pgTrxRecon.tid.in(tids).and(pgTrxRecon.stateCd.in("0", "1")))
                    .fetchOne();

            if (sum != null) {
                totalPgAppAmount = sum;
            }
        }

        // 4.3 PG 취소금액 합계 (상태코드 1 또는 2인 거래)
        BigDecimal totalPgCancelAmount = BigDecimal.ZERO;
        if (!tids.isEmpty()) {
            // 조건 수정:
            // 1. tid가 tids에 포함되고 상태코드가 1 또는 2인 경우
            // 2. 또는 otid가 tids에 포함되고 상태코드가 2인 경우
            BooleanExpression cancelCondition = pgTrxRecon.tid.in(tids).and(pgTrxRecon.stateCd.in("1", "2"))
                    .or(pgTrxRecon.otid.in(tids).and(pgTrxRecon.stateCd.eq("2")));

            BigDecimal sum = queryFactory
                    .select(pgTrxRecon.goodsAmt.sum())
                    .from(pgTrxRecon)
                    .where(cancelCondition)
                    .fetchOne();

            if (sum != null) {
                totalPgCancelAmount = sum;
            }
        }

        // 4.4 PG 결제금액 합계 (승인금액 - 취소금액)
        BigDecimal totalPgPaymentAmount = totalPgAppAmount.subtract(totalPgCancelAmount);

        // 5. 합계 DTO 생성 및 반환
        return ChgPaymentSummaryDto.builder()
                .totalChgPrice(totalChgPrice != null ? totalChgPrice : 0)
                .totalCancelCost(totalCancelCost != null ? totalCancelCost : 0)
                .totalRealCost(totalRealCost != null ? totalRealCost : 0)
                .totalPgAppAmount(totalPgAppAmount)
                .totalPgCancelAmount(totalPgCancelAmount)
                .totalPgPaymentAmount(totalPgPaymentAmount)
                .build();
    }

    /**
     * 기존 Paging 조회시 동일 코드 중복없이 조회하는 방법
     */
    @Override
    public List<ChgPaymentInfoDto> findAllChgPaymentInfoListWithoutPagination(String startMonthSearch,
            String endMonthSearch, String searchOp, String searchContent, Long companyId, String levelPath,
            boolean isSuperAdmin) {
        // 페이징 없이 모든 데이터를 가져오기 위해 임시 Pageable 객체 생성
        Pageable unpaged = Pageable.unpaged();

        // 기존 페이징 메서드를 활용하여 데이터 조회
        Page<ChgPaymentInfoDto> page = findChgPaymentInfo(startMonthSearch, endMonthSearch,
                searchOp, searchContent, companyId, unpaged, levelPath, isSuperAdmin);

        // Page 객체에서 List 추출하여 반환
        return page.getContent();
    }

    @Override
    public BigDecimal findTotalSalesByYear(Long companyId, String searchOp, String searchContent, Integer year) {
        QCompany company = QCompany.company;
        QCpInfo cpInfo = QCpInfo.cpInfo;
        QCsInfo csInfo = QCsInfo.csInfo;
        QPgSettlmntDetail pgSettlmntDetail = QPgSettlmntDetail.pgSettlmntDetail;

        BooleanBuilder builder = new BooleanBuilder();

        if (companyId != null) {
            builder.and(csInfo.company.id.eq(companyId));
        }

        if ((searchOp != null && !searchOp.isEmpty()) && (searchContent != null && !searchContent.isEmpty())) {
            switch (searchOp) {
                case "stationId" -> builder.and(csInfo.id.contains(searchContent));
                case "stationName" -> {
                    builder.and(csInfo.stationName.contains(searchContent));
                }
                default -> {
                }
            }
        }

        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);
        builder.and(pgSettlmntDetail.settlmntDt.between(startOfYear, endOfYear));

        return queryFactory
                .select(pgSettlmntDetail.depositAmt.sum())
                .from(pgSettlmntDetail)
                .leftJoin(cpInfo).on(pgSettlmntDetail.chargerId.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.eq(company))
                .where(builder)
                .fetchOne();
    }

    @Override
    public List<PurchaseSalesLineChartBaseDto> searchMonthlyTotalSales(Long companyId, String searchOp,
            String searchContent,
            Integer year) {
        log.info("companyId: {}, searchOp: {}, searchContent: {}, year: {}", companyId, searchOp, searchContent, year);

        QCompany company = QCompany.company;
        QCpInfo cpInfo = QCpInfo.cpInfo;
        QCsInfo csInfo = QCsInfo.csInfo;
        QPgSettlmntDetail pgSettlmntDetail = QPgSettlmntDetail.pgSettlmntDetail;

        BooleanBuilder builder = new BooleanBuilder();

        if (companyId != null) {
            builder.and(csInfo.company.id.eq(companyId));
        }

        if ((searchOp != null && !searchOp.isEmpty()) && (searchContent != null && !searchContent.isEmpty())) {
            switch (searchOp) {
                case "stationId" -> builder.and(csInfo.id.contains(searchContent));
                case "stationName" -> {
                    builder.and(csInfo.stationName.contains(searchContent));
                }
                default -> {
                }
            }
        }

        builder.and(Expressions.numberTemplate(Integer.class, "YEAR({0})", pgSettlmntDetail.settlmntDt).eq(year));

        // 월별 결과 초기화
        List<PurchaseSalesLineChartBaseDto> monthlyResults = IntStream.rangeClosed(1, 12)
                .mapToObj(month -> PurchaseSalesLineChartBaseDto.builder()
                        .month(month)
                        .year(year)
                        .totalPrice(BigDecimal.ZERO)
                        .build())
                .collect(Collectors.toList());

        NumberTemplate<Integer> monthTemplate = Expressions.numberTemplate(Integer.class, "MONTH({0})", pgSettlmntDetail.settlmntDt);        
        // 월별 매출액 조회
        List<Tuple> results = queryFactory
                .select(
                    monthTemplate.as("month"),
                    pgSettlmntDetail.depositAmt.sum())
                .from(pgSettlmntDetail)
                .leftJoin(cpInfo).on(pgSettlmntDetail.chargerId.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.eq(company))
                .where(builder)
                .groupBy(monthTemplate)
                .fetch();

        for (Tuple tuple : results) {
            Integer month = tuple.get(0, Integer.class);
            BigDecimal total = tuple.get(1, BigDecimal.class);
            monthlyResults.get(month - 1).setTotalPrice(total);
        }

        return monthlyResults;
    }

    @Override
    public SalesDashboardDto findPaymentByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        QChargerPaymentInfo chgPaymentInfo = QChargerPaymentInfo.chargerPaymentInfo;
        QCompany company = QCompany.company;
        QCpInfo cpInfo = QCpInfo.cpInfo;
        QCsInfo csInfo = QCsInfo.csInfo;
        QPgTrxRecon pgTrxRecon = QPgTrxRecon.pgTrxRecon;
        QCpModel model = QCpModel.cpModel;

        // 관제 결제 데이터의 TID 목록 추출
        List<String> tids = queryFactory
                .select(chgPaymentInfo.preTid)
                .from(chgPaymentInfo)
                .leftJoin(cpInfo).on(chgPaymentInfo.chargerId.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.eq(company))
                .where(chgPaymentInfo.timestamp.between(startDate, endDate))
                .fetch();

        /* 
         * 결제금액 = 승인금액 (상태코드 0) - 취소금액 (상태코드 2)
         * 상태코드 1은 승인금액과 취소금액 둘 다 변영되므로 제외
         */
        // 승인금액
        SalesDashboardDto approved = queryFactory.select(Projections.fields(SalesDashboardDto.class,
            Expressions.numberTemplate(BigDecimal.class,
                    "SUM(CASE WHEN COALESCE({0}, '') = 'SPEEDLOW' THEN {1} ELSE 0 END)", model.cpType, pgTrxRecon.goodsAmt)
                    .as("lowSales"),
            Expressions.numberTemplate(BigDecimal.class,
                    "SUM(CASE WHEN COALESCE({0}, '') = 'SPEEDFAST' THEN {1} ELSE 0 END)", model.cpType, pgTrxRecon.goodsAmt)
                    .as("fastSales"),
            Expressions.numberTemplate(BigDecimal.class,
                    "SUM(CASE WHEN COALESCE({0}, '') = 'SPEEDDESPN' THEN {1} ELSE 0 END)", model.cpType, pgTrxRecon.goodsAmt)
                    .as("despnSales")))
            .from(pgTrxRecon)
            .leftJoin(cpInfo).on(pgTrxRecon.goodsNm.eq(cpInfo.id))
            .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
            .where(pgTrxRecon.tid.in(tids).and(pgTrxRecon.stateCd.in("0")))
            .fetchOne();

        // 취소금액
        SalesDashboardDto canceled = queryFactory.select(Projections.fields(SalesDashboardDto.class,
            Expressions.numberTemplate(BigDecimal.class,
                    "SUM(CASE WHEN COALESCE({0}, '') = 'SPEEDLOW' THEN {1} ELSE 0 END)", model.cpType, pgTrxRecon.goodsAmt)
                    .as("lowSales"),
            Expressions.numberTemplate(BigDecimal.class,
                    "SUM(CASE WHEN COALESCE({0}, '') = 'SPEEDFAST' THEN {1} ELSE 0 END)", model.cpType, pgTrxRecon.goodsAmt)
                    .as("fastSales"),
            Expressions.numberTemplate(BigDecimal.class,
                    "SUM(CASE WHEN COALESCE({0}, '') = 'SPEEDDESPN' THEN {1} ELSE 0 END)", model.cpType, pgTrxRecon.goodsAmt)
                    .as("despnSales")))
            .from(pgTrxRecon)
            .leftJoin(cpInfo).on(pgTrxRecon.goodsNm.eq(cpInfo.id))
            .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
            .where(pgTrxRecon.tid.in(tids).and(pgTrxRecon.stateCd.in("2"))
                    .or(pgTrxRecon.otid.in(tids).and(pgTrxRecon.stateCd.eq("2"))))
            .fetchOne();

        if (approved == null) {
            approved = new SalesDashboardDto();
            approved.setLowSales(BigDecimal.ZERO);
            approved.setFastSales(BigDecimal.ZERO);
            approved.setDespnSales(BigDecimal.ZERO);
        } else {
            if (approved.getLowSales() == null) approved.setLowSales(BigDecimal.ZERO);
            if (approved.getFastSales() == null) approved.setFastSales(BigDecimal.ZERO);
            if (approved.getDespnSales() == null) approved.setDespnSales(BigDecimal.ZERO);
        }

        if (canceled == null) {
            canceled = new SalesDashboardDto();
            canceled.setLowSales(BigDecimal.ZERO);
            canceled.setFastSales(BigDecimal.ZERO);
            canceled.setDespnSales(BigDecimal.ZERO);
        } else {
            if (canceled.getLowSales() == null) canceled.setLowSales(BigDecimal.ZERO);
            if (canceled.getFastSales() == null) canceled.setFastSales(BigDecimal.ZERO);
            if (canceled.getDespnSales() == null) canceled.setDespnSales(BigDecimal.ZERO);
        }

        log.info("approved >> {}", approved.toString());
        log.info("canceled >> {}", canceled.toString());

        SalesDashboardDto result = new SalesDashboardDto();
        result.setLowSales(approved.getLowSales().subtract(canceled.getLowSales()));
        result.setFastSales(approved.getFastSales().subtract(canceled.getFastSales()));
        result.setDespnSales(approved.getDespnSales().subtract(canceled.getDespnSales()));
        log.info("result >> {}", result.toString());
        return result;
    }
}
