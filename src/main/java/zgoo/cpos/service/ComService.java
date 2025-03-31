package zgoo.cpos.service;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.users.Users;
import zgoo.cpos.dto.menu.MenuAuthorityDto.MenuAuthorityBaseDto;
import zgoo.cpos.repository.menu.MenuAuthorityRepository;
import zgoo.cpos.repository.users.UsersRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComService {

    private final MenuAuthorityRepository menuAuthorityRepository;
    private final UsersRepository usersRepository;

    // Super Admin check
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

    // Super Admin & Admin check
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
                MenuAuthorityBaseDto dto = this.menuAuthorityRepository.findUserMenuAuthority(loginUser.getCompany().getId(),
                    authorityName, menuCode);
                String modYn = dto.getModYn();

                log.info("[checkModYn] authority >> {}", authorityName);
                return modYn.equals("Y");
            }
        }

        return false;
    }

    // loginUserId & authority check
    public ResponseEntity<String> checkUserPermissions(Principal principal, String menuCode) {
        String loginUserId = principal.getName();
        if (loginUserId == null || loginUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 정보를 찾을 수 없습니다.");
        }

        boolean isMod = checkModYn(loginUserId, menuCode);
        if (!isMod) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return null;
    }

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

    public ResponseEntity<String> checkSuperAdminPermissions(Principal principal) {
        String loginUserId = principal.getName();
        if (loginUserId == null || loginUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 정보를 찾을 수 없습니다.");
        }

        boolean isSuperAdmin = checkSuperAdmin(loginUserId);
        if (!isSuperAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return null;
    }

    public ResponseEntity<String> checkAdminPermissions(Principal principal) {
        String loginUserId = principal.getName();
        if (loginUserId == null || loginUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 정보를 찾을 수 없습니다.");
        }

        boolean isAdmin = checkAdmin(loginUserId);
        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return null;
    }

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
}
