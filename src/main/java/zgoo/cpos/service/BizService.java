package zgoo.cpos.service;

import java.util.List;

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
import zgoo.cpos.util.AESUtil;

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
            Page<BizInfoListDto> bizList;

            if (companyId == null && (searchOp == null || searchOp.isEmpty()) && (searchContent == null || searchContent.isEmpty())) {
                log.info("Executing the [findBizWithPagination]");
                bizList = this.bizRepository.findBizWithPagination(pageable);
            } else {
                log.info("Executing the [searchBizWithPagination]");
                bizList = this.bizRepository.searchBizWithPagination(companyId, searchOp, searchContent, pageable);
            }

            // 카드번호, TID 복호화
            bizList.forEach(bizDto -> {
                try {
                    decryptCardNumAndTid(bizDto);
                } catch (Exception e) {
                    log.error("Error decrypting card number or TID: {}", e.getMessage(), e);
                }
            });

            return bizList;
        } catch (Exception e) {
            log.error("[findBizInfoWithPagination] error: {}", e.getMessage(), e);
            return Page.empty(pageable);
        }
    }

    // 법인 단건 조회
    public BizInfoRegDto findBizOne(Long bizId) {
        try {
            BizInfoRegDto biz = this.bizRepository.findBizOne(bizId);

            // AES256 카드번호, TID 복호화
            decryptCardNumAndTidRegDto(biz);
            if (biz.getCardNum() != null && biz.getCardNum().length() == 16) {
                biz.setCardNum1(biz.getCardNum().substring(0, 4));
                biz.setCardNum2(biz.getCardNum().substring(4, 8));
                biz.setCardNum3(biz.getCardNum().substring(8, 12));
                biz.setCardNum4(biz.getCardNum().substring(12, 16));
            }

            return biz;
        } catch (Exception e) {
            log.error("[findBizOne] error : {}", e.getMessage());
            return null;
        }
    }

    // 법인 단건 조회(회원 리스트에서 사용)
    public BizInfoRegDto findBizOneCustom(Long bizId) {
        try {
            BizInfoRegDto biz = this.bizRepository.findBizOneCustom(bizId);
            return biz;
        } catch (Exception e) {
            log.error("[findBizOneCustom] error : {}", e.getMessage());
            return null;
        }
    }

    // 법인명으로 조회
    public List<BizInfoRegDto> findBizList(String bizName) {
        try {
            List<BizInfoRegDto> bizList = this.bizRepository.findBizByBizName(bizName);
            return bizList;
        } catch (Exception e) {
            log.error("[findBizList] error : {}", e.getMessage());
            return null;
        }
    }

    // 법인 정보 저장
    public void saveBiz(BizInfoRegDto dto) {
        try {
            Company company = this.companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("company not found with id: " + dto.getCompanyId()));
            
            if (dto.getCardNum() == null || dto.getCardNum().isEmpty()) {
                // 카드번호가 null이면 카드사 null 처리
                dto.setFnCode(null);
            } else {
                // AES256 카드번호, TID 암호화
                encryptCardNumAndTidRegDto(dto);
            }

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
            if (dto.getCardNum() == null || dto.getCardNum().isEmpty()) {
                // 카드번호가 null이면 카드사 null 처리
                dto.setFnCode(null);
            } else {
                // AES256 카드번호, TID 암호화
                encryptCardNumAndTidRegDto(dto);
            }
            
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

    // 카드번호, TID 복호화 + 카드번호 마스킹
    private void decryptCardNumAndTid(BizInfoListDto bizDto) throws Exception {
        bizDto.setCardNum(bizRepository.maskCardNum(AESUtil.decrypt(bizDto.getCardNum())));
        bizDto.setTid(AESUtil.decrypt(bizDto.getTid()));
    }

    // 카드번호, TID 복호화
    private void decryptCardNumAndTidRegDto(BizInfoRegDto bizDto) throws Exception {
        bizDto.setCardNum(AESUtil.decrypt(bizDto.getCardNum()));
        bizDto.setTid(AESUtil.decrypt(bizDto.getTid()));
    }

    // 카드번호, TID 암호화(값이 있을 경우에만 암호화 처리)
    private void encryptCardNumAndTidRegDto(BizInfoRegDto bizDto) throws Exception {
        if (bizDto.getCardNum() != null && !bizDto.getCardNum().isEmpty()) {
            bizDto.setCardNum(AESUtil.encrypt(bizDto.getCardNum()));
        }
        if (bizDto.getTid() != null && !bizDto.getTid().isEmpty()) {
            bizDto.setTid(AESUtil.encrypt(bizDto.getTid()));
        }
    }
}
