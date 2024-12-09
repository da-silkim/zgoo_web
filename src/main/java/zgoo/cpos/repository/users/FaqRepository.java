package zgoo.cpos.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.users.Faq;

public interface FaqRepository extends JpaRepository<Faq, Long>, FaqRepositoryCustom {

}
