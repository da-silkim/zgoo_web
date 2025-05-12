package zgoo.cpos.repository.cpmodel;

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
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.company.QCompanyRelationInfo;
import zgoo.cpos.domain.cp.QCpConnector;
import zgoo.cpos.domain.cp.QCpModel;
import zgoo.cpos.domain.cp.QCpModelDetail;
import zgoo.cpos.dto.cp.CpModelDto.CpConnectorDto;
import zgoo.cpos.dto.cp.CpModelDto.CpModelDetailDto;
import zgoo.cpos.dto.cp.CpModelDto.CpModelListDto;
import zgoo.cpos.dto.cp.CpModelDto.CpModelRegDto;
import zgoo.cpos.util.QueryUtils;

@Slf4j
@RequiredArgsConstructor
public class CpModelRepositoryCustomImpl implements CpModelRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QCpModel model = QCpModel.cpModel;
    QCpModelDetail modelDetail = QCpModelDetail.cpModelDetail;
    QCpConnector connector = QCpConnector.cpConnector;
    QCompany company = QCompany.company;
    QCompanyRelationInfo relation = QCompanyRelationInfo.companyRelationInfo;
    QCommonCode manufCdName = new QCommonCode("manufCd");
    QCommonCode cpTypeName = new QCommonCode("cpType");
    QCommonCode installationTypeName = new QCommonCode("installationType");
    QCommonCode connectorTypeName = new QCommonCode("connectorType");

    @Override
    public Page<CpModelListDto> findCpModelWithPagination(Pageable pageable, String levelPath,
            boolean isSuperAdmin) {

        BooleanBuilder builder = new BooleanBuilder();

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        List<CpModelListDto> modelList = queryFactory.select(Projections.fields(CpModelListDto.class,
                model.id.as("modelId"),
                model.modelCode.as("modelCode"),
                model.modelName.as("modelName"),
                model.powerUnit.as("powerUnit"),
                model.regDt.as("regDt"),
                manufCdName.name.as("manufCdName"),
                cpTypeName.name.as("cpTypeName"),
                company.companyName.as("companyName")))
                .from(model)
                .leftJoin(company).on(model.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(manufCdName).on(model.manufCd.eq(manufCdName.commonCode))
                .leftJoin(cpTypeName).on(model.cpType.eq(cpTypeName.commonCode))
                .where(builder)
                .orderBy(model.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(model.count())
                .from(model)
                .leftJoin(company).on(model.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(manufCdName).on(model.manufCd.eq(manufCdName.commonCode))
                .leftJoin(cpTypeName).on(model.cpType.eq(cpTypeName.commonCode))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(modelList, pageable, totalCount);
    }

    @Override
    public Page<CpModelListDto> searchCpModelWithPagination(Long companyId, String manuf, String chgSpeed,
            Pageable pageable, String levelPath, boolean isSuperAdmin) {
        BooleanBuilder builder = new BooleanBuilder();

        if (!isSuperAdmin && levelPath != null && !levelPath.isEmpty()) {
            builder.and(QueryUtils.hasCompanyLevelAccess(relation, levelPath));
        }

        if (companyId != null) {
            builder.and(model.company.id.eq(companyId));
        }

        if (manuf != null && !manuf.isEmpty()) {
            builder.and(model.manufCd.eq(manuf));
        }

        if (chgSpeed != null && !chgSpeed.isEmpty()) {
            builder.and(model.cpType.eq(chgSpeed));
        }

        List<CpModelListDto> modelList = queryFactory.select(Projections.fields(CpModelListDto.class,
                model.id.as("modelId"),
                model.modelCode.as("modelCode"),
                model.modelName.as("modelName"),
                model.powerUnit.as("powerUnit"),
                model.regDt.as("regDt"),
                manufCdName.name.as("manufCdName"),
                cpTypeName.name.as("cpTypeName"),
                company.companyName.as("companyName")))
                .from(model)
                .leftJoin(company).on(model.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(manufCdName).on(model.manufCd.eq(manufCdName.commonCode))
                .leftJoin(cpTypeName).on(model.cpType.eq(cpTypeName.commonCode))
                .orderBy(model.id.desc())
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(model.count())
                .from(model)
                .leftJoin(company).on(model.company.eq(company))
                .leftJoin(relation).on(company.companyRelationInfo.eq(relation))
                .leftJoin(manufCdName).on(model.manufCd.eq(manufCdName.commonCode))
                .leftJoin(cpTypeName).on(model.cpType.eq(cpTypeName.commonCode))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(modelList, pageable, totalCount);
    }

    @Override
    public CpModelRegDto findCpModelOne(Long modelId) {
        List<CpConnectorDto> connectorInfo = findCpConnectorByModelId(modelId);

        CpModelRegDto modelDto = queryFactory.select(Projections.fields(CpModelRegDto.class,
                model.id.as("modelId"),
                model.manufCd.as("manufCd"),
                model.modelCode.as("modelCode"),
                model.modelName.as("modelName"),
                model.powerUnit.as("powerUnit"),
                model.cpType.as("cpType"),
                model.dualYn.as("dualYn"),
                model.installationType.as("installationType"),
                modelDetail.inputVoltage.as("inputVoltage"),
                modelDetail.inputFrequency.as("inputFrequency"),
                modelDetail.inputType.as("inputType"),
                modelDetail.inputCurr.as("inputCurr"),
                modelDetail.inputPower.as("inputPower"),
                modelDetail.powerFactor.as("powerFactor"),
                modelDetail.outputVoltage.as("outputVoltage"),
                modelDetail.maxOutputCurr.as("maxOutputCurr"),
                modelDetail.ratedPower.as("ratedPower"),
                modelDetail.peakEfficiency.as("peakEfficiency"),
                modelDetail.thdi.as("thdi"),
                modelDetail.grdType.as("grdType"),
                modelDetail.opAltitude.as("opAltitude"),
                modelDetail.opTemperature.as("opTemperature"),
                modelDetail.temperatureDerating.as("temperatureDerating"),
                modelDetail.storageTemperatureRange.as("storageTemperatureRange"),
                modelDetail.humidity.as("humidity"),
                modelDetail.dimensions.as("dimensions"),
                modelDetail.ipNIk.as("ipNIk"),
                modelDetail.weight.as("weight"),
                modelDetail.material.as("material"),
                modelDetail.cableLength.as("cableLength"),
                modelDetail.screen.as("screen"),
                modelDetail.rfid.as("rfid"),
                modelDetail.emergencyBtn.as("emergencyBtn"),
                modelDetail.communicationInterface.as("communicationInterface"),
                modelDetail.lang.as("lang"),
                modelDetail.coolingMethod.as("coolingMethod"),
                modelDetail.emc.as("emc"),
                modelDetail.protection.as("protection"),
                modelDetail.opFunc.as("opFunc"),
                modelDetail.standard.as("standard"),
                modelDetail.powerModule.as("powerModule"),
                modelDetail.charger.as("charger"),
                company.companyName.as("companyName")))
                .from(model)
                .leftJoin(company).on(model.company.eq(company))
                .leftJoin(modelDetail).on(model.id.eq(modelDetail.cpModel.id))
                .where(model.id.eq(modelId))
                .fetchOne();

        if (modelDto != null) {
            modelDto.setConnector(connectorInfo);
        }

        return modelDto;
    }

    @Override
    public CpModelDetailDto findCpModelDetailOne(Long modelId) {
        List<CpConnectorDto> connectorInfo = findCpConnectorByModelId(modelId);

        CpModelDetailDto modelDetailDto = queryFactory.select(Projections.fields(CpModelDetailDto.class,
                model.id.as("modelId"),
                model.modelCode.as("modelCode"),
                model.modelName.as("modelName"),
                model.powerUnit.as("powerUnit"),
                model.dualYn.as("dualYn"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.inputVoltage)
                        .as("inputVoltage"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.inputFrequency)
                        .as("inputFrequency"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.inputType)
                        .as("inputType"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.inputCurr)
                        .as("inputCurr"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.inputPower)
                        .as("inputPower"),
                Expressions.stringTemplate("IF({0} IS NULL, '정보없음', {0})", modelDetail.powerFactor)
                        .as("powerFactorToString"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.outputVoltage)
                        .as("outputVoltage"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.maxOutputCurr)
                        .as("maxOutputCurr"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.ratedPower)
                        .as("ratedPower"),
                Expressions.stringTemplate("IF({0} IS NULL, '정보없음', {0})", modelDetail.peakEfficiency)
                        .as("peakEfficiencyToString"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})", modelDetail.thdi)
                        .as("thdi"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.grdType)
                        .as("grdType"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.opAltitude)
                        .as("opAltitude"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.opTemperature)
                        .as("opTemperature"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.temperatureDerating)
                        .as("temperatureDerating"),
                Expressions
                        .stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                                modelDetail.storageTemperatureRange)
                        .as("storageTemperatureRange"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.humidity)
                        .as("humidity"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.dimensions)
                        .as("dimensions"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.ipNIk).as("ipNIk"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.weight).as("weight"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.material)
                        .as("material"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.cableLength)
                        .as("cableLength"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.screen).as("screen"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})", modelDetail.rfid)
                        .as("rfid"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.emergencyBtn)
                        .as("emergencyBtn"),
                Expressions
                        .stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                                modelDetail.communicationInterface)
                        .as("communicationInterface"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})", modelDetail.lang)
                        .as("lang"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.coolingMethod)
                        .as("coolingMethod"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})", modelDetail.emc)
                        .as("emc"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.protection)
                        .as("protection"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.opFunc).as("opFunc"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.standard)
                        .as("standard"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.powerModule)
                        .as("powerModule"),
                Expressions.stringTemplate("IF({0} IS NULL OR {0} = '', '정보없음', {0})",
                        modelDetail.charger)
                        .as("charger"),
                manufCdName.name.as("manufCdName"),
                cpTypeName.name.as("cpTypeName"),
                installationTypeName.name.as("installationTypeName"),
                company.companyName.as("companyName")))
                .from(model)
                .leftJoin(company).on(model.company.eq(company))
                .leftJoin(modelDetail).on(model.id.eq(modelDetail.cpModel.id))
                .leftJoin(manufCdName).on(model.manufCd.eq(manufCdName.commonCode))
                .leftJoin(cpTypeName).on(model.cpType.eq(cpTypeName.commonCode))
                .leftJoin(installationTypeName)
                .on(model.installationType.eq(installationTypeName.commonCode))
                .where(model.id.eq(modelId))
                .fetchOne();

        if (modelDetailDto != null) {
            modelDetailDto.setConnector(connectorInfo);
        }

        return modelDetailDto;
    }

    @Override
    public List<CpConnectorDto> findCpConnectorByModelId(Long modelId) {
        List<CpConnectorDto> connectorInfo = queryFactory.select(Projections.fields(CpConnectorDto.class,
                connector.connectorId.as("connectorId"),
                connector.connectorType.as("connectorType"),
                connectorTypeName.name.as("connectorTypeName")))
                .from(connector)
                .leftJoin(connectorTypeName)
                .on(connector.connectorType.eq(connectorTypeName.commonCode))
                .where(connector.cpModel.id.eq(modelId))
                .fetch();
        return connectorInfo;
    }

    @Override
    public List<CpModelListDto> findCpModelListByCompanyId(Long companyId) {
        List<CpModelListDto> modelList = queryFactory.select(Projections.fields(CpModelListDto.class,
                model.id.as("modelId"),
                model.modelCode.as("modelCode"),
                model.modelName.as("modelName"),
                model.powerUnit.as("powerUnit"),
                model.regDt.as("regDt"),
                manufCdName.name.as("manufCdName"),
                cpTypeName.name.as("cpTypeName"),
                company.companyName.as("companyName")))
                .from(model)
                .leftJoin(company).on(model.company.eq(company))
                .leftJoin(manufCdName).on(model.manufCd.eq(manufCdName.commonCode))
                .leftJoin(cpTypeName).on(model.cpType.eq(cpTypeName.commonCode))
                .where(model.company.id.eq(companyId))
                .orderBy(model.id.desc())
                .fetch();

        return modelList;
    }

    @Override
    public CpModelListDto findCpModelModalOne(String modelCode) {
        return queryFactory.select(Projections.fields(CpModelListDto.class,
                model.id.as("modelId"),
                model.modelCode.as("modelCode"),
                model.modelName.as("modelName"),
                model.powerUnit.as("powerUnit"),
                model.regDt.as("regDt"),
                model.manufCd.as("manufCd"),
                manufCdName.name.as("manufCdName"),
                model.cpType.as("cpType"),
                cpTypeName.name.as("cpTypeName"),
                model.dualYn.as("dualYn")))
                .from(model)
                .leftJoin(company).on(model.company.eq(company))
                .leftJoin(manufCdName).on(model.manufCd.eq(manufCdName.commonCode))
                .leftJoin(cpTypeName).on(model.cpType.eq(cpTypeName.commonCode))
                .where(model.modelCode.eq(modelCode))
                .orderBy(model.id.desc())
                .fetchOne();
    }
}
