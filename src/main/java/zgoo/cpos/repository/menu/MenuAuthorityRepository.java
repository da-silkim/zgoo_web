package zgoo.cpos.repository.menu;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.menu.MenuAuthority;

public interface MenuAuthorityRepository extends JpaRepository<MenuAuthority, Long>, MenuAuthorityRepositoryCustom {

}
