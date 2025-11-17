package org.baljaguk.global.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.baljaguk.domain.user.dto.CustomUserDetails;
import org.baljaguk.domain.user.entity.User;
import org.baljaguk.domain.user.repository.UserRepository;
import org.baljaguk.global.config.JWTConfig;
import org.baljaguk.global.util.JWTUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final JWTConfig jwtConfig;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(jwtConfig.getHeader()); // ex. "Authorization"

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7); // "Bearer " 이후 토큰만 추출

        if (!jwtUtil.isTokenValid(token)) {
            log.warn("Invalid JWT Token");
            filterChain.doFilter(request, response);
            return;
        }

        // 사용자 ID 추출
        Long userId = jwtUtil.getUserId(token);

        if (userId == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        User user = userOptional.get();
        CustomUserDetails userDetails = new CustomUserDetails(user);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
