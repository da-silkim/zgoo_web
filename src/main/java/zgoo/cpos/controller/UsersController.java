package zgoo.cpos.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import zgoo.cpos.service.UsersService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/system/user")
public class UsersController {

    private final UsersService usersService;

    // 사용자 등록
    @PostMapping("/new")
    public String createUsers(@RequestBody UsersDto.UsersRegDto dto) {
        log.info("=== create user info ===");
        log.info("사용자 DTO 정보: {}", dto.toString());

        try {
            this.usersService.saveUsers(dto);
        } catch (Exception e) {
            log.error("[사용자 등록] 중 알 수 없는 오류 발생: {}", e.getMessage());
        }

        return "redirect:/system/user/list";
    }

    // userID 중복 검사
    @GetMapping("/checkUserId")
    public ResponseEntity<Boolean> checkUserId(@RequestParam String userId) {
        log.info("=== duplicate check userId ===");
        
        try {
            boolean response = usersService.isUserIdDuplicate(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("userID 중복 검사 중 알 수 없는 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 사용자 - 단건 조회
    @GetMapping("/get/{userId}")
    public ResponseEntity<UsersDto.UsersRegDto> findUserOne(@PathVariable("userId") String userId) {
        log.info("=== find user info ===");

        try {
            UsersDto.UsersRegDto usersDto = this.usersService.findUserOne(userId);

            if ( usersDto != null ) {
                return ResponseEntity.ok(usersDto);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 사용자 - 검색
    @GetMapping("/search")
    public ResponseEntity<List<UsersDto.UsersListDto>> searchUsers(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String companyType,
            @RequestParam(required = false) String name) {

        log.info("=== search user info ===");
        
        if (companyType != null && companyType.trim().isEmpty()) {
            companyType = null;
        }

        if (name != null && name.trim().isEmpty()) {
            name = null;
        }

        log.info("companyId: {}, companyType: {}, name: {}", companyId, companyType, name);

        try {
            List<UsersDto.UsersListDto> uList = this.usersService.searchUsersList(companyId, companyType, name);
            log.info("조회된 사용자 리스트 >> {}", uList);
            return ResponseEntity.ok(uList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); 
        }
    }
    
    // 사용자 수정
    @PatchMapping("/update")
    public ResponseEntity<Map<String, Object>> updateUsers(@RequestBody UsersDto.UsersRegDto dto) {

        Map<String, Object> response = new HashMap<>();

        log.info("update user info: {}", dto.toString());

        try {
            this.usersService.updateUsers(dto);
            response.put("message", "사용자 정보 수정 성공");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[사용자 수정] 중 알 수 없는 오류 발생: {}", e.getMessage());
            response.put("message", "사용자 정보 수정 실패");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 사용자 삭제
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUsers(@PathVariable("userId") String userId) {
        log.info("=== delete user info ===");

        try {
            this.usersService.deleteUsers(userId);
            return ResponseEntity.ok("사용자 정보 삭제 성공");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 사용자 정보를 찾을 수 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사용자 정보 삭제 중 오류 발생");
        }
    }
}
