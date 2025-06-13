package zgoo.cpos.repository.member;

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
import zgoo.cpos.domain.charger.QCpInfo;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.company.QCompanyRelationInfo;
import zgoo.cpos.domain.cp.QCpModel;
import zgoo.cpos.domain.cs.QCsInfo;
import zgoo.cpos.domain.member.QMemberAuthHist;
import zgoo.cpos.dto.member.MemberAuthHistDto;
import zgoo.cpos.util.QueryUtils;

@Slf4j
@RequiredArgsConstructor
public class MemberAuthHistRepositoryCusotmImpl implements MemberAuthHistRepositoryCusotm {
    private final JPAQueryFactory queryFactory;

    QCompany company = QCompany.company;
    QCpInfo cpInfo = QCpInfo.cpInfo;
    QCsInfo csInfo = QCsInfo.csInfo;
    QCpModel model = QCpModel.cpModel;
    QMemberAuthHist memberAuthHist = QMemberAuthHist.memberAuthHist;
    QCompanyRelationInfo relation = QCompanyRelationInfo.companyRelationInfo;
    QCommonCode cpTypeName = new QCommonCode("cpType");
    QCommonCode memberTypeName = new QCommonCode("bizType");

    @Override
    public Page<MemberAuthHistDto> findAllMemberAuthHistWithPagination(Pageable pageable, String levelPath,
            boolean isSuperAdmin) {

        BooleanBuilder builder = new BooleanBuilder();

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        List<MemberAuthHistDto> memberAuthHistList = queryFactory.select(Projections.fields(MemberAuthHistDto.class,
                csInfo.company.companyName.as("companyName"),
                csInfo.stationName.as("stationName"),
                memberAuthHist.chargerId.as("chargerId"),
                cpTypeName.name.as("cpType"),
                memberAuthHist.member.name.as("memberName"),
                memberTypeName.name.as("memberType"),
                memberAuthHist.idTag.as("idTag"),
                memberAuthHist.authResult.as("result"),
                memberAuthHist.authDt.as("authTime")))
                .from(memberAuthHist)
                .leftJoin(cpInfo).on(memberAuthHist.chargerId.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.id.eq(company.id))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .leftJoin(cpTypeName).on(model.cpType.eq(cpTypeName.commonCode))
                .leftJoin(memberTypeName).on(memberAuthHist.member.bizType.eq(memberTypeName.commonCode))
                .where(builder)
                .orderBy(memberAuthHist.authDt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(memberAuthHist.count())
                .from(memberAuthHist)
                .leftJoin(cpInfo).on(memberAuthHist.chargerId.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.id.eq(company.id))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(memberAuthHistList, pageable, totalCount);
    }

    @Override
    public Page<MemberAuthHistDto> findMemberAuthHistWithPagination(Long companyId, String searchOp,
            String contentSearch, String fromDate, String toDate, Pageable pageable, String levelPath,
            boolean isSuperAdmin) {

        log.info(
                "=== >> findMemberAuthHistWithPagination with search condition: companyId: {}, searchOp: {}, contentSearch: {}, fromDate: {}, toDate: {}",
                companyId, searchOp, contentSearch, fromDate, toDate);

        BooleanBuilder builder = new BooleanBuilder();

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        if (companyId != null) {
            builder.and(csInfo.company.id.eq(companyId));
        }

        if (fromDate != null && !fromDate.isEmpty()) {
            // 날짜 형식 확인 및 변환
            LocalDateTime fromDateTime;
            if (fromDate.length() <= 10) { // yyyy-MM-dd 형식인 경우
                fromDateTime = LocalDateTime.parse(fromDate + " 00:00:00",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else { // 이미 yyyy-MM-dd HH:mm:ss 형식인 경우
                fromDateTime = LocalDateTime.parse(fromDate,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            builder.and(memberAuthHist.authDt.after(fromDateTime));
        }

        if (toDate != null && !toDate.isEmpty()) {
            // 날짜 형식 확인 및 변환
            LocalDateTime toDateTime;
            if (toDate.length() <= 10) { // yyyy-MM-dd 형식인 경우
                toDateTime = LocalDateTime.parse(toDate + " 23:59:59",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else { // 이미 yyyy-MM-dd HH:mm:ss 형식인 경우
                toDateTime = LocalDateTime.parse(toDate,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            builder.and(memberAuthHist.authDt.before(toDateTime));
        }

        if (searchOp != null && !searchOp.isEmpty()) {
            switch (searchOp.toLowerCase()) {
                case "stationname":
                    builder.and(csInfo.stationName.containsIgnoreCase(contentSearch));
                    break;
                case "stationid":
                    builder.and(csInfo.id.containsIgnoreCase(contentSearch));
                    break;
                case "chargerid":
                    builder.and(memberAuthHist.chargerId.containsIgnoreCase(contentSearch));
                    break;
                case "idtag":
                    builder.and(memberAuthHist.idTag.containsIgnoreCase(contentSearch));
                    break;
                case "membername":
                    builder.and(memberAuthHist.member.name.containsIgnoreCase(contentSearch));
                    break;

                default:
                    break;
            }
        }

        List<MemberAuthHistDto> memberAuthHistList = queryFactory.select(Projections.fields(MemberAuthHistDto.class,
                csInfo.company.companyName.as("companyName"),
                csInfo.stationName.as("stationName"),
                memberAuthHist.chargerId.as("chargerId"),
                cpTypeName.name.as("cpType"),
                memberAuthHist.member.name.as("memberName"),
                memberTypeName.name.as("memberType"),
                memberAuthHist.idTag.as("idTag"),
                memberAuthHist.authResult.as("result"),
                memberAuthHist.authDt.as("authTime")))
                .from(memberAuthHist)
                .leftJoin(cpInfo).on(memberAuthHist.chargerId.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.id.eq(company.id))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(model).on(cpInfo.modelCode.eq(model.modelCode))
                .leftJoin(cpTypeName).on(model.cpType.eq(cpTypeName.commonCode))
                .leftJoin(memberTypeName).on(memberAuthHist.member.bizType.eq(memberTypeName.commonCode))
                .where(builder)
                .orderBy(memberAuthHist.authDt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(memberAuthHist.count())
                .from(memberAuthHist)
                .leftJoin(cpInfo).on(memberAuthHist.chargerId.eq(cpInfo.id))
                .leftJoin(csInfo).on(cpInfo.stationId.eq(csInfo))
                .leftJoin(company).on(csInfo.company.id.eq(company.id))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(memberAuthHistList, pageable, totalCount);

    }
}
