package zgoo.cpos.repository.history;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.history.QChgCommLog;
import zgoo.cpos.dto.history.ChgCommlogDto;

@RequiredArgsConstructor
@Slf4j
public class ChgCommLogRepositoryCustomImpl implements ChgCommLogRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChgCommlogDto> findAllChgCommlog(Pageable pageable) {
        QChgCommLog commLog = QChgCommLog.chgCommLog;
        List<ChgCommlogDto> logList = queryFactory.select(Projections.fields(ChgCommlogDto.class,
                commLog.chargerId.as("chargerId"),
                commLog.connectorId.as("connectorId"),
                commLog.action.as("action"),
                commLog.recvTime.as("recvTime"),
                commLog.recvUuid.as("recvUuid"),
                commLog.recvPayload.as("recvPayload"),
                commLog.sendTime.as("sendTime"),
                commLog.sendUuid.as("sendUuid"),
                commLog.sendPayload.as("sendPayload")))
                .from(commLog)
                .orderBy(commLog.recvTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory.select(commLog.count())
                .from(commLog)
                .fetchOne();

        log.info("===ChgCommlog Total Count: {}", totalCount);

        return new PageImpl<>(logList, pageable, totalCount);
    }

    @Override
    public Page<ChgCommlogDto> findChgCommlog(String searchOp, String searchContent, String recvFrom, String recvTo,
            Pageable pageable) {
        QChgCommLog commLog = QChgCommLog.chgCommLog;
        log.info(
                "=== >> findChgCommlog with search condition: searchOp: {}, searchContent: {}, recvFrom: {}, recvTo: {}",
                searchOp, searchContent, recvFrom, recvTo);

        BooleanBuilder builder = new BooleanBuilder();

        if (recvFrom != null && !recvFrom.isEmpty()) {
            // 날짜 형식 확인 및 변환
            LocalDateTime fromDateTime;
            if (recvFrom.length() <= 10) { // yyyy-MM-dd 형식인 경우
                fromDateTime = LocalDateTime.parse(recvFrom + " 00:00:00",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else { // 이미 yyyy-MM-dd HH:mm:ss 형식인 경우
                fromDateTime = LocalDateTime.parse(recvFrom,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            // Timestamp 타입과 비교하기 위해 LocalDateTime 사용
            builder.and(commLog.recvTime.goe(Timestamp.valueOf(fromDateTime)));
        }

        if (recvTo != null && !recvTo.isEmpty()) {
            // 날짜 형식 확인 및 변환
            LocalDateTime toDateTime;
            if (recvTo.length() <= 10) { // yyyy-MM-dd 형식인 경우
                toDateTime = LocalDateTime.parse(recvTo + " 23:59:59",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else { // 이미 yyyy-MM-dd HH:mm:ss 형식인 경우
                toDateTime = LocalDateTime.parse(recvTo,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            // Timestamp 타입과 비교하기 위해 LocalDateTime 사용
            builder.and(commLog.recvTime.loe(Timestamp.valueOf(toDateTime)));
        }

        if (searchOp != null && !searchOp.isEmpty()) {
            switch (searchOp.toLowerCase()) {
                case "action":
                    builder.and(commLog.action.containsIgnoreCase(searchContent));
                    break;
                case "chargerid":
                    builder.and(commLog.chargerId.containsIgnoreCase(searchContent));
                    break;
                case "uuid":
                    builder.and(commLog.recvUuid.containsIgnoreCase(searchContent));
                    break;
                default:
                    break;
            }
        }

        List<ChgCommlogDto> logList = queryFactory.select(Projections.fields(ChgCommlogDto.class,
                commLog.chargerId.as("chargerId"),
                commLog.connectorId.as("connectorId"),
                commLog.action.as("action"),
                commLog.recvTime.as("recvTime"),
                commLog.recvUuid.as("recvUuid"),
                commLog.recvPayload.as("recvPayload"),
                commLog.sendTime.as("sendTime"),
                commLog.sendUuid.as("sendUuid"),
                commLog.sendPayload.as("sendPayload")))
                .from(commLog)
                .where(builder)
                .orderBy(commLog.recvTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory.select(commLog.count())
                .from(commLog)
                .where(builder)
                .fetchOne();

        log.info("===ChgCommlog Total Count: {}", totalCount);

        return new PageImpl<>(logList, pageable, totalCount);
    }

}
