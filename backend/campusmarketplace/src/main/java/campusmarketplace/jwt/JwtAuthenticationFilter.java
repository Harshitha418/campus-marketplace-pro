package campusmarketplace.jwt;

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
import java.util.Collections;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {
    private final JwtService jwtService;

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
        System.out.println("FILTER HIT");
        System.out.println("HEADER = " + header);

        if (header != null &&
                header.startsWith("Bearer ")) {
            String token = header.substring(7);
            boolean valid = jwtService.validateToken(token);
            System.out.println(
                    "JWT Valid: " + valid);
            if (!valid) {
                response.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter()
                        .write("Invalid JWT Token");

                return;
            }
            String email = jwtService.extractEmail(token);
            System.out.println(
                    "Authenticated User: "
                            + email);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(
                request,
                response);
    }
}
