package ru.ac.secondhand.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * Фильтр для обработки CORS (Cross-Origin Resource Sharing) и включения поддержки
 * авторизации на основе HTTP Basic Authentication.
 * <p>
 * Этот фильтр добавляет заголовок "Access-Control-Allow-Credentials" со значением "true"
 * к ответу на каждый HTTP-запрос и передает запрос дальше по цепи фильтров с помощью
 * метода `doFilter`. Он используется для обеспечения поддержки авторизации при запросах
 * с других источников (Cross-Origin), позволяя передавать учетные данные (например, куки или
 * HTTP-авторизацию) в таких запросах.
 * </p>
 * <p>
 * Данный фильтр является компонентом Spring и автоматически применяется к каждому HTTP-запросу.
 * </p>
 * <p>
 * Расширяет класс {@code OncePerRequestFilter}, что гарантирует, что этот фильтр будет
 * выполнен только один раз для каждого HTTP-запроса.
 * </p>
 */
@Component
public class BasicAuthCorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
