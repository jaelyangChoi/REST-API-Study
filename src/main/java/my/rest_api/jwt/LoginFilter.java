package my.rest_api.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import my.rest_api.dto.CustomUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // JWT 발급
        String username = ((CustomUserDetails) authResult.getPrincipal()).getUsername();
        String authority = authResult.getAuthorities().iterator().next().getAuthority();

        String accessToken = jwtUtil.createJwt("access", username, authority, 1000 * 60 * 10L);
        String refreshToken = jwtUtil.createJwt("refresh", username, authority, 1000 * 60 * 60 * 24L);

        // Refresh 토큰 저장
        saveRefreshToken(username, refreshToken, 1000 * 60 * 60 * 24L);

        // 발급 토큰 응답
        response.setHeader("access", accessToken);
        response.addCookie(createCookie("refresh", refreshToken));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private void saveRefreshToken(String username, String refreshToken, long expiredMs) {
        Date expireDate = new Date(System.currentTimeMillis() + expiredMs);

        RefreshToken tokenEntity = new RefreshToken();
        tokenEntity.setRefreshToken(refreshToken);
        tokenEntity.setUsername(username);
        tokenEntity.setExpiration(expireDate.toString());

        refreshTokenRepository.save(tokenEntity);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setHttpOnly(true);
        return cookie;
    }
}
