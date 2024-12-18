package zgoo.cpos.repository.menu;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.menu.CompanyMenuAuthority;

public interface CompanyMenuAuthorityRepository extends JpaRepository<CompanyMenuAuthority, Long>, CompanyMenuAuthorityRepositoryCustom {

}
