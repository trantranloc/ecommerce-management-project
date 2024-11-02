package com.project.ecommerce.configuration;

import com.project.ecommerce.service.admin.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        registry -> registry
                                .dispatcherTypeMatchers(HttpMethod.valueOf("/admin/**")).authenticated() // yêu cầu xác thực cho admin
                                .anyRequest().permitAll() // Cho phép tất cả truy cập
                )
                .formLogin(
                        form -> form
                                .loginPage("/login").permitAll()
                                .successHandler((request, response, authentication) -> {
                                    // Chuyển hướng đến /admin sau khi đăng nhập thành công
                                    response.sendRedirect("/admin");
                                })
                )
                .logout(LogoutConfigurer::permitAll)
                .exceptionHandling(eh ->
                        eh.authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/error"))
                                .accessDeniedHandler((request, response, accessDeniedException) -> response.sendRedirect("/error"))
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
