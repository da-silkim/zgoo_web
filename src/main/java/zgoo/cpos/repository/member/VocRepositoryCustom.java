package zgoo.cpos.repository.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.dto.member.VocDto.VocListDto;
import zgoo.cpos.dto.member.VocDto.VocRegDto;

public interface VocRepositoryCustom {

    // 1:1문의 전체 조회
    Page<VocListDto> findVocWithPagination(Pageable pageable);

    // 1:1문의 검색 조회
    Page<VocListDto> searchVocWithPagination(String type, String replyStat, String name, Pageable pageable);

    // 1:1문의 단건 조회
    VocRegDto findVocOne(Long vocId);
}
