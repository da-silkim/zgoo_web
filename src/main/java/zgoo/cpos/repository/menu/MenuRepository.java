package zgoo.cpos.repository.menu;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.menu.Menu;

public interface MenuRepository extends JpaRepository<Menu, String>, MenuRepositoryCustom {

}
