package zgoo.cpos.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.cpos.domain.users.Users;

public interface UsersRepository extends  JpaRepository<Users, String>, UsersRepositoryCustom {
    boolean existsByUserId(String userId);
}
