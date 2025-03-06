package zgoo.cpos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import zgoo.cpos.service.LoginHistService;
import zgoo.cpos.util.CustomLoginSuccessHandler;
import zgoo.cpos.util.CustomLogoutSuccessHandler;
import zgoo.cpos.util.SHA256Encoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private LoginHistService loginHistService;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // .authorizeHttpRequests(authorize -> authorize
                // .requestMatchers("/**").permitAll())
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/", "/resources/**", "/static/**", "/css/**", "/js/**",
                                "/img/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .successHandler(new CustomLoginSuccessHandler(loginHistService))
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
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        // .logoutSuccessUrl("/")
                        .logoutSuccessHandler(new CustomLogoutSuccessHandler(loginHistService))
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .sessionManagement(session -> session
                        .invalidSessionUrl("/"))
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new SHA256Encoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
