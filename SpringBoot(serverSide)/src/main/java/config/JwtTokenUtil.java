package config;
import com.businessanddecisions.models.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

import static config.Constants.ACCESS_TOKEN_VALIDITY_SECONDS;
import static config.Constants.SIGNING_KEY;


@Component
public class JwtTokenUtil implements Serializable {

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String  getRole(String token) {
        Claims claims = getAllClaimsFromToken(token);
        List <LinkedHashMap<String,String>> listScopes = claims.get("scopes",List.class);
        String role="";
        for(LinkedHashMap<String,String> lhm : listScopes) {
           role = lhm.get("authority");
        }
        return role;
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {

        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserModel user) {
        return doGenerateToken(user.getEmail(),user.getRole(),user.getId());
    }

    private String doGenerateToken(String subject,String role,long id) {
        Claims claims = Jwts.claims().setSubject(subject);
        /*SimpleGrantedAuthority  roles = new SimpleGrantedAuthority(role);
        String [] roleee = {roles.getAuthority()};
        System.out.println("the users role =  "+roleee[0]);*/
        claims.put("scopes", Arrays.asList(new SimpleGrantedAuthority(role)));
        claims.put("identifier",id);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("OAO")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String email = getUsernameFromToken(token);
        return (
                email.equals(userDetails.getUsername())
                        && !isTokenExpired(token)
        );
    }

}
