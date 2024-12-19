package zgoo.cpos.repository.users;

import zgoo.cpos.domain.users.LoginHist;

public interface LoginHistRepositoryCustom {
    // 최근 로그인 기록 조회
    LoginHist findRecentLoginHist(String userId);
}
