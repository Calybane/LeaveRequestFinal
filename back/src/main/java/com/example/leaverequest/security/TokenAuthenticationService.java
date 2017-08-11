package com.example.leaverequest.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class TokenAuthenticationService {
    static final long EXPIRATIONTIME = 172_800_000; // 2 days
    static final String SECRET = "ThisIs@Secret";
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";
    
    static void addAuthentication(HttpServletResponse res, String username) {
        String JWT = Jwts.builder().setSubject(username).setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME)).signWith(SignatureAlgorithm.HS512, SECRET).compact();
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
    }
    
    static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            
            String user = null;
            
            // TODO : catch exception when the token is expired
            
            try {
                // parse the token.
                user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().getSubject();
            
            /*
             * SET roles here because the app doesn't load them with the WebSecurityConfig
             */
                if (user != null)
                {
                    List<GrantedAuthority> updatedAuthorities = new ArrayList<>();
                    updatedAuthorities.add(new SimpleGrantedAuthority("USER"));
                    if (user.equals("manager"))
                    {
                        updatedAuthorities.add(new SimpleGrantedAuthority("MANAGER"));
                    }
                    if (user.equals("hr"))
                    {
                        updatedAuthorities.add(new SimpleGrantedAuthority("MANAGER"));
                        updatedAuthorities.add(new SimpleGrantedAuthority("HR"));
                    }
                    if (user.equals("admin"))
                    {
                        updatedAuthorities.add(new SimpleGrantedAuthority("MANAGER"));
                        updatedAuthorities.add(new SimpleGrantedAuthority("HR"));
                        updatedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
                    }
        
                    return new UsernamePasswordAuthenticationToken(user, null, updatedAuthorities);
                }
                else
                {
                    return null;
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return null;
    }
}