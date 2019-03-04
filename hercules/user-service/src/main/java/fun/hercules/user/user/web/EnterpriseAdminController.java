package fun.hercules.user.user.web;

import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.common.exceptions.BadRequestException;
import fun.hercules.user.user.domain.Role;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.service.UserService;
import fun.hercules.user.user.validators.UserValidator;
import fun.hercules.user.user.domain.Role;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/enterprise-admin", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Enterprise admin", description = "Access to enterprise admin")
public class EnterpriseAdminController {

    private final UserService userService;

    public EnterpriseAdminController(UserService userService) {
        this.userService = userService;
    }

    @InitBinder("user")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new UserValidator());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Register enterprise admin user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Get user successfully"),
            @ApiResponse(code = 400, message = "invalid user information"),
            @ApiResponse(code = 409, message = "user name conflicts")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Valid User user, HttpServletResponse response) {
        checkRole(user);
        setResettableTrueIfEnterpriseAdmin(user);
        return userService.createAndLogin(user, authorization ->
                response.addHeader(HttpHeaders.AUTHORIZATION, authorization));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, path = "/{id}")
    @ApiOperation(value = "Update enterprise admin")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Update enterprise admin successfully"),
            @ApiResponse(code = 400, message = "invalid user information")
    })
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User updateUser(@PathVariable("id") String id, @RequestBody User user) {
        return userService.updateEnterpriseAdmin(id, user);
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "Get enterprise admin by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = String.class, message = "Get user successfully"),
            @ApiResponse(code = 404, message = "No user matches given id")
    })
    public User getUser(@PathVariable("id") String userId) {
        return userService.getById(userId);
    }

    @GetMapping(path = "/users/{id}")
    @ApiOperation(value = "Get enterprise user by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = String.class, message = "Get enterprise user successfully"),
            @ApiResponse(code = 404, message = "No user matches given id")
    })
    public User getEnterpriseUser(@PathVariable("id") String userId) {
        return userService.getEnterpriseUser(userService.getById(userId));
    }

    @PostMapping(path = "/{id}/reset-password")
    @ApiOperation(value = "Reset enterprise admin password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Reset enterprise admin password successfully")
    })
    public void updateUserPassword(@PathVariable("id") String id) {
        userService.resetEnterpriseAdminPassword(id);
    }

    private void checkRole(@RequestBody @Valid User user) {
        if (!Role.Type.EnterpriseAdmin.name().equals(user.getRole().getName())) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST, "The user role should be enterprise admin");
        }
    }

    private void setResettableTrueIfEnterpriseAdmin(User user) {
        if (Role.Type.EnterpriseAdmin.name().equals(user.getRole().getName())) {
            user.setResettable(true);
        }
    }
}
