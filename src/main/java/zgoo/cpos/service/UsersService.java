package zgoo.cpos.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.company.Company;
import zgoo.cpos.domain.users.Users;
import zgoo.cpos.dto.users.UsersDto;
import zgoo.cpos.mapper.UsersMapper;
import zgoo.cpos.repository.company.CompanyRepository;
import zgoo.cpos.repository.users.UsersRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersService {
    private final CompanyRepository companyRepository;
    private final UsersRepository usersRepository;

    // 사용자 - 전체 조회
    @Transactional
    public List<UsersDto.UsersListDto> findUsersAll() {
        List<Users> usersList = this.usersRepository.findAll();
        // List<UsersDto.UsersListDto> list = null;

        if(usersList.isEmpty()) {
            log.info("=== no users found ===");
            return new ArrayList<>();   // empty list
        }

        List<UsersDto.UsersListDto> list = UsersMapper.toDtoList(usersList);
        log.info("=== users DB search success ===");

        return list;
    }

    // 사용자 - 단건 조회
    public UsersDto.UsersRegDto findUserOne(String userId) {
        Users user = this.usersRepository.findUserOne(userId);
        return UsersMapper.toDto(user);
    }

    // 사용자 - 등록
    @Transactional
    public void saveUsers(UsersDto.UsersRegDto dto) {
        Company company = this.companyRepository.findById(dto.getCompanyId())
            .orElseThrow(() -> new IllegalArgumentException("=== company not found ==="));

        // dto >> entity
        Users user = UsersMapper.toEntity(dto, company);
        usersRepository.save(user);
    }

    // userId 중복 검사
    public boolean isUserIdDuplicate(String userId) {
        return this.usersRepository.existsById(userId);
    }

    // 사용자 - 수정
    @Transactional
    public UsersDto.UsersRegDto updateUsers(UsersDto.UsersRegDto dto) {
        Users user = this.usersRepository.findUserOne(dto.getUserId());

        log.info("=== before update: {}", user.toString());

        user.updateUsersinfo(dto);

        log.info("=== after update: {}", user.toString());

        return UsersMapper.toDto(user);
    }

    // 사용자 - 삭제
    @Transactional
    public void deleteUsers(String userId) {
        Long count = this.usersRepository.deleteUserOne(userId);
        log.info("=== delete user info: {}", count);
    }
}
