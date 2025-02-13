package zgoo.cpos.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.users.Notice;
import zgoo.cpos.domain.users.Users;
import zgoo.cpos.dto.users.NoticeDto;
import zgoo.cpos.dto.users.NoticeDto.NoticeListDto;
import zgoo.cpos.dto.users.UsersDto;
import zgoo.cpos.mapper.NoticeMapper;
import zgoo.cpos.repository.users.NoticeRepository;
import zgoo.cpos.repository.users.UsersRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final UsersRepository usersRepository;

    // 공지사항 - 전체 조회 페이징
    public Page<NoticeDto.NoticeListDto> findNoticeAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<NoticeDto.NoticeListDto> noticeList = this.noticeRepository.findNoticeWithPagination(pageable);
            return noticeList;
        } catch (DataAccessException dae) {
            log.error("[findNoticeAll] database error : {}", dae.getMessage());
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("[findNoticeAll] error : {}", e.getMessage());
            return Page.empty(pageable);
        }
    }

    // 공지사항 - 검색 페이징
    @Transactional
    public Page<NoticeDto.NoticeListDto> searchNoticeListwithPagination(Long companyId, LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<NoticeDto.NoticeListDto> noticeList = this.noticeRepository.searchNoticeListwithPagination(companyId, startDate, endDate, pageable);
            return noticeList;
        } catch (DataAccessException dae) {
            log.error("[searchNoticeListwithPagination] database error : {}", dae.getMessage());
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("[searchNoticeListwithPagination] error : {}", e.getMessage(), e);
            return Page.empty(pageable);
        }
    }

    // 공지사항 - 단건 조회
    public NoticeDto.NoticeRegDto findNoticeOne(Long idx) {
        try {
            Notice notice = this.noticeRepository.findNoticeOne(idx);
            return NoticeMapper.toDto(notice);
        } catch (Exception e) {
            log.error("[findNoticeOne] error : {}", e.getMessage());
            return null;
        }
    }

    // 공지사항 - 단건 조회(detail)
    public NoticeDto.NoticeDetailDto findNoticeDetailOne(Long idx) {
        try {
            NoticeDto.NoticeDetailDto notice = this.noticeRepository.findNoticeDetailOne(idx);
            return notice;
        } catch (Exception e) {
            log.error("[findNoticeDetailOne] error : {}", e.getMessage());
            return null;
        }
    }

    // 이전글 조회
    public NoticeDto.NoticeDetailDto findPreviousNotice(Long idx, Long companyId, LocalDate startDate, LocalDate endDate) {
        try {
            NoticeDto.NoticeDetailDto notice = this.noticeRepository.findPreviousNotice(idx, companyId, startDate, endDate);
            return notice;
        } catch (Exception e) {
            log.error("[findPreviousNotice] error : {}", e.getMessage());
            return null;
        }
    }

    // 다음글 조회
    public NoticeDto.NoticeDetailDto findNextNotice(Long idx, Long companyId, LocalDate startDate, LocalDate endDate) {
        try {
            NoticeDto.NoticeDetailDto notice = this.noticeRepository.findNextNotice(idx, companyId, startDate, endDate);
            return notice;
        } catch (Exception e) {
            log.error("[findNextNotice] error : {}", e.getMessage());
            return null;
        }
    }

    // 조회수 증가
    @Transactional
    public void incrementViewCount(Long idx) {
        try {
            Notice notice = this.noticeRepository.findNoticeOne(idx);

            if (notice != null) {
                notice.setViews(notice.getViews() + 1);
                this.noticeRepository.save(notice);
            }
        } catch (Exception e) {
            log.error("[incrementViewCount] error : {}", e.getMessage());
        }
    }

    // 공지사항 - 등록
    @Transactional
    public void saveNotice(NoticeDto.NoticeRegDto dto) {
        try {
            System.out.println("공지사항 사용자ID: " + dto.getUserId());
            // Users users = this.noticeRepository.findUserOne(dto.getUserId());
            Users users = this.usersRepository.finsUserOneNotJoinedComapny(dto.getUserId());
            System.out.println("사용자 확인1: " + users.getUserId());
            Notice notice = NoticeMapper.toEntity(dto, users);
            System.out.println("사용자 확인2: " + notice.getUser().getUserId());
            this.noticeRepository.save(notice);
        } catch (Exception e) {
            log.error("[saveNotice] error: {}", e.getMessage());
        }
    }

    // 공지사항 - 수정
    @Transactional
    public NoticeDto.NoticeRegDto updateNotice(NoticeDto.NoticeRegDto dto) {
        try {
            Notice notice = this.noticeRepository.findNoticeOne(dto.getIdx());

            log.info("=== before update: {}", notice.toString());

            notice.updateNoticeInfo(dto);

            log.info("=== after update: {}", notice.toString());

            return NoticeMapper.toDto(notice);
        } catch (Exception e) {
            log.error("[updateNotice] error: {}", e.getMessage());
            return null;
        }
    }
    
    // 공지사항 - 삭제(Invisible)
    @Transactional
    public void deleteNotice(Long idx) {
        Long count = this.noticeRepository.deleteNoticeOne(idx);
        log.info("=== delete notice info: {}", count);
    }

    // 공지사항 최신 조회
    public List<NoticeListDto> findLatestNoticeList() {
        try {
            List<NoticeListDto> noticeList = this.noticeRepository.findLatestNoticeList();
            log.info("[findLatestNoticeList] success");
            return noticeList;
        } catch (Exception e) {
            log.error("[findLatestNoticeList] error: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
}
