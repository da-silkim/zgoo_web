package zgoo.cpos.repository.authority;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.authority.MenuAuthority;
import zgoo.cpos.type.MenuAuthorityKey;

public interface MenuAuthorityRepository extends JpaRepository<MenuAuthority, MenuAuthorityKey>, MenuAuthorityRepositoryCustom {

}
