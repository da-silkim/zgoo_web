package zgoo.cpos.repository.menu;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.company.QCompany;
import zgoo.cpos.domain.menu.CompanyMenuAuthority;
import zgoo.cpos.domain.menu.QCompanyMenuAuthority;
import zgoo.cpos.domain.menu.QMenu;
import zgoo.cpos.dto.menu.CompanyMenuAuthorityDto;
import zgoo.cpos.dto.menu.CompanyMenuAuthorityDto.CompanyMenuAuthorityListDto;
import zgoo.cpos.dto.menu.CompanyMenuAuthorityDto.CompanyMenuRegDto;

@Slf4j
@RequiredArgsConstructor
public class CompanyMenuAuthorityRepositoryCustomImpl implements CompanyMenuAuthorityRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QCompany company = QCompany.company;
    QMenu menu = QMenu.menu;
    QMenu parentMenu = new QMenu("parentMenu");
    QMenu childMenu = new QMenu("childMenu");
    QCompanyMenuAuthority companyMenuAuthority = QCompanyMenuAuthority.companyMenuAuthority;
    QCompanyMenuAuthority childCma = new QCompanyMenuAuthority("childCma");

    @Override
    public List<Long> findDistinctCompanyIds() {
        return queryFactory
                .select(companyMenuAuthority.companyId)
                .distinct()
                .from(companyMenuAuthority)
                .fetch();
    }

    @Override
    public List<CompanyMenuAuthorityDto.CompanyMenuRegDto> findDistinctCompanyWithCompanyName() {
        List<CompanyMenuAuthorityDto.CompanyMenuRegDto> companyList = queryFactory.select(Projections.fields(CompanyMenuAuthorityDto.CompanyMenuRegDto.class,
                companyMenuAuthority.companyId.as("companyId"),
                company.companyName.as("companyName"))).distinct()
                .from(companyMenuAuthority)
                .leftJoin(company).on(companyMenuAuthority.companyId.eq(company.id))
                .fetch();

        return companyList;
    }

    @Override
    public Page<CompanyMenuRegDto> findCompanyMenuWithPagination(Pageable pageable) {
        List<CompanyMenuAuthorityDto.CompanyMenuRegDto> companyList = queryFactory.select(Projections.fields(CompanyMenuAuthorityDto.CompanyMenuRegDto.class,
                companyMenuAuthority.companyId.as("companyId"),
                company.companyName.as("companyName")))
                .from(companyMenuAuthority)
                .leftJoin(company).on(companyMenuAuthority.companyId.eq(company.id))
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(companyMenuAuthority.companyId.countDistinct())
                .from(companyMenuAuthority)
                .fetchOne();

        return new PageImpl<>(companyList, pageable, totalCount);
    }

    @Override
    public Page<CompanyMenuRegDto> searchCompanyMenuWithPagination(String companyName, Pageable pageable) {
        List<CompanyMenuAuthorityDto.CompanyMenuRegDto> companyList = queryFactory.select(Projections.fields(CompanyMenuAuthorityDto.CompanyMenuRegDto.class,
                companyMenuAuthority.companyId.as("companyId"),
                company.companyName.as("companyName")))
                .from(companyMenuAuthority)
                .leftJoin(company).on(companyMenuAuthority.companyId.eq(company.id))
                .where(company.companyName.like("%" + companyName + "%"))
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(companyMenuAuthority.companyId.countDistinct())
                .from(companyMenuAuthority)
                .leftJoin(company).on(companyMenuAuthority.companyId.eq(company.id)) 
                .where(company.companyName.like("%" + companyName + "%"))
                .fetchOne();

        return new PageImpl<>(companyList, pageable, totalCount);
    }

    @Override
    public List<CompanyMenuAuthorityListDto> findCompanyMenuAuthorityList(Long companyId) {            
        List<CompanyMenuAuthorityListDto> companyList = queryFactory
                .select(Projections.fields(CompanyMenuAuthorityListDto.class,
                    companyMenuAuthority.companyId.as("companyId"),
                    companyMenuAuthority.menuCode.as("menuCode"),
                    companyMenuAuthority.useYn.as("useYn"),
                    menu.iconClass.as("iconClass"),
                    menu.menuLv.as("menuLv"),
                    menu.menuUrl.as("menuUrl"),
                    menu.parentCode.as("parentCode"),
                    menu.menuName.as("menuName"),
                    parentMenu.menuName.as("parentCodeName")))
                .from(companyMenuAuthority)
                .leftJoin(menu).on(companyMenuAuthority.menuCode.eq(menu.menuCode))
                .leftJoin(parentMenu).on(parentMenu.menuCode.eq(menu.parentCode))
                .where(companyMenuAuthority.companyId.eq(companyId))
                .orderBy(companyMenuAuthority.companyId.asc(), companyMenuAuthority.menuCode.asc())
                .fetch();

        for (CompanyMenuAuthorityListDto dto : companyList) {
            // 자식 메뉴 수를 계산하는 서브쿼리
            Long childCount = queryFactory
                .select(childMenu.count())
                .from(childMenu)
                .join(childCma).on(childCma.menuCode.eq(childMenu.menuCode))
                .where(childMenu.parentCode.eq(dto.getMenuCode())     // 현재 메뉴의 menuCode와 자식 메뉴의 parentCode 비교
                    .and(childCma.useYn.eq("Y"))                // 자식 메뉴의 use_yn이 'Y'인 경우만 계산
                    .and(childCma.companyId.eq(dto.getCompanyId())))  // 동일한 companyId의 자식 메뉴만 카운트
                .fetchOne();
        
            dto.setChildCnt(childCount != null ? childCount : 0);     // 자식 메뉴 카운트를 DTO에 설정
        }

        return companyList;
    }

    @Override
    public void companyMenuAuthorityUseYnUpdate(CompanyMenuAuthority cma) {
        String parentCode = getParentCode(cma.getMenuCode());
        if (parentCode != null) {
            // menuLv 1 이상
            updateParentMenuUseYn(parentCode, cma);
        } else {
            return;
        }

        // menuLv 2
        parentCode = getParentCode(parentCode);
        if (parentCode != null) {
            updateParentMenuUseYn(parentCode, cma);
        }
    }

    @Override
    public String getParentCode(String menuCode) {
        return queryFactory
            .select(menu.parentCode)
            .from(menu)
            .where(menu.menuCode.eq(menuCode))
            .fetchOne();
    }

    @Override
    public void updateParentMenuUseYn(String parentCode, CompanyMenuAuthority cma) {
        Long childMenuCount = queryFactory
            .select(companyMenuAuthority.count())
            .from(companyMenuAuthority)
            .leftJoin(menu).on(companyMenuAuthority.menuCode.eq(menu.menuCode))
            .where(menu.parentCode.eq(parentCode)
                    .and(companyMenuAuthority.companyId.eq(cma.getCompanyId()))  // 동일한 companyId 내에서 조회
                    .and(companyMenuAuthority.useYn.eq("Y")))              // 자식 메뉴 중 useYn이 'Y'인 것만 카운트
            .fetchOne();

        // 자식 메뉴가 없으면 부모 메뉴의 useYn을 "N"으로 변경
        String newUseYn = (childMenuCount == null || childMenuCount == 0) ? "N" : "Y";

        // 부모 메뉴의 useYn 업데이트
        queryFactory
            .update(companyMenuAuthority)
            .set(companyMenuAuthority.useYn, newUseYn)
            .where(companyMenuAuthority.menuCode.eq(parentCode)
                    .and(companyMenuAuthority.companyId.eq(cma.getCompanyId())))
            .execute();
    }

    @Override
    public Long companyMenuAuthorityRegCheck(Long companyId) {
        return queryFactory
            .selectFrom(companyMenuAuthority)
            .where(companyMenuAuthority.companyId.eq(companyId))
            .fetchCount();
    }

    @Override
    public CompanyMenuAuthority findCompanyMenuAuthorityOne(Long companyId, String menuCode) {
        return queryFactory
            .selectFrom(companyMenuAuthority)
            .where(companyMenuAuthority.companyId.eq(companyId)
                .and(companyMenuAuthority.menuCode.eq(menuCode)))
            .fetchOne();

    }

    @Override
    public Long deleteCompanyMenuAuthorityOne(Long companyId) {
        return queryFactory
            .delete(companyMenuAuthority)
            .where(companyMenuAuthority.companyId.eq(companyId))
            .execute();
    }

    @Override
    public Long deleteCompanyMenuAuthorityMenuCodeAll(String menuCode) {
        return queryFactory
            .delete(companyMenuAuthority)
            .where(companyMenuAuthority.menuCode.eq(menuCode))
            .execute();
    }
}
