package fun.hercules.order.order;

import fun.hercules.order.order.clients.UserServiceClient;
import fun.hercules.order.order.clients.dto.User;
import fun.hercules.order.order.platform.security.SecurityConstants;
import fun.hercules.order.order.platform.user.CurrentUser;
import fun.hercules.order.order.platform.user.Privilege;
import fun.hercules.order.order.platform.user.Role;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Before;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.charset.Charset;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class OrderIntegrationTestBase {
    @MockBean
    protected CurrentUser currentUser;


    @MockBean(name = "userServiceClient")
    private UserServiceClient serviceClient;

    public void assertThrows(Runnable action, Class<? extends Exception> expectedException) {
        try {
            action.run();
        } catch (Exception e) {
            assertTrue(String.format("expects %s, but throws %s", expectedException, e.getClass()),
                    expectedException.isAssignableFrom(e.getClass()));
            return;
        }
        fail(String.format("expects %s, but nothings throws", expectedException.getSimpleName()));
    }

    @Before
    public void setUpCurrentUser() throws Exception {
        when(currentUser.getUserId()).thenReturn("userId");
        when(currentUser.getEnterpriseId()).thenReturn("enterpriseId");
        when(serviceClient.getUserById("userId")).thenReturn(new User(
                "userId",
                "user",
                "fullname",
                "enterpriseId",
                "12310102020",
                "010-123321",
                "EnterpriseUser"
        ));
        switchRole(Role.EnterpriseUser);
    }

    protected void switchRole(Role role) {
        when(currentUser.getRole()).thenReturn(role.toString());

        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(currentUser,
                        null,
                        Arrays.asList(new SimpleGrantedAuthority(currentUser.getRole()))));
    }

    public String generateAuthorization(fun.hercules.order.order.platform.user.User user) throws Exception {
        JwtBuilder builder = Jwts.builder();
        return "Bearer " + builder
                .claim("userId", user.getUserId())
                .claim("enterpriseId", user.getEnterpriseId())
                .claim("role", user.getRole())
                .claim("privileges", Privilege.of(Privilege.Type.OrderAccessPrivilege))
                .setExpiration(Date.from(Instant.now().plus(10, ChronoUnit.DAYS)))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes(Charset.defaultCharset()))
                .compact();
    }

    public String generateOperatorAuthorization(fun.hercules.order.order.platform.user.User user) throws Exception {
        JwtBuilder builder = Jwts.builder();
        return "Bearer " + builder
                .claim("userId", user.getUserId())
                .claim("enterpriseId", user.getEnterpriseId())
                .claim("role", user.getRole())
                .claim("privileges", String.join(",", user.getPrivileges().stream()
                        .map(Privilege::getName).collect(Collectors.toList())))
                .setExpiration(Date.from(Instant.now().plus(10, ChronoUnit.DAYS)))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes(Charset.defaultCharset()))
                .compact();
    }
}
