package zgoo.cpos.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.users.LoginHist;

public interface LoginHistRepository extends JpaRepository<LoginHist, Long>, LoginHistRepositoryCustom {

}
