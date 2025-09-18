package zgoo.cpos.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.users.UsersDto;
import zgoo.cpos.dto.users.UsersDto.UsersPasswordDto;
import zgoo.cpos.service.UsersService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/system/user")
public class UsersController {

    private final UsersService usersService;
    private final MessageSource messageSource;

    // 사용자 등록
    @PostMapping("/new")
    public ResponseEntity<String> createUsers(@RequestBody UsersDto.UsersRegDto dto, Principal principal) {
        log.info("=== create user info ===");

        try {
            this.usersService.saveUsers(dto, principal.getName());
            return ResponseEntity.ok(messageSource.getMessage("userManagement.api.registerSuccess", null,
                    LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            log.error("[createUsers] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("userManagement.api.registerError", null,
                            LocaleContextHolder.getLocale()));
        }
    }

    // userID 중복 검사
    @GetMapping("/checkUserId")
    public ResponseEntity<Boolean> checkUserId(@RequestParam("userId") String userId) {
        log.info("=== duplicate check userId ===");

        try {
            boolean response = usersService.isUserIdDuplicate(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[checkUserId] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 사용자 - 단건 조회
    @GetMapping("/get/{userId}")
    public ResponseEntity<UsersDto.UsersRegDto> findUserOne(@PathVariable("userId") String userId) {
        log.info("=== find user info ===");

        try {
            UsersDto.UsersRegDto userFindOne = this.usersService.findUserOne(userId);

            if (userFindOne != null) {
                return ResponseEntity.ok(userFindOne);
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("[findUserOne] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 사용자 - 검색
    @GetMapping("/search")
    public ResponseEntity<List<UsersDto.UsersListDto>> searchUsers(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String companyType,
            @RequestParam(required = false) String name, Principal principal) {
        log.info("=== search user info ===");

        if (companyType != null && companyType.isEmpty()) {
            companyType = null;
        }

        if (name != null && name.trim().isEmpty()) {
            name = null;
        }

        log.info("companyId: {}, companyType: {}, name: {}", companyId, companyType, name);

        try {
            List<UsersDto.UsersListDto> usersList = this.usersService.searchUsersList(companyId, companyType, name,
                    principal.getName());
            log.info("조회된 사용자 리스트 >> {}", usersList);
            return ResponseEntity.ok(usersList);
        } catch (Exception e) {
            log.error("[searchUsers] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 사용자 수정
    @PatchMapping("/update")
    public ResponseEntity<String> updateUsers(@RequestBody UsersDto.UsersRegDto dto, Principal principal) {
        log.info("update user info: {}", dto.toString());

        try {
            this.usersService.updateUsers(dto, principal.getName());
            return ResponseEntity.ok(messageSource.getMessage("userManagement.api.updateSuccess", null,
                    LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            log.error("[updateUsers] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("userManagement.api.updateError", null,
                            LocaleContextHolder.getLocale()));
        }
    }

    // 사용자 삭제
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUsers(@PathVariable("userId") String userId, Principal principal) {
        log.info("=== delete user info ===");

        try {
            this.usersService.deleteUsers(userId, principal.getName());
            return ResponseEntity.ok(messageSource.getMessage("userManagement.api.deleteSuccess", null,
                    LocaleContextHolder.getLocale()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    messageSource.getMessage("userManagement.api.userNotFound", null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("userManagement.api.deleteError", null,
                            LocaleContextHolder.getLocale()));
        }
    }

    // 비밀번호 변경
    @PatchMapping("/update/password/{userId}")
    public ResponseEntity<Map<String, Object>> updateUsersPassword(@PathVariable("userId") String userId,
            @RequestBody UsersPasswordDto dto) {
        log.info("=== update user password info ===");

        Map<String, Object> response = new HashMap<>();

        try {
            Integer result = this.usersService.updateUsersPasswordInfo(userId, dto);

            switch (result) {
                case 0 -> response.put("message", messageSource.getMessage("userManagement.api.passwordMismatch", null,
                        LocaleContextHolder.getLocale()));
                case 1 -> response.put("message", messageSource.getMessage("userManagement.api.passwordChanged", null,
                        LocaleContextHolder.getLocale()));
                case 2 -> response.put("message", messageSource.getMessage("userManagement.api.newPasswordMismatch",
                        null, LocaleContextHolder.getLocale()));
                default -> response.put("message", messageSource.getMessage("userManagement.api.passwordChangeFailed",
                        null, LocaleContextHolder.getLocale()));
            }

            response.put("state", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[updateUsersPassword] error: {}", e.getMessage());
            response.put("message", messageSource.getMessage("userManagement.api.passwordChangeError", null,
                    LocaleContextHolder.getLocale()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/btncontrol/{userId}")
    public ResponseEntity<Map<String, Object>> buttonControl(@PathVariable("userId") String userId,
            Principal principal) {
        log.info("=== update & delete button authority info ===");

        Map<String, Object> response = new HashMap<>();

        try {
            boolean btnControl = this.usersService.buttonControl(userId, principal.getName());
            System.out.println("btnControl >> " + btnControl);
            response.put("btnControl", btnControl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[UsersController >> buttonControl] error: {}", e.getMessage());
            response.put("message", messageSource.getMessage("userManagement.api.buttonAuthError", null,
                    LocaleContextHolder.getLocale()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
