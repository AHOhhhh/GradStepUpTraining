package fun.hercules.webapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import fun.hercules.webapi.client.UserClient;
import com.netflix.zuul.context.RequestContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static fun.hercules.webapi.security.SecurityConstants.EXPIRATION_TIME;
import static fun.hercules.webapi.security.SecurityConstants.HEADER_STRING;
import static fun.hercules.webapi.security.SecurityConstants.SECRET;
import static fun.hercules.webapi.security.SecurityConstants.TOKEN_PREFIX;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final UserClient userClient;

    private final ObjectMapper objectMapper;

    private final Set<String> forwardKeys = ImmutableSet.of("user",
            "userId", "enterpriseId",
            "role", "privileges");

    public AuthorizationFilter(AuthenticationManager authManager, UserClient userClient, ObjectMapper objectMapper) {
        super(authManager);
        this.userClient = userClient;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String token = request.getHeader(HEADER_STRING);
            if (token != null) {

                Claims body = null;
                try {
                    body = Jwts.parser()
                            .setSigningKey(SECRET.getBytes(Charset.defaultCharset()))
                            .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                            .getBody();
                } catch (ExpiredJwtException e) {
                    throw new UserAuthenticationException(ErrorCode.TOKEN_EXPIRED);
                }

                String userId = String.valueOf(body.get("userId"));
                User user;

                try {
                    user = userClient.getUserById(userId);
                } catch (Exception e) {
                    throw new UserAuthenticationException(ErrorCode.INVALID_USER);
                }

                if (!user.getStatus().equals("ENABLED")) {
                    throw new UserAuthenticationException(ErrorCode.DISABLED_STATUS);
                }

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                authenticationToken.setDetails(user);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);


                Map<String, Object> claims = body.entrySet().stream()
                        .filter(entry -> forwardKeys.contains(entry.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                claims.put("userName", user.getUsername());
                String refreshedToken = Jwts.builder()
                        .setClaims(claims)
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                        .signWith(SignatureAlgorithm.HS512, SECRET.getBytes(Charset.defaultCharset()))
                        .compact();

                // header must add to zuul request header
                RequestContext.getCurrentContext().addZuulRequestHeader(HEADER_STRING, TOKEN_PREFIX + refreshedToken);
                response.addHeader(HEADER_STRING, TOKEN_PREFIX + refreshedToken);
            }
            chain.doFilter(request, response);
        } catch (UserAuthenticationException ex) {
            // skip chain and return 401
            onUnsuccessfulAuthentication(request, response, ex);
        }
    }

    @Override
    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        if (failed instanceof UserAuthenticationException) {
            UserAuthenticationException ex = (UserAuthenticationException) failed;
            response.getWriter().println(objectMapper.writeValueAsString(ex.getErrorCode()));
        }
        response.getWriter().flush();
    }

}
