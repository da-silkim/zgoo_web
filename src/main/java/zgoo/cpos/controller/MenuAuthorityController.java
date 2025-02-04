package zgoo.cpos.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.menu.MenuAuthorityDto;
import zgoo.cpos.dto.menu.MenuAuthorityDto.MenuAuthorityBaseDto;
import zgoo.cpos.service.MenuAuthorityService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/system/authority")
public class MenuAuthorityController {

    private final MenuAuthorityService menuAuthorityService;

    @GetMapping("/get/{companyId}/{authority}")
    public ResponseEntity<Map<String, Object>> findMenuAuthority(@PathVariable("companyId") Long companyId,
            @PathVariable("authority") String authority) {
        log.info("=== find menu authority info ===");

        Map<String, Object> response = new HashMap<>();

        try {
            List<MenuAuthorityDto.MenuAuthorityListDto> authorityList = this.menuAuthorityService.findMenuAuthorityList(companyId, authority);
            response.put("authorityList", authorityList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[findMenuAuthority] error: {}", e.getMessage());
            response.put("message", "서버 오류가 발생했습니다. 다시 시도해 주세요.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<String> createMenuAuthority(@RequestBody List<MenuAuthorityBaseDto> dto) {
        log.info("=== create menu authority info ===");

        try {
            this.menuAuthorityService.saveMenuAuthorities(dto);
            return ResponseEntity.ok("권한이 정상적으로 저장되었습니다.");
        } catch (Exception e) {
            log.error("[createMenuAuthority] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }
}
