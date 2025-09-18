package zgoo.cpos.repository.menu;

import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.menu.Menu;
import zgoo.cpos.domain.menu.QMenu;
import zgoo.cpos.dto.menu.MenuDto.MenuAuthorityListDto;
import zgoo.cpos.dto.menu.MenuDto.MenuListDto;
import zgoo.cpos.util.LocaleUtil;

@Slf4j
@RequiredArgsConstructor
public class MenuRepositoryCustomImpl implements MenuRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QMenu menu = QMenu.menu;
    QMenu child = new QMenu("child");
    QMenu parent = new QMenu("parent");

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
    public List<Menu> findByMenuCodeIn(Set<String> menuCodes) {
        return queryFactory
                .selectFrom(menu)
                .where(menu.menuCode.in(menuCodes)) // menuCode가 menuCodes에 포함되는 메뉴들
                .fetch();
    }

    @Override
    public List<Menu> findByParentCode(String parentCode) {
        return queryFactory
                .selectFrom(menu)
                .where(menu.parentCode.eq(parentCode)) // 부모 코드와 일치하는 메뉴들
                .fetch();
    }

    @Override
    public List<MenuListDto> getMuenListWithChildCount() {
        List<MenuListDto> menuList = queryFactory
                .select(Projections.fields(MenuListDto.class,
                        menu.menuCode.as("menuCode"),
                        LocaleUtil.isEnglish() ? menu.menuNameEn.as("menuName") : menu.menuName.as("menuName"),
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
                                .otherwise("알 수 없음").as("menuLvName")))
                .from(menu)
                .leftJoin(child).on(menu.menuCode.eq(child.parentCode))
                .where(menu.menuLv.in("0", "1", "2"))
                .groupBy(menu.menuCode)
                .fetch();

        return menuList;
    }

    @Override
    public List<MenuAuthorityListDto> findMenuListWithParentName() {
        List<MenuAuthorityListDto> menuList = queryFactory
                .select(Projections.fields(MenuAuthorityListDto.class,
                        menu.menuCode.as("menuCode"),
                        LocaleUtil.isEnglish() ? menu.menuNameEn.as("menuName") : menu.menuName.as("menuName"),
                        menu.menuUrl.as("menuUrl"),
                        menu.menuLv.as("menuLv"),
                        menu.parentCode.as("parentCode"),
                        menu.iconClass.as("iconClass"),
                        menu.useYn.as("useYn"),
                        LocaleUtil.isEnglish() ? parent.menuNameEn.as("parentCodeName")
                                : parent.menuName.as("parentCodeName")))
                .from(menu)
                .leftJoin(parent).on(parent.menuCode.eq(menu.parentCode))
                .fetch();
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
