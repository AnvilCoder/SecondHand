package ru.ac.secondhand.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Конфигурация безопасности веб-приложения.
 * <p>
 * Этот класс аннотирован как {@code @EnableWebSecurity}, что позволяет настроить
 * параметры безопасности приложения. Он определяет правила доступа для различных
 * URL-адресов и конфигурирует кросс-доменные запросы (CORS) для разрешенных источников.
 * </p>
 * <p>
 * Внутри этого класса определено белое списком URL-адресов ({@code AUTH_WHITELIST}),
 * которые доступны без аутентификации. Также определены правила доступа для остальных
 * URL-адресов, включая разрешенные HTTP-методы и заголовки.
 * </p>
 * <p>
 * Класс также создает бин {@code PasswordEncoder} для хеширования паролей пользователей
 * с использованием алгоритма BCrypt.
 * </p>
 */
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register",
            "/image/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests(
                        authorization ->
                                authorization
                                        .mvcMatchers(AUTH_WHITELIST)
                                        .permitAll()
                                        .mvcMatchers(HttpMethod.GET, "/ads")
                                        .permitAll()
                                        .mvcMatchers("/ads/**", "/users/**")
                                        .authenticated())
                .cors(cors -> cors
                        .configurationSource(request -> {
                            CorsConfiguration config = new CorsConfiguration();
                            config.setAllowCredentials(true);
                            config.addAllowedOrigin("http://localhost:3000");
                            config.addAllowedMethod("*");
                            config.addAllowedHeader("*");
                            return config;
                        }))
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
