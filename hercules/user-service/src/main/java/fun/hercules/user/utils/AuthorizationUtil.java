package fun.hercules.user.utils;

import fun.hercules.user.user.domain.Privilege;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.domain.Privilege;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.security.SecurityConstants;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.stream.Collectors;

import static fun.hercules.user.user.security.SecurityConstants.SECRET;

public class AuthorizationUtil {

    /**
     * 创建JWToken
     *
     * @param user 用户信息
     * @return 创建成功的JWToken
     */
    public static String createJwtToken(User user) {
        JwtBuilder builder = Jwts.builder();
        return builder
                .claim("userId", user.getId())
                .claim("userName", user.getUsername())
                .claim("enterpriseId", user.getEnterpriseId())
                .claim("role", user.getRole())
                .claim("privileges", String.join(",", user.getPrivileges().stream()
                        .map(Privilege::getName).collect(Collectors.toList())))
                .setExpiration(new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes(Charset.defaultCharset()))
                .compact();
    }
}
