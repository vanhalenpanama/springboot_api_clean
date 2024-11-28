package com.noteapp.common.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.token-validity-in-hours}")
    private long tokenValidityInHours = 6;

    public enum Role {
        ROLE_ADMIN,
        ROLE_USER
    }

    public static class CurrentUser {
        private final String id;
        private final Role role;

        public CurrentUser(String id, Role role) {
            this.id = id;
            this.role = role;
        }

        public String getId() {
            return id;
        }

        public Role getRole() {
            return role;
        }

        @Override
        public String toString() {
            return String.format("%s(%s)", id, role);
        }
    }

    public String createToken(String id, Role role) {
        Claims claims = Jwts.claims().setSubject(id);
        claims.put("role", role.name());

        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInHours * 3600000);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority(claims.get("role").toString()));

        CurrentUser principal = new CurrentUser(
                claims.getSubject(),
                Role.valueOf(claims.get("role").toString())
        );

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public CurrentUser getCurrentUser(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        String id = claims.getSubject();
        Role role = Role.valueOf(claims.get("role", String.class));

        if (id == null) {
            throw new JwtException("Invalid token");
        }

        return new CurrentUser(id, role);
    }
}


//package com.noteapp.middleware;
//
//        import io.jsonwebtoken.*;
//        import org.springframework.beans.factory.annotation.Value;
//        import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//        import org.springframework.security.core.Authentication;
//        import org.springframework.security.core.GrantedAuthority;
//        import org.springframework.security.core.authority.SimpleGrantedAuthority;
//        import org.springframework.stereotype.Component;
//
//        import jakarta.servlet.http.HttpServletRequest;
//        import java.util.*;
//        import java.util.stream.Collectors;
//
//@Component
//public class JwtTokenProvider {
//
//    @Value("${jwt.secret}")
//    private String jwtSecret;
//
//    @Value("${jwt.token-validity-in-hours}")
//    private long tokenValidityInHours = 6;
//
//    public enum Role {
//        ROLE_ADMIN,
//        ROLE_USER
//    }
//
//    public static class CurrentUser {
//        private final String id;
//        private final Role role;
//
//        public CurrentUser(String id, Role role) {
//            this.id = id;
//            this.role = role;
//        }
//
//        public String getId() {
//            return id;
//        }
//
//        public Role getRole() {
//            return role;
//        }
//
//        @Override
//        public String toString() {
//            return String.format("%s(%s)", id, role);
//        }
//    }
//
//    public String createToken(String email, Role role) {
//        Claims claims = Jwts.claims().setSubject(email);
//        claims.put("role", role.name());
//
//        Date now = new Date();
//        Date validity = new Date(now.getTime() + tokenValidityInHours * 3600000);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(now)
//                .setExpiration(validity)
//                .signWith(SignatureAlgorithm.HS256, jwtSecret)
//                .compact();
//    }
//
//    public Authentication getAuthentication(String token) {
//        Claims claims = Jwts.parser()
//                .setSigningKey(jwtSecret)
//                .parseClaimsJws(token)
//                .getBody();
//
//        Collection<? extends GrantedAuthority> authorities =
//                Collections.singletonList(new SimpleGrantedAuthority(claims.get("role").toString()));
//
//        CurrentUser principal = new CurrentUser(
//                claims.getSubject(),  // email을 id로 사용
//                Role.valueOf(claims.get("role").toString())
//        );
//
//        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
//    }
//
//    public String resolveToken(HttpServletRequest req) {
//        String bearerToken = req.getHeader("Authorization");
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            return false;
//        }
//    }
//
//    public CurrentUser getCurrentUser(String token) {
//        Claims claims = Jwts.parser()
//                .setSigningKey(jwtSecret)
//                .parseClaimsJws(token)
//                .getBody();
//
//        String email = claims.getSubject();
//        Role role = Role.valueOf(claims.get("role", String.class));
//
//        if (email == null) {
//            throw new JwtException("Invalid token");
//        }
//
//        return new CurrentUser(email, role);
//    }
//}