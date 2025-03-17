package zgoo.cpos.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.company.Company;
import zgoo.cpos.domain.users.Users;
import zgoo.cpos.dto.code.CodeDto.CommCdBaseDto;
import zgoo.cpos.dto.users.UsersDto;
import zgoo.cpos.dto.users.UsersDto.UsersListDto;
import zgoo.cpos.dto.users.UsersDto.UsersPasswordDto;
import zgoo.cpos.mapper.UsersMapper;
import zgoo.cpos.repository.code.CommonCodeRepository;
import zgoo.cpos.repository.company.CompanyRepository;
import zgoo.cpos.repository.users.UsersRepository;
import zgoo.cpos.util.EncryptionUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersService {

    private final CompanyRepository companyRepository;
    private final UsersRepository usersRepository;
    private final CommonCodeRepository commonCodeRepository;

    // 사용자 - 전체 조회
    @Transactional
    public List<UsersDto.UsersListDto> findUsersAll() {
        List<Users> usersList = this.usersRepository.findAll();

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
            Page<UsersDto.UsersListDto> usersList = this.usersRepository.findUsersWithPaginationToDto(pageable);
            return usersList;
        } catch (DataAccessException dae) {
            log.error("Database error occurred while fetching users with pagination: {}", dae.getMessage(), dae);
            return Page.empty(pageable);
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
            Page<UsersDto.UsersListDto> usersList = this.usersRepository.searchUsersWithPaginationToDto(companyId, companyType, name, pageable);
            return usersList;
        } catch (DataAccessException dae) {
            log.error("Database error occurred while fetching users with pagination: {}", dae.getMessage(), dae);
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching users with pagination: {}", e.getMessage(), e);
            return Page.empty(pageable);
        }
    }

    // 사용자 조회
    public Page<UsersListDto> findUsersWithPagination(Long companyId, String companyType, String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<UsersListDto> usersList;

            if (companyId == null && (companyType == null || companyType.isEmpty()) && (name == null || name.isEmpty())) {
                log.info("Executing the [findUsersWithPaginationToDto]");
                usersList = this.usersRepository.findUsersWithPaginationToDto(pageable);
            } else {
                log.info("Executing the [searchUsersWithPaginationToDto]");
                usersList = this.usersRepository.searchUsersWithPaginationToDto(companyId, companyType, name, pageable);
            }

            return usersList;
        } catch (Exception e) {
            log.error("[findUsersWithPagination] error: {}", e.getMessage());
            return Page.empty(pageable);
        }
    }

    public Long findCompanyId(String userId) {
        try {
            Long companyId = this.usersRepository.findCompanyId(userId);
            
            if (companyId != null) {
                return companyId;
            } else {
                log.error("[findCompanyId] companyId is null: {}", userId);
                return 0L;
            }
        } catch (Exception e) {
            log.error("[findCompanyId] error: {}", e.getMessage());
            return 0L;
        }
    }

    // 비밀번호 변경
    @Transactional
    public Integer updateUsersPasswordInfo(String userId, UsersPasswordDto dto) {
        Users user = this.usersRepository.findUserOne(userId);

        try {
            if (user == null) {
                return -1;
            }

            dto.setExistPassword(EncryptionUtils.encryptSHA256(dto.getExistPassword()));

            // 1. 현재 비밀번호 일치여부
            if (!user.getPassword().equals(dto.getExistPassword())) {
                log.info("[updateUsersPasswordInfo] doesn't match the current password");
                return 0;
            }
            // 2. 새 비밀번호 == 새 비밀번호 확인 값이 같은지 체크
            if (!dto.getNewPassword().equals(dto.getNewPasswordCheck())) {
                log.info("[updateUsersPasswordInfo] two password values do not match");  
                return 2;
            }
            // password SHA-256
            dto.setNewPassword(EncryptionUtils.encryptSHA256(dto.getNewPassword()));
            user.updatePasswordInfo(dto.getNewPassword());
            log.info("[updateUsersPasswordInfo] password change complete");
            return 1;
        } catch (Exception e) {
            log.error("[updateUsersPasswordInfo] error: {}", e.getMessage());
            return null;
        }
    }

    // 사용자 권한에 따른 메뉴권한리스트
    public List<CommCdBaseDto> searchMenuAccessList(String userId) {
        try {
            Users user = this.usersRepository.findUserOne(userId);
            String auth = user.getAuthority();
            return this.commonCodeRepository.commonCodeUsersAuthority(auth);
        } catch (Exception e) {
            log.error("[searchMenuAccessList] error: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
}
