package fun.hercules.user.user.web;

import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.service.UserService;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Plantform admin", description = "Access to plantform admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createAdmin(@RequestBody User user) {
        return userService.createAdmin(user);
    }
}
