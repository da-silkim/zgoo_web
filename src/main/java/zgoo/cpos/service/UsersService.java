package zgoo.cpos.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import zgoo.cpos.util.EncryptionUtils;

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

        List<UsersDto.UsersListDto> userListDto = UsersMapper.toDtoList(usersList);
        log.info("=== users DB search success ===");

        return userListDto;
    }

    // 사용자 - 단건 조회
    public UsersDto.UsersRegDto findUserOne(String userId) {
        Users user = this.usersRepository.findUserOne(userId);
        return UsersMapper.toDto(user);
    }

    // 사용자 - 검색
    @Transactional
    public List<UsersDto.UsersListDto> searchUsersList(Long companyId, String companyType, String name) {
        log.info("=== search user info ===");

        List<Users> usersList = this.usersRepository.searchUsers(companyId, companyType, name);
        log.info("search data >>> {}", usersList);

        if(usersList.isEmpty()) {
            log.info("=== no users found ===");
            return new ArrayList<>();   // empty list
        }
        
        try {
            List<UsersDto.UsersListDto> userListDto = UsersMapper.toDtoList(usersList);
            log.info("=== users DB search success ===");
            log.info("entity >> dto user info >>> {}", userListDto);
            return userListDto;
        } catch (Exception e) {
            log.info("=== users DB search failure ===", e);
            return new ArrayList<>();   // empty list
        }
    }

    // 사용자 - 등록
    @Transactional
    public void saveUsers(UsersDto.UsersRegDto dto) {
        Company company = this.companyRepository.findById(dto.getCompanyId())
            .orElseThrow(() -> new IllegalArgumentException("=== company not found ==="));

        // password SHA-256
        dto.setPassword(EncryptionUtils.encryptSHA256(dto.getPassword()));

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

        // password SHA-256
        dto.setPassword(EncryptionUtils.encryptSHA256(dto.getPassword()));
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

    // 사용자 - 전체 조회 페이징
    @Transactional
    public Page<UsersDto.UsersListDto> findUsersAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        
        try {
            Page<Users> usersPage = this.usersRepository.findUsersWithPagination(pageable);

            // Users -> UsersListDto로 변환
            List<UsersDto.UsersListDto> usersListDto = usersPage.getContent().stream()
                    .map(user -> UsersMapper.toListDto(user))  // Users -> UsersListDto 변환
                    .collect(Collectors.toList());

            log.info("=== paging user info >> {}", usersListDto.toString());

            return new PageImpl<>(usersListDto, pageable, usersPage.getTotalElements());
        } catch (Exception e) {
            log.error("Error occurred while fetching users with pagination: {}", e.getMessage(), e);
            return Page.empty(pageable);
        }
    }

    // 사용자 - 검색 페이징
    @Transactional
    public Page<UsersDto.UsersListDto> searchUsersListWithPagination(Long companyId, String companyType, String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        
        try {
            Page<Users> usersPage = this.usersRepository.searchUsersWithPagination(companyId, companyType, name, pageable);

            // Users -> UsersListDto로 변환
            List<UsersDto.UsersListDto> usersListDto = usersPage.getContent().stream()
                    .map(user -> UsersMapper.toListDto(user))  // Users -> UsersListDto 변환
                    .collect(Collectors.toList());

            log.info("=== paging user info >> {}", usersListDto.toString());

            return new PageImpl<>(usersListDto, pageable, usersPage.getTotalElements());
        } catch (Exception e) {
            log.error("Error occurred while fetching users with pagination: {}", e.getMessage(), e);
            return Page.empty(pageable);
        }
    }
}
