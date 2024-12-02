package zgoo.cpos.repository.menu;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.menu.Menu;
import zgoo.cpos.domain.menu.QMenu;
import zgoo.cpos.dto.menu.MenuDto.MenuListDto;

@Slf4j
@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QMenu menu = QMenu.menu;
    QMenu child = new QMenu("child");

    @Override
    public String findMenuCode(String menucode, String menulv) {
        return queryFactory
            .select(menu.menuCode)
            .from(menu)
            .where(menu.menuCode.eq(menucode)
                .and(menu.menuLv.eq(menulv)))
            .fetchOne();
    }

    @Override
    public List<MenuListDto> getMuenListWithChildCount() {
        List<MenuListDto> menuList = queryFactory
                .select(Projections.fields(MenuListDto.class,
                            menu.menuCode.as("menuCode"),
                            menu.menuName.as("menuName"),
                            menu.menuUrl.as("menuUrl"),
                            menu.menuLv.as("menuLv"),
                            menu.parentCode.as("parentCode"),
                            menu.iconClass.as("iconClass"),
                            menu.useYn.as("useYn"),
                            menu.regDt.as("regDt"),
                            menu.modDt.as("modDt"),
                            child.menuCode.count().as("childCnt"),
                            new CaseBuilder()
                                    .when(menu.menuLv.eq("0")).then("상위메뉴")
                                    .when(menu.menuLv.eq("1")).then("중위메뉴")
                                    .when(menu.menuLv.eq("2")).then("하위메뉴")
                                    .otherwise("알 수 없음").as("menuLvName")
                        )
                )
                .from(menu)
                .leftJoin(child).on(menu.menuCode.eq(child.parentCode))
                .where(menu.menuLv.in("0", "1"))
                .groupBy(menu.menuCode, menu.menuName)
                .fetch();

        // for (MenuListDto menu : menuList) {
        //     if (menu.getMenuLv().equals("0")) {
        //         menu.setNavMenuClass(menu.getChildCnt() > 0 ? "nav-menu" : "nav-menu list-hover");
        //     } else if (menu.getMenuLv().equals("1")) {
        //         menu.setNavMenuClass("nav-menu");
        //     }
        // }

        return menuList;
    }

    @Override
    public List<Menu> findByMenuLv(String menuLv) {
        return queryFactory
                .selectFrom(menu)
                .where(menu.menuLv.eq(menuLv))
                .fetch();
    }

    @Override
    public Menu findMenuOne(String menucode) {
        return queryFactory
            .selectFrom(menu)
            .where(menu.menuCode.eq(menucode))
            .fetchOne();
    }

    @Transactional
    @Override
    public Long deleteMenuOne(String menucode) {
        return queryFactory
            .delete(menu)
            .where(menu.menuCode.eq(menucode))
            .execute();
    }
}


