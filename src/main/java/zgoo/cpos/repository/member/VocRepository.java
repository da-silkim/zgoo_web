package zgoo.cpos.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.member.Voc;

public interface VocRepository extends JpaRepository<Voc, Long>, VocRepositoryCustom {

}
