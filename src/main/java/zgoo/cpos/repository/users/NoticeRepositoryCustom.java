package zgoo.cpos.repository.users;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.domain.users.Notice;
import zgoo.cpos.domain.users.Users;
import zgoo.cpos.dto.users.NoticeDto;
import zgoo.cpos.dto.users.NoticeDto.NoticeListDto;

public interface NoticeRepositoryCustom {

    // 공지사항 - 전체 조회
    Page<NoticeDto.NoticeListDto> findNoticeWithPagination(Pageable pageable);

    // 공지사항 - 검색 조회
    Page<NoticeDto.NoticeListDto> searchNoticeListwithPagination(Long companyId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    // 공지사항 - 단건 조회
    Notice findNoticeOne(Long idx);

    // 공지사항 - 단건 조회(detail)
    NoticeDto.NoticeDetailDto findNoticeDetailOne(Long idx);

    // 이전글, 다음글 조회
    NoticeDto.NoticeDetailDto findPreviousNotice(Long idx, Long companyId, LocalDate startDate, LocalDate endDate);
    NoticeDto.NoticeDetailDto findNextNotice(Long idx, Long companyId, LocalDate startDate, LocalDate endDate);

    // 공지사항 - 삭제
    Long deleteNoticeOne(Long idx);

    Users findUserOne (String userId);
}
