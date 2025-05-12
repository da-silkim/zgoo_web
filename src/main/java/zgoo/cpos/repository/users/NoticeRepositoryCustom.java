package zgoo.cpos.repository.users;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.domain.users.Notice;
import zgoo.cpos.domain.users.Users;
import zgoo.cpos.dto.users.NoticeDto;
import zgoo.cpos.dto.users.NoticeDto.NoticeListDto;

public interface NoticeRepositoryCustom {

    // 공지사항 - 전체 조회
    Page<NoticeDto.NoticeListDto> findNoticeWithPagination(Pageable pageable, String levelPath, boolean isSuperAdmin);

    // 공지사항 - 검색 조회
    Page<NoticeDto.NoticeListDto> searchNoticeListwithPagination(Long companyId, LocalDate startDate, LocalDate endDate,
            Pageable pageable, String levelPath, boolean isSuperAdmin);

    // 공지사항 - 단건 조회
    Notice findNoticeOne(Long idx);

    // 공지사항 - 단건 조회(detail)
    NoticeDto.NoticeDetailDto findNoticeDetailOne(Long idx);

    // 이전글, 다음글 조회
    NoticeDto.NoticeDetailDto findPreviousNotice(Long idx, Long companyId, LocalDate startDate, LocalDate endDate,
            String levelPath, boolean isSuperAdmin);

    NoticeDto.NoticeDetailDto findNextNotice(Long idx, Long companyId, LocalDate startDate, LocalDate endDate,
            String levelPath, boolean isSuperAdmin);

    // 공지사항 - 삭제
    Long deleteNoticeOne(Long idx);

    Users findUserOne(String userId);

    // 공지사항 최신 5건 조회(대시보드)
    List<NoticeListDto> findLatestNoticeList(String levelPath, boolean isSuperAdmin);
}
