package zgoo.cpos.service;

import java.util.Collection;

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
}
