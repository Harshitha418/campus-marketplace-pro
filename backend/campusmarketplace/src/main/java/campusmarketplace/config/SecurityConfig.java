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
                                                .requestMatchers(
                                                                "/api/auth/**",
                                                                "/api/products/**")
                                                .permitAll()
                                                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**")
                                                .permitAll()
                                                .anyRequest()
                                                .authenticated())
                                .httpBasic(Customizer.withDefaults());

                http.addFilterBefore(
                                jwtFilter,
                                UsernamePasswordAuthenticationFilter.class);
                return http.build();
        }
}