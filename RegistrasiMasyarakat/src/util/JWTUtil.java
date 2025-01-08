package util;

import io.jsonwebtoken.*;

import java.util.Date;

public class JWTUtil {
    private static final String SECRET_KEY = "your_secret_key_here";
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24 jam dalam milliseconds
    
    public static String generateToken(int userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired");
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported token");
        } catch (MalformedJwtException e) {
            System.out.println("Malformed token");
        } catch (SignatureException e) {
            System.out.println("Invalid signature");
        } catch (Exception e) {
            System.out.println("Token is invalid");
        }
        return false;
    }
    
    public static int getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
                
        return Integer.parseInt(claims.getSubject());
    }


}
