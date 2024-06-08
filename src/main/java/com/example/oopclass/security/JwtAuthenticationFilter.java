package com.example.oopclass.security;

import com.example.oopclass.domain.user.User;
import com.example.oopclass.domain.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider,
                                   UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String jwt = jwtTokenProvider.resolveToken(request);
        if (jwt != null && jwtTokenProvider.validateToken(jwt)) {
            String userId = jwtTokenProvider.getUserIdFromJWT(jwt);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
//            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!11확인용ㅇ" + userDetails);

            if (userDetails != null) {
                JwtAuthenticationToken authentication = new JwtAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }
}
