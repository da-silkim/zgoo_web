package zgoo.cpos.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.domain.users.LoginHist;
import zgoo.cpos.mapper.UsersMapper;
import zgoo.cpos.repository.users.LoginHistRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginHistService {
    private final LoginHistRepository loginHistRepository;

    // 로그인 기록 저장
    public void recordLogin(String userId) {
        try {
            LoginHist loginHist = UsersMapper.loginHistToEntity(userId);
            this.loginHistRepository.save(loginHist);
        } catch (Exception e) {
            log.error("[recordLogin] error: {}", e.getMessage());
        }
    }

    // 로그아웃 기록 저장
    @Transactional
    public void recordLogout(String userId) {
        try {
            log.info("=== logout date update before ===");
            LoginHist loginHist = this.loginHistRepository.findRecentLoginHist(userId);
            loginHist.updateLogoutDate();
            log.info("=== logout date update after ===");
        } catch (Exception e) {
            log.error("[recordLogout] error: {}", e.getMessage());
        }
    }
}
