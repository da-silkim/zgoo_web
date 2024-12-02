package zgoo.cpos.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zgoo.cpos.dto.menu.MenuDto;
import zgoo.cpos.service.MenuService;

@RestController
@RequestMapping("/api/nav")
public class NavController {
    private final MenuService menuService;
    
    public NavController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/list")
    public List<MenuDto.MenuListDto> getMenuList() {
        return this.menuService.findMenuListWithChild();
    }
}
