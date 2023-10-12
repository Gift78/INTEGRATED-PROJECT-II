package sit.int221.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.AntPathMatcher;

import static sit.int221.utils.UserRole.admin;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private static final String[][] PUBLIC_ENDPOINTS = {
            { HttpMethod.POST.toString(), "/api/token" },
            { HttpMethod.OPTIONS.toString(), "/**"}
    };

    private static final String[] ADMIN_ENDPOINTS = {
            "/api/users/**",
            "/api/match/**",
    };

    public static boolean isPublicEndpoint(String method, String path) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String[] endpoint : PUBLIC_ENDPOINTS) {
            if (antPathMatcher.match(endpoint[1], path) && endpoint[0].equals(method)) {
                return true;
            }
        }
        return false;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> {
                            request.requestMatchers(ADMIN_ENDPOINTS).hasRole(admin.name());
                            for (String[] endpoint : PUBLIC_ENDPOINTS) {
                                String httpMethod = endpoint[0];
                                String endpointPath = endpoint[1];
                                request.requestMatchers(httpMethod, endpointPath).permitAll();
                            }
                            request.anyRequest().authenticated();
                        }
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
