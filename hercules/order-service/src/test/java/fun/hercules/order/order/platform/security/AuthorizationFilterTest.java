package fun.hercules.order.order.platform.security;

import fun.hercules.order.order.platform.user.Privilege;
import fun.hercules.order.order.platform.user.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.nio.charset.Charset;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class AuthorizationFilterTest {

    @Test
    public void generateAuthorization() throws Exception {
        System.out.println("Bearer " + Jwts.builder()
                .claim("userId", "userId")
                .claim("enterpriseId", "userId")
                .claim("role", Role.EnterpriseUser)
                .claim("privileges", Privilege.of(Privilege.Type.OrderAccessPrivilege))
                .setExpiration(Date.from(Instant.now().plus(10, ChronoUnit.DAYS)))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes(Charset.defaultCharset()))
                .compact());
    }
}