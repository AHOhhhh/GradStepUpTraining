package fun.hercules.user.user.web;

import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.common.exceptions.ForbiddenException;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.service.UserService;
import fun.hercules.user.utils.AuthorizationUtil;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    private final UserService userService;

    private final boolean enableTokenGeneration;

    public TokenController(UserService userService, @Value("${debug.jwt-token.enabled}") boolean enableTokenGeneration) {
        this.userService = userService;
        this.enableTokenGeneration = enableTokenGeneration;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/token")
    public String generateToken(@RequestParam("user") String userName) {
        if (enableTokenGeneration) {
            User user = userService.getByUsername(userName);
            return "Bearer " + AuthorizationUtil.createJwtToken(user);
        } else {
            throw new ForbiddenException(ErrorCode.FORBIDDEN, "token generation is disabled");
        }
    }
}
