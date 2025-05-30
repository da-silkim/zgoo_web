package zgoo.cpos.repository.fw;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.company.QCompanyRelationInfo;
import zgoo.cpos.domain.cp.QCpModel;
import zgoo.cpos.domain.fw.QCpFwVersion;
import zgoo.cpos.dto.fw.CpFwversionDto;
import zgoo.cpos.util.QueryUtils;

@RequiredArgsConstructor
@Slf4j
public class CpFwVersionRepositoryCustomImpl implements CpFwVersionRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    QCpModel model = QCpModel.cpModel;
    QCompany company = QCompany.company;
    QCompanyRelationInfo relation = QCompanyRelationInfo.companyRelationInfo;
    QCpFwVersion fwVersion = QCpFwVersion.cpFwVersion;
    QCommonCode manufCdName = new QCommonCode("manfName");

    @Override
    public Page<CpFwversionDto> findAll(Pageable pageable, String levelPath, boolean isAdmin) {
        BooleanBuilder builder = new BooleanBuilder();

        if (!isAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        List<CpFwversionDto> list = queryFactory.select(Projections.fields(CpFwversionDto.class,
                fwVersion.id.as("fwId"),
                company.companyName.as("companyName"),
                manufCdName.name.as("manfName"),
                fwVersion.modelCode.as("modelCode"),
                fwVersion.fwVersion.as("fwVersion"),
                fwVersion.url.as("url"),
                fwVersion.regId.as("regUser"),
                fwVersion.regDate.as("regDate")))
                .from(fwVersion)
                .leftJoin(model).on(fwVersion.modelCode.eq(model.modelCode))
                .leftJoin(company).on(model.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(manufCdName).on(model.manufCd.eq(manufCdName.commonCode))
                .where(builder)
                .orderBy(fwVersion.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory.select(fwVersion.count())
                .from(fwVersion)
                .leftJoin(model).on(fwVersion.modelCode.eq(model.modelCode))
                .leftJoin(company).on(model.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(manufCdName).on(model.manufCd.eq(manufCdName.commonCode))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(list, pageable, totalCount);
    }

    @Override
    public List<CpFwversionDto> findFwVersionListByCompanyAndModel(Long companyId, String modelCode) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(fwVersion.companyId.eq(companyId));
        builder.and(fwVersion.modelCode.eq(modelCode));

        List<CpFwversionDto> list = queryFactory.select(Projections.fields(CpFwversionDto.class,
                fwVersion.id.as("fwId"),
                company.companyName.as("companyName"),
                manufCdName.name.as("manfName"),
                fwVersion.modelCode.as("modelCode"),
                fwVersion.fwVersion.as("fwVersion"),
                fwVersion.url.as("url"),
                fwVersion.regId.as("regUser"),
                fwVersion.regDate.as("regDate")))
                .from(fwVersion)
                .leftJoin(model).on(fwVersion.modelCode.eq(model.modelCode))
                .leftJoin(company).on(model.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(manufCdName).on(model.manufCd.eq(manufCdName.commonCode))
                .where(builder)
                .fetch();

        return list;
    }

    @Override
    public String findFwUrlByCompanyAndModelAndVersion(Long companyId, String modelCode, String version) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(fwVersion.companyId.eq(companyId));
        builder.and(fwVersion.modelCode.eq(modelCode));
        builder.and(fwVersion.fwVersion.eq(version));

        return queryFactory.select(fwVersion.url).from(fwVersion).where(builder).fetchOne();
    }
}