package my.rest_api.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.rest_api.accounts.Account;
import my.rest_api.accounts.AccountRole;
import my.rest_api.dto.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //request 에서 AccessToken 을 찾아 검증
        String accessToken = request.getHeader("access");

        if (accessToken == null) {
            log.info("accessToken null");
            //조건이 해당되면 해당 필터 종료 (필수)
            filterChain.doFilter(request, response);
            return;
        }

        //토큰 만료 검증. 만료 시 프론트에 즉시 응답
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            response.getWriter().write("access token expired");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //토큰이 AccessToken 인지 확인
        if (!jwtUtil.getCategory(accessToken).equals("access")) {
            response.getWriter().write("invalid access token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //토큰에서 username 과 role 획득하여 인증 토큰 생성
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        Account account = new Account();
        account.setEmail(username);
        account.setRole(AccountRole.valueOf(role));

        CustomUserDetails userDetails = new CustomUserDetails(account);
        Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
