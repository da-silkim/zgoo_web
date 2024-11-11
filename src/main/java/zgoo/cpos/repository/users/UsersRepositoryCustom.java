package zgoo.cpos.repository.users;

import java.util.List;

import zgoo.cpos.domain.users.Users;

public interface UsersRepositoryCustom {

    // 사용자 전체 조회
    List<Users> findAll();

    // 사용자 조회 - 사용자ID로 조회
    Users findUserOne(String userId);

    // 사용자 삭제
    Long deleteUserOne(String userId);
}
