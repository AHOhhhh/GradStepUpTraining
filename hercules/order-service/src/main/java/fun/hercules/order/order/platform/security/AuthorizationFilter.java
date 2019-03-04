package fun.hercules.order.order.platform.security;

import fun.hercules.order.order.common.errors.ErrorCode;
import fun.hercules.order.order.common.exceptions.UnauthorizedException;
import fun.hercules.order.order.platform.user.Privilege;
import fun.hercules.order.order.platform.user.User;
import fun.hercules.order.order.platform.user.UserImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthorizationFilter extends BasicAuthenticationFilter {
    public AuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        try {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (token == null) {
                throw new UnauthorizedException(ErrorCode.INVALID_USER_INFO, String.format("missing authorization header"));
            }
            Claims body = Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET.getBytes(Charset.defaultCharset()))
                    .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                    .getBody();
            try {
                User user = new UserImpl(
                        body.get("userId").toString(),
                        Optional.ofNullable(body.get("enterpriseId")).map(Object::toString).orElse(null),
                        body.get("role").toString(),
                        getPrivileges(body.get("privileges").toString()));
                SecurityContext context = SecurityContextHolder.getContext();
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
                context.setAuthentication(new UsernamePasswordAuthenticationToken(user, null, Arrays.asList(authority)));
            } catch (UncheckedIOException ex) {
                throw new UnauthorizedException(ErrorCode.INVALID_USER_INFO, "can't read user from token", ex);
            }
            chain.doFilter(request, response);
        } catch (RuntimeException ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.toString());
        }
    }

    private Set<Privilege> getPrivileges(String privileges) {
        if (StringUtils.isEmpty(privileges)) {
            return Collections.EMPTY_SET;
        }
        return Arrays.stream(privileges.split(",")).map(privilege -> Privilege.of(privilege))
                .collect(Collectors.toSet());
    }

}
