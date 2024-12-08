package com.deliboyraz.eticaret.security;

import com.deliboyraz.eticaret.service.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private JwtFilter jwtFilter;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    // Herkese açık endpointler
                    auth.requestMatchers("/welcome/",
                            "/auth/register/customer",
                            "/auth/login",
                            "/auth/sellerlogin",
                            "/product/search",
                            "/products/search/**").permitAll();

                    // Ürün listeleme endpoint'i herkese açık
                    auth.requestMatchers(HttpMethod.GET, "/product/**").permitAll();

                    auth.requestMatchers("/cart/**").hasAuthority("CUSTOMER");

                    // Customer profil endpoint'leri - OPTIONS metoduna izin ver
                    auth.requestMatchers(HttpMethod.OPTIONS, "/customer/**").permitAll();

                    // Customer profil endpoint'leri - sadece müşteriler erişebilir
                    auth.requestMatchers("/customer/**").hasAuthority("CUSTOMER");

                    // Admin yetkilendirmesi gerektiren endpointler
                    auth.requestMatchers("/auth/register/admin",
                            "/auth/register/seller").hasAuthority("ADMIN");

                    // Satıcıya özel endpointler
                    auth.requestMatchers("/seller/**").hasAuthority("SELLER");

                    auth.requestMatchers("/category/**").permitAll();

                    // Ürün işlemleri (Sadece satıcıya yetkilendirilmiş)
                    auth.requestMatchers(HttpMethod.POST, "/product/**").hasAuthority("SELLER");
                    auth.requestMatchers(HttpMethod.PUT, "/product/**").hasAuthority("SELLER");
                    auth.requestMatchers(HttpMethod.DELETE, "/product/**").hasAuthority("SELLER");

                    // Diğer tüm istekler doğrulama gerektirir
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

}