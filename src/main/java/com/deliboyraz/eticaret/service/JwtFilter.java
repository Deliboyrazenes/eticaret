package com.deliboyraz.eticaret.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SellerService sellerService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        try {
            if (request.getMethod().equals("OPTIONS")) {
                chain.doFilter(request, response);
                return;
            }

            String authorizationHeader = request.getHeader("Authorization");
            log.debug("Authorization Header: {}", authorizationHeader);

            String token = null;
            String username = null;
            String userType = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
                username = jwtUtil.extractUsername(token);
                userType = jwtUtil.extractUserType(token);

                log.debug("Extracted username: {}, userType: {}", username, userType);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtUtil.validateToken(token)) {
                    UserDetails userDetails = loadUserByUsername(username, userType);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    log.debug("Authentication set for user: {}", username);
                } else {
                    log.warn("Token validation failed");
                }
            }

        } catch (Exception e) {
            log.error("JWT Authentication error", e);
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }

    private UserDetails loadUserByUsername(String username, String userType) {
        try {
            if ("SELLER".equals(userType)) {
                return sellerService.findSellerByPhone(username);
            } else if ("CUSTOMER".equals(userType)) {
                return customerService.findCustomerByEmail(username);
            }
            throw new UsernameNotFoundException("User not found");
        } catch (Exception e) {
            log.error("Error loading user: {}", e.getMessage());
            throw new UsernameNotFoundException("Error loading user", e);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/auth/") ||
                path.equals("/welcome/") ||
                (path.startsWith("/product/") && request.getMethod().equals("GET"));
    }
}