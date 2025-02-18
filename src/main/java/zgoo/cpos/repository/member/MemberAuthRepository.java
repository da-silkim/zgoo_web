package zgoo.cpos.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.member.MemberAuth;

public interface MemberAuthRepository extends JpaRepository<MemberAuth, String>, MemberAuthRepositoryCustom {
    MemberAuth findByIdTag(String idTag);
}
