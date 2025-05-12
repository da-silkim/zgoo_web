package zgoo.cpos.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.users.Faq;
import zgoo.cpos.domain.users.Users;
import zgoo.cpos.dto.menu.MenuAuthorityDto.MenuAuthorityBaseDto;
import zgoo.cpos.dto.users.FaqDto;
import zgoo.cpos.dto.users.FaqDto.FaqListDto;
import zgoo.cpos.mapper.FaqMapper;
import zgoo.cpos.repository.company.CompanyRepository;
import zgoo.cpos.repository.menu.MenuAuthorityRepository;
import zgoo.cpos.repository.users.FaqRepository;
import zgoo.cpos.repository.users.UsersRepository;
import zgoo.cpos.util.MenuConstants;

@Service
@RequiredArgsConstructor
@Slf4j
public class FaqService {
    private final FaqRepository faqRepository;
    private final UsersRepository usersRepository;
    private final MenuAuthorityRepository menuAuthorityRepository;
    private final CompanyRepository companyRepository;
    private final ComService comService;

    // FAQ 전체 조회
    public Page<FaqDto.FaqListDto> findFaqAll(int page, int size, String userId) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            boolean isSuperAdmin = comService.checkSuperAdmin(userId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                // 관계정보가 없을경우 빈 리스트 전달
                return Page.empty(pageable);
            }

            Page<FaqDto.FaqListDto> faqList = this.faqRepository.findFaqWithPagination(pageable, levelPath,
                    isSuperAdmin);
            return faqList;
        } catch (DataAccessException dae) {
            log.error("[findFaqAll] database error : {}", dae.getMessage());
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("[findFaqAll] error : {}", e.getMessage());
            return Page.empty(pageable);
        }
    }

    // FAQ 검색 조회
    public Page<FaqDto.FaqListDto> searchFaqListWithPagination(String section, int page, int size, String userId) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            boolean isSuperAdmin = comService.checkSuperAdmin(userId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                // 관계정보가 없을경우 빈 리스트 전달
                return Page.empty(pageable);
            }

            Page<FaqDto.FaqListDto> faqList = this.faqRepository.searchFaqListWithPagination(section, pageable,
                    levelPath, isSuperAdmin);
            return faqList;
        } catch (DataAccessException dae) {
            log.error("[searchFaqListWithPagination] database error : {}", dae.getMessage());
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("[searchFaqListWithPagination] error : {}", e.getMessage());
            return Page.empty(pageable);
        }
    }

    // FAQ 조회
    public Page<FaqListDto> findFaqWithPagination(String section, int page, int size, String userId) {
        Pageable pageable = PageRequest.of(page, size);

        try {

            boolean isSuperAdmin = comService.checkSuperAdmin(userId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                // 관계정보가 없을경우 빈 리스트 전달
                return Page.empty(pageable);
            }

            Page<FaqListDto> faqList;

            if (section == null || section.isEmpty()) {
                log.info("Executing the [findFaqWithPagination]");
                faqList = this.faqRepository.findFaqWithPagination(pageable, levelPath, isSuperAdmin);
            } else {
                log.info("Executing the [findFaqWithPagination]");
                faqList = this.faqRepository.searchFaqListWithPagination(section, pageable, levelPath, isSuperAdmin);
            }

            LocalDateTime now = LocalDateTime.now();

            faqList.forEach(faq -> {
                LocalDateTime registractionDate = faq.getRegDt();
                long daysBetween = Duration.between(registractionDate, now).toDays();
                faq.setNew(daysBetween < 3);
            });

            return faqList;
        } catch (Exception e) {
            log.error("[findFaqWithPagination] error : {}", e.getMessage());
            return Page.empty(pageable);
        }
    }

    // FAQ 단건 조회
    public FaqDto.FaqRegDto findFaqOne(Long id, String userId) {
        try {
            boolean isSuperAdmin = comService.checkSuperAdmin(userId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                // 관계정보가 없을경우 빈 리스트 전달
                return null;
            }

            Faq faq = this.faqRepository.findFaqOne(id, levelPath, isSuperAdmin);
            return FaqMapper.toDto(faq);
        } catch (Exception e) {
            log.error("[findFaqOne] error : {}", e.getMessage());
            return null;
        }
    }

    // FAQ 단건 조회(detail)
    public FaqDto.FaqDetailDto findFaqDetailOne(Long id, String userId) {
        try {
            boolean isSuperAdmin = comService.checkSuperAdmin(userId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                // 관계정보가 없을경우 빈 리스트 전달
                return null;
            }

            FaqDto.FaqDetailDto faq = this.faqRepository.findFaqDetailOne(id, levelPath, isSuperAdmin);
            return faq;
        } catch (Exception e) {
            log.error("[findFaqDetailOne] error: {}", e.getMessage());
            return null;
        }
    }

    // 이전글 조회
    public FaqDto.FaqDetailDto findPreviousFaq(Long id, String section, String userId) {
        try {
            boolean isSuperAdmin = comService.checkSuperAdmin(userId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                // 관계정보가 없을경우 빈 리스트 전달
                return null;
            }

            Faq currentFaq = this.faqRepository.findFaqOne(id, levelPath, isSuperAdmin);
            FaqDto.FaqDetailDto faq = this.faqRepository.findPreviousFaq(id, section, currentFaq, levelPath,
                    isSuperAdmin);
            return faq;
        } catch (Exception e) {
            log.error("[findPreviousFaq] error: {}", e.getMessage());
            return null;
        }
    }

    // 다음글 조회
    public FaqDto.FaqDetailDto findNextFaq(Long id, String section, String userId) {
        try {
            boolean isSuperAdmin = comService.checkSuperAdmin(userId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(userId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                // 관계정보가 없을경우 빈 리스트 전달
                return null;
            }

            Faq currentFaq = this.faqRepository.findFaqOne(id, levelPath, isSuperAdmin);
            FaqDto.FaqDetailDto faq = this.faqRepository.findNextFaq(id, section, currentFaq, levelPath, isSuperAdmin);
            return faq;
        } catch (Exception e) {
            log.error("[findNextFaq] error: {}", e.getMessage());
            return null;
        }
    }

    // FAQ 등록
    @Transactional
    public void saveFaq(FaqDto.FaqRegDto dto, String loginUserId) {
        try {
            if (loginUserId == null || loginUserId.isEmpty()) {
                throw new IllegalArgumentException("User ID is missing. Cannot save faq without login user ID.");
            }
            dto.setUserId(loginUserId);

            boolean isMod = checkSaveAuthority(loginUserId);

            if (isMod) {
                Users users = this.usersRepository.finsUserOneNotJoinedComapny(dto.getUserId());
                Faq faq = FaqMapper.toEntity(dto, users);
                this.faqRepository.save(faq);
            } else {
                log.warn("[saveFaq] Access without permission.");
            }

        } catch (IllegalArgumentException e) {
            log.error("[saveFaq] Illegal argument error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[saveFaq] error: {}", e.getMessage());
        }
    }

    // FAQ 수정
    @Transactional
    public FaqDto.FaqRegDto updateFaq(FaqDto.FaqRegDto dto, String loginUserId) {
        try {
            if (loginUserId == null || loginUserId.isEmpty()) {
                throw new IllegalArgumentException("User ID is missing. Cannot update faq without login user ID.");
            }
            boolean isMod = checkUpdateAndDeleteAuthority(dto.getId(), loginUserId);

            boolean isSuperAdmin = comService.checkSuperAdmin(loginUserId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(loginUserId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                // 관계정보가 없을경우 빈 리스트 전달
                return null;
            }

            if (isMod) {
                Faq faq = this.faqRepository.findFaqOne(dto.getId(), levelPath, isSuperAdmin);

                log.info("=== before update: {}", faq.toString());

                faq.updateFaqInfo(dto);

                log.info("=== after update: {}", faq.toString());

                return FaqMapper.toDto(faq);
            }

            log.warn("[updateFaq] Access without permission.");
            return null;
        } catch (IllegalArgumentException e) {
            log.error("[updateFaq] Illegal argument error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[updateFaq] error: {}", e.getMessage());
            return null;
        }
    }

    // FAQ 삭제
    @Transactional
    public void deleteFaq(Long id, String loginUserId) {
        try {
            if (loginUserId == null || loginUserId.isEmpty()) {
                throw new IllegalArgumentException("User ID is missing. Cannot delete faq without login user ID.");
            }

            boolean isMod = checkUpdateAndDeleteAuthority(id, loginUserId);

            if (isMod) {
                Long count = this.faqRepository.deleteFaqOne(id);
                log.info("=== delete faq info: {}", count);
            } else {
                log.warn("[deleteFaq] Access without permission.");
            }

        } catch (IllegalArgumentException e) {
            log.error("[deleteFaq] Illegal argument error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[deleteFaq] error: {}", e.getMessage());
        }

    }

    // edit & delete button control
    public boolean buttonControl(Long id, String loginUserId) {
        try {

            boolean isSuperAdmin = comService.checkSuperAdmin(loginUserId);
            Long loginUserCompanyId = comService.getLoginUserCompanyId(loginUserId);
            String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
            log.info("== levelPath : {}", levelPath);
            if (levelPath == null) {
                // 관계정보가 없을경우 빈 리스트 전달
                return false;
            }

            Faq faq = this.faqRepository.findFaqOne(id, levelPath, isSuperAdmin);
            String writer = faq.getUser().getUserId();

            Users user = this.usersRepository.findUserOne(writer);
            String userAuthority = user.getAuthority();

            Users loginUser = this.usersRepository.findUserOne(loginUserId);
            String loginUserAuthority = loginUser.getAuthority();

            if (loginUserAuthority.equals("SU")) {
                return true;
            }

            if (loginUserAuthority.equals("AD")) {
                return !userAuthority.equals("SU");
            }

            return writer.equals(loginUserId);
        } catch (Exception e) {
            log.error("[FaqService >> buttonControl] error: {}", e.getMessage());
            return false;
        }
    }

    // faq save check
    public boolean checkSaveAuthority(String loginUserId) {
        Users loginUser = this.usersRepository.findUserOne(loginUserId);
        String loginUserAuthority = loginUser.getAuthority();

        if (loginUserAuthority.equals("SU")) {
            log.info("[FaqService >> checkSaveAuthority] Supter Admin");
            return true;
        }

        MenuAuthorityBaseDto dto = this.menuAuthorityRepository.findUserMenuAuthority(loginUser.getCompany().getId(),
                loginUserAuthority, MenuConstants.FAQ);
        return dto.getModYn().equals("Y");
    }

    // faq update & delete check
    public boolean checkUpdateAndDeleteAuthority(Long id, String loginUserId) {
        Users loginUser = this.usersRepository.findUserOne(loginUserId);
        String loginUserAuthority = loginUser.getAuthority();

        if (loginUserAuthority.equals("SU")) {
            log.info("[FaqService >> checkUpdateAndDeleteAuthority] Super Admin");
            return true;
        }

        boolean isSuperAdmin = comService.checkSuperAdmin(loginUserId);
        Long loginUserCompanyId = comService.getLoginUserCompanyId(loginUserId);
        String levelPath = companyRepository.findLevelPathByCompanyId(loginUserCompanyId);
        log.info("== levelPath : {}", levelPath);
        if (levelPath == null) {
            // 관계정보가 없을경우 빈 리스트 전달
            return false;
        }

        Faq faq = this.faqRepository.findFaqOne(id, levelPath, isSuperAdmin);
        String writer = faq.getUser().getUserId();
        Users user = this.usersRepository.findUserOne(writer);
        String userAuthority = user.getAuthority();

        MenuAuthorityBaseDto dto = this.menuAuthorityRepository.findUserMenuAuthority(loginUser.getCompany().getId(),
                loginUserAuthority, MenuConstants.FAQ);
        String modYn = dto.getModYn();

        if (modYn.equals("Y")) {
            if (loginUserAuthority.equals("AD")) {
                log.info("[FaqService >> checkUpdateAndDeleteAuthority] Admin");
                return !userAuthority.equals("SU");
            }
            return writer.equals(loginUserId);
        }
        log.info("[FaqService >> checkUpdateAndDeleteAuthority] update & delete no permission");
        return false;
    }
}
