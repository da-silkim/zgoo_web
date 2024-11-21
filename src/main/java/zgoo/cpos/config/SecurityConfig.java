package zgoo.cpos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import zgoo.cpos.util.SHA256Encoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // .authorizeHttpRequests(authorize -> authorize
            //     .requestMatchers("/**").permitAll()
            // )
            .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                .requestMatchers("/", "/resources/**", "/static/**", "/css/**", "/js/**", "/img/**").permitAll()
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/dashboard", true)
                .successHandler((request, response, authentication) -> {
                    // Remember Id checkbox
                    String remember = request.getParameter("rememberCheckbox");
                    if ("on".equals(remember)) {
                        String userId = request.getParameter("userId");
                        Cookie cookie = new Cookie("rememberedUserId", userId);
                        cookie.setMaxAge(365 * 24 * 60 * 60);   // 1년 동안 유지
                        cookie.setPath("/");
                        cookie.setHttpOnly(false);
                        response.addCookie(cookie);
                    } else {
                        Cookie cookie = new Cookie("rememberedUserId", null);
                        cookie.setMaxAge(0);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                    
                    response.sendRedirect("/dashboard");
                })
                .failureHandler((request, response, exception) -> {
                    String userId = request.getParameter("userId");
                    String password = request.getParameter("password");
                    request.getSession().setAttribute("userId", userId); 
                    request.getSession().setAttribute("password", password);
                    request.getSession().setAttribute("loginError", "아이디 또는 비밀번호가 잘못됐습니다.");
                    response.sendRedirect("/?error");
                })
                .usernameParameter("userId")
                .passwordParameter("password")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .csrf().disable();
        
        return http.build();
    }

     @Bean
    PasswordEncoder passwordEncoder() {
        return new SHA256Encoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
