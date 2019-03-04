package fun.hercules.webapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import fun.hercules.webapi.client.UserClient;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static fun.hercules.webapi.security.SecurityConstants.EXPIRATION_TIME;
import static fun.hercules.webapi.security.SecurityConstants.HEADER_STRING;
import static fun.hercules.webapi.security.SecurityConstants.SECRET;
import static fun.hercules.webapi.security.SecurityConstants.TOKEN_PREFIX;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserClient userClient;

    private ObjectMapper objectMapper;

    private Map<String, Object> configs;

    public AuthenticationFilter(UserClient userClient, ObjectMapper objectMapper, Map<String, Object> configs) {
        this.userClient = userClient;
        this.objectMapper = objectMapper;
        setFilterProcessesUrl("/webapi/login");
        this.configs = configs;
        setAuthenticationFailureHandler(new HerculesAuthenticationFailureHandler(objectMapper));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {

            //Comment: for test upload file with chinese name messy code.
            if (request.getCharacterEncoding() == null) {
                request.setCharacterEncoding("UTF-8");
            }

            LoginInformation loginInfo = objectMapper.readValue(request.getInputStream(), LoginInformation.class);

            if ((Boolean) configs.get("verify.captcha") && !userClient.verifyCaptcha(new Pair(loginInfo.getCaptchaId(), loginInfo.getCaptcha()))) {
                throw new UserAuthenticationException(ErrorCode.INVALID_CAPTCHA);
            }

            User user = userClient.verifyPassword(new Pair(loginInfo.getUsername(), loginInfo.getPassword()));
            if (user == null) {
                throw new UserAuthenticationException(ErrorCode.INVALID_PASSWORD);
            }

            if (!isLegalRole(user)) {
                throw new UserAuthenticationException(ErrorCode.INVALID_ROLE);
            }

            if (!user.getStatus().equals("ENABLED")) {
                throw new UserAuthenticationException(ErrorCode.DISABLED_STATUS);
            }

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), "", new ArrayList<>());
            token.setDetails(user);

            return token;
        } catch (IOException e) {
            throw new RuntimeException("invalid input");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        User user = (User) auth.getDetails();
        String token = Jwts.builder()
//                .claim("user", objectMapper.writeValueAsString(user))
                .claim("userId", user.getId())
                .claim("userName", user.getUsername())
                .claim("enterpriseId", user.getEnterpriseId())
                .claim("role", user.getRole())
                .claim("privileges", String.join(",", Optional.ofNullable(user.getPrivileges()).orElse(Collections.EMPTY_SET)))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes(Charset.defaultCharset()))
                .compact();
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        getFailureHandler().onAuthenticationFailure(request, response, failed);
    }

    protected boolean isLegalRole(User user) {
        return user.isEnterpriseAdmin() || user.isEnterpriseUser();
    }
}
