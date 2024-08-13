package com.example.fse2.AuthenticationMS.services;

import com.example.fse2.AuthenticationMS.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private CustomerDetailsService customerDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String bearerToken = request.getHeader("Authorization");
            if(bearerToken==null ){
                throw new Exception("Authorization bearer token is Null");
            }
            if (!bearerToken.startsWith("Bearer ")){
                throw new Exception("Authorization bearer word is not found");
            }

            String jwt = bearerToken.substring(7);
            Claims claims = jwtService.getTokenClaims(jwt);

            if (claims == null){
                throw new Exception("Token is not Valid");
            }

            String email = claims.getSubject();
            var userDetails = userRepository.findByEmail(email);
            Collection<? extends GrantedAuthority> getAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(userDetails.getRole());

            UsernamePasswordAuthenticationToken authenticationToken = new
                    UsernamePasswordAuthenticationToken(userDetails,null,getAuthorities);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (Exception e){
            System.out.println("Cannot Authenticate user: "+e.getMessage());
        }

        filterChain.doFilter(request,response);
    }
}
