package zgoo.cpos.repository.users;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zgoo.cpos.domain.users.Users;
import zgoo.cpos.dto.users.UsersDto;

public interface UsersRepositoryCustom {

    // 사용자 전체 조회
    List<Users> findAll();

    // 사용자 전체 조회 - 페이징
    Page<Users> findUsersWithPagination(Pageable pageable);
    Page<UsersDto.UsersListDto> findUsersWithPaginationToDto(Pageable pageable);

    // 사용자 조회 - 사용자ID로 조회
    Users findUserOne(String userId);

    // 사용자 조회 - 검색
    List<Users> searchUsers(Long companyId, String companyType, String name);

    // 사용자 조회 - 검색(페이징)
    Page<Users> searchUsersWithPagination(Long companyId, String companyType, String name, Pageable pageable);
    Page<UsersDto.UsersListDto> searchUsersWithPaginationToDto(Long companyId, String companyType, String name, Pageable pageable);

    // 사용자 삭제
    Long deleteUserOne(String userId);
}
