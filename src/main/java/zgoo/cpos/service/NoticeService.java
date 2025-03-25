package zgoo.cpos.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import zgoo.cpos.dto.menu.MenuAuthorityDto.MenuAuthorityBaseDto;
import zgoo.cpos.dto.users.NoticeDto;
import zgoo.cpos.dto.users.NoticeDto.NoticeListDto;
import zgoo.cpos.mapper.NoticeMapper;
import zgoo.cpos.repository.menu.MenuAuthorityRepository;
import zgoo.cpos.repository.users.NoticeRepository;
import zgoo.cpos.repository.users.UsersRepository;
import zgoo.cpos.util.MenuConstants;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final UsersRepository usersRepository;
    private final MenuAuthorityRepository menuAuthorityRepository;

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

    // 공지사항 조회
    public Page<NoticeListDto> findNoticeWithPagintaion(Long companyId, LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<NoticeListDto> noticeList;

            if (companyId == null && startDate == null && endDate == null) {
                log.info("Executing the [findNoticeWithPagination]");
                noticeList = this.noticeRepository.findNoticeWithPagination(pageable);
            } else {
                log.info("Executing the [searchNoticeListwithPagination]");
                noticeList = this.noticeRepository.searchNoticeListwithPagination(companyId, startDate, endDate, pageable);
            }

            LocalDateTime now = LocalDateTime.now();

            noticeList.forEach(notice -> {
                LocalDateTime registrationDate = notice.getRegDt();
                long daysBetween = Duration.between(registrationDate, now).toDays();
                notice.setNew(daysBetween < 3);
            });

            return noticeList;
        } catch (Exception e) {
            log.error("[findNoticeWithPagintaion] error: {}", e.getMessage(), e);
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
    public void saveNotice(NoticeDto.NoticeRegDto dto, String loginUserId) {
        try {
            if (loginUserId == null || loginUserId.isEmpty()) {
                throw new IllegalArgumentException("User ID is missing. Cannot save notice without login user ID."); 
            }
            dto.setUserId(loginUserId);

            boolean isMod = checkSaveAuthority(loginUserId);

            if (isMod) {
                Users users = this.usersRepository.finsUserOneNotJoinedComapny(dto.getUserId());
                Notice notice = NoticeMapper.toEntity(dto, users);
                this.noticeRepository.save(notice);
            } else {
                log.warn("[saveNotice] Access without permission.");
            }

        } catch (IllegalArgumentException e) {
            log.error("[saveNotice] Illegal argument error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[saveNotice] error: {}", e.getMessage());
        }
    }

    // 공지사항 - 수정
    @Transactional
    public NoticeDto.NoticeRegDto updateNotice(NoticeDto.NoticeRegDto dto, String loginUserId) {
        try {
            if (loginUserId == null || loginUserId.isEmpty()) {
                throw new IllegalArgumentException("User ID is missing. Cannot update notice without login user ID."); 
            }

            boolean isMod = checkUpdateAndDeleteAuthority(dto.getIdx(), loginUserId);

            if (isMod) {
                Notice notice = this.noticeRepository.findNoticeOne(dto.getIdx());

                log.info("=== before update: {}", notice.toString());

                notice.updateNoticeInfo(dto);

                log.info("=== after update: {}", notice.toString());
    
                return NoticeMapper.toDto(notice);
            }

            log.warn("[updateNotice] Access without permission.");
            return null;
        } catch (IllegalArgumentException e) {
            log.error("[updateNotice] Illegal argument error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[updateNotice] error: {}", e.getMessage());
            return null;
        }
    }
    
    // 공지사항 - 삭제(Invisible)
    @Transactional
    public void deleteNotice(Long idx, String loginUserId) {
        try {
            if (loginUserId == null || loginUserId.isEmpty()) {
                throw new IllegalArgumentException("User ID is missing. Cannot delete notice without login user ID."); 
            }

            boolean isMod = checkUpdateAndDeleteAuthority(idx, loginUserId);

            if (isMod) {
                Long count = this.noticeRepository.deleteNoticeOne(idx);
                log.info("=== delete notice info: {}", count);
            } else {
                log.warn("[deleteNotice] Access without permission.");
            }

        } catch (IllegalArgumentException e) {
            log.error("[deleteNotice] Illegal argument error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[deleteNotice] error: {}", e.getMessage());
        }
    }

    // 공지사항 최신 조회
    public List<NoticeListDto> findLatestNoticeList() {
        try {
            List<NoticeListDto> noticeList = this.noticeRepository.findLatestNoticeList();

            LocalDateTime now = LocalDateTime.now();
            noticeList.forEach(notice -> {
                LocalDateTime registrationDate = notice.getRegDt();
                long daysBetween = Duration.between(registrationDate, now).toDays();
                notice.setNew(daysBetween < 3);
            });
            
            log.info("[findLatestNoticeList] success");
            return noticeList;
        } catch (Exception e) {
            log.error("[findLatestNoticeList] error: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // edit & delete button control
    public boolean buttonControl(Long id, String loginUserId) {
        try {
            Notice notice = this.noticeRepository.findNoticeOne(id);
            String writer = notice.getUser().getUserId();

            Users user = this.usersRepository.findUserOne(writer);
            String userAuthority = user.getAuthority();

            Users loginUser = this.usersRepository.findUserOne(loginUserId); // 로그인 사용자
            String loginUserAuthority = loginUser.getAuthority(); // 로그인 사용자 권한

            if (loginUserAuthority.equals("SU")) {
                return true;
            }

            if (loginUserAuthority.equals("AD")) {
                return !userAuthority.equals("SU");
            }

            return writer.equals(loginUserId);
        } catch (Exception e) {
            log.error("[NoticeService >> buttonControl] error: {}", e.getMessage());
            return false;
        }
    }

    // notice save check
    public boolean checkSaveAuthority(String loginUserId) {
        Users loginUser = this.usersRepository.findUserOne(loginUserId); // 로그인 사용자
        String loginUserAuthority = loginUser.getAuthority();            // 로그인 사용자 권한

        if (loginUserAuthority.equals("SU")) {
            log.info("[checkSaveAuthority] Super Admin");
            return true;
        }

        // mod_yn check
        MenuAuthorityBaseDto dto = this.menuAuthorityRepository.findUserMenuAuthority(loginUser.getCompany().getId(),
            loginUserAuthority, MenuConstants.NOTICE);
        return dto.getModYn().equals("Y");
    }

    // notice update, delete check
    public boolean checkUpdateAndDeleteAuthority(Long id, String loginUserId) {
        Users loginUser = this.usersRepository.findUserOne(loginUserId); // 로그인 사용자
        String loginUserAuthority = loginUser.getAuthority();            // 로그인 사용자 권한

        if (loginUserAuthority.equals("SU")) {
            log.info("[NoticeService >> checkUpdateAndDeleteAuthority] Super Admin");
            return true;
        }

        Notice notice = this.noticeRepository.findNoticeOne(id);
        String writer = notice.getUser().getUserId();
        Users user = this.usersRepository.findUserOne(writer); // 작성자
        String userAuthority = user.getAuthority();            // 작성자 권한

        // mod_yn check
        MenuAuthorityBaseDto dto = this.menuAuthorityRepository.findUserMenuAuthority(loginUser.getCompany().getId(),
            loginUserAuthority, MenuConstants.NOTICE);
        String modYn = dto.getModYn();

        if (modYn.equals("Y")) {
            if (loginUserAuthority.equals("AD")) {
                log.info("[NoticeService >> checkUpdateAndDeleteAuthority] Admin");
                return !userAuthority.equals("SU");
            }
            return writer.equals(loginUserId);
        }
        log.info("[NoticeService >> checkUpdateAndDeleteAuthority] update & delete no permission");
        return false;
    }
}
