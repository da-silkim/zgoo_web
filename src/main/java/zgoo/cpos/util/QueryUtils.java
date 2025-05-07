package zgoo.cpos.util;

import com.querydsl.core.types.dsl.BooleanExpression;

import zgoo.cpos.domain.company.QCompanyRelationInfo;

public class QueryUtils {

    /**
     * 회사 레벨 접근 권한 조회
     * 
     * @param relation  회사관계 정보 엔티티
     * @param levelPath 현재 사용자의 관계 레벨 경로
     * @return 레벨 경로 조건 표현식
     */
    public static BooleanExpression hasCompanyLevelAccess(QCompanyRelationInfo relation, String levelPath) {
        if (levelPath == null || levelPath.isEmpty()) {
            return null;
        }
        return relation.levelPath.eq(levelPath).or(relation.levelPath.startsWith(levelPath + "."));
    }
}
