package zgoo.cpos.repository.biz;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.biz.BizInfoDto;
import zgoo.cpos.dto.biz.BizInfoDto.BizInfoListDto;
import zgoo.cpos.dto.biz.BizInfoDto.BizInfoRegDto;

public interface BizRepositoryCustom {

    // 법인 전체 조회
    Page<BizInfoListDto> findBizWithPagination(Pageable pageable);

    // 법인 검색 조회
    Page<BizInfoListDto> searchBizWithPagination(Long companyId, String searchOp, String searchContent, Pageable pageable);

    // 카드결제번호 마스킹
    String maskCardNum(String cardNum);

    // 법인 단건 조회
    BizInfoRegDto findBizOne(Long bizId);

    // 법인 단건 조회(회원 리스트 화면에서 사용)
    BizInfoRegDto findBizOneCustom(Long bizId);

    // 법인명 조회
    List<BizInfoRegDto> findBizByBizName(String bizName);
}
