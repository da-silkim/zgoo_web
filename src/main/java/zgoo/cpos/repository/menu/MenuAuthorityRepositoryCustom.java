package zgoo.cpos.repository.menu;

import java.util.List;

import com.querydsl.core.Tuple;

public interface MenuAuthorityRepositoryCustom {
    // 사업자 권한 리스트 조회
    List<Tuple> companyAuthorityList();
}
