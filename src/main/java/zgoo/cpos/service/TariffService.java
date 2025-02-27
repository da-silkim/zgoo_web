package zgoo.cpos.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.company.Company;
import zgoo.cpos.domain.company.CpPlanPolicy;
import zgoo.cpos.domain.tariff.TariffInfo;
import zgoo.cpos.domain.tariff.TariffPolicy;
import zgoo.cpos.dto.company.CompanyDto.CpPlanDto;
import zgoo.cpos.dto.tariff.TariffDto.TariffInfoDto;
import zgoo.cpos.dto.tariff.TariffDto.TariffPolicyDto;
import zgoo.cpos.dto.tariff.TariffDto.TariffRegDto;
import zgoo.cpos.mapper.CompanyMapper;
import zgoo.cpos.mapper.TariffMapper;
import zgoo.cpos.repository.company.CompanyRepository;
import zgoo.cpos.repository.company.CpPlanPolicyRepository;
import zgoo.cpos.repository.tariff.TariffInfoRepository;
import zgoo.cpos.repository.tariff.TariffPolicyRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class TariffService {

    private final CompanyRepository companyRepository;
    private final CpPlanPolicyRepository planRepository;
    private final TariffPolicyRepository tariffPolicyRepository;
    private final TariffInfoRepository tariffInfoRepository;

    // 저장
    @Transactional
    public void savePlanPolicy(Long companyId, CpPlanDto dto) {
        log.info("==  Start save PlanPolicy >> companyId:{}, dto:{}", companyId, dto.toString());

        // 신규요금제 추가인지, 기존요금제에 tariff_policy 추가인지 판단
        // => 요청data에 요금제 및 companyID로 조회시 있을경우 동일 요금제로 간주
        CpPlanPolicy cpPlan = planRepository.findByPlanNameAndCompanyId(dto.getPlanName(), companyId);
        if (cpPlan == null) {
            // 신규저장
            // 업체정보 조회
            Company findCompany = companyRepository.findById(companyId)
                    .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + companyId));
            // dto >> entity
            CpPlanPolicy planEntity = CompanyMapper.toEntityCpPolicyPlan(dto, findCompany);
            // 저장
            CpPlanPolicy result = planRepository.save(planEntity);

            log.info("== Charger Plan policy save success >> policy_id:{}", result.getId());
        } else {
            log.info("== Charger Plan policy already exist.");
        }

    }

    @Transactional
    public void updateTariffPolicy(TariffRegDto reqdto) {
        log.info("== Start update TariffPolicy >> dto:{}", reqdto.toString());

        TariffPolicy findOne = tariffPolicyRepository.findById(reqdto.getTariffId()).orElseThrow(
                () -> new IllegalArgumentException("Tariffpolicy Not found with tariffId: " + reqdto.getTariffId()));

        // 적용날짜 수정
        log.info("[BeFore] update apply date:{}", findOne.getApply_date());
        LocalDate updateDate = reqdto.getApplyStartDate();
        LocalDate nowDate = LocalDate.now();
        if (updateDate.isAfter(nowDate)) {
            // 현재시간 이후의 날짜만 업데이트 처리
            findOne.updateApplyDate(updateDate.atStartOfDay());
            log.info("[After] update apply date:{}", findOne.getApply_date());
        } else {
            throw new IllegalArgumentException("오늘 이후 날짜만 가능합니다.");
        }

        // 요금정보 수정
        List<TariffInfoDto> reqTariffInfoList = reqdto.getTariffInfo();

        List<TariffInfo> findTariffInfoList = tariffInfoRepository.findTariffInfoListByTariffId(reqdto.getTariffId())
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Tariffpolicy Not found with tariffId: " + reqdto.getTariffId()));

        log.info("TariffInfo found ===> tariffId:{}, listSize:{}", reqdto.getTariffId(), findTariffInfoList.size());
        if (findTariffInfoList != null && !findTariffInfoList.isEmpty()) {
            if (reqTariffInfoList.size() != findTariffInfoList.size()) {
                throw new IllegalArgumentException("Mismatch between existing and new tariff info list sizes.");
            }
            for (int i = 0; i < findTariffInfoList.size(); i++) {
                TariffInfo existOne = findTariffInfoList.get(i);
                TariffInfoDto newOne = reqTariffInfoList.get(i);

                // update
                existOne.updateTariffInfo(newOne);
            }

            log.info("== Tariff Update Complete!");

        } else {
            log.error("There is no TariffInfo data, tariffId:{}", reqdto.getTariffId());
        }

    }

    @Transactional
    public Long saveTariffPolicy(TariffRegDto dto) {
        log.info("==  Start save TariffPolicy >> dto:{}", dto.toString());

        // plan policy 조회
        CpPlanPolicy planPolicy = planRepository.findByPlanName(dto.getPolicyName());

        // dto >> entity
        TariffPolicy reqEntity = TariffMapper.toEntityTariffPolicy(dto, planPolicy);

        // 단가적용상태코드 처리(신규, 기존)
        // 기존에 특정 policy_id로 등록된 tariff_policy를 검색
        TariffPolicy asis = tariffPolicyRepository.findByApplyCodeAndPolicyId(reqEntity.getApply_code(),
                planPolicy.getId());
        if (asis != null) {
            if (reqEntity.getApply_code().equals("TFCURR")) {
                // 기존 data의 단가상태를 현재 > 과거로 업데이트
                asis.updateApplyCode("TFPAST");

            } else if (reqEntity.getApply_code().equals("TFFUTR")) {
                log.info("Tariff policy save success >> Before:{}", asis.toString());
                // 기존 data 삭제
                // 1.tariffInfo 제거
                tariffInfoRepository.deleteTariffInfoByTariffId(asis.getId());
                log.info("[TariffInfo] Delete Success >> tariffID: {}", asis.getId());
                // 2.tariff policy 제거
                tariffPolicyRepository.delete(asis);
                log.info("[TariffPolicy] Delete Success >> tariffID: {}", asis.getId());
            }
        }
        // 신규데이터 저장
        TariffPolicy saved = tariffPolicyRepository.save(reqEntity);
        log.info("== Tariff Policy save success >> tariff_id:{}", saved.getId());

        return saved.getId();
    }

    @Transactional
    public void saveTariffInfo(TariffRegDto reqDto, Long tariffId) {
        log.info("==  Start save TariffInfo with tariffId: {} >> list size: {}", tariffId,
                reqDto.getTariffInfo().size());

        // tariffpolicy 조회
        TariffPolicy tpolicy = tariffPolicyRepository.findById(tariffId).orElseThrow(
                () -> new IllegalArgumentException("Tariffpolicy Not found with tariffId: " + tariffId));

        try {
            log.info("tariffPolicy find success >> tariffId : {}", tpolicy.getId());
            List<TariffInfo> tinfolist = TariffMapper.toEntityListTariffInfo(tpolicy, reqDto);

            tariffInfoRepository.saveAll(tinfolist);

            log.info("TariffInfo saved successfully. Total records: {}", tinfolist.size());

        } catch (Exception e) {
            log.error("save tariffInfo: ", e, e.getMessage());
        }
    }

    // 조회
    public List<CpPlanDto> searchPlanPolicyAll() {
        log.info("==  Start search PlanPolicy All");
        List<CpPlanPolicy> planEntityList = planRepository.findAll();
        List<CpPlanDto> dtolist = CompanyMapper.toDtoListCpPolicyPlan(planEntityList);

        return dtolist;
    }

    public List<CpPlanDto> searchPlanListWithCompanyId(Long companyId) {
        List<CpPlanDto> resultList = planRepository.findPlanListByCompanyId(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Plan Info Not found with companyid: " + companyId));

        return resultList;
    }

    public List<CpPlanPolicy> searchPlanPolicyByCompanyId(Long companyId) {
        log.info("==  Start search PlanPolicy by CompanyId : {}", companyId);

        List<CpPlanPolicy> findList = planRepository.findAllByCompanyId(companyId);

        return findList;
    }

    public Page<TariffPolicyDto> searchTariffPolicyAll(int page, int size) {
        log.info("==  Start search TariffPolicy All");

        Pageable pageable = PageRequest.of(page, size);
        return tariffPolicyRepository.findAllTariffPolicyPaging(pageable);
    }

    public Page<TariffPolicyDto> searchTariffPolicyByCompanyId(int page, int size, Long companyId) {
        log.info("==  Start search TariffPolicy By CompanyID : {}", companyId);

        Pageable pageable = PageRequest.of(page, size);
        return tariffPolicyRepository.findTariffPolicyByCompanyIdPaging(pageable, companyId);
    }

    public CpPlanPolicy searchPlanByPlanName(String planName) {
        return planRepository.findByPlanName(planName);
    }

    public Long searchTariffIdByPlanName(String planName) {
        return tariffPolicyRepository.findTariffIdByPlanName(planName);
    }

    public List<TariffInfoDto> searchTariffInfoListByTariffId(Long tariffId) {
        List<TariffInfoDto> resultList = tariffPolicyRepository.findTariffInfoListByTariffId(tariffId)
                .orElseThrow(() -> new IllegalArgumentException("TariffInfo Not found with tariffId: " + tariffId));

        return resultList;
    }

    // 수정

    // 삭제
    @Transactional
    public void deletePlan(Long tariffId) {
        try {
            TariffPolicy tpolicy = tariffPolicyRepository.findById(tariffId).orElseThrow(
                    () -> new IllegalArgumentException("Tariff Policy Not found with tariffId: " + tariffId));

            Long policyId = tpolicy.getPolicy().getId();

            // 1. tariff Info 제거
            tariffInfoRepository.deleteTariffInfoByTariffId(tpolicy.getId());
            log.info("[TariffInfo] Delete Success >> tariffID: {}, policyId:{}", tpolicy.getId(), policyId);

            // 2.tariff policy 제거
            tariffPolicyRepository.delete(tpolicy);
            log.info("[TariffPolicy] Delete Success >> tariffID:{}, policyId:{}", tpolicy.getId(), policyId);

            // 3.charger plan 제거 >> tariff_policy size가 0일 경우
            List<TariffPolicy> tarList = tariffPolicyRepository.findAll();
            if (tarList.size() < 1) {
                CpPlanPolicy cppolicy = planRepository.findById(policyId).orElseThrow(
                        () -> new IllegalArgumentException("Charger Policy Not found with policyId: " + policyId));
                planRepository.delete(cppolicy);
                log.info("[CpPlanPolicy] Delete Success >> planName: {}", cppolicy.getName());
            }

        } catch (Exception e) {
            log.error("delete charger plan Error: ", e);
        }

    }
}
