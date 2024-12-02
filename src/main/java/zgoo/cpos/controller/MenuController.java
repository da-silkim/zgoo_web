package zgoo.cpos.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.dto.menu.MenuDto;
import zgoo.cpos.service.MenuService;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/system/menu")
public class MenuController {
    private final MenuService menuService;
    
    // 메뉴 등록
    @PostMapping("/new")
    public ResponseEntity<String> createMenu(@RequestBody MenuDto.MenuRegDto dto) {
        log.info("=== create menu info ===");
        
        try {
            this.menuService.saveMenu(dto);
            return ResponseEntity.ok("메뉴가 정상적으로 등록되었습니다.");
        } catch (Exception e) {
            log.error("[메뉴 등록] 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("메뉴 등록 중 오류 발생");
        }
    }

    @GetMapping("/parent/{menuLv}")
    @ResponseBody
    public List<MenuDto.MenuRegDto> getParentMenu(@PathVariable("menuLv") String menuLv) {
        log.info("=== Fetching parent menu for menuLv: {} ===", menuLv);
        return this.menuService.getParentMenuByMenuLv(menuLv);
    }

    // 메뉴 - 단건 조회
    @GetMapping("/get/{menuCode}")
    public ResponseEntity<MenuDto.MenuRegDto> findMenuOne(@PathVariable("menuCode") String menuCode) {
        log.info("=== find menu info ===");

        try {
            MenuDto.MenuRegDto menuFindOne = this.menuService.findMenuOne(menuCode);

            if ( menuFindOne != null) {
                return ResponseEntity.ok(menuFindOne);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            
        } catch (Exception e) {
            log.error("[메뉴 단건 조회] 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 메뉴 수정
    @PatchMapping("/update")
    public ResponseEntity<String> updateMenu(@RequestBody MenuDto.MenuRegDto dto) {
        log.info("=== update menu info ===");

        try {
            this.menuService.updateMenu(dto);
            return ResponseEntity.ok("메뉴가 정상적으로 수정되었습니다.");
        } catch (Exception e) {
            log.error("[메뉴 수정] 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                  .body("메뉴 수정 중 오류 발생");
        }
    }

    // 메뉴 삭제
    @DeleteMapping("/delete/{menuCode}")
    public ResponseEntity<String> deleteMenu(@PathVariable("menuCode") String menuCode) {
        log.info("=== delete menu info ===");

        try {
            this.menuService.deleteMenu(menuCode);
            return ResponseEntity.ok("메뉴가 정상적으로 삭제되었습니다.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("메뉴 정보를 찾을 수 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메뉴 삭제 중 오류 발생");
        }
    }
}
