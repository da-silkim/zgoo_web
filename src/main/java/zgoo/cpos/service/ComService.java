package zgoo.cpos.service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.users.Users;
import zgoo.cpos.dto.menu.MenuAuthorityDto.MenuAuthorityBaseDto;
import zgoo.cpos.dto.statistics.YearOptionDto;
import zgoo.cpos.repository.menu.MenuAuthorityRepository;
import zgoo.cpos.repository.users.UsersRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComService {

    private final MenuAuthorityRepository menuAuthorityRepository;
    private final UsersRepository usersRepository;

    /*
     * super admin check
     * if) SU: true
     * else) false
     */
    public boolean checkSuperAdmin(String loginUserId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String authorityName = authority.getAuthority();

            if (!"SU".equals(authorityName)) {
                log.info("[checkSuperAdmin] not a super admin");
                return false;
            }
        }

        return true;
    }

    /*
     * super admin & admin check
     * if) SU, AD: true
     * else) false
     */
    public boolean checkAdmin(String loginUserId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String authorityName = authority.getAuthority();

            if ("SU".equals(authorityName) || "AD".equals(authorityName)) {
                log.info("[checkAdmin] Super Admin or Admin");
                return true;
            }
        }

        return false;
    }

    @Transactional(readOnly = true)
    public Long getLoginUserCompanyId(String loginUserId) {
        try {
            Users loginUser = this.usersRepository.findUserOne(loginUserId);

            log.info("=== getLoginUserCompanyId: {}", loginUser.getCompany().getId());
            return loginUser.getCompany().getId();
        } catch (Exception e) {
            log.error("[getLoginUserCompanyId] error: {}", e.getMessage());
            return null;
        }
    }

    // mod_yn check
    public boolean checkModYn(String loginUserId, String menuCode) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Users loginUser = this.usersRepository.findUserOne(loginUserId);

        for (GrantedAuthority authority : authorities) {
            String authorityName = authority.getAuthority();

            if ("SU".equals(authorityName)) {
                log.info("[checkModYn] Super Admin");
                return true;
            } else {
                MenuAuthorityBaseDto dto = this.menuAuthorityRepository.findUserMenuAuthority(
                        loginUser.getCompany().getId(),
                        authorityName, menuCode);
                String modYn = dto.getModYn();

                log.info("[checkModYn] authority >> {}", authorityName);
                return modYn.equals("Y");
            }
        }

        return false;
    }

    /*
     * excel_yn check
     * if) SU: true
     * else) AD, NO, AS: excel authority check
     */
    public boolean checkExcelYn(String loginUserId, String menuCode) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Users loginUser = this.usersRepository.findUserOne(loginUserId);

        for (GrantedAuthority authority : authorities) {
            String authorityName = authority.getAuthority();

            if ("SU".equals(authorityName)) {
                log.info("[checkExcelYn] Super Admin");
                return true;
            } else {
                MenuAuthorityBaseDto dto = this.menuAuthorityRepository.findUserMenuAuthority(
                        loginUser.getCompany().getId(),
                        authorityName, menuCode);
                String excelYn = dto.getExcelYn();

                log.info("[checkExcelYn] authority >> {}", authorityName);
                return excelYn.equals("Y");
            }
        }

        return false;
    }

    /*
     * login User mod_yn authority check
     * return type: ResponseEntity<String>
     */
    public ResponseEntity<String> checkUserPermissions(Principal principal, String menuCode) {
        String loginUserId = principal.getName();
        if (loginUserId == null || loginUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 정보를 찾을 수 없습니다.");
        }

        boolean isMod = checkModYn(loginUserId, menuCode);
        if (!isMod) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
        }

        return null;
    }

    /*
     * 로그인 사용자 권한 체크
     * checkUserPermissions 함수와 기능동일
     * 컨트롤러 별 다른 리턴타입에 대응
     */
    public ResponseEntity<Map<String, String>> checkUserPermissionsMsg(Principal principal, String menuCode) {
        Map<String, String> response = new HashMap<>();

        String loginUserId = principal.getName();
        if (loginUserId == null || loginUserId.isEmpty()) {
            response.put("message", "사용자 정보를 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        boolean isMod = checkModYn(loginUserId, menuCode);
        if (!isMod) {
            response.put("message", "FORBIDDEN");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        return null;
    }

    /*
     * super admin check
     */
    public ResponseEntity<String> checkSuperAdminPermissions(Principal principal) {
        String loginUserId = principal.getName();
        if (loginUserId == null || loginUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 정보를 찾을 수 없습니다.");
        }

        boolean isSuperAdmin = checkSuperAdmin(loginUserId);
        if (!isSuperAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
        }

        return null;
    }

    /*
     * admin check
     */
    public ResponseEntity<String> checkAdminPermissions(Principal principal) {
        String loginUserId = principal.getName();
        if (loginUserId == null || loginUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 정보를 찾을 수 없습니다.");
        }

        boolean isAdmin = checkAdmin(loginUserId);
        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
        }

        return null;
    }

    /*
     * super admin check
     * return type: ResponseEntity<Map<String, String>>
     */
    public ResponseEntity<Map<String, String>> checkSuperAdminPermissionsMsg(Principal principal) {
        Map<String, String> response = new HashMap<>();

        String loginUserId = principal.getName();
        if (loginUserId == null || loginUserId.isEmpty()) {
            response.put("message", "사용자 정보를 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        boolean isSuperAdmin = checkSuperAdmin(loginUserId);
        if (!isSuperAdmin) {
            response.put("message", "FORBIDDEN");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        return null;
    }

    /*
     * login User excel_yn authority check
     */
    public boolean checkExcelPermissions(Principal principal, String menuCode) {
        String loginUserId = principal.getName();
        if (loginUserId == null || loginUserId.isEmpty()) {
            log.warn("loginUserId is null");
            return false;
        }

        boolean isExcel = checkExcelYn(loginUserId, menuCode);
        if (!isExcel) {
            log.warn("No permission to download Excel.");
            return false;
        }

        return true;
    }

    public List<YearOptionDto> generateYearOptions() {
        int currentYear = LocalDate.now().getYear();

        List<YearOptionDto> yearOptions = new ArrayList<>();

        for (int year = currentYear; year >= 2024; year--) {
            yearOptions.add(
                    YearOptionDto.builder()
                            .text(year + "년")
                            .value(year)
                            .build());
        }

        return yearOptions;
    }
}
