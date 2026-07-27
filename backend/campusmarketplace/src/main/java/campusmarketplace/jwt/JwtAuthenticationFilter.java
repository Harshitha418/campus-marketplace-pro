package campusmarketplace.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import campusmarketplace.service.JwtService;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
public class JwtAuthenticationFilter
                extends OncePerRequestFilter {
        private final JwtService jwtService;

        private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

        public JwtAuthenticationFilter(JwtService jwtService) {
                this.jwtService = jwtService;
        }

        @Override
        protected void doFilterInternal(
                        HttpServletRequest request,
                        HttpServletResponse response,
                        FilterChain filterChain)
                        throws ServletException, IOException {
                String path = request.getServletPath();
                if (path.startsWith("/api/auth")) {
                        filterChain.doFilter(request, response);
                        return;
                }
                String header = request.getHeader("Authorization");
                logger.debug("JWT filter triggered for path: {}", request.getServletPath());

                if (header != null &&
                                header.startsWith("Bearer ")) {
                        String token = header.substring(7);
                        boolean valid = jwtService.validateToken(token);
                        logger.debug("JWT valid: {}", valid);
                        if (!valid) {
                                filterChain.doFilter(request, response);
                                return;
                        }
                        String email = jwtService.extractEmail(token);
                        String role = jwtService.extractRole(token);
                        logger.debug("Authenticated user: {} with role: {}", email, role);

                        // Spring Security expects the "ROLE_" prefix when using hasRole("ADMIN").
                        // Older tokens may have no role claim, so default to empty authorities.
                        List<SimpleGrantedAuthority> authorities = (role == null)
                                        ? List.of()
                                        : List.of(new SimpleGrantedAuthority("ROLE_" + role));

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                        email,
                                        null,
                                        authorities);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                filterChain.doFilter(
                                request,
                                response);
        }
}
