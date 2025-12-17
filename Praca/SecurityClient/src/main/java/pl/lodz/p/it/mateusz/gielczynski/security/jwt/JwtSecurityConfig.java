package pl.lodz.p.it.mateusz.gielczynski.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@EnableMethodSecurity
@ConditionalOnProperty("app.security.filter.enabled")
public class JwtSecurityConfig {

    private final RSAPublicKey publicKey;

    @Value("${app.security.filter.permitAll:}")
    private String permittedEndpoints;

    @Value("${app.security.filter.permitGet:}")
    private String permitGetEndpoints;

    @Value("${app.security.filter.anonymous:}")
    private String anonymousEndpoints;

    CorsConfiguration corsConfigurationSource() {
        var corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("*"));
//        corsConfig.setAllowCredentials(true);
        corsConfig.setAllowedMethods(List.of("GET", "PATCH", "POST", "PUT", "DELETE"));
        corsConfig.addAllowedHeader("*");
        corsConfig.addExposedHeader(HttpHeaders.ETAG);
        return corsConfig;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtDecoder decoder, JwtAuthenticationConverter converter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> {
                            String[] permitted = formatPermittedEndpoints(permittedEndpoints);
                            if (permitted.length > 0) {
                                authorizeRequests.requestMatchers(permitted).permitAll();
                            }
                            String[] permitGet = formatPermittedEndpoints(permitGetEndpoints);
                            if (permitGet.length > 0) {
                                authorizeRequests.requestMatchers(HttpMethod.GET, permitGet).permitAll();
                            }
                            String[] anonymous = formatPermittedEndpoints(anonymousEndpoints);
                            if (anonymous.length > 0) {
                                authorizeRequests.requestMatchers(anonymous).anonymous();
                            }
                            authorizeRequests.anyRequest().authenticated();
                        }
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(decoder)
                                .jwtAuthenticationConverter(converter))
                )
                .cors(cors -> cors.configurationSource(_ -> corsConfigurationSource()))
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }


    private String[] formatPermittedEndpoints(String permittedEndpoints) {
        if (permittedEndpoints == null || permittedEndpoints.isBlank()) {
            return new String[0];
        }
        return Arrays.stream(permittedEndpoints.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
    }


    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).signatureAlgorithm(SignatureAlgorithm.RS512).build();
    }

    @Bean
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}

