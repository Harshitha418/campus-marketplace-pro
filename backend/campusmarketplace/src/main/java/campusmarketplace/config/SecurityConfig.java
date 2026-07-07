package campusmarketplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import campusmarketplace.jwt.JwtAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtFilter;

        public SecurityConfig(
                        JwtAuthenticationFilter jwtFilter) {

                this.jwtFilter = jwtFilter;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(
                        HttpSecurity http) throws Exception {

                http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(HttpMethod.OPTIONS, "/**")
                                                .permitAll()
                                                .requestMatchers("/api/auth/**")
                                                .permitAll()
                                                // Anyone can browse/search products (read-only)
                                                .requestMatchers(HttpMethod.GET, "/api/products/**")
                                                .permitAll()
                                                // Creating, editing, deleting a product requires a valid JWT
                                                .requestMatchers(HttpMethod.POST, "/api/products/**")
                                                .authenticated()
                                                .requestMatchers(HttpMethod.PUT, "/api/products/**")
                                                .authenticated()
                                                .requestMatchers(HttpMethod.DELETE, "/api/products/**")
                                                .authenticated()
                                                .anyRequest()
                                                .authenticated())
                                .httpBasic(Customizer.withDefaults());

                http.addFilterBefore(
                                jwtFilter,
                                UsernamePasswordAuthenticationFilter.class);
                return http.build();
        }
}