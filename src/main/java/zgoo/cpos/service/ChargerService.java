package zgoo.cpos.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.charger.CpInfo;
import zgoo.cpos.domain.charger.CpModem;
import zgoo.cpos.domain.company.CpPlanPolicy;
import zgoo.cpos.domain.cs.CsInfo;
import zgoo.cpos.dto.cp.ChargerDto.ChargerDetailListDto;
import zgoo.cpos.dto.cp.ChargerDto.ChargerListDto;
import zgoo.cpos.dto.cp.ChargerDto.ChargerRegDto;
import zgoo.cpos.dto.cp.ChargerDto.ChargerSearchDto;
import zgoo.cpos.dto.cp.ChargerDto.ConnectorStatusDto;
import zgoo.cpos.dto.cp.CpModelDto.CpModelListDto;
import zgoo.cpos.mapper.CpMapper;
import zgoo.cpos.mapper.CpModemMapper;
import zgoo.cpos.repository.charger.ChargerRepository;
import zgoo.cpos.repository.charger.ConnectorStatusRepository;
import zgoo.cpos.repository.charger.CpModemRepository;
import zgoo.cpos.repository.code.CommonCodeRepository;
import zgoo.cpos.repository.company.CpPlanPolicyRepository;
import zgoo.cpos.repository.cpmodel.CpModelRepository;
import zgoo.cpos.repository.cs.CsRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChargerService {

    private final ChargerRepository chargerRepository;
    private final CpModemRepository cpModemRepository;
    private final CsRepository csRepository;
    private final CpPlanPolicyRepository cpPlanPolicyRepository;
    private final ConnectorStatusRepository connectorStatusRepository;
    private final CpModelRepository cpModelRepository;
    private final CommonCodeRepository commonCodeRepository;

    /*
     * 저장 > 충전기 정보 저장
     */
    @Transactional
    public String createCpInfo(ChargerRegDto dto) {

        try {
            // modem entity정보 생성
            CpModem cpmodementity = CpModemMapper.toEntity(dto);

            // station정보 조회
            CsInfo csInfo = csRepository.findById(dto.getStationId()).orElseThrow(
                    () -> new IllegalArgumentException("Station not found with stationId:" + dto.getStationId()));

            // 요금제 정보 조회
            CpPlanPolicy plan = cpPlanPolicyRepository.findById(dto.getPricePolicyId()).orElseThrow(
                    () -> new IllegalArgumentException(
                            "Price Policy not found with policyId:" + dto.getPricePolicyId()));

            // 충전기 정보 저장
            CpInfo cpEntity = CpMapper.toEntity(dto, csInfo, cpmodementity, plan);
            CpInfo saved = chargerRepository.save(cpEntity);

            return saved.getId() != null ? saved.getId() : null;
        } catch (Exception e) {
            log.error("[ChargerService >> saveCpInfo] error: {}", e.getMessage());
            return null;
        }
    }

    /*
     * 충전기ID 자동 채번
     */
    public String createCpId(String stationId) {

        String cpid = "";
        // CpInfo 테이블에 연결하고자 하는 충전소ID가 존재하는지 확인
        Optional<CpInfo> optionalCpInfo = chargerRepository.findCpByStationId(stationId);

        if (optionalCpInfo.isEmpty()) {
            // 새로운 cpid 생성(01부터)
            cpid = stationId.trim() + "01";
        } else {
            CpInfo cpInfo = optionalCpInfo.get();
            String lastCpId = cpInfo.getId();
            int tmp_cpid = Integer.parseInt(lastCpId.substring(6));
            cpid = String.format(stationId + "%02d", tmp_cpid + 1);
        }

        log.info("CPID is created : {}", cpid);

        return cpid;
    }

    /*
     * 조회 > 충전기 전체 조회
     */
    public Page<ChargerListDto> searchCpListPageAll(int page, int size) {
        log.info("=== Start search ChargerList All");

        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<ChargerListDto> cpList = chargerRepository.findAllChargerListPaging(pageable);
            return cpList;
        } catch (DataAccessException dae) {
            log.error("Database error occurred while fetching cpList with pagination: {}", dae.getMessage(), dae);
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching cpList with pagination: {}", e.getMessage(), e);
            return Page.empty(pageable);
        }

    }

    /*
     * 조회 > 충전기 조회 with 검색조건
     */
    public Page<ChargerListDto> searchCpListPage(Long reqCompanyId, String reqManfCd, String reqOpSearch,
            String reqSearchContent, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        log.info("=== Start search ChargerList with conditions >> companyId:{}, manufcd:{}, condition:{}, content:{}",
                reqCompanyId, reqManfCd, reqOpSearch, reqSearchContent);

        try {
            Page<ChargerListDto> cpList = chargerRepository.findChargerListPaging(reqCompanyId, reqManfCd, reqOpSearch,
                    reqSearchContent, pageable);
            return cpList;
        } catch (DataAccessException dae) {
            log.error("Database error occurred while fetching cpList with pagination: {}", dae.getMessage(), dae);
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching cpList with pagination: {}", e.getMessage(), e);
            return Page.empty(pageable);
        }
    }

    /*
     * 조회 > 모뎀 시리얼번호 중복체크
     */
    public boolean isModemSerialDuplicate(String serialNum) {
        return cpModemRepository.isModemSerialDuplicate(serialNum);
    }

    /*
     * 조회 > 모뎀 번호 중복체크
     */
    public boolean isModemNumDuplicate(String modemNum) {
        return cpModemRepository.isModemNumDuplicate(modemNum);
    }

    /*
     * 충전기 커넥터 상태 조회
     */
    public List<ConnectorStatusDto> searchConStatListAll() {
        try {
            return connectorStatusRepository.findAllConnectorStatusList();
        } catch (Exception e) {
            log.error("Error while fetching connector_status list:{}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /*
     * stationId로 충전기 전체 조회
     */
    public List<ChargerSearchDto> searchChargerList(String stationId) {
        try {
            return chargerRepository.findChargerListByStationId(stationId);
        } catch (Exception e) {
            log.error("[ChargerService >> searchChargerList] error:", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /*
     * 충전기 정보 update
     */
    @Transactional
    public boolean updateCpInfo(ChargerRegDto reqdto) {
        try {
            // 충전기 정보 수정
            CpInfo cpInfo = chargerRepository.findById(reqdto.getChargerId()).orElseThrow(
                    () -> new IllegalArgumentException("Charger not found with chargerId:" + reqdto.getChargerId()));

            // 충전기 정보 수정
            cpInfo.updateCpInfo(reqdto);

            return true;
        } catch (Exception e) {
            log.error("[ChargerService >> updateCpInfo] error:", e.getMessage(), e);
            return false;
        }
    }

    /*
     * 충전기 정보 삭제
     */
    @Transactional
    public boolean deleteCpInfo(String chargerId) {
        try {
            chargerRepository.deleteById(chargerId);
            return true;
        } catch (Exception e) {
            log.error("[ChargerService >> deleteCpInfo] error:", e.getMessage(), e);
            return false;
        }
    }

    /*
     * 충전기 정보 조회
     */
    @Transactional(readOnly = true)
    public ChargerDetailListDto getChargerInfo(String chargerId) {
        try {
            CpInfo cpInfo = chargerRepository.findCpInfoByChargerId(chargerId);

            CpModelListDto cpModel = cpModelRepository.findCpModelModalOne(cpInfo.getModelCode());

            // get commoncode names
            String commonTypeName = "";
            String noUseReason = "";
            String modemContractStatus = "";
            if (cpInfo != null) {
                if (cpInfo.getCommonType() != null && !cpInfo.getCommonType().isEmpty()) {
                    commonTypeName = commonCodeRepository.findCommonCodeName(cpInfo.getCommonType());
                }
                if (cpInfo.getReason() != null && !cpInfo.getReason().isEmpty()) {
                    noUseReason = commonCodeRepository.findCommonCodeName(cpInfo.getReason());
                }
                if (cpInfo.getCpmodemInfo().getContractStatus() != null
                        && !cpInfo.getCpmodemInfo().getContractStatus().isEmpty()) {
                    modemContractStatus = commonCodeRepository
                            .findCommonCodeName(cpInfo.getCpmodemInfo().getContractStatus());
                }
            }

            ChargerDetailListDto resDto = CpMapper.toDetailDto(cpInfo, cpModel);
            resDto.setCommonTypeName(commonTypeName);
            resDto.setReasonName(noUseReason);
            resDto.setModemContractStatusNm(modemContractStatus);

            return resDto;
        } catch (Exception e) {
            log.error("[ChargerService >> getChargerInfo] error:", e.getMessage(), e);
            return null;
        }
    }

    /*
     * 조회 > 충전기 전체 조회
     */
    @Transactional(readOnly = true)
    public List<ChargerListDto> findAllChargerListWithoutPagination(Long companyId, String manufCd,
            String searchOp, String searchContent) {
        log.info("=== Finding all charger list: companyId={}, manufCd={}, searchOp={}, searchContent={} ===",
                companyId, manufCd, searchOp, searchContent);

        // 대용량 데이터 처리를 위한 스트림 처리 또는 배치 처리 고려
        return chargerRepository.findAllChargerListWithoutPagination(companyId, manufCd, searchOp, searchContent);
    }
}
