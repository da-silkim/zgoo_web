package zgoo.cpos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.biz.BizInfo;
import zgoo.cpos.domain.company.Company;
import zgoo.cpos.dto.biz.BizInfoDto.BizInfoListDto;
import zgoo.cpos.dto.biz.BizInfoDto.BizInfoRegDto;
import zgoo.cpos.mapper.BizMapper;
import zgoo.cpos.repository.biz.BizRepository;
import zgoo.cpos.repository.company.CompanyRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class BizService {
    private final BizRepository bizRepository;
    private final CompanyRepository companyRepository;

    // 법인 정보 조회
    public Page<BizInfoListDto> findBizInfoWithPagination(Long companyId, String searchOp, String searchContent, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            if (companyId == null && (searchOp == null || searchOp.isEmpty()) && (searchContent == null || searchContent.isEmpty())) {
                log.info("Executing the [findBizWithPagination]");
                return this.bizRepository.findBizWithPagination(pageable);
            } else {
                log.info("Executing the [searchBizWithPagination]");
                return this.bizRepository.searchBizWithPagination(companyId, searchOp, searchContent, pageable);
            }
        } catch (Exception e) {
            log.error("[findBizInfoWithPagination] error: {}", e.getMessage(), e);
            return Page.empty(pageable);
        }
    }

    // 법인 단건 조회
    public BizInfoRegDto findBizOne(Long bizId) {
        try {
            BizInfoRegDto biz = this.bizRepository.findBizOne(bizId);
            return biz;
        } catch (Exception e) {
            log.error("[findBizOne] error : {}", e.getMessage());
            return null;
        }
    }

    // 법인 정보 저장
    public void saveBiz(BizInfoRegDto dto) {
        try {
            Company company = this.companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("company not found with id: " + dto.getCompanyId()));
            BizInfo biz = BizMapper.toEntity(dto, company);
            this.bizRepository.save(biz);
        } catch (Exception e) {
            log.error("[saveBiz] error: {}", e.getMessage());
        }
    }

    // 법인 정보 수정
    @Transactional
    public void updateBizInfo(Long bizId, BizInfoRegDto dto) {
        BizInfo biz = this.bizRepository.findById(bizId)
            .orElseThrow(() -> new IllegalArgumentException("biz info not found with id: " + bizId));
        try {
            biz.updateBizInfo(dto);
        } catch (Exception e) {
            log.error("[updateBizInfo] error: {}", e.getMessage());
        }
    }

    // 법인 정보 삭제
    @Transactional
    public void deleteBiz(Long bizId) {
        BizInfo biz = this.bizRepository.findById(bizId)
            .orElseThrow(() -> new IllegalArgumentException("biz info not found with id: " + bizId));

        try {
            this.bizRepository.deleteById(bizId);
            log.info("==== bizId: {} is deleted..", bizId);
        } catch (Exception e) {
            log.error("[deleteBiz] error: {}", e.getMessage());
        }
    }
}
