package zgoo.cpos.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import zgoo.cpos.domain.users.Users;
import zgoo.cpos.repository.users.UsersRepository;
import zgoo.cpos.type.UserRole;

@RequiredArgsConstructor
@Service
public class UsersSecurityService implements UserDetailsService{

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Users _users = this.usersRepository.findUserOne(userId);
        if(_users == null) {
            throw new UsernameNotFoundException("User not found");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        switch (_users.getAuthority()) {
            case "SU":
                authorities.add(new SimpleGrantedAuthority(UserRole.SUPERADMIN.getValue()));
                break;
            case "AD":
                authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
                break;
            case "AS":
                authorities.add(new SimpleGrantedAuthority(UserRole.ASMANAGER.getValue()));
                break;
            default:
                authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
                break;
        }
        
        return new User(_users.getUserId(), _users.getPassword(), authorities);
    }
}
