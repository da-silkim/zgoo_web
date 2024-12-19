package zgoo.cpos.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import zgoo.cpos.service.LoginHistService;

@Component
public class CustomLoginEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private LoginHistService loginHistService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        // Authentication authentication = event.getAuthentication();
        // String userId = authentication.getName();
        // this.loginHistService.recordLogin(userId);
    }
}
