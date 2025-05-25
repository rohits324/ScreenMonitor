////package com.sevi.RoleBasedAuth.service;
////
////import com.sevi.RoleBasedAuth.model.User;
////import com.sevi.RoleBasedAuth.repository.TokenRepository;
////import io.jsonwebtoken.Claims;
////import io.jsonwebtoken.Jwts;
////import io.jsonwebtoken.io.Decoders;
////import io.jsonwebtoken.security.Keys;
////import org.springframework.security.core.userdetails.UserDetails;
////import org.springframework.stereotype.Service;
////
////import javax.crypto.SecretKey;
////import java.util.Date;
////import java.util.function.Function;
////
////@Service
////public class JwtService {
////    private final String SECRET_KEY = "bc0c23f9a10eb1e89824186287d8d20cfdc33bcd93c3cc4d92c9d40471468000";
////    private final TokenRepository tokenRepository;
////
////    public JwtService(TokenRepository tokenRepository) {
////        this.tokenRepository = tokenRepository;
////    }
////
////    public String extractUsername(String token) {
////        return extractClaim(token, Claims::getSubject);
////    }
////
////
////    public boolean isValid(String token, UserDetails user) {
////        String username = extractUsername(token);
////
////        boolean validToken = tokenRepository
////                .findByToken(token)
////                .map(t -> !t.isLoggedOut())
////                .orElse(false);
////
////        return (username.equals(user.getUsername())) && !isTokenExpired(token) && validToken;
////    }
////
////    private boolean isTokenExpired(String token) {
////        return extractExpiration(token).before(new Date());
////    }
////
////    private Date extractExpiration(String token) {
////        return extractClaim(token, Claims::getExpiration);
////    }
////
////    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
////        Claims claims = extractAllClaims(token);
////        return resolver.apply(claims);
////    }
////
////    private Claims extractAllClaims(String token) {
////        return Jwts
////                .parserBuilder()
////                .setSigningKey(getSigninKey())
////                .build()
////                .parseClaimsJws(token)
////                .getBody();
////    }
////
////
////
////    public String generateToken(User user) {
////        String token = Jwts
////                .builder()
////                .setSubject(user.getUsername())
////                .setIssuedAt(new Date(System.currentTimeMillis()))
////                .setExpiration(new Date(System.currentTimeMillis() + 24*60*60*1000 ))
////                .signWith(getSigninKey())
////                .compact();
////
////        return token;
////    }
////
////    private SecretKey getSigninKey() {
////        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
////        return Keys.hmacShaKeyFor(keyBytes);
////    }
////
////}
//package com.sevi.RoleBasedAuth.service;
//
//import com.sevi.RoleBasedAuth.model.User;
//import com.sevi.RoleBasedAuth.repository.TokenRepository;
//import com.sevi.RoleBasedAuth.repository.UserRepository;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.SecretKey;
//import java.util.Date;
//import java.util.function.Function;
//
//@Service
//public class JwtService {
//
//    //private final String SECRET_KEY = "u4Yp9zG5q9RbF7hN9dGzXl8Op8XeKbJo4kB8k2aGjFQ=";
//   // private final String SECRET_KEY = "1txPjdK3e4rsr1vD8mPZ3aX9JmFqzH4e6rNq2pWvP3Rt7uZcYr8bQdAxLmNoP9aB";
//    private final String SECRET_KEY = "1txPjdK3e4rsr1vD8mPZ3aX9JmFqzH4e6rNq2pWvP3Rt7uZcYr8bQdAxLmNoP9aB";
//
//
//    private final TokenRepository tokenRepository;
//    private final UserRepository userRepository;
//
//    public JwtService(TokenRepository tokenRepository, UserRepository userRepository) {
//        this.tokenRepository = tokenRepository;
//        this.userRepository = userRepository;
//    }
//
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public boolean isValid(String token, UserDetails user) {
//        String username = extractUsername(token);
//
//        boolean validToken = tokenRepository
//                .findByToken(token)
//                .map(t -> !t.isLoggedOut())
//                .orElse(false);
//
//        return (username.equals(user.getUsername())) && !isTokenExpired(token) && validToken;
//    }
//
//    public User extractUser(String token) {
//        String username = extractUsername(token);
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
//    }
//
//    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    private Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
//        Claims claims = extractAllClaims(token);
//        return resolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts
//                .parserBuilder()
//                .setSigningKey(getSigninKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    public String generateToken(User user) {
//        return Jwts
//                .builder()
//                .setSubject(user.getUsername())
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 24 hours
//                .signWith(getSigninKey(), io.jsonwebtoken.SignatureAlgorithm.HS384)
//                .compact();
//    }
//
//    private SecretKey getSigninKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//}

package com.sevi.RoleBasedAuth.service;

import com.sevi.RoleBasedAuth.model.User;
import com.sevi.RoleBasedAuth.repository.TokenRepository;
import com.sevi.RoleBasedAuth.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    // ✅ A 384-bit (48-byte) key encoded in Base64
  private final String SECRET_KEY = "q9zK5p3QbL7sG9xW0mV4kPz8nT6yF2cD3uJ1aZ8rX5eH0tM7bL4cN2dA1fE6hR9k";



    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public JwtService(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isValid(String token, UserDetails user) {
        String username = extractUsername(token);

        boolean validToken = tokenRepository
                .findByToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);

        return (username.equals(user.getUsername())) && !isTokenExpired(token) && validToken;
    }

    public User extractUser(String token) {
        String username = extractUsername(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(User user) {
        return Jwts
                .builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 24 hours
                .signWith(getSigninKey(), SignatureAlgorithm.HS384) // ✅ Specify HS384
                .compact();
    }

    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes); // ✅ Generates secure key for HS384
    }
}
