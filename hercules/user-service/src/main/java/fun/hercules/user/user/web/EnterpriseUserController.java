package fun.hercules.user.user.web;

import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.common.exceptions.BadRequestException;
import fun.hercules.user.enterprise.domain.Enterprise;
import fun.hercules.user.enterprise.domain.EnterpriseBase;
import fun.hercules.user.enterprise.service.EnterpriseService;
import fun.hercules.user.user.domain.Role;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.security.CurrentUserContext;
import fun.hercules.user.user.service.UserService;
import fun.hercules.user.user.validators.UserValidator;
import fun.hercules.user.user.domain.Role;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.security.CurrentUserContext;
import fun.hercules.user.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
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

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/enterprise-user", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Enterprise user", description = "Access to enterprise user")
public class EnterpriseUserController {

    private final UserService userService;

    private final CurrentUserContext currentUserContext;

    private final EnterpriseService enterpriseService;

    public EnterpriseUserController(UserService userService,
                                    CurrentUserContext currentUserContext,
                                    EnterpriseService enterpriseService) {
        this.userService = userService;
        this.currentUserContext = currentUserContext;
        this.enterpriseService = enterpriseService;
    }

    @InitBinder("user")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new UserValidator());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Register normal user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Get user successfully"),
            @ApiResponse(code = 400, message = "invalid user information"),
            @ApiResponse(code = 403, message = "no authorization"),
            @ApiResponse(code = 409, message = "user name conflicts")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public User createEnterpriseUser(@RequestBody @Valid User user) {
        checkEnterpriseIsAuthorized(currentUserContext.getUser().getEnterpriseId());
        checkRole(user);
        return userService.createEnterpriseUser(user);
    }

    private void checkEnterpriseIsAuthorized(String enterpriseId) {
        Enterprise enterprise = enterpriseService.getById(enterpriseId);
        if (!enterprise.getValidationStatus().equals(EnterpriseBase.ValidationStatus.Authorized)) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST, "The enterprise is not yet audited");
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, path = "/{id}")
    @ApiOperation(value = "Update enterprise user")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Update enterprise user successfully")
    })
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateEnterpriseUser(@PathVariable("id") String userId, @RequestBody User user) {
        userService.updateEnterpriseUser(userService.getById(userId), user);
    }

    @PostMapping(path = "/{userId}/status", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Register normal user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "update user successfully"),
            @ApiResponse(code = 400, message = "invalid operator information"),
            @ApiResponse(code = 404, message = "can't find user"),
            @ApiResponse(code = 403, message = "no authorization")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public User updateNormalUserStatus(@PathVariable String userId,
                                       @RequestBody Map status) {
        User user = userService.getById(userId);
        return userService.updateStatus(user, (String) status.get("status"));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, path = "/{id}/init-password")
    @ApiOperation(value = "Init enterprise user password")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Init enterprise user password successfully")
    })
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void initUserPassword(@PathVariable("id") String userId, @RequestBody Map map) {
        userService.initPassword(userService.getById(userId), (String) map.get("originalPassword"), (String) map.get("password"),
                (String) map.get("captchaId"), (String) map.get("captcha"));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, path = "/{id}/reset-password")
    @ApiOperation(value = "Update enterprise user password")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Update enterprise user password successfully")
    })
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateUserPassword(@PathVariable("id") String id, @RequestBody Map password) {
        userService.resetEnterpriseUserPassword(id, (String) password.get("password"));
    }

    private void checkRole(@RequestBody @Valid User user) {
        if (!Role.Type.EnterpriseUser.name().equals(user.getRole().getName())) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST, "The user role should be enterprise user");
        }
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "Get enterprise user information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get enterprise user password successfully")
    })
    public User getEnterpriseUserById(@PathVariable("id") String id) {
        return userService.getEnterpriseUserById(id);
    }
}
