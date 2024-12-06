package zgoo.cpos.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.users.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom {

}
