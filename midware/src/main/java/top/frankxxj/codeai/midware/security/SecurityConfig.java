package top.frankxxj.codeai.midware.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import top.frankxxj.codeai.midware.ai.ModelAccessManager;
import top.frankxxj.codeai.midware.security.rsa.RsaKeyProperties;
import top.frankxxj.codeai.midware.web.filter.ModelAccessFilter;

import java.security.SecureRandom;
import java.util.List;

@Configuration
@EnableConfigurationProperties({RsaKeyProperties.class})
class SecurityConfig {
    @Bean
    PasswordEncoder bcryptEncoder() {
        return new BCryptPasswordEncoder(13, new SecureRandom("CODEAI".getBytes()));
    }

    @Bean
    SecurityFilterChain filterChain1(HttpSecurity http,
                                     UserDetailsService detailsService,
                                     ModelAccessManager modelAccessManager) throws Exception {
        return http
                .cors(cors -> cors
                        .configurationSource(myPluginConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .userDetailsService(detailsService)
                .oauth2ResourceServer(oauth -> {
                    oauth.jwt(conf -> {
                        // TODO: 5/8/2025 finish jwt setup
                    });
                })
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.POST, "/api/login/token", "/api/user/create").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/model/all").permitAll();
                    auth.anyRequest().authenticated();
                })
                .httpBasic(Customizer.withDefaults())
                .addFilterAfter(new ModelAccessFilter(modelAccessManager), AnonymousAuthenticationFilter.class)
                .build();
    }

    @Bean
    JwtDecoder jwtDecoder(RsaKeyProperties rsaKeyProperties) {
        return NimbusJwtDecoder.withPublicKey(rsaKeyProperties.publicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder(RsaKeyProperties rsaKeyProperties) {
        // create jwksource using RsaKeyProperties
        JWK keys = new RSAKey.Builder(rsaKeyProperties.publicKey()).privateKey(rsaKeyProperties.privateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(keys));
        return new NimbusJwtEncoder(jwkSource);
    }

    UrlBasedCorsConfigurationSource myPluginConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // TODO: 5/11/2025 configure CORS
        configuration.setAllowedOrigins(List.of("https://localhost"));
        configuration.setAllowedMethods(List.of(CorsConfiguration.ALL));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}
