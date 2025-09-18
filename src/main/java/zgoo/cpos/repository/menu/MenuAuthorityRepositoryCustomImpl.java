package zgoo.cpos.repository.menu;

import java.util.List;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.code.QCommonCode;
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.menu.MenuAuthority;
import zgoo.cpos.domain.menu.QCompanyMenuAuthority;
import zgoo.cpos.domain.menu.QMenu;
import zgoo.cpos.domain.menu.QMenuAuthority;
import zgoo.cpos.dto.menu.MenuAuthorityDto.MenuAuthorityBaseDto;
import zgoo.cpos.dto.menu.MenuAuthorityDto.MenuAuthorityListDto;
import zgoo.cpos.util.LocaleUtil;

@Slf4j
@RequiredArgsConstructor
public class MenuAuthorityRepositoryCustomImpl implements MenuAuthorityRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QCompany company = QCompany.company;
    QCommonCode commonCode = QCommonCode.commonCode1;
    QMenuAuthority menuAuthority = QMenuAuthority.menuAuthority;
    QCompanyMenuAuthority comMenuAuthority = QCompanyMenuAuthority.companyMenuAuthority;
    QMenu menu = QMenu.menu;
    QMenu parent = new QMenu("parent");

    @Override
    public List<Tuple> companyAuthorityList() {
        return queryFactory
                .select(company.id, company.companyName, commonCode.commonCode, commonCode.name)
                .from(company, commonCode)
                .where(commonCode.group.grpCode.eq("MENUACCLV")
                        .and(commonCode.commonCode.ne("SU")))
                .orderBy(company.id.asc(), commonCode.commonCode.asc())
                .fetch();
    }

    @Override
    public List<MenuAuthorityListDto> findMenuAuthorityList(Long companyId, String authority) {
        List<MenuAuthorityListDto> authorityList = queryFactory
                .select(Projections.fields(MenuAuthorityListDto.class,
                        menuAuthority.id.as("menuAuthortyId"),
                        menuAuthority.company.id.as("companyId"),
                        menuAuthority.menu.menuCode.as("menuCode"),
                        LocaleUtil.isEnglish() ? menuAuthority.menu.menuNameEn.as("menuName")
                                : menuAuthority.menu.menuName.as("menuName"),
                        menuAuthority.menu.menuUrl.as("menuUrl"),
                        menuAuthority.menu.menuLv.as("menuLv"),
                        menuAuthority.authority.as("authority"),
                        menuAuthority.modYn.as("modYn"),
                        menuAuthority.readYn.as("readYn"),
                        menuAuthority.excelYn.as("excelYn"),
                        menuAuthority.regAt.as("regAt"),
                        menuAuthority.modAt.as("modAt"),
                        menu.parentCode.as("parentCode"),
                        parent.menuName.as("parentCodeName"),
                        comMenuAuthority.useYn.as("useYn")))
                .from(comMenuAuthority)
                .leftJoin(menu).on(menu.menuCode.eq(comMenuAuthority.menu.menuCode))
                .leftJoin(parent).on(parent.menuCode.eq(menu.parentCode))
                .leftJoin(menuAuthority).on(
                        comMenuAuthority.menu.menuCode.eq(menuAuthority.menu.menuCode)
                                .and(menuAuthority.company.id.eq(comMenuAuthority.companyId)))
                .where(comMenuAuthority.companyId.eq(companyId)
                        .and(menuAuthority.authority.eq(authority))
                        .and(comMenuAuthority.useYn.eq("Y")))
                .fetch();

        // authorityList.forEach(dto -> {
        // log.info("menuName: {}, useYn: {}", dto.getMenuName(), dto.getUseYn());
        // });

        return authorityList;
    }

    @Override
    public List<MenuAuthorityListDto> defaultMenuAuthorityList(Long companyId, String authority) {
        String modYn = authority.equals("NO") ? "N" : "Y";

        List<MenuAuthorityListDto> authorityList = queryFactory
                .select(Projections.fields(MenuAuthorityListDto.class,
                        comMenuAuthority.companyId.as("companyId"),
                        menu.menuCode.as("menuCode"),
                        LocaleUtil.isEnglish() ? menu.menuNameEn.as("menuName") : menu.menuName.as("menuName"),
                        menu.menuUrl.as("menuUrl"),
                        menu.menuLv.as("menuLv"),
                        menu.parentCode.as("parentCode"),
                        parent.menuName.as("parentCodeName"),
                        comMenuAuthority.useYn.as("useYn")))
                .from(comMenuAuthority)
                .leftJoin(menu).on(menu.menuCode.eq(comMenuAuthority.menu.menuCode))
                .leftJoin(parent).on(parent.menuCode.eq(menu.parentCode))
                .leftJoin(menuAuthority).on(
                        comMenuAuthority.menu.menuCode.eq(menuAuthority.menu.menuCode)
                                .and(menuAuthority.company.id.eq(comMenuAuthority.companyId)))
                .where(comMenuAuthority.companyId.eq(companyId)
                        .and(comMenuAuthority.useYn.eq("Y")))
                .fetch();

        authorityList.forEach(dto -> {
            dto.setModYn(modYn);
            dto.setReadYn("Y");
            dto.setExcelYn("Y");
        });

        return authorityList;
    }

    @Override
    public Long menuAuthorityRegCheck(Long companyId, String authority) {
        return queryFactory
                .select(menuAuthority.count())
                .from(menuAuthority)
                .where(menuAuthority.company.id.eq(companyId)
                        .and(menuAuthority.authority.eq(authority)))
                .fetchOne();
    }

    @Override
    public MenuAuthority findMenuAuthorityOne(Long companyId, String authority, String menuCode) {
        return queryFactory
                .selectFrom(menuAuthority)
                .where(menuAuthority.company.id.eq(companyId)
                        .and(menuAuthority.menu.menuCode.eq(menuCode))
                        .and(menuAuthority.authority.eq(authority)))
                .fetchOne();
    }

    @Override
    public MenuAuthorityBaseDto findUserMenuAuthority(Long companyId, String authority, String menuCode) {
        MenuAuthorityBaseDto dto = queryFactory
                .select(Projections.fields(MenuAuthorityBaseDto.class,
                        menuAuthority.authority.as("authority"),
                        menuAuthority.menu.menuCode.as("menuCode"),
                        menuAuthority.readYn.as("readYn"),
                        menuAuthority.modYn.as("modYn"),
                        menuAuthority.excelYn.as("excelYn"),
                        menuAuthority.company.id.as("companyId"),
                        menuAuthority.company.companyName.as("companyName")))
                .from(menuAuthority)
                .leftJoin(company).on(company.id.eq(menuAuthority.company.id))
                .where(company.id.eq(companyId)
                        .and(menuAuthority.menu.menuCode.eq(menuCode))
                        .and(menuAuthority.authority.eq(authority)))
                .fetchOne();
        return dto;
    }
}
