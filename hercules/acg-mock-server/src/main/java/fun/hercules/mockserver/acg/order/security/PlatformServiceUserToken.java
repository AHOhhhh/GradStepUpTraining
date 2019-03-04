package fun.hercules.mockserver.acg.order.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class PlatformServiceUserToken {


    @Getter
    private String userId;

    private String role;

    private String secret;

    // in minutes
    private int expires;

    public PlatformServiceUserToken(@Value("${hlp.platform.service-user.id}") String userId,
                                    @Value("${hlp.platform.service-user.role}") String role,
                                    @Value("${hlp.authorization.jwt.secret}") String secret,
                                    @Value("${hlp.authorization.jwt.expires}") int expires) {
        this.userId = userId;
        this.role = role;
        this.secret = secret;
        this.expires = expires;
    }


    public String getToken() {
        return "Bearer " + Jwts.builder()
                .claim("userId", userId)
                .claim("enterpriseId", null)
                .claim("role", role)
                .claim("privileges", "AllPrivileges")
                .setExpiration(Date.from(Instant.now().plus(expires, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes(Charset.defaultCharset()))
                .compact();
    }
}
