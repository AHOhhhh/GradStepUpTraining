package fun.hercules.user.user.security;

import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Optional;

import static fun.hercules.user.user.security.SecurityConstants.HEADER_STRING;
import static fun.hercules.user.user.security.SecurityConstants.SECRET;
import static fun.hercules.user.user.security.SecurityConstants.TOKEN_PREFIX;

/**
 * spring security专用，用于鉴权过滤
 */
public class AuthorizationFilter extends BasicAuthenticationFilter {
    private final UserService userService;

    public AuthorizationFilter(AuthenticationManager authManager, UserService userService) {
        super(authManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        // 从header中取出Authorization，即token
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // 去除prefix后，对JWToken进行解析
            Claims body = Jwts.parser()
                    .setSigningKey(SECRET.getBytes(Charset.defaultCharset()))
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();

//            User user = JsonUtils.unmarshal((String) body.get("user"), User.class);

            String userId = String.valueOf(body.get("userId"));
            // TODO - remove test user-id
            Optional<User> user = userId.equals("userId") ? userService.findByUsername("twadmin") : userService.findById(userId);
            if (user.isPresent()) {
                //TODO: fix it when user role relation has been done
                String role = user.get().getRole().getName();
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);

                PreAuthenticatedAuthenticationToken authenticationToken = new
                        PreAuthenticatedAuthenticationToken(user.get(), null, Arrays.asList(authority));
                // 将role作为spring-security的权限(SimpleGrantedAuthority) 放进当前请求的authentication中，并设置为登录
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                // 如果用户不存在则返回401错误
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, String.format("can't find user %s", userId));
                return;
            }
        }

        chain.doFilter(request, response);
    }

}
