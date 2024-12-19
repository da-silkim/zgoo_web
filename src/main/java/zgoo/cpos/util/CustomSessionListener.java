package zgoo.cpos.util;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.RequiredArgsConstructor;
import zgoo.cpos.service.LoginHistService;

@Component
@RequiredArgsConstructor
public class CustomSessionListener implements HttpSessionListener {

    private final LoginHistService loginHistService;

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        String userId = (String) session.getAttribute("userId");
        if (userId != null) {
            System.out.println("세션 만료: 사용자 ID가 확인되었습니다. userId: " + userId);
            loginHistService.recordLogout(userId);
        } else {
            System.out.println("세션 만료: 사용자 ID가 세션에서 확인되지 않았습니다.");
        }
    }
}
