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
import zgoo.cpos.dto.menu.CompanyMenuAuthorityDto;
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
            log.error("[createMenu] error: {}", e.getMessage());
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
            log.error("[findMenuOne] error: {}", e.getMessage());
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
            log.error("[updateMenu] error: {}", e.getMessage());
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

    // 메뉴권한 모달창에 보여줄 메뉴 조회(초기 메뉴 세팅)
    @GetMapping("/company")
    @ResponseBody
    public List<MenuDto.MenuAuthorityListDto> getCompanyMenuList() {
        return this.menuService.findMenuListWithParentName();
    }

    // 사업자별 메뉴 권한 저장
    @PostMapping("/company/new")
    public ResponseEntity<String> createCompanuMenuAuthority(@RequestBody List<CompanyMenuAuthorityDto.CompanyMenuAuthorityBaseDto> dto) {
        log.info("=== create company menu info ===");
        
        try {
            int result = this.menuService.saveCompanyMenuAuthorities(dto);

            return switch (result) {
                case 0 -> ResponseEntity.status(HttpStatus.CONFLICT).body("이미 등록된 데이터입니다.");
                case -1-> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("DTO가 비어 있습니다.");
                case 1 -> ResponseEntity.status(HttpStatus.OK).body("정상적으로 등록되었습니다.");
                default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다.");
            };

        } catch (Exception e) {
            log.error("[createCompanuMenuAuthority] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    // 사업자별 메뉴 권한 - 단건 조회
    @GetMapping("/company/get/{companyId}")
    @ResponseBody
    public List<CompanyMenuAuthorityDto.CompanyMenuAuthorityListDto> findCompanyMenuOne(@PathVariable("companyId") Long companyId) {
        return this.menuService.findCompanyMenuAuthorityList(companyId);
    }

    // 사업자별 메뉴 권한 수정
    @PatchMapping("/company/update")
    public ResponseEntity<String> updateCompanyMenuAuthority(@RequestBody List<CompanyMenuAuthorityDto.CompanyMenuAuthorityBaseDto> dto) {
        log.info("=== update company menu info ===");

        try {
            this.menuService.updateCompanyMenuAuthority(dto);
            return ResponseEntity.ok("메뉴 권한이 정상적으로 수정되었습니다.");
        } catch (Exception e) {
            log.error("[updateCompanyMenu] error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                  .body("사업장별 메뉴 권한 수정 중 오류 발생");
        }
    }

    // 사업장별 메뉴 권한 삭제
    @DeleteMapping("/company/delete/{companyId}")
    public ResponseEntity<String> deleteCompanyMenuAuthority(@PathVariable("companyId") Long companyId) {
        log.info("=== delete company menu info ===");

        try {
            this.menuService.deleteCompanyMenuAuthority(companyId);
            return ResponseEntity.ok("메뉴 권한이 정상적으로 삭제되었습니다.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("메뉴 권한 정보를 찾을 수 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메뉴 권한 삭제 중 오류 발생");
        }
    }
}
