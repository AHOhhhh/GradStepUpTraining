package fun.hercules.user.user.web;

import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.common.exceptions.BadRequestException;
import fun.hercules.user.common.exceptions.NotFoundException;
import fun.hercules.user.enterprise.domain.Enterprise;
import fun.hercules.user.enterprise.domain.PayMethod;
import fun.hercules.user.enterprise.repository.EnterpriseRepository;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.security.CurrentUserContext;
import fun.hercules.user.user.service.UserService;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.security.CurrentUserContext;
import fun.hercules.user.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;

import static fun.hercules.user.common.errors.ErrorCode.PAY_METHOD_NOT_FOUND;

@Slf4j
@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Enterprise user", description = "Access to enterprise user")
public class UserController {

    private final UserService userService;

    private final EnterpriseRepository enterpriseRepository;

    private CurrentUserContext currentUserContext;

    public UserController(UserService userService,
                          EnterpriseRepository enterpriseRepository,
                          CurrentUserContext currentUserContext) {
        this.userService = userService;
        this.enterpriseRepository = enterpriseRepository;
        this.currentUserContext = currentUserContext;
    }

    @GetMapping(path = "/me")
    public User getCurrentUser() {
        return currentUserContext.getUser();
    }


    @GetMapping(path = "/{id}")
    @ApiOperation(value = "Get user information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get user")
    })
    public User getEnterpriseUserById(@PathVariable("id") String id) {
        return userService.getById(id);
    }

    @GetMapping(path = "/{id}/payMethods")
    @ApiOperation(value = "Get user payMethods information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get user payMethods ")
    })
    public Set<String> getPayMethods(@PathVariable("id") String id,
                                     @RequestParam("orderType") String orderType) {
        User user = userService.getById(id);
        Optional<Enterprise> enterprise = enterpriseRepository.findById(user.getEnterpriseId());
        if (enterprise.isPresent()) {
            return enterprise.get().getPayMethods().stream()
                    .filter(payMethod -> payMethod.getOrderType().equalsIgnoreCase(orderType))
                    .findFirst().map(PayMethod::getMethodsAsSet).orElseThrow(() -> new NotFoundException(PAY_METHOD_NOT_FOUND));

        } else {
            throw new BadRequestException(ErrorCode.ENTERPRISE_NOT_FOUND, "can not find enterprise by userId: " + id);
        }
    }
}
